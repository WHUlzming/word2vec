package org.opendedup.sdfs.io;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.commons.collections.map.AbstractLinkedMap;
import org.apache.commons.collections.map.LRUMap;
import org.opendedup.collections.HashtableFullException;
import org.opendedup.collections.LargeLongByteArrayMap;
import org.opendedup.collections.LongByteArrayMap;
import org.opendedup.hashing.AbstractHashEngine;
import org.opendedup.hashing.HashFunctionPool;
import org.opendedup.sdfs.Main;
import org.opendedup.sdfs.filestore.DedupFileStore;
import org.opendedup.sdfs.servers.HCServiceProxy;
import org.opendedup.util.DeleteDir;
import org.opendedup.util.SDFSLogger;
import org.opendedup.util.ThreadPool;
import org.opendedup.util.VMDKParser;

public class SparseDedupFile implements DedupFile {

    private ArrayList<DedupFileLock> locks = new ArrayList<DedupFileLock>();

    private String GUID = "";

    private transient MetaDataDedupFile mf;

    private transient ArrayList<DedupFileChannel> buffers = new ArrayList<DedupFileChannel>();

    private transient String databasePath = null;

    private transient String databaseDirPath = null;

    private transient String chunkStorePath = null;

    private DedupFileChannel staticChannel = null;

    public long lastSync = 0;

    LongByteArrayMap bdb = null;

    MessageDigest digest = null;

    private static HashFunctionPool hashPool = new HashFunctionPool(Main.writeThreads + 1);

    protected static final transient ThreadPool pool = new ThreadPool(Main.writeThreads + 1, 8192);

    private final ReentrantLock channelLock = new ReentrantLock();

    private final ReentrantLock initLock = new ReentrantLock();

    private final ReentrantLock writeBufferLock = new ReentrantLock();

    private final ReentrantLock syncLock = new ReentrantLock();

    private LargeLongByteArrayMap chunkStore = null;

    private int maxWriteBuffers = ((Main.maxWriteBuffers * 1024 * 1024) / Main.CHUNK_LENGTH) + 1;

    protected transient ConcurrentHashMap<Long, WritableCacheBuffer> flushingBuffers = new ConcurrentHashMap<Long, WritableCacheBuffer>(4096, .75f, Main.writeThreads + 1);

    @SuppressWarnings("serial")
    protected transient LRUMap writeBuffers = new LRUMap(maxWriteBuffers + 1, false) {

        protected boolean removeLRU(AbstractLinkedMap.LinkEntry eldest) {
            if (size() >= maxWriteBuffers) {
                WritableCacheBuffer writeBuffer = (WritableCacheBuffer) eldest.getValue();
                if (writeBuffer != null) {
                    try {
                        writeBuffer.flush();
                    } catch (Exception e) {
                        SDFSLogger.getLog().debug("while closing position " + writeBuffer.getFilePosition(), e);
                    }
                }
            }
            return false;
        }
    };

    private boolean closed = true;

    static {
        File f = new File(Main.dedupDBStore);
        if (!f.exists()) f.mkdirs();
    }

    public SparseDedupFile(MetaDataDedupFile mf) throws IOException {
        SDFSLogger.getLog().debug("dedup file opened for " + mf.getPath());
        this.mf = mf;
        this.init();
    }

    public DedupFile snapshot(MetaDataDedupFile snapmf) throws IOException, HashtableFullException {
        DedupFileChannel ch = null;
        try {
            ch = this.getChannel();
            this.writeCache();
            this.sync();
            SparseDedupFile _df = new SparseDedupFile(snapmf);
            this.writeBufferLock.lock();
            bdb.copy(_df.getDatabasePath());
            chunkStore.copy(_df.chunkStorePath);
            return _df;
        } catch (Exception e) {
            SDFSLogger.getLog().warn("unable to clone file " + mf.getPath(), e);
            throw new IOException("unable to clone file " + mf.getPath(), e);
        } finally {
            this.writeBufferLock.unlock();
            if (ch != null) ch.close();
            ch = null;
        }
    }

    public void copyTo(String path) throws IOException {
        DedupFileChannel ch = null;
        File dest = new File(path + File.separator + "ddb" + File.separator + this.GUID.substring(0, 2) + File.separator + this.GUID);
        dest.mkdirs();
        try {
            ch = this.getChannel();
            this.writeCache();
            this.sync();
            this.writeBufferLock.lock();
            bdb.copy(dest.getPath() + File.separator + this.GUID + ".map");
            chunkStore.copy(dest.getPath() + File.separator + this.GUID + ".chk");
        } catch (Exception e) {
            SDFSLogger.getLog().warn("unable to copy to" + mf.getPath(), e);
            throw new IOException("unable to clone file " + mf.getPath(), e);
        } finally {
            this.writeBufferLock.unlock();
            if (ch != null) ch.close();
            ch = null;
        }
    }

    public void createBlankFile(long len) throws IOException {
        try {
            long numChks = len / Main.CHUNK_LENGTH;
            for (int i = 0; i < numChks; i++) {
                int pos = i * Main.CHUNK_LENGTH;
                SparseDataChunk chunk = new SparseDataChunk(true, Main.blankHash, false, System.currentTimeMillis());
                bdb.put(pos, chunk.getBytes());
            }
            mf.setLength(len, true);
        } catch (Exception e) {
            SDFSLogger.getLog().warn("unable to create blank file " + mf.getPath(), e);
            throw new IOException("unable to create blank file " + mf.getPath(), e);
        } finally {
        }
    }

    private boolean overLaps(long pos, long sz, boolean sharedRequested) {
        for (int i = 0; i < locks.size(); i++) {
            DedupFileLock lock = locks.get(i);
            if (lock.overLaps(pos, sz) && lock.isValid()) {
                if (!lock.isShared() || !sharedRequested) return true;
            }
        }
        return false;
    }

    public boolean delete() {
        this.channelLock.lock();
        try {
            this.forceClose();
            String filePath = Main.dedupDBStore + File.separator + this.GUID.substring(0, 2) + File.separator + this.GUID;
            DedupFileStore.removeOpenDedupFile(this.mf);
            return DeleteDir.deleteDirectory(new File(filePath));
        } catch (Exception e) {
        } finally {
            this.channelLock.unlock();
        }
        return false;
    }

    public boolean isClosed() {
        return this.closed;
    }

    public int writeCache() throws IOException, HashtableFullException {
        Object[] buffers = null;
        this.writeBufferLock.lock();
        try {
            SDFSLogger.getLog().debug("Flushing Cache of for " + mf.getPath() + " of size " + this.writeBuffers.size());
            if (this.writeBuffers.size() > 0) {
                buffers = this.writeBuffers.values().toArray();
            } else {
                return 0;
            }
        } finally {
            this.writeBufferLock.unlock();
        }
        int z = 0;
        for (int i = 0; i < buffers.length; i++) {
            WritableCacheBuffer buf = (WritableCacheBuffer) buffers[i];
            try {
                buf.flush();
            } catch (BufferClosedException e) {
                SDFSLogger.getLog().debug("while closing position " + buf.getFilePosition(), e);
            }
            z++;
        }
        try {
            SDFSLogger.getLog().debug("Flushing Cache of for " + mf.getPath() + " of size " + this.writeBuffers.size());
            buffers = this.flushingBuffers.values().toArray();
        } finally {
        }
        z = 0;
        for (int i = 0; i < buffers.length; i++) {
            WritableCacheBuffer buf = (WritableCacheBuffer) buffers[i];
            try {
                buf.close();
            } catch (Exception e) {
                SDFSLogger.getLog().debug("while closing position " + buf.getFilePosition(), e);
            }
            z++;
        }
        return z;
    }

    public void writeCache(WritableCacheBuffer writeBuffer) throws IOException, HashtableFullException {
        if (this.closed) {
            return;
        }
        if (writeBuffer == null) return;
        if (writeBuffer.isDirty()) {
            AbstractHashEngine hc = hashPool.borrowObject();
            byte[] hash = null;
            try {
                hash = hc.getHash(writeBuffer.getFlushedBuffer());
            } catch (Exception e) {
                throw new IOException(e);
            } finally {
                hashPool.returnObject(hc);
            }
            boolean doop = false;
            try {
                doop = HCServiceProxy.writeChunk(hash, writeBuffer.getFlushedBuffer(), writeBuffer.getLength(), writeBuffer.capacity(), mf.isDedup());
                mf.getIOMonitor().addVirtualBytesWritten(writeBuffer.capacity());
                if (!doop) {
                    if (writeBuffer.isNewChunk() || writeBuffer.isPrevDoop()) {
                        mf.getIOMonitor().addActualBytesWritten(writeBuffer.capacity());
                    }
                    if (writeBuffer.isPrevDoop() && !writeBuffer.isNewChunk()) {
                        mf.getIOMonitor().removeDuplicateBlock();
                    }
                }
                if (writeBuffer.getFilePosition() == 0 && mf.getPath().endsWith(".vmdk")) {
                    try {
                        VMDKData data = VMDKParser.parserVMDKFile(writeBuffer.getFlushedBuffer());
                        if (data != null) {
                            mf.setVmdk(true);
                            SDFSLogger.getLog().debug(data.toString());
                        }
                    } catch (Exception e) {
                        SDFSLogger.getLog().warn("Unable to parse vmdk header for  " + mf.getPath(), e);
                    }
                }
                this.updateMap(writeBuffer, hash, doop);
                if (doop && !writeBuffer.isPrevDoop()) mf.getIOMonitor().addDulicateBlock();
            } catch (Exception e) {
                SDFSLogger.getLog().fatal("unable to add chunk [" + writeBuffer.getHash() + "] at position " + writeBuffer.getFilePosition(), e);
            } finally {
            }
        }
    }

    private void updateMap(WritableCacheBuffer writeBuffer, byte[] hash, boolean doop) throws IOException {
        if (this.closed) {
            this.forceClose();
            throw new IOException("file already closed");
        }
        try {
            long filePosition = writeBuffer.getFilePosition();
            SparseDataChunk chunk = null;
            if (mf.isDedup() || doop) {
                chunk = new SparseDataChunk(doop, hash, false, System.currentTimeMillis());
            } else {
                chunk = new SparseDataChunk(doop, hash, true, System.currentTimeMillis());
                this.chunkStore.put(filePosition, writeBuffer.getFlushedBuffer());
            }
            bdb.put(filePosition, chunk.getBytes());
        } catch (Exception e) {
            SDFSLogger.getLog().fatal("unable to write " + hash + " closing " + mf.getPath(), e);
            throw new IOException(e);
        } finally {
        }
    }

    public WritableCacheBuffer getWriteBuffer(long position, boolean newBuff) throws IOException {
        if (this.closed) {
            throw new IOException("file already closed");
        }
        try {
            long chunkPos = this.getChuckPosition(position);
            this.writeBufferLock.lock();
            WritableCacheBuffer writeBuffer = (WritableCacheBuffer) this.writeBuffers.get(chunkPos);
            if (writeBuffer == null) {
                writeBuffer = (WritableCacheBuffer) this.flushingBuffers.get(chunkPos);
            }
            if (writeBuffer == null) {
                writeBuffer = marshalWriteBuffer(chunkPos, newBuff);
            }
            writeBuffer.open();
            return writeBuffer;
        } finally {
            this.writeBufferLock.unlock();
        }
    }

    private WritableCacheBuffer marshalWriteBuffer(long chunkPos, boolean newChunk) throws IOException {
        WritableCacheBuffer writeBuffer = null;
        DedupChunk ck = null;
        if (newChunk) ck = createNewChunk(chunkPos); else ck = this.getHash(chunkPos, true);
        if (ck.isNewChunk()) {
            writeBuffer = new WritableCacheBuffer(ck.getHash(), chunkPos, ck.getLength(), this);
        } else {
            writeBuffer = new WritableCacheBuffer(ck, this);
            writeBuffer.setPrevDoop(ck.isDoop());
        }
        return writeBuffer;
    }

    public DedupChunk getReadBuffer(long position) throws IOException {
        if (this.closed) {
            channelLock.lock();
            try {
                this.initDB();
            } catch (Exception e) {
            } finally {
                this.channelLock.unlock();
            }
        }
        try {
            long chunkPos = this.getChuckPosition(position);
            this.writeBufferLock.lock();
            WritableCacheBuffer writeBuffer = (WritableCacheBuffer) this.writeBuffers.get(chunkPos);
            if (writeBuffer == null) {
                writeBuffer = (WritableCacheBuffer) this.flushingBuffers.get(chunkPos);
            }
            if (writeBuffer == null) {
                writeBuffer = marshalWriteBuffer(chunkPos, false);
            }
            writeBuffer.open();
            return writeBuffer;
        } finally {
            this.writeBufferLock.unlock();
        }
    }

    public long getNumberofChunks() throws IOException {
        if (this.closed) {
            throw new IOException("file already closed");
        }
        try {
            long count = -1;
            return count;
        } catch (Exception e) {
            SDFSLogger.getLog().warn("Table does not exist in database " + this.GUID, e);
        } finally {
        }
        return -1;
    }

    public void sync() throws IOException {
        if (this.closed) {
            throw new IOException("file already closed");
        }
        if (Main.safeSync) {
            this.syncLock.lock();
            try {
                this.bdb.sync();
                Object[] buffers = null;
                try {
                    this.writeBufferLock.lock();
                    SDFSLogger.getLog().debug("Flushing Cache of for " + mf.getPath() + " of size " + this.writeBuffers.size());
                    buffers = this.writeBuffers.values().toArray();
                } finally {
                    this.writeBufferLock.unlock();
                }
                for (int i = 0; i < buffers.length; i++) {
                    WritableCacheBuffer buf = (WritableCacheBuffer) buffers[i];
                    try {
                        buf.sync();
                    } catch (IOException e) {
                    }
                }
            } catch (Exception e) {
                throw new IOException(e);
            } finally {
                this.syncLock.unlock();
            }
        } else {
            try {
                this.writeCache();
            } catch (Exception e) {
                throw new IOException(e);
            }
        }
    }

    public DedupFileChannel getChannel() throws IOException {
        if (!Main.safeClose) {
            if (this.staticChannel == null) {
                this.staticChannel = new DedupFileChannel(mf);
            }
            return this.staticChannel;
        } else {
            channelLock.lock();
            try {
                if (this.isClosed() || this.buffers.size() == 0) this.initDB();
                DedupFileChannel channel = new DedupFileChannel(mf);
                this.buffers.add(channel);
                return channel;
            } catch (IOException e) {
                throw e;
            } finally {
                channelLock.unlock();
            }
        }
    }

    public void unRegisterChannel(DedupFileChannel channel) {
        if (Main.safeClose) {
            channelLock.lock();
            try {
                this.buffers.remove(channel);
                if (this.buffers.size() == 0) {
                    this.forceClose();
                }
            } catch (Exception e) {
            } finally {
                channelLock.unlock();
            }
        }
    }

    public boolean hasOpenChannels() {
        channelLock.lock();
        try {
            if (this.buffers.size() > 0) return true; else return false;
        } catch (Exception e) {
            return false;
        } finally {
            channelLock.unlock();
        }
    }

    public void forceClose() {
        this.writeBufferLock.lock();
        this.initLock.lock();
        this.channelLock.lock();
        try {
            if (!this.closed) {
                if (Main.safeClose) {
                    try {
                        ArrayList<DedupFileChannel> al = new ArrayList<DedupFileChannel>();
                        for (int i = 0; i < this.buffers.size(); i++) {
                            al.add(this.buffers.get(i));
                        }
                        for (int i = 0; i < al.size(); i++) {
                            al.get(i).close();
                        }
                    } catch (Exception e) {
                        SDFSLogger.getLog().error("error closing " + mf.getPath(), e);
                    }
                } else {
                    try {
                        this.staticChannel.forceClose();
                        this.staticChannel = null;
                    } catch (Exception e) {
                    }
                }
                try {
                    int nwb = this.writeCache();
                    SDFSLogger.getLog().debug("Flushed " + nwb + " buffers");
                } catch (Exception e) {
                    SDFSLogger.getLog().error("unable to flush " + this.databasePath, e);
                }
                try {
                    this.bdb.sync();
                } catch (Exception e) {
                }
                try {
                    this.bdb.close();
                } catch (Exception e) {
                }
                this.bdb = null;
                this.closed = true;
                try {
                    this.chunkStore.close();
                } catch (Exception e) {
                }
                mf.setDedupFile(this);
                mf.sync();
            }
        } catch (Exception e) {
            SDFSLogger.getLog().error("error closing " + mf.getPath(), e);
        } finally {
            DedupFileStore.removeOpenDedupFile(mf);
            bdb = null;
            chunkStore = null;
            this.closed = true;
            this.channelLock.unlock();
            this.initLock.unlock();
            this.writeBufferLock.unlock();
        }
    }

    public String getGUID() {
        return GUID;
    }

    public MetaDataDedupFile getMetaFile() {
        return this.mf;
    }

    public void removeLock(DedupFileLock lock) {
        this.locks.remove(lock);
    }

    public DedupFileLock addLock(DedupFileChannel ch, long position, long len, boolean shared) throws IOException {
        if (this.overLaps(position, len, shared)) throw new IOException("Overlapping Lock requested"); else {
            return new DedupFileLock(ch, position, len, shared);
        }
    }

    private void init() throws IOException {
        if (mf.getDfGuid() == null) {
            this.GUID = UUID.randomUUID().toString();
        } else {
            this.GUID = mf.getDfGuid();
        }
        try {
            if (bdb == null) this.initDB();
        } catch (Exception e) {
            SDFSLogger.getLog().error("unable to init " + this.GUID, e);
            throw new IOException(e);
        }
    }

    public String getDatabasePath() {
        return this.databasePath;
    }

    public String getDatabaseDirPath() {
        return this.databaseDirPath;
    }

    private void initDB() throws IOException {
        this.initLock.lock();
        try {
            if (this.isClosed()) {
                File directory = new File(Main.dedupDBStore + File.separator + this.GUID.substring(0, 2) + File.separator + this.GUID);
                File dbf = new File(directory.getPath() + File.separator + this.GUID + ".map");
                File dbc = new File(directory.getPath() + File.separator + this.GUID + ".chk");
                this.databaseDirPath = directory.getPath();
                this.databasePath = dbf.getPath();
                this.chunkStorePath = dbc.getPath();
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                this.chunkStore = new LargeLongByteArrayMap(chunkStorePath, (long) -1, Main.CHUNK_LENGTH);
                this.bdb = new LongByteArrayMap(this.databasePath);
                DedupFileStore.addOpenDedupFile(this);
                this.closed = false;
            }
        } catch (IOException e) {
            throw e;
        } finally {
            this.initLock.unlock();
        }
    }

    public void optimize() throws HashtableFullException {
        DedupFileChannel ch = null;
        try {
            ch = this.getChannel();
            if (this.closed) {
                throw new IOException("file already closed");
            }
            if (!mf.isDedup()) {
                this.writeCache();
                this.checkForDups();
                this.chunkStore.close();
                new File(this.chunkStorePath).delete();
                this.chunkStore = new LargeLongByteArrayMap(chunkStorePath, (long) -1, Main.CHUNK_LENGTH);
            } else {
                this.pushLocalDataToChunkStore();
                this.chunkStore.vanish();
                this.chunkStore = null;
                this.chunkStore = new LargeLongByteArrayMap(chunkStorePath, (long) -1, Main.CHUNK_LENGTH);
            }
        } catch (IOException e) {
        } finally {
            try {
                ch.close();
            } catch (Exception e) {
            }
            ch = null;
        }
    }

    private void pushLocalDataToChunkStore() throws IOException {
        if (this.closed) {
            throw new IOException("file already closed");
        }
        bdb.iterInit();
        Long l = bdb.nextKey();
        SDFSLogger.getLog().debug("removing for dups within " + mf.getPath());
        long doops = 0;
        long records = 0;
        while (l > -1) {
            try {
                SparseDataChunk pck = new SparseDataChunk(bdb.get(l));
                WritableCacheBuffer writeBuffer = null;
                if (pck.isLocalData() && mf.isDedup()) {
                    byte[] chunk = chunkStore.get(l);
                    boolean doop = HCServiceProxy.writeChunk(pck.getHash(), chunk, Main.CHUNK_LENGTH, Main.CHUNK_LENGTH, mf.isDedup());
                    DedupChunk ck = new DedupChunk(pck.getHash(), chunk, l, Main.CHUNK_LENGTH);
                    writeBuffer = new WritableCacheBuffer(ck, this);
                    this.updateMap(writeBuffer, writeBuffer.getHash(), doop);
                    ck = null;
                    chunk = null;
                    if (doop) doops++;
                }
                pck = null;
                writeBuffer = null;
                records++;
                l = bdb.nextKey();
            } catch (Exception e) {
                SDFSLogger.getLog().error("error pushing data for " + mf.getPath(), e);
            }
        }
        if (this.buffers.size() == 0) {
            this.forceClose();
        }
        SDFSLogger.getLog().debug("Checked [" + records + "] blocks found [" + doops + "] new duplicate blocks");
    }

    private void checkForDups() throws IOException {
        if (this.closed) {
            throw new IOException("file already closed");
        }
        bdb.iterInit();
        Long l = bdb.nextKey();
        SDFSLogger.getLog().debug("checking for dups");
        long doops = 0;
        long records = 0;
        long localRec = 0;
        long pos = 0;
        LargeLongByteArrayMap newCS = new LargeLongByteArrayMap(this.chunkStorePath + ".new", (long) -1, Main.CHUNK_LENGTH);
        this.chunkStore.lockCollection();
        try {
            while (l > -1) {
                pos = l;
                SparseDataChunk pck = new SparseDataChunk(bdb.get(l));
                if (pck.isLocalData()) {
                    boolean doop = HCServiceProxy.hashExists(pck.getHash());
                    if (doop) {
                        doops++;
                        pck.setLocalData(false);
                        this.bdb.put(l, pck.getBytes());
                    } else {
                        newCS.put(l, chunkStore.get(l, false));
                    }
                    localRec++;
                }
                pck = null;
                records++;
                l = bdb.nextKey();
            }
            newCS.close();
            new File(this.chunkStorePath).delete();
            newCS.move(this.chunkStorePath);
            newCS = null;
            this.chunkStore = new LargeLongByteArrayMap(chunkStorePath, (long) -1, Main.CHUNK_LENGTH);
        } catch (Exception e) {
            throw new IOException(e);
        } finally {
            this.chunkStore.unlockCollection();
        }
        SDFSLogger.getLog().debug("Checked [" + records + "] blocks found [" + doops + "] new duplicate blocks from [" + localRec + "] local records last key was [" + pos + "]");
    }

    public long lastModified() throws IOException {
        return mf.lastModified();
    }

    public DedupChunk getHash(long location, boolean create) throws IOException {
        if (this.closed) {
            throw new IOException("file already closed");
        }
        long place = this.getChuckPosition(location);
        DedupChunk ck = null;
        try {
            byte[] b = this.bdb.get(place);
            if (b != null) {
                SparseDataChunk pck = new SparseDataChunk(b);
                boolean dataEmpty = !pck.isLocalData();
                if (dataEmpty) {
                    ck = new DedupChunk(pck.getHash(), place, Main.CHUNK_LENGTH, false);
                } else {
                    byte dk[] = chunkStore.get(place);
                    ck = new DedupChunk(pck.getHash(), dk, place, Main.CHUNK_LENGTH);
                }
                ck.setDoop(pck.isDoop());
                pck = null;
            }
            b = null;
            if (ck == null && create == true) {
                return createNewChunk(place);
            } else {
                return ck;
            }
        } catch (Exception e) {
            SDFSLogger.getLog().warn("unable to fetch chunk at position " + place, e);
            return null;
        } finally {
        }
    }

    public void removeHash(long location) throws IOException {
        if (this.closed) {
            throw new IOException("file already closed");
        }
        long place = this.getChuckPosition(location);
        this.writeBufferLock.lock();
        try {
            this.writeCache();
            this.bdb.remove(place);
            if (!mf.isDedup()) this.chunkStore.remove(place);
        } catch (Exception e) {
            SDFSLogger.getLog().warn("unable to remove chunk at position " + place, e);
            throw new IOException(e);
        } finally {
            this.writeBufferLock.unlock();
        }
    }

    public void truncate(long size) throws IOException {
        this.writeBufferLock.lock();
        try {
            if (this.closed) {
                throw new IOException("file already closed");
            }
            this.writeCache();
            if (size == 0) {
                this.mf.getIOMonitor().clearAllCounters();
            }
            this.bdb.truncate(size);
            if (!mf.isDedup()) this.chunkStore.setLength(size);
        } catch (Exception e) {
            SDFSLogger.getLog().warn("unable to truncate to " + size, e);
            throw new IOException(e);
        } finally {
            this.writeBufferLock.unlock();
        }
    }

    private DedupChunk createNewChunk(long location) {
        DedupChunk ck = new DedupChunk(new byte[Main.hashLength], location, Main.CHUNK_LENGTH, true);
        return ck;
    }

    public long getChuckPosition(long location) {
        long place = location / Main.CHUNK_LENGTH;
        place = place * Main.CHUNK_LENGTH;
        return place;
    }

    public boolean isAbsolute() {
        return true;
    }
}
