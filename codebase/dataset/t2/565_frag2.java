    public int binarySearch(final double key) {

        int low = 0;

        int middle;

        int high = super.size() - 1;

        double middleValue;

        while (low <= high) {

            middle = (low + high) / 2;

            middleValue = this.doubleArray[middle];

            if (middleValue < key) {

                low = middle + 1;

            } else if (middleValue > key) {

                high = middle - 1;

            } else {

                return middle;

            }

        }

        return -(low + 1);

    }
