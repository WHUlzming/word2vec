    public void reload() {

        try {

            this.holder = loadChannelsFromNetAndStore();

        } catch (Exception e) {

            Log.e(CLASS_NAME, "Reload failed");

        }

    }
