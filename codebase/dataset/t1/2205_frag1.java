    public static void sysWriteln(String s1, String s2) throws VM_PragmaNoInline {

        swLock();

        write(s1);

        write(s2);

        writeln();

        swUnlock();

    }
