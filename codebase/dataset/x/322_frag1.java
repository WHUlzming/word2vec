            } else if (ch == '&') {

                if (i > last) {

                    out.append(input, last, i - last);

                }

                last = i + 1;

                out.append(AMP_ENCODE);

            } else if (ch == '"') {

                if (i > last) {

                    out.append(input, last, i - last);

                }

                last = i + 1;

                out.append(QUOTE_ENCODE);

            }

        }

        if (last == 0) {

            return string;

        }

        if (i > last) {

            out.append(input, last, i - last);

        }

        return out.toString();

    }



    public static final String escapeForSpecial(String string) {

        if (string == null) {

            return null;

        }

        char ch;

        int i = 0;

        int last = 0;

        char[] input = string.toCharArray();

        int len = input.length;

        StringBuffer out = new StringBuffer((int) (len * 1.3));

        for (; i < len; i++) {

            ch = input[i];

            if (ch > '>') {

                continue;

            } else if (ch == '<') {

                if (i > last) {

                    out.append(input, last, i - last);

                }

                last = i + 1;

                out.append(LT_ENCODE);

            } else if (ch == '&') {

                if (i > last) {

                    out.append(input, last, i - last);

                }

                last = i + 1;
