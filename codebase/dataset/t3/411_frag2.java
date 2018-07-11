    public static final String replaceIgnoreCase(String line, String oldString, String newString, int[] count) {

        if (line == null) {

            return null;

        }

        String lcLine = line.toLowerCase();

        String lcOldString = oldString.toLowerCase();

        int i = 0;

        if ((i = lcLine.indexOf(lcOldString, i)) >= 0) {

            int counter = 0;

            char[] line2 = line.toCharArray();

            char[] newString2 = newString.toCharArray();

            int oLength = oldString.length();

            StringBuffer buf = new StringBuffer(line2.length);

            buf.append(line2, 0, i).append(newString2);

            i += oLength;

            int j = i;

            while ((i = lcLine.indexOf(lcOldString, i)) > 0) {

                counter++;

                buf.append(line2, j, i - j).append(newString2);

                i += oLength;

                j = i;

            }

            buf.append(line2, j, line2.length - j);

            count[0] = counter;

            return buf.toString();

        }

        return line;

    }
