            int len43 = len * 4 / 3;

            byte[] outBuff = new byte[(len43) + ((len % 3) > 0 ? 4 : 0) + (breakLines ? (len43 / MAX_LINE_LENGTH) : 0)];

            int d = 0;

            int e = 0;

            int len2 = len - 2;

            int lineLength = 0;

            for (; d < len2; d += 3, e += 4) {

                encode3to4(source, d + off, 3, outBuff, e, options);

                lineLength += 4;

                if (breakLines && lineLength == MAX_LINE_LENGTH) {

                    outBuff[e + 4] = NEW_LINE;

                    e++;

                    lineLength = 0;

                }

            }

            if (d < len) {

                encode3to4(source, d + off, len - d, outBuff, e, options);
