    void saveLastPosition() {

        savedNextAvailableLsn = nextAvailableLsn;

        savedLastUsedLsn = lastUsedLsn;

        savedPrevOffset = prevOffset;

        savedForceNewFile = forceNewFile;

        savedCurrentFileNum = currentFileNum;

    }
