            public void mouseClicked(MouseEvent e) {

                TableColumnModel columnModel = tableView.getColumnModel();

                int viewColumn = columnModel.getColumnIndexAtX(e.getX());

                int column = tableView.convertColumnIndexToModel(viewColumn);

                if ((e.getClickCount() != 1) || (column == -1)) return;

                int shiftPressed = e.getModifiers() & 0x1;

                boolean ascending = shiftPressed == 0;

                TableSorter.this.sortByColumn(column, ascending);

            }
