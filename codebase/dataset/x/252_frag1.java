        if ((((DX7FamilyDevice) (getDevice())).getSwOffMemProtFlag() & 0x01) == 1) {

            YamahaTX802SysexHelpers.swOffMemProt(this, (byte) (getChannel() + 0x10));

        } else {

            if ((((DX7FamilyDevice) (getDevice())).getTipsMsgFlag() & 0x01) == 1) {
