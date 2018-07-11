    protected Object readObjectFile(File file) throws Exception {

        FileInputStream fis = new FileInputStream(file);

        try {

            ObjectInputStream dis = new ObjectInputStream(fis);

            Object object = dis.readObject();

            dis.close();

            return object;

        } catch (Exception e) {

            throw e;

        } finally {

            fis.close();

        }

    }
