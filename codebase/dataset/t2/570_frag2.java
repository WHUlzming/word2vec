        if ("transactionCount".equals(pColumn)) {

            Sorter sorter = new Sorter();

            long[] temp = new long[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getTransactionCount();

            }

            fSortOrder = sorter.sortLong(temp, fSortOrder, up);

        }

        if ("creationDate".equals(pColumn)) {

            fComparator = new DateComparator();

            java.sql.Date[] temp = new java.sql.Date[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getCreationDate();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("cost".equals(pColumn)) {
