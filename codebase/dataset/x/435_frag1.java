            pmdata = new PlayerMcomData();

            XdrBufferDecodingStream xdr = new XdrBufferDecodingStream(buffer);

            xdr.beginDecoding();

            pmdata.setFull((char) xdr.xdrDecodeByte());

            int dataCount = xdr.xdrDecodeInt();

            xdr.endDecoding();

            xdr.close();