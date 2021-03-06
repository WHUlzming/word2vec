    public static ThreadIOWaitData ioWaitRead(int fd, double totalWaitTime) {

        long maxWaitNano = getMaxWaitNano(totalWaitTime);

        ThreadIOWaitData waitData = new ThreadIOWaitData(maxWaitNano);

        waitData.readFds = new int[] { fd };

        if (noIoWait) {

            waitData.markAllAsReady();

        } else {

            GreenThread.ioWaitImpl(waitData);

        }

        return waitData;

    }
