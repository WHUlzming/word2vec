        if ("provolatile".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getProvolatile();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("pronargs".equals(pColumn)) {

            Sorter sorter = new Sorter();

            short[] temp = new short[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getPronargs();

            }

            fSortOrder = sorter.sortShort(temp, fSortOrder, up);

        }

        if ("prorettype".equals(pColumn)) {
