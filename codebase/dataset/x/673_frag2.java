        if ("recipient".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getRecipient();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("beneficiary".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getBeneficiary();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("message4x35".equals(pColumn)) {
