        if ("obj".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getObj();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("privilege".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getPrivilege();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("type".equals(pColumn)) {
