    public RolandMT32SystemDriver(final Device device) {

        super(device, "System", "Fred Jan Kraan");

        sysexID = "F041**16";

        patchSize = HSIZE + SSIZE + 1;

        patchNameStart = 0;

        patchNameSize = 0;

        deviceIDoffset = 0;

        checksumStart = 5;

        checksumEnd = 10;

        checksumOffset = 0;

        bankNumbers = new String[] { "" };

        patchNumbers = new String[] { "" };

    }
