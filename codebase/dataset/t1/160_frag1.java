            Number n1 = (Number) data.getValueAt(row1, column);

            double d1 = n1.doubleValue();

            Number n2 = (Number) data.getValueAt(row2, column);

            double d2 = n2.doubleValue();

            if (d1 < d2) {

                return -1;

            } else if (d1 > d2) {

                return 1;

            } else {

                return 0;
