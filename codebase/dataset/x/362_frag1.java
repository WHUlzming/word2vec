    public boolean startScan() {

        if (CURRENT_BUTTONS_STATE != START_BUTTONS_STATE) {

            return false;

        }

        startButtonListener.actionPerformed(startButtonAction);

        return true;

    }
