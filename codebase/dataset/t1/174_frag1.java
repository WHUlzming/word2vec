    protected static final void insertionsort(Object[] a, int[] b, int p, int r, Comparator cmp) {

        for (int j = p + 1; j <= r; ++j) {

            Object key = a[j];

            int val = b[j];

            int i = j - 1;

            while (i >= p && cmp.compare(a[i], key) > 0) {

                a[i + 1] = a[i];

                b[i + 1] = b[i];

                --i;

            }

            a[i + 1] = key;

            b[i + 1] = val;

        }

    }
