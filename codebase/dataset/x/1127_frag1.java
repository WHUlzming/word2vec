        if ("paramName".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getParamName();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("paramType".equals(pColumn)) {

            fComparator = new StringComparator();

            String[] temp = new String[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getParamType();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("intParam".equals(pColumn)) {
