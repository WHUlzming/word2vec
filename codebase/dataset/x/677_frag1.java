        if ("codeAn".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getCodeAn();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("itemNumber".equals(pColumn)) {

            Sorter sorter = new Sorter();

            long[] temp = new long[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getItemNumber();

            }

            fSortOrder = sorter.sortLong(temp, fSortOrder, up);

        }

        if ("supplierID".equals(pColumn)) {

            Sorter sorter = new Sorter();

            long[] temp = new long[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getSupplierID();

            }

            fSortOrder = sorter.sortLong(temp, fSortOrder, up);

        }

        if ("name".equals(pColumn)) {
