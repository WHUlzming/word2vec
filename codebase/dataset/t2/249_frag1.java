        if ("cost".equals(pColumn)) {

            fComparator = new BigDecimalComparator();

            java.math.BigDecimal[] temp = new java.math.BigDecimal[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getCost();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        if ("costPostProcessing".equals(pColumn)) {

            fComparator = new BigDecimalComparator();

            java.math.BigDecimal[] temp = new java.math.BigDecimal[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getCostPostProcessing();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        fSortedColumn = pColumn;
