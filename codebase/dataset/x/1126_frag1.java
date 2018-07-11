        Object o2 = data.getValueAt(row2, column);

        if (o1 == null && o2 == null) {

            return 0;

        } else if (o1 == null) {

            return -1;

        } else if (o2 == null) {

            return 1;

        }

        if (type.getSuperclass() == java.lang.Number.class) {

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

            }

        } else if (type == java.util.Date.class) {

            Date d1 = (Date) data.getValueAt(row1, column);

            long n1 = d1.getTime();

            Date d2 = (Date) data.getValueAt(row2, column);

            long n2 = d2.getTime();

            if (n1 < n2) {

                return -1;

            } else if (n1 > n2) {

                return 1;

            } else {

                return 0;

            }

        } else if (type == String.class) {

            String s1 = (String) data.getValueAt(row1, column);
