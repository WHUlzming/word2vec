    private byte[] exportAutoEpgList(HTTPurl urlData) throws Exception {

        StringBuffer buff = new StringBuffer();

        buff.append("HTTP/1.0 200 OK\nContent-Type: text/xml\n");

        buff.append("Content-Disposition: attachment; filename=\"EpgAutoAdd.xml\"\n");

        buff.append("Pragma: no-cache\n");

        buff.append("Cache-Control: no-cache\n");

        buff.append("\n");

        store.saveEpgAutoList(buff);

        return buff.toString().getBytes();

    }
