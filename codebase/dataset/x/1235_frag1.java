        if ("amountRef".equals(pColumn)) {

            fComparator = new BigDecimalComparator();

            java.math.BigDecimal[] temp = new java.math.BigDecimal[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getAmountRef();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("userID".equals(pColumn)) {

            Sorter sorter = new Sorter();

            long[] temp = new long[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getUserID();

            }

            fSortOrder = sorter.sortLong(temp, fSortOrder, up);

        }

        if ("validFrom".equals(pColumn)) {
