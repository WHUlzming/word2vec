                } else if (prefix1.equals("Age")) {

                    int agestart = infoProperty.indexOf("0010,1010");

                    int ageend = agestart + 30;

                    String age = infoProperty.substring(agestart + 25, ageend);

                    whichprefix1 = age.trim();

                } else if (prefix1.equals("Sex")) {
