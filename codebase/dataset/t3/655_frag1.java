        if ((writeLevel & 0xFFFFFFF0) != 0) throw new IllegalArgumentException("'writeLevel' out of range!");

        byte[] aPDU = TFrame.getNew(8);

        aPDU[TFrame.APDU_START + 0] = (byte) (APCI.PROPERTYDESCR_RES >> 24);

        aPDU[TFrame.APDU_START + 1] = (byte) (APCI.PROPERTYDESCR_RES >> 16);

        aPDU[TFrame.APDU_START + 2] = (byte) objIdx;

        aPDU[TFrame.APDU_START + 3] = (byte) propID;
