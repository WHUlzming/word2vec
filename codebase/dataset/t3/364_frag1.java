        if ("itu".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getItu();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("fips".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getFips();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("ds".equals(pColumn)) {
