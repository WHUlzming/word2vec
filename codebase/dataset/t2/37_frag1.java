        if ("amountCredit".equals(pColumn)) {

            fComparator = new BigDecimalComparator();

            java.math.BigDecimal[] temp = new java.math.BigDecimal[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getAmountCredit();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("currencyCredit".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getCurrencyCredit();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("exchangeRate".equals(pColumn)) {
