    public static MfiFileFormat getMfiFileFormat(InputStream stream) throws InvalidMfiDataException, IOException {

        for (int i = 0; i < readers.length; i++) {

            try {

                MfiFileFormat mff = readers[i].getMfiFileFormat(stream);

                return mff;

            } catch (Exception e) {

                Debug.println(e);

            }

        }

        throw new InvalidMfiDataException("unsupported stream: " + stream);

    }
