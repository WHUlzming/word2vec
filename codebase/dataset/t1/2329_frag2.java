                        numSigBytes = decode4to3(b4, 0, buffer, 0, options);

                        position = 0;

                    } else if (i == 0) {

                        return -1;

                    } else {

                        throw new java.io.IOException("Improperly padded Base64 input.");

                    }

                }

            }

            if (position >= 0) {

                if (position >= numSigBytes) return -1;

                if (encode && breakLines && lineLength >= MAX_LINE_LENGTH) {

                    lineLength = 0;

                    return '\n';

                } else {

                    lineLength++;

                    int b = buffer[position++];

                    if (position >= bufferLength) position = -1;

                    return b & 0xFF;

                }

            } else {

                throw new java.io.IOException("Error in Base64 code reading stream.");

            }

        }
