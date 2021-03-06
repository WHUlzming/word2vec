    void processSplitRegion(Vector region) throws IOException {

        if (DEBUG) System.out.println("params=\"" + params + "\"");

        QuotedStringTokenizer pst = new QuotedStringTokenizer(params);

        if (!pst.hasMoreTokens()) throw new IOException("Missing value in SPLIT");

        String value = pst.nextToken();

        if (!pst.hasMoreTokens()) throw new IOException("Missing separators in SPLIT");

        String sep = pst.nextToken();

        if (!pst.hasMoreTokens()) throw new IOException("Missing variables in SPLIT");

        int numVars = pst.countTokens();

        String[] var_names = new String[numVars];

        for (int i = 0; i < numVars; i++) var_names[i] = pst.nextToken();

        StringTokenizer vst = new StringTokenizer(value, sep);

        String[] values = new String[numVars];

        for (int i = 0; i < numVars; i++) if (vst.hasMoreTokens()) values[i] = vst.nextToken(); else values[i] = "";

        if (DEBUG) System.out.println("doing split with value \"" + value + "\" to vars :" + params.substring(value.length() + 3));

        for (int j = 1; j < region.size(); j++) {

            try {

                String currentLine = (String) region.elementAt(j);

                String result = currentLine;

                for (int curVar = 0; curVar < var_names.length; curVar++) result = substitute(result, var_names[curVar], values[curVar]);

                out.print(result + "\n");

            } catch (ClassCastException e) {

                Vector oldRegion = (Vector) region.elementAt(j);

                Vector newRegion = oldRegion;

                for (int curVar = 0; curVar < var_names.length; curVar++) newRegion = substituteInRegion(newRegion, var_names[curVar], values[curVar]);

                processTemplateRegion(newRegion);

            }

        }

    }
