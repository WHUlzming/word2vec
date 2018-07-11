    public Long getBitrateAsNumer(String pathToFile) {

        Long bitrateAsNumer = null;

        try {

            File file = new File(pathToFile);

            AudioFile audioFile = AudioFileIO.read(file);

            AudioHeader audioHeader = audioFile.getAudioHeader();

            if (audioHeader != null) {

                bitrateAsNumer = audioHeader.getBitRateAsNumber();

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return bitrateAsNumer;

    }
