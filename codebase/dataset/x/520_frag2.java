        int minSpaceSoft = 1200;

        try {

            minSpaceSoft = Integer.parseInt(store.getProperty("capture.minspacesoft"));

        } catch (Exception e) {

        }

        int autoSelectMethod = 0;

        try {

            autoSelectMethod = Integer.parseInt(store.getProperty("capture.autoselectmethod"));

        } catch (Exception e) {

        }

        try {
