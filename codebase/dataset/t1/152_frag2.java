    void processForeachRegion(Vector region) throws IOException {

        QuotedStringTokenizer pst = new QuotedStringTokenizer(params);

        if (!pst.hasMoreTokens()) throw new IOException("Missing variable in FOREACH");

        String var_name = pst.nextToken();

        if (!pst.hasMoreTokens()) throw new IOException("Missing filename in FOREACH");

        String file_name = pst.nextToken();

        String select = null;

        String start = null;

        String end = null;

        boolean inRange = false;

        if (pst.hasMoreTokens()) {

            select = pst.nextToken();

            if (!pst.hasMoreTokens()) throw new IOException("Missing field value in FOREACH");

            String fval = pst.nextToken();

            int dotdot = fval.indexOf("..");

            if (dotdot != -1 && dotdot == fval.lastIndexOf("..")) {

                start = fval.substring(0, dotdot);

                end = fval.substring(dotdot + 2);

            } else {

                start = fval;

            }

        }

        if (DEBUG) System.out.println("doing foreach with varname " + var_name + " on data file :" + file_name);

        if (DEBUG && select != null) {

            System.out.print("   selecting records with " + select);

            if (end == null) System.out.println(" equal to \"" + start + "\""); else System.out.println(" between \"" + start + "\" and \"" + end + "\"");

        }

        BufferedReader data = new BufferedReader(new FileReader(file_name));
