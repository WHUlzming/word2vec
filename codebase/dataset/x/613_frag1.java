    public boolean documentExists(String name, String container) {

        XmlContainer cont = null;

        boolean ret = false;

        try {

            cont = getContainer(container);

            XmlDocument doc = cont.getDocument(name);

            ret = true;

        } catch (Exception ex) {

            ret = false;

        } finally {

            try {

                if (cont != null) {

                }

            } catch (Exception ex) {

                ex.printStackTrace();

            }

        }

        return ret;

    }
