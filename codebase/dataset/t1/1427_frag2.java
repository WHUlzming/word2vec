            if (excludePrivate) ds.excludePrivate().writeDataset(out, netParam); else ds.writeDataset(out, netParam);

            if (parser.getReadTag() == Tags.PixelData) {

                DcmDecodeParam fileParam = parser.getDcmDecodeParam();

                ds.writeHeader(out, netParam, parser.getReadTag(), parser.getReadVR(), parser.getReadLength());

                if (netParam.encapsulated) {

                    parser.parseHeader();

                    while (parser.getReadTag() == Tags.Item) {

                        ds.writeHeader(out, netParam, parser.getReadTag(), parser.getReadVR(), parser.getReadLength());

                        writeValueTo(out, false);
