    public int dumpText() {

        try {

            do {

                while (htmlDoc[dumpPos] == LT) {

                    if ((htmlDoc[dumpPos + 1] == '!') && (htmlDoc[dumpPos + 2] == DASH) && (htmlDoc[dumpPos + 3] == DASH) && (htmlDoc[dumpPos + 4] == SPACE) && (htmlDoc[dumpPos + 5] == 'h') && (htmlDoc[dumpPos + 6] == 'o') && (htmlDoc[dumpPos + 7] == 'u') && (htmlDoc[dumpPos + 8] == 's') && (htmlDoc[dumpPos + 9] == 'e') && (htmlDoc[dumpPos + 10] == 's') && (htmlDoc[dumpPos + 11] == 'p') && (htmlDoc[dumpPos + 12] == 'i') && (htmlDoc[dumpPos + 13] == 'd') && (htmlDoc[dumpPos + 14] == 'e') && (htmlDoc[dumpPos + 15] == 'r') && (htmlDoc[dumpPos + 16] == SPACE)) {

                        dumpPos += 16;

                        if ((htmlDoc[dumpPos + 1] == 'n') && (htmlDoc[dumpPos + 2] == 'o') && (htmlDoc[dumpPos + 3] == 'i') && (htmlDoc[dumpPos + 4] == 'n') && (htmlDoc[dumpPos + 5] == 'd') && (htmlDoc[dumpPos + 6] == 'e') && (htmlDoc[dumpPos + 7] == 'x') && (htmlDoc[dumpPos + 8] == SPACE) && (htmlDoc[dumpPos + 9] == DASH) && (htmlDoc[dumpPos + 10] == DASH) && (htmlDoc[dumpPos + 11] == GT)) {

                            dumpPos += 11;

                            index = false;

                        } else if ((htmlDoc[dumpPos + 1] == 'i') && (htmlDoc[dumpPos + 2] == 'n') && (htmlDoc[dumpPos + 3] == 'd') && (htmlDoc[dumpPos + 4] == 'e') && (htmlDoc[dumpPos + 5] == 'x') && (htmlDoc[dumpPos + 6] == SPACE) && (htmlDoc[dumpPos + 7] == DASH) && (htmlDoc[dumpPos + 8] == DASH) && (htmlDoc[dumpPos + 9] == GT)) {

                            dumpPos += 9;

                            index = true;

                        }

                    } else if (!headdone && (Character.toLowerCase(htmlDoc[dumpPos + 1]) == 'h') && (Character.toLowerCase(htmlDoc[dumpPos + 2]) == 'e') && (Character.toLowerCase(htmlDoc[dumpPos + 3]) == 'a') && (Character.toLowerCase(htmlDoc[dumpPos + 4]) == 'd') && (htmlDoc[dumpPos + 5] == GT)) {

                        dumpPos += 5;

                        titledone = true;

                        dumpPos += 6;

                        while ((dumpPos < htmlDocLength) && !((htmlDoc[dumpPos - 6] == LT) && (Character.toLowerCase(htmlDoc[dumpPos - 5]) == '/') && (Character.toLowerCase(htmlDoc[dumpPos - 4]) == 'h') && (Character.toLowerCase(htmlDoc[dumpPos - 3]) == 'e') && (Character.toLowerCase(htmlDoc[dumpPos - 2]) == 'a') && (Character.toLowerCase(htmlDoc[dumpPos - 1]) == 'd') && (htmlDoc[dumpPos] == GT))) {

                            if ((htmlDoc[dumpPos - 6] == LT) && (Character.toLowerCase(htmlDoc[dumpPos - 5]) == 'b') && (Character.toLowerCase(htmlDoc[dumpPos - 4]) == 'o') && (Character.toLowerCase(htmlDoc[dumpPos - 3]) == 'd') && (Character.toLowerCase(htmlDoc[dumpPos - 2]) == 'y') && (htmlDoc[dumpPos - 1] == GT)) {

                                hasbody = true;

                                System.out.println("Warning, missing \"</head>\".");

                                break;

                            } else {

                                dumpPos++;

                            }

                        }

                    } else if (!titledone && (Character.toLowerCase(htmlDoc[dumpPos + 1]) == 't') && (Character.toLowerCase(htmlDoc[dumpPos + 2]) == 'i') && (Character.toLowerCase(htmlDoc[dumpPos + 3]) == 't') && (Character.toLowerCase(htmlDoc[dumpPos + 4]) == 'l') && (Character.toLowerCase(htmlDoc[dumpPos + 5]) == 'e') && (htmlDoc[dumpPos + 6] == GT)) {

                        dumpPos += 6;

                        titledone = true;

                        dumpPos += 7;

                        while ((dumpPos < htmlDocLength) && (!((htmlDoc[dumpPos - 7] == LT) && (Character.toLowerCase(htmlDoc[dumpPos - 6]) == '/') && (Character.toLowerCase(htmlDoc[dumpPos - 5]) == 't') && (Character.toLowerCase(htmlDoc[dumpPos - 4]) == 'i') && (Character.toLowerCase(htmlDoc[dumpPos - 3]) == 't') && (Character.toLowerCase(htmlDoc[dumpPos - 2]) == 'l') && (Character.toLowerCase(htmlDoc[dumpPos - 1]) == 'e') && (htmlDoc[dumpPos] == GT)))) dumpPos++;

                    } else if ((Character.toLowerCase(htmlDoc[dumpPos + 1]) == 's') && (Character.toLowerCase(htmlDoc[dumpPos + 2]) == 'c') && (Character.toLowerCase(htmlDoc[dumpPos + 3]) == 'r') && (Character.toLowerCase(htmlDoc[dumpPos + 4]) == 'i') && (Character.toLowerCase(htmlDoc[dumpPos + 5]) == 'p') && (Character.toLowerCase(htmlDoc[dumpPos + 6]) == 't')) {

                        dumpPos += 6;

                        dumpPos += 8;

                        while ((dumpPos < htmlDocLength) && (!((htmlDoc[dumpPos - 8] == LT) && (Character.toLowerCase(htmlDoc[dumpPos - 7]) == '/') && (Character.toLowerCase(htmlDoc[dumpPos - 6]) == 's') && (Character.toLowerCase(htmlDoc[dumpPos - 5]) == 'c') && (Character.toLowerCase(htmlDoc[dumpPos - 4]) == 'r') && (Character.toLowerCase(htmlDoc[dumpPos - 3]) == 'i') && (Character.toLowerCase(htmlDoc[dumpPos - 2]) == 'p') && (Character.toLowerCase(htmlDoc[dumpPos - 1]) == 't') && (htmlDoc[dumpPos] == GT)))) dumpPos++;

                    } else if ((Character.toLowerCase(htmlDoc[dumpPos + 1]) == 's') && (Character.toLowerCase(htmlDoc[dumpPos + 2]) == 't') && (Character.toLowerCase(htmlDoc[dumpPos + 3]) == 'y') && (Character.toLowerCase(htmlDoc[dumpPos + 4]) == 'l') && (Character.toLowerCase(htmlDoc[dumpPos + 5]) == 'e')) {

                        dumpPos += 5;

                        dumpPos += 7;

                        while ((dumpPos < htmlDocLength) && (!((htmlDoc[dumpPos - 7] == LT) && (Character.toLowerCase(htmlDoc[dumpPos - 6]) == '/') && (Character.toLowerCase(htmlDoc[dumpPos - 5]) == 's') && (Character.toLowerCase(htmlDoc[dumpPos - 4]) == 't') && (Character.toLowerCase(htmlDoc[dumpPos - 3]) == 'y') && (Character.toLowerCase(htmlDoc[dumpPos - 2]) == 'l') && (Character.toLowerCase(htmlDoc[dumpPos - 1]) == 'e') && (htmlDoc[dumpPos] == GT)))) dumpPos++;

                    } else if ((Character.toLowerCase(htmlDoc[dumpPos + 1]) == 'n') && (Character.toLowerCase(htmlDoc[dumpPos + 2]) == 'o') && (Character.toLowerCase(htmlDoc[dumpPos + 3]) == 'i') && (Character.toLowerCase(htmlDoc[dumpPos + 4]) == 'n') && (Character.toLowerCase(htmlDoc[dumpPos + 5]) == 'd') && (Character.toLowerCase(htmlDoc[dumpPos + 6]) == 'e') && (Character.toLowerCase(htmlDoc[dumpPos + 7]) == 'x')) {

                        dumpPos += 7;

                        dumpPos += 9;

                        while ((dumpPos < htmlDocLength) && (!((htmlDoc[dumpPos - 9] == LT) && (Character.toLowerCase(htmlDoc[dumpPos - 8]) == '/') && (Character.toLowerCase(htmlDoc[dumpPos - 7]) == 'n') && (Character.toLowerCase(htmlDoc[dumpPos - 6]) == 'o') && (Character.toLowerCase(htmlDoc[dumpPos - 5]) == 'i') && (Character.toLowerCase(htmlDoc[dumpPos - 4]) == 'n') && (Character.toLowerCase(htmlDoc[dumpPos - 3]) == 'd') && (Character.toLowerCase(htmlDoc[dumpPos - 2]) == 'e') && (Character.toLowerCase(htmlDoc[dumpPos - 1]) == 'x') && (htmlDoc[dumpPos] == GT)))) dumpPos++;

                    } else if ((htmlDoc[dumpPos + 1] == '!') && (htmlDoc[dumpPos + 2] == DASH) && (htmlDoc[dumpPos + 3] == DASH)) {

                        dumpPos += 3;

                        while ((dumpPos < htmlDocLength) && (!((htmlDoc[dumpPos - 2] == DASH) && (htmlDoc[dumpPos - 1] == DASH) && (htmlDoc[dumpPos] == GT)))) dumpPos++;

                    } else if (!hasbody && (Character.toLowerCase(htmlDoc[dumpPos + 1]) == 'b') && (Character.toLowerCase(htmlDoc[dumpPos + 2]) == 'o') && (Character.toLowerCase(htmlDoc[dumpPos + 3]) == 'd') && (Character.toLowerCase(htmlDoc[dumpPos + 4]) == 'y')) {

                        hasbody = true;

                    } else if (!frameset && (Character.toLowerCase(htmlDoc[dumpPos + 1]) == 'f') && (Character.toLowerCase(htmlDoc[dumpPos + 2]) == 'r') && (Character.toLowerCase(htmlDoc[dumpPos + 3]) == 'a') && (Character.toLowerCase(htmlDoc[dumpPos + 4]) == 'm') && (Character.toLowerCase(htmlDoc[dumpPos + 5]) == 'e') && (Character.toLowerCase(htmlDoc[dumpPos + 6]) == 's') && (Character.toLowerCase(htmlDoc[dumpPos + 7]) == 'e') && (Character.toLowerCase(htmlDoc[dumpPos + 8]) == 't')) {

                        frameset = true;

                    }

                    while (htmlDoc[dumpPos++] != GT) ;

                }

                i = 0;

                if (!index) {

                    dumpPos++;

                } else {

                    while ((htmlDoc[dumpPos] == SPACE) && (ignoreWords[i] != null) && (i < MAXIGNORE)) {

                        len = ignoreWords[i].length();

                        StringBuffer strbuf = new StringBuffer();

                        for (int j = 1; j < len + 1; j++) {

                            strbuf.append(htmlDoc[dumpPos + j]);

                        }

                        str = strbuf.toString();

                        if (str.equalsIgnoreCase(ignoreWords[i]) && ((htmlDoc[dumpPos + len + 1] == SPACE) || (htmlDoc[dumpPos + len + 1] == '.') || (htmlDoc[dumpPos + len + 1] == ',') || (htmlDoc[dumpPos + len + 1] == ':') || (htmlDoc[dumpPos + len + 1] == ';') || (htmlDoc[dumpPos + len + 1] == '!') || (htmlDoc[dumpPos + len + 1] == '?'))) {

                            dumpPos = dumpPos + len + 1;

                            if (debug >= 3) System.out.println("Ignoring \"" + ignoreWords[i] + "\".");

                            i = 0;

                        } else i++;

                    }

                    if ((dumpLastChar == SPACE) && (htmlDoc[dumpPos] == SPACE)) dumpPos++; else {

                        if (dumpPos >= htmlDocLength) return (NEWLINE);

                        dumpLastChar = htmlDoc[dumpPos];

                        return (htmlDoc[dumpPos++]);

                    }

                }

            } while (true);

        } catch (ArrayIndexOutOfBoundsException aioobe) {

            return (NEWLINE);

        }

    }
