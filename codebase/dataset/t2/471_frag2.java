        if ("clearingNr".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getClearingNr();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("accountNumber".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getAccountNumber();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("ibanNumber".equals(pColumn)) {
