            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getValueText();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("validFrom".equals(pColumn)) {

            fComparator = new DateComparator();

            java.sql.Date[] temp = new java.sql.Date[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getValidFrom();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("validTo".equals(pColumn)) {
