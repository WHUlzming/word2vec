        if ("principal".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getPrincipal();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("currencyBook".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getCurrencyBook();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("amount".equals(pColumn)) {
