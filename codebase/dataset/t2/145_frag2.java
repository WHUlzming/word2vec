        if ("orderID".equals(pColumn)) {

            Sorter sorter = new Sorter();

            long[] temp = new long[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getOrderID();

            }

            fSortOrder = sorter.sortLong(temp, fSortOrder, up);

        }

        if ("customerID".equals(pColumn)) {

            Sorter sorter = new Sorter();

            long[] temp = new long[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getCustomerID();

            }

            fSortOrder = sorter.sortLong(temp, fSortOrder, up);

        }

        if ("amount".equals(pColumn)) {
