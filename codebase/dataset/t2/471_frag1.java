        if ("statusName".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getStatusName();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("dbUser".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getDbUser();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("userID".equals(pColumn)) {
