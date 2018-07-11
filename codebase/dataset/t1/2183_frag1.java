    private String evalIf(StreamTokenizer st) throws IOException {

        if (st.nextToken() != '(') throw new IOException("Missing '('");

        boolean cond = evalCond(st);

        if (st.nextToken() != ',') throw new IOException("Missing ','");

        String valtrue = evalStrExpr(st);

        if (st.nextToken() != ',') throw new IOException("Missing ','");

        String valfalse = evalStrExpr(st);

        if (st.nextToken() != ')') throw new IOException("Missing ')'");

        if (cond) return valtrue;

        return valfalse;

    }
