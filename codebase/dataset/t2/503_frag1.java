                } else if (prefix2.equals("Sex")) {

                    int sexstart = infoProperty.indexOf("0010,0040");

                    int sexend = sexstart + 27;

                    String sex = infoProperty.substring(sexstart + 25, sexend);

                    whichprefix2 = sex.trim();

                } else if (prefix2.equals("StudyDescription")) {
