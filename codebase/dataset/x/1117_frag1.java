        if ("ktvID".equals(pColumn)) {

            Sorter sorter = new Sorter();

            long[] temp = new long[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getKtvID();

            }

            fSortOrder = sorter.sortLong(temp, fSortOrder, up);

        }

        if ("qstCode".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getQstCode();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("qstStateCode".equals(pColumn)) {
