    private String evalPad(StreamTokenizer st) throws IOException {

        if (st.nextToken() != '(') throw new IOException("Missing '('");

        StringBuffer val = new StringBuffer(evalStrExpr(st));

        if (st.nextToken() != ',') throw new IOException("Missing ','");

        int len = evalExpr(st);

        if (st.nextToken() != ',') throw new IOException("Missing ','");

        if (st.nextToken() != '"') throw new IOException("Invalid string");

        String pad = st.sval;

        if (st.nextToken() != ')') throw new IOException("Missing ')'");

        while (val.length() < len) val.append(pad);

        val.setLength(len);

        return val.toString();

    }
