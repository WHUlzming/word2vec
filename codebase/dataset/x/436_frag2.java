    public void setChannelRB(Channel ch_RB) {

        synchronized (lockObj) {

            mpvRB.setChannel(ch_RB);

            isRestoreFromRB = true;

        }

    }
