            if (StartPosB == -1) {

                StartPosB = myJobLine.length() - 1;

                System.out.println("StartPosA:" + StartPosA);

                System.out.println("StartPosB:" + StartPosB);

                for (int i = 0; i < 5; i++) {

                    GenTD meinGenTD = new GenTD("graphs/" + myJobLine.substring(StartPosA, StartPosB));
