    public String getMonth() {

        String sVal = get("val");

        if (sVal.compareTo("") == 0) {

            return "-1";

        }

        String sPattern = "^(\\d+|X+)-(\\d+|H\\d|Q\\d|SP|SU|WI|FA)(-|$)";

        Pattern pattern = Pattern.compile(sPattern);

        Matcher m = pattern.matcher(sVal);

        if (m.find()) {

            try {

                String sRes = m.group(2);

                return sRes;

            } catch (Exception e) {

            }

        }

        return "-1";

    }



    public int getWeek() {

        String sVal = get("val");

        if (sVal.compareTo("") == 0) return -1;

        String sPattern = "W(\\d+)(-|$)";

        Pattern pattern = Pattern.compile(sPattern);

        Matcher m = pattern.matcher(sVal);

        if (m.find()) {

            try {

                int iRes = Integer.parseInt(m.group(1));

                return iRes;

            } catch (Exception e) {

            }

        }

        return -1;

    }



    public int getDayOfMonth() {
