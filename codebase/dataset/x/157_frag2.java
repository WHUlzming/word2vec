        if ("modified".equals(pColumn)) {

            fComparator = new TimestampComparator();

            java.sql.Timestamp[] temp = new java.sql.Timestamp[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getModified();

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

            fComparator = new DateComparator();

            java.sql.Date[] temp = new java.sql.Date[fTableData.length];

            for (int i = 0; i < temp.length; i++) {

                temp[i] = fTableData[i].getValidTo();

            }

            sort(temp, 0, temp.length - 1, up);

        }

        fSortedColumn = pColumn;

    }



    @SuppressWarnings("unchecked")

    private void sort(Object[] a, int lo0, int hi0, boolean up) {

        int lo = lo0;

        int hi = hi0;

        if (lo >= hi) {

            return;

        }

        int mid = (lo + hi) / 2;

        sort(a, lo, mid, up);

        sort(a, mid + 1, hi, up);

        int end_lo = mid;

        int start_hi = mid + 1;

        while ((lo <= end_lo) && (start_hi <= hi)) {

            boolean isChange;

            if (up) {

                isChange = (fComparator.compare(a[fSortOrder[lo]], a[fSortOrder[start_hi]]) <= 0);

            } else {

                isChange = (fComparator.compare(a[fSortOrder[lo]], a[fSortOrder[start_hi]]) >= 0);

            }

            if (isChange) {

                lo++;

            } else {

                int T = fSortOrder[start_hi];

                for (int k = start_hi - 1; k >= lo; k--) {

                    fSortOrder[k + 1] = fSortOrder[k];

                }

                fSortOrder[lo] = T;

                lo++;

                end_lo++;

                start_hi++;

            }

        }

    }



    public String[] getSortedColumns() {

        if (fSortedColumn != null) {

            return new String[] { fSortedColumn };

        } else {

            return null;

        }

    }



    public Object getRowAt(int index) {

        return (fTableData != null ? fTableData[fSortOrder[index]] : null);

    }



    public Object getTotalRow() {

        return fTotal;

    }



    public int getRowCount() {

        return (fTableData != null) ? fTableData.length : 0;
