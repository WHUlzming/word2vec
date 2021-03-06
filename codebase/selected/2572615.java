package javax.imageio;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageInputStreamSpi;
import javax.imageio.spi.ImageOutputStreamSpi;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.spi.ImageTranscoderSpi;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.spi.ServiceRegistry;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;

/**
 * An uninstantiable class that provides static methods for locating
 * and using image readers and writers.
 */
public final class ImageIO {

    /**
   * Construct an ImageIO.  Private since ImageIO is not instantiable.
   */
    private ImageIO() {
    }

    private static final class ReaderFormatFilter implements ServiceRegistry.Filter {

        private String formatName;

        public ReaderFormatFilter(String formatName) {
            this.formatName = formatName;
        }

        public boolean filter(Object provider) {
            if (provider instanceof ImageReaderSpi) {
                ImageReaderSpi spi = (ImageReaderSpi) provider;
                String[] formatNames = spi.getFormatNames();
                for (int i = formatNames.length - 1; i >= 0; --i) if (formatName.equals(formatNames[i])) return true;
            }
            return false;
        }
    }

    private static final class ReaderMIMETypeFilter implements ServiceRegistry.Filter {

        private String MIMEType;

        public ReaderMIMETypeFilter(String MIMEType) {
            this.MIMEType = MIMEType;
        }

        public boolean filter(Object provider) {
            if (provider instanceof ImageReaderSpi) {
                ImageReaderSpi spi = (ImageReaderSpi) provider;
                String[] mimetypes = spi.getMIMETypes();
                for (int i = mimetypes.length - 1; i >= 0; --i) if (MIMEType.equals(mimetypes[i])) return true;
            }
            return false;
        }
    }

    private static final class ReaderObjectFilter implements ServiceRegistry.Filter {

        private Object object;

        public ReaderObjectFilter(Object object) {
            this.object = object;
        }

        public boolean filter(Object provider) {
            if (provider instanceof ImageReaderSpi) {
                ImageReaderSpi spi = (ImageReaderSpi) provider;
                try {
                    if (spi.canDecodeInput(object)) return true;
                } catch (IOException e) {
                }
            }
            return false;
        }
    }

    private static final class ReaderSuffixFilter implements ServiceRegistry.Filter {

        private String fileSuffix;

        public ReaderSuffixFilter(String fileSuffix) {
            this.fileSuffix = fileSuffix;
        }

        public boolean filter(Object provider) {
            if (provider instanceof ImageReaderSpi) {
                ImageReaderSpi spi = (ImageReaderSpi) provider;
                String[] suffixes = spi.getFileSuffixes();
                for (int i = suffixes.length - 1; i >= 0; --i) if (fileSuffix.equals(suffixes[i])) return true;
            }
            return false;
        }
    }

    private static final class WriterFormatFilter implements ServiceRegistry.Filter {

        private String formatName;

        public WriterFormatFilter(String formatName) {
            this.formatName = formatName;
        }

        public boolean filter(Object provider) {
            if (provider instanceof ImageWriterSpi) {
                ImageWriterSpi spi = (ImageWriterSpi) provider;
                String[] formatNames = spi.getFormatNames();
                for (int i = formatNames.length - 1; i >= 0; --i) if (formatName.equals(formatNames[i])) return true;
            }
            return false;
        }
    }

    private static final class WriterMIMETypeFilter implements ServiceRegistry.Filter {

        private String MIMEType;

        public WriterMIMETypeFilter(String MIMEType) {
            this.MIMEType = MIMEType;
        }

        public boolean filter(Object provider) {
            if (provider instanceof ImageWriterSpi) {
                ImageWriterSpi spi = (ImageWriterSpi) provider;
                String[] mimetypes = spi.getMIMETypes();
                for (int i = mimetypes.length - 1; i >= 0; --i) if (MIMEType.equals(mimetypes[i])) return true;
            }
            return false;
        }
    }

    private static final class WriterSuffixFilter implements ServiceRegistry.Filter {

        private String fileSuffix;

        public WriterSuffixFilter(String fileSuffix) {
            this.fileSuffix = fileSuffix;
        }

        public boolean filter(Object provider) {
            if (provider instanceof ImageWriterSpi) {
                ImageWriterSpi spi = (ImageWriterSpi) provider;
                String[] suffixes = spi.getFileSuffixes();
                for (int i = suffixes.length - 1; i >= 0; --i) if (fileSuffix.equals(suffixes[i])) return true;
            }
            return false;
        }
    }

    private static final class WriterObjectFilter implements ServiceRegistry.Filter {

        private ImageTypeSpecifier type;

        private String formatName;

        public WriterObjectFilter(ImageTypeSpecifier type, String formatName) {
            this.type = type;
            this.formatName = formatName;
        }

        public boolean filter(Object provider) {
            if (provider instanceof ImageWriterSpi) {
                ImageWriterSpi spi = (ImageWriterSpi) provider;
                if (spi.canEncodeImage(type)) {
                    String[] formatNames = spi.getFormatNames();
                    for (int i = formatNames.length - 1; i >= 0; --i) if (formatName.equals(formatNames[i])) return true;
                }
            }
            return false;
        }
    }

    private static final class TranscoderFilter implements ServiceRegistry.Filter {

        private ImageReader reader;

        private ImageWriter writer;

        public TranscoderFilter(ImageReader reader, ImageWriter writer) {
            this.reader = reader;
            this.writer = writer;
        }

        public boolean filter(Object provider) {
            if (provider instanceof ImageTranscoderSpi) {
                ImageTranscoderSpi spi = (ImageTranscoderSpi) provider;
                if (spi.getReaderServiceProviderName().equals(reader.getOriginatingProvider().getClass().getName()) && spi.getWriterServiceProviderName().equals(writer.getOriginatingProvider().getClass().getName())) return true;
            }
            return false;
        }
    }

    private static final class ImageReaderIterator implements Iterator {

        Iterator it;

        Object readerExtension;

        public ImageReaderIterator(Iterator it, Object readerExtension) {
            this.it = it;
            this.readerExtension = readerExtension;
        }

        public boolean hasNext() {
            return it.hasNext();
        }

        public Object next() {
            try {
                return ((ImageReaderSpi) it.next()).createReaderInstance(readerExtension);
            } catch (IOException e) {
                return null;
            }
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private static final class ImageWriterIterator implements Iterator {

        Iterator it;

        Object writerExtension;

        public ImageWriterIterator(Iterator it, Object writerExtension) {
            this.it = it;
            this.writerExtension = writerExtension;
        }

        public boolean hasNext() {
            return it.hasNext();
        }

        public Object next() {
            try {
                return ((ImageWriterSpi) it.next()).createWriterInstance(writerExtension);
            } catch (IOException e) {
                return null;
            }
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private static File cacheDirectory;

    private static boolean useCache = true;

    private static Iterator getReadersByFilter(Class type, ServiceRegistry.Filter filter, Object readerExtension) {
        try {
            Iterator it = getRegistry().getServiceProviders(type, filter, true);
            return new ImageReaderIterator(it, readerExtension);
        } catch (IllegalArgumentException e) {
            return Collections.EMPTY_SET.iterator();
        }
    }

    private static Iterator getWritersByFilter(Class type, ServiceRegistry.Filter filter, Object writerExtension) {
        try {
            Iterator it = getRegistry().getServiceProviders(type, filter, true);
            return new ImageWriterIterator(it, writerExtension);
        } catch (IllegalArgumentException e) {
            return Collections.EMPTY_SET.iterator();
        }
    }

    /**
   * Retrieve the current cache directory.
   *
   * @return the current cache directory or null if none is set.
   */
    public static File getCacheDirectory() {
        return cacheDirectory;
    }

    /**
   * Retrieve an iterator over all registered readers for the given
   * format.
   *
   * @param formatName an infomal format name (e.g. "jpeg" or "bmp")
   *
   * @return an iterator over a collection of image readers
   *
   * @exception IllegalArgumentException if formatName is null
   */
    public static Iterator getImageReadersByFormatName(String formatName) {
        if (formatName == null) throw new IllegalArgumentException("formatName may not be null");
        return getReadersByFilter(ImageReaderSpi.class, new ReaderFormatFilter(formatName), formatName);
    }

    /**
   * Retrieve an iterator over all registered readers for the given
   * MIME type.
   *
   * @param MIMEType a MIME specification for an image type
   * (e.g. "image/jpeg" or "image/x-bmp")
   *
   * @return an iterator over a collection of image readers
   *
   * @exception IllegalArgumentException if MIMEType is null
   */
    public static Iterator getImageReadersByMIMEType(String MIMEType) {
        if (MIMEType == null) throw new IllegalArgumentException("MIMEType may not be null");
        return getReadersByFilter(ImageReaderSpi.class, new ReaderMIMETypeFilter(MIMEType), MIMEType);
    }

    /**
   * Retrieve an iterator over all registered readers for the given
   * file suffix.
   *
   * @param fileSuffix an image file suffix (e.g. "jpg" or "bmp")
   *
   * @return an iterator over a collection of image readers
   *
   * @exception IllegalArgumentException if fileSuffix is null
   */
    public static Iterator getImageReadersBySuffix(String fileSuffix) {
        if (fileSuffix == null) throw new IllegalArgumentException("formatName may not be null");
        return getReadersByFilter(ImageReaderSpi.class, new ReaderSuffixFilter(fileSuffix), fileSuffix);
    }

    /**
   * Retrieve an iterator over all registered writers for the given
   * format.
   *
   * @param formatName an infomal format name (e.g. "jpeg" or "bmp")
   *
   * @return an iterator over a collection of image writers
   *
   * @exception IllegalArgumentException if formatName is null
   */
    public static Iterator getImageWritersByFormatName(String formatName) {
        if (formatName == null) throw new IllegalArgumentException("formatName may not be null");
        return getWritersByFilter(ImageWriterSpi.class, new WriterFormatFilter(formatName), formatName);
    }

    /**
   * Retrieve an iterator over all registered writers for the given
   * MIME type.
   *
   * @param MIMEType a MIME specification for an image type
   * (e.g. "image/jpeg" or "image/x-bmp")
   *
   * @return an iterator over a collection of image writers
   *
   * @exception IllegalArgumentException if MIMEType is null
   */
    public static Iterator getImageWritersByMIMEType(String MIMEType) {
        if (MIMEType == null) throw new IllegalArgumentException("MIMEType may not be null");
        return getWritersByFilter(ImageWriterSpi.class, new WriterMIMETypeFilter(MIMEType), MIMEType);
    }

    /**
   * Retrieve an iterator over all registered writers for the given
   * file suffix.
   *
   * @param fileSuffix an image file suffix (e.g. "jpg" or "bmp")
   *
   * @return an iterator over a collection of image writers
   *
   * @exception IllegalArgumentException if fileSuffix is null
   */
    public static Iterator getImageWritersBySuffix(String fileSuffix) {
        if (fileSuffix == null) throw new IllegalArgumentException("fileSuffix may not be null");
        return getWritersByFilter(ImageWriterSpi.class, new WriterSuffixFilter(fileSuffix), fileSuffix);
    }

    /**
   * Retrieve all the informal format names supported by the
   * collection of registered image readers.
   *
   * @return an array of format names
   */
    public static String[] getReaderFormatNames() {
        try {
            Iterator it = getRegistry().getServiceProviders(ImageReaderSpi.class, true);
            ArrayList result = new ArrayList();
            while (it.hasNext()) {
                ImageReaderSpi spi = (ImageReaderSpi) it.next();
                String[] names = spi.getFormatNames();
                for (int i = names.length - 1; i >= 0; --i) result.add(names[i]);
            }
            return (String[]) result.toArray(new String[result.size()]);
        } catch (IllegalArgumentException e) {
            return new String[0];
        }
    }

    /**
   * Retrieve all the MIME types supported by the collection of
   * registered image readers.
   *
   * @return an array of MIME types
   */
    public static String[] getReaderMIMETypes() {
        try {
            Iterator it = getRegistry().getServiceProviders(ImageReaderSpi.class, true);
            ArrayList result = new ArrayList();
            while (it.hasNext()) {
                ImageReaderSpi spi = (ImageReaderSpi) it.next();
                String[] names = spi.getMIMETypes();
                for (int i = names.length - 1; i >= 0; --i) result.add(names[i]);
            }
            return (String[]) result.toArray(new String[result.size()]);
        } catch (IllegalArgumentException e) {
            return new String[0];
        }
    }

    private static IIORegistry getRegistry() {
        return IIORegistry.getDefaultInstance();
    }

    /**
   * Check whether or not an on-disk cache is used for image input and
   * output streams.
   *
   * @return true if an on-disk cache is available, false otherwise
   */
    public static boolean getUseCache() {
        return useCache;
    }

    /**
   * Retrieve all the informal format names supported by the
   * collection of registered image writers.
   *
   * @return an array of format names
   */
    public static String[] getWriterFormatNames() {
        try {
            Iterator it = getRegistry().getServiceProviders(ImageWriterSpi.class, true);
            ArrayList result = new ArrayList();
            while (it.hasNext()) {
                ImageWriterSpi spi = (ImageWriterSpi) it.next();
                String[] names = spi.getFormatNames();
                for (int i = names.length - 1; i >= 0; --i) result.add(names[i]);
            }
            return (String[]) result.toArray(new String[result.size()]);
        } catch (IllegalArgumentException e) {
            return new String[0];
        }
    }

    /**
   * Retrieve all the MIME types supported by the collection of
   * registered image writers.
   *
   * @return an array of MIME types
   */
    public static String[] getWriterMIMETypes() {
        try {
            Iterator it = getRegistry().getServiceProviders(ImageWriterSpi.class, true);
            ArrayList result = new ArrayList();
            while (it.hasNext()) {
                ImageWriterSpi spi = (ImageWriterSpi) it.next();
                String[] names = spi.getMIMETypes();
                for (int i = names.length - 1; i >= 0; --i) result.add(names[i]);
            }
            return (String[]) result.toArray(new String[result.size()]);
        } catch (IllegalArgumentException e) {
            return new String[0];
        }
    }

    /**
   * Rescans the application classpath for ImageIO service providers
   * and registers them.
   */
    public static void scanForPlugins() {
        IIORegistry.getDefaultInstance().registerApplicationClasspathSpis();
    }

    /**
   * Set the directory to be used for caching image data.  A null
   * argument means to use the default system temporary directory.
   * This cache directory is only used if getUseCache returns true.
   *
   * @param cacheDirectory the directory where image data should be
   * cached
   *
   * @exception IllegalArgumentException if cacheDirectory is not a
   * directory
   */
    public static void setCacheDirectory(File cacheDirectory) {
        if (cacheDirectory != null) {
            if (!cacheDirectory.isDirectory()) throw new IllegalArgumentException("cacheDirectory must be a directory");
            cacheDirectory.canWrite();
        }
        ImageIO.cacheDirectory = cacheDirectory;
    }

    /**
   * Control whether or not an on-disk cache is used.  This cache is
   * used to store input or output data from an image data stream when
   * data in the stream needs to be re-processed.
   *
   * If useCache is false the cache will be stored in memory.  Doing
   * so eliminates file creation and deletion overhead.  The default
   * is to use an on-disk cache.
   *
   * @param useCache true to use an on-disk cache, false otherwise
   */
    public static void setUseCache(boolean useCache) {
        ImageIO.useCache = useCache;
    }

    /**
   * Write an image to a file using a registered writer that supports
   * the given format, overwriting the file if it already exists.
   *
   * @param im the image data to write
   * @param formatName an informal description of the output format
   * @param output the file to which the image will be written
   *
   * @return false if no registered writer supports the given format,
   * true otherwise
   *
   * @exception IllegalArgumentException if any argument is null
   * @exception IOException if a writing error occurs
   */
    public static boolean write(RenderedImage im, String formatName, File output) throws IOException {
        if (im == null || formatName == null || output == null) throw new IllegalArgumentException("null argument");
        return write(im, formatName, new FileOutputStream(output));
    }

    /**
   * Write an image to an output stream using a registered writer that
   * supports the given format.
   *
   * @param im the image data to write
   * @param formatName an informal description of the output format
   * @param output the output stream to which the image will be
   * written
   *
   * @return false if no registered writer supports the given format,
   * true otherwise
   *
   * @exception IllegalArgumentException if any argument is null
   * @exception IOException if a writing error occurs
   */
    public static boolean write(RenderedImage im, String formatName, OutputStream output) throws IOException {
        if (im == null || formatName == null || output == null) throw new IllegalArgumentException("null argument");
        return write(im, formatName, new MemoryCacheImageOutputStream(output));
    }

    /**
   * Write an image to an ImageOutputStream using a registered writer
   * that supports the given format.  Image data is written starting
   * at the ImageOutputStream's current stream pointer, overwriting
   * any existing data.
   *
   * @param im the image data to write
   * @param formatName an informal description of the output format
   * @param output the image output stream to which the image will be
   * written
   *
   * @return false if no registered writer supports the given format,
   * true otherwise
   *
   * @exception IllegalArgumentException if any argument is null
   * @exception IOException if a writing error occurs
   */
    public static boolean write(RenderedImage im, String formatName, ImageOutputStream output) throws IOException {
        if (im == null || formatName == null || output == null) throw new IllegalArgumentException("null argument");
        Iterator writers = getImageWritersByFormatName(formatName);
        IIOImage img = new IIOImage(im, null, null);
        while (writers.hasNext()) {
            ImageWriter w = (ImageWriter) writers.next();
            try {
                w.setOutput(output);
            } catch (IllegalArgumentException e) {
                continue;
            }
            w.write(null, img, null);
            output.close();
            return true;
        }
        return false;
    }

    /**
   * Create a buffered image from an image input stream.  An image
   * reader that supports the given image data is automatically
   * selected from the collection of registered readers.  If no
   * registered reader can handle the input format, null is returned.
   *
   * @param stream the image input stream from which to read image
   * data
   *
   * @return a new buffered image created from the given image data,
   * or null
   *
   * @exception IllegalArgumentException if stream is null
   * @exception IOException if a reading error occurs
   */
    public static BufferedImage read(ImageInputStream stream) throws IOException {
        if (stream == null) throw new IllegalArgumentException("null argument");
        Iterator providers = getRegistry().getServiceProviders(ImageReaderSpi.class, true);
        while (providers.hasNext()) {
            ImageReaderSpi spi = (ImageReaderSpi) providers.next();
            if (spi.canDecodeInput(stream)) {
                ImageReader reader = spi.createReaderInstance();
                reader.setInput(stream);
                return reader.read(0, null);
            }
        }
        return null;
    }

    /**
   * Create a buffered image from a URL.  An image reader that
   * supports the given image data is automatically selected from the
   * collection of registered readers.  If no registered reader can
   * handle the input format, null is returned.
   *
   * The image data will be cached in the current cache directory if
   * caching is enabled.
   *
   * This method does not locate readers that read data directly from
   * a URL.  To locate such readers manually, use IIORegistry and
   * ImageReaderSpi.
   *
   * @param input the URL from which to retrieve the image file
   *
   * @return a new buffered image created from the given image URL, or
   * null
   *
   * @exception IllegalArgumentException if input is null
   * @exception IOException if a reading error occurs
   */
    public static BufferedImage read(URL input) throws IOException {
        if (input == null) throw new IllegalArgumentException("null argument");
        return read(input.openStream());
    }

    /**
   * Create a buffered image from an input stream.  An image reader
   * that supports the given image data is automatically selected from
   * the collection of registered readers.  If no registered reader
   * can handle the input format, null is returned.
   *
   * The image data will be cached in the current cache directory if
   * caching is enabled.
   *
   * This method does not locate readers that read data directly from
   * an input stream.  To locate such readers manually, use
   * IIORegistry and ImageReaderSpi.
   *
   * @param input the input stream from which to read the image data
   *
   * @return a new buffered image created from the given input stream,
   * or null
   *
   * @exception IllegalArgumentException if input is null
   * @exception IOException if a reading error occurs
   */
    public static BufferedImage read(InputStream input) throws IOException {
        if (input == null) throw new IllegalArgumentException("null argument");
        return read(new MemoryCacheImageInputStream(input));
    }

    /**
   * Create a buffered image from a file.  An image reader that
   * supports the given image data is automatically selected from the
   * collection of registered readers.  If no registered reader can
   * handle the input format, null is returned.
   *
   * The image data will be cached in the current cache directory if
   * caching is enabled.
   *
   * This method does not locate readers that read data directly from
   * a file.  To locate such readers manually, use IIORegistry and
   * ImageReaderSpi.
   *
   * @param input the file from which to read image data
   *
   * @return a new buffered image created from the given image file,
   * or null
   *
   * @exception IllegalArgumentException if input is null
   * @exception IOException if a reading error occurs
   */
    public static BufferedImage read(File input) throws IOException {
        if (input == null) throw new IllegalArgumentException("null argument");
        return read(new FileInputStream(input));
    }

    /**
   * Create an image input stream from the given object.  The
   * collection of ImageInputStreamSpis registered with the
   * IIORegistry is searched for an image input stream that can take
   * input from the given object.  null is returned if no such SPI is
   * registered.
   *
   * The image data will be cached in the current cache directory if
   * caching is enabled.
   *
   * @param input an object from which to read image data
   *
   * @return an ImageInputStream that can read data from input, or
   * null
   *
   * @exception IllegalArgumentException if input is null
   * @exception IOException if caching is required but not enabled
   */
    public static ImageInputStream createImageInputStream(Object input) throws IOException {
        if (input == null) throw new IllegalArgumentException("null argument");
        Iterator spis = getRegistry().getServiceProviders(ImageInputStreamSpi.class, true);
        ImageInputStreamSpi foundSpi = null;
        while (spis.hasNext()) {
            ImageInputStreamSpi spi = (ImageInputStreamSpi) spis.next();
            if (input.getClass().equals(spi.getInputClass())) {
                foundSpi = spi;
                break;
            }
        }
        return foundSpi == null ? null : foundSpi.createInputStreamInstance(input, getUseCache(), getCacheDirectory());
    }

    /**
   * Create an image output stream from the given object.  The
   * collection of ImageOutputStreamSpis registered with the
   * IIORegistry is searched for an image output stream that can send
   * output to the given object.  null is returned if no such SPI is
   * registered.
   *
   * The image data will be cached in the current cache directory if
   * caching is enabled.
   *
   * @param input an object to which to write image data
   *
   * @return an ImageOutputStream that can send data to output, or
   * null
   *
   * @exception IllegalArgumentException if output is null
   * @exception IOException if caching is required but not enabled
   */
    public static ImageOutputStream createImageOutputStream(Object output) throws IOException {
        if (output == null) throw new IllegalArgumentException("null argument");
        Iterator spis = getRegistry().getServiceProviders(ImageOutputStreamSpi.class, true);
        ImageOutputStreamSpi foundSpi = null;
        while (spis.hasNext()) {
            ImageOutputStreamSpi spi = (ImageOutputStreamSpi) spis.next();
            if (output.getClass().equals(spi.getOutputClass())) {
                foundSpi = spi;
                break;
            }
        }
        return foundSpi == null ? null : foundSpi.createOutputStreamInstance(output, getUseCache(), getCacheDirectory());
    }

    /**
   * Retrieve an image reader corresponding to an image writer, or
   * null if writer is not registered or if no corresponding reader is
   * registered.
   *
   * @param writer a registered image writer
   *
   * @return an image reader corresponding to writer, or null
   *
   * @exception IllegalArgumentException if writer is null
   */
    public static ImageReader getImageReader(ImageWriter writer) {
        if (writer == null) throw new IllegalArgumentException("null argument");
        ImageWriterSpi spi = (ImageWriterSpi) getRegistry().getServiceProviderByClass(writer.getClass());
        String[] readerSpiNames = spi.getImageReaderSpiNames();
        ImageReader r = null;
        if (readerSpiNames != null) {
            try {
                Class readerClass = Class.forName(readerSpiNames[0]);
                r = (ImageReader) readerClass.newInstance();
            } catch (Exception e) {
                return null;
            }
        }
        return r;
    }

    /**
   * Retrieve an iterator over the collection of registered image
   * readers that support reading data from the given object.
   *
   * @param input the object for which to retrieve image readers
   *
   * @return an iterator over a collection of image readers
   */
    public static Iterator getImageReaders(Object input) {
        if (input == null) throw new IllegalArgumentException("null argument");
        return getRegistry().getServiceProviders(ImageReaderSpi.class, new ReaderObjectFilter(input), true);
    }

    /**
   * Retrieve an iterator over the collection of registered image
   * writers that support writing images of the given type and in the
   * given format.
   *
   * @param type the output image's colour and sample models
   * @param formatName the output image format
   *
   * @return an iterator over a collection of image writers
   */
    public static Iterator getImageWriters(ImageTypeSpecifier type, String formatName) {
        if (type == null || formatName == null) throw new IllegalArgumentException("null argument");
        return getRegistry().getServiceProviders(ImageWriterSpi.class, new WriterObjectFilter(type, formatName), true);
    }

    /**
   * Retrieve an image writer corresponding to an image reader, or
   * null if reader is not registered or if no corresponding writer is
   * registered.  This method is useful for preserving metadata
   * without needing to understand its format, since the returned
   * writer will be able to write, unchanged, the metadata passed to
   * it by the reader.
   *
   * @param reader a registered image reader
   *
   * @return an image writer corresponding to reader, or null
   *
   * @exception IllegalArgumentException if reader is null
   */
    public static ImageWriter getImageWriter(ImageReader reader) {
        if (reader == null) throw new IllegalArgumentException("null argument");
        ImageReaderSpi spi = (ImageReaderSpi) getRegistry().getServiceProviderByClass(reader.getClass());
        String[] writerSpiNames = spi.getImageWriterSpiNames();
        ImageWriter w = null;
        if (writerSpiNames != null) {
            try {
                Class writerClass = Class.forName(writerSpiNames[0]);
                w = (ImageWriter) writerClass.newInstance();
            } catch (Exception e) {
                return null;
            }
        }
        return w;
    }

    /**
   * Retrieve an iterator over a collection of image transcoders that
   * support transcoding from the given image reader's metadata format
   * to the given writer's metadata format.
   *
   * @param reader an image reader
   * @param writer an image writer
   *
   * @return an iterator over a collection of image transcoders
   *
   * @exception IllegalArgumentException if either reader or writer is
   * null
   */
    public static Iterator getImageTranscoders(ImageReader reader, ImageWriter writer) {
        if (reader == null || writer == null) throw new IllegalArgumentException("null argument");
        return getRegistry().getServiceProviders(ImageTranscoderSpi.class, new TranscoderFilter(reader, writer), true);
    }
}
