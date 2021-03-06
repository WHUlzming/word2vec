        Class type = model.getColumnClass(column);

        TableModel data = model;

        Object o1 = data.getValueAt(row1, column);

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

            if (d1 < d2) return -1; else if (d1 > d2) return 1; else return 0;
