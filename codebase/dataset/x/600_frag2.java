    public void FetchAll() {

        try {

            BuildAndSendQuery();

            ReadQueryHeader();

            AllocateArray();

            while (FindRecordHeader()) {

                ReadRecord();

                ReadRecordTrailer();

            }

            ReadQueryTrailer();

            in.close();

        } catch (IOException e) {

        }

    }
