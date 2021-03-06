                    ds = oFact.newDataset();

                    parser.setDcmHandler(ds.getDcmHandler());

                    parser.parseDcmFile(format, Tags.PixelData);

                    if (parser.getReadTag() == Tags.PixelData) {

                        if (parser.getStreamPosition() + parser.getReadLength() > file.length()) {

                            throw new EOFException("Pixel Data Length: " + parser.getReadLength() + " exceeds file length: " + file.length());

                        }

                    }

                    log.info(MessageFormat.format(messages.getString("readDone"), new Object[] { file }));
