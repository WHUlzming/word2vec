            long[] temp = new long[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getListSubType();

            }

            fSortOrder = sorter.sortLong(temp, fSortOrder, up);

        }
