        shuttlesort(to, from, middle, high);

        int p = low;

        int q = middle;

        if (high - low >= 4 && compare(from[middle - 1], from[middle]) <= 0) {

            for (int i = low; i < high; i++) {

                to[i] = from[i];

            }

            return;

        }

        for (int i = low; i < high; i++) {

            if (q >= high || (p < middle && compare(from[p], from[q]) <= 0)) {
