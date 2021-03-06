    public int getIndexForVMThread(int threadPointer) throws Exception {

        if (threadPointer == 0) return 0;

        try {

            VM_Field field = bmap.findVMField("VM_Thread", "threadSlot");

            int address = mem.read(threadPointer + field.getOffset());

            return address;

        } catch (BmapNotFoundException e) {

            throw new Exception("cannot find VM_Thread.threadSlot, has VM_Thread.java been changed?");

        }

    }
