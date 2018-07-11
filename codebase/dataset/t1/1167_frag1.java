    private static void sort1(long x[], int[] y, int off, int len) {

        if (len < 7) {

            for (int i = off; i < len + off; i++) for (int j = i; j > off && x[j - 1] > x[j]; j--) swap(x, y, j, j - 1);

            return;

        }

        int m = off + len / 2;

        if (len > 7) {

            int l = off;

            int n = off + len - 1;

            if (len > 40) {

                int s = len / 8;

                l = med3(x, l, l + s, l + 2 * s);

                m = med3(x, m - s, m, m + s);

                n = med3(x, n - 2 * s, n - s, n);

            }

            m = med3(x, l, m, n);

        }

        long v = x[m];

        int a = off, b = a, c = off + len - 1, d = c;

        while (true) {

            while (b <= c && x[b] <= v) {

                if (x[b] == v) swap(x, y, a++, b);

                b++;

            }

            while (c >= b && x[c] >= v) {

                if (x[c] == v) swap(x, y, c, d--);

                c--;

            }

            if (b > c) break;

            swap(x, y, b++, c--);

        }

        int s, n = off + len;

        s = Math.min(a - off, b - a);

        vecswap(x, y, off, b - s, s);

        s = Math.min(d - c, n - d - 1);

        vecswap(x, y, b, n - s, s);

        if ((s = b - a) > 1) sort1(x, y, off, s);

        if ((s = d - c) > 1) sort1(x, y, n - s, s);

    }
