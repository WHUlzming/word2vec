        }

    }



    private void promptErrRSP(String prefix, int status, FileInfo info, DicomObject cmd) {

        System.err.println(prefix + StringUtils.shortToHex(status) + "H for " + info.f + ", cuid=" + info.cuid + ", tsuid=" + info.tsuid);

        System.err.println(cmd.toString());

    }



    private void onDimseRSP(DicomObject cmd) {

        int status = cmd.getInt(Tag.Status);

        int msgId = cmd.getInt(Tag.MessageIDBeingRespondedTo);

        FileInfo info = files.get(msgId - 1);
