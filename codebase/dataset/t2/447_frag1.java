        if ("prorettype".equals(pColumn)) {

            Sorter sorter = new Sorter();

            long[] temp = new long[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getProrettype();

            }

            fSortOrder = sorter.sortLong(temp, fSortOrder, up);

        }

        if ("proargtypes".equals(pColumn)) {

            fComparator = new ObjectComparator();

            Object[] temp = new Object[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getProargtypes();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("proargnames".equals(pColumn)) {

            fComparator = new ObjectComparator();

            Object[] temp = new Object[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getProargnames();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("prosrc".equals(pColumn)) {
