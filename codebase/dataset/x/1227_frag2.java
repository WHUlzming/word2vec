        if (!"u".equals(pSortDirection)) {

            up = false;

        }

        if ("id".equals(pColumn)) {

            Sorter sorter = new Sorter();

            long[] temp = new long[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getId();

            }

            fSortOrder = sorter.sortLong(temp, fSortOrder, up);

        }

        if ("trxID".equals(pColumn)) {

            Sorter sorter = new Sorter();

            long[] temp = new long[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getTrxID();

            }

            fSortOrder = sorter.sortLong(temp, fSortOrder, up);

        }

        if ("typeID".equals(pColumn)) {

            Sorter sorter = new Sorter();

            long[] temp = new long[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getTypeID();

            }

            fSortOrder = sorter.sortLong(temp, fSortOrder, up);

        }

        if ("typeName".equals(pColumn)) {
