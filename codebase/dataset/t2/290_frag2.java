        if ("amountPrevious".equals(pColumn)) {

            fComparator = new BigDecimalComparator();

            java.math.BigDecimal[] temp = new java.math.BigDecimal[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getAmountPrevious();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("amountDebit".equals(pColumn)) {

            fComparator = new BigDecimalComparator();

            java.math.BigDecimal[] temp = new java.math.BigDecimal[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getAmountDebit();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("amountCredit".equals(pColumn)) {
