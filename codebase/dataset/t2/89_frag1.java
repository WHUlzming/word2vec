        if ("flagPersonal".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getFlagPersonal();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("creationDate".equals(pColumn)) {

            fComparator = new DateComparator();

            java.sql.Date[] temp = new java.sql.Date[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getCreationDate();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("dueDate".equals(pColumn)) {

            fComparator = new DateComparator();

            java.sql.Date[] temp = new java.sql.Date[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getDueDate();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("recipientID".equals(pColumn)) {
