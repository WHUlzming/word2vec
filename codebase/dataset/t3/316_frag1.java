    public void closeSession() {

        if (activeAssociation != null) {

            try {

                activeAssociation.release(true);

            } catch (Exception e) {

                log.warn("release association throws:", e);

            }

            activeAssociation = null;

        }

    }
