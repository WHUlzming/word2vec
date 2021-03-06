    private String threadQueueToString(int queuePointer) {

        String result = "";

        int count = 0;

        int fieldOffset;

        int thisThreadPointer, headThreadPointer, tailThreadPointer;

        VM_Field field;

        try {

            field = bmap.findVMField("VM_ThreadQueue", "head");

            headThreadPointer = mem.read(queuePointer + field.getOffset());

            field = bmap.findVMField("VM_ThreadQueue", "tail");

            tailThreadPointer = mem.read(queuePointer + field.getOffset());

            thisThreadPointer = headThreadPointer;

            if (thisThreadPointer != 0) {

                result += "   " + threadToString(thisThreadPointer) + "\n";

                count++;

            }

            while (thisThreadPointer != tailThreadPointer) {

                field = bmap.findVMField("VM_Thread", "next");

                thisThreadPointer = mem.read(thisThreadPointer + field.getOffset());

                if (thisThreadPointer != 0) {

                    result += "   " + threadToString(thisThreadPointer) + "\n";

                    count++;

                } else {

                    thisThreadPointer = tailThreadPointer;

                }

            }

        } catch (BmapNotFoundException e) {

            return "ERROR: cannot find VM_ThreadQueue.head or tail, has VM_ThreadQueue been changed?";

        }

        String heading = "";

        heading += "  ID  VM_Thread   top stack frame\n";

        heading += "  -- -----------  -----------------\n";

        return "Threads in queue:  " + count + "\n" + heading + result;

    }
