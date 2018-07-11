        if ("esrAccountID".equals(pColumn)) {

            Sorter sorter = new Sorter();

            long[] temp = new long[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getEsrAccountID();

            }

            fSortOrder = sorter.sortLong(temp, fSortOrder, up);

        }

        if ("recipientID".equals(pColumn)) {

            Sorter sorter = new Sorter();

            long[] temp = new long[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getRecipientID();

            }

            fSortOrder = sorter.sortLong(temp, fSortOrder, up);

        }

        if ("beneficiaryID".equals(pColumn)) {

            Sorter sorter = new Sorter();

            long[] temp = new long[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getBeneficiaryID();

            }

            fSortOrder = sorter.sortLong(temp, fSortOrder, up);

        }

        if ("esrReferenceNumber".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getEsrReferenceNumber();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("esrCorrectionCode".equals(pColumn)) {
