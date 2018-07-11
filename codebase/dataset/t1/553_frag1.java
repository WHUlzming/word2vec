            public void mouseClicked(MouseEvent e) {

                TableColumnModel columnModel = tableView.getColumnModel();

                int viewColumn = columnModel.getColumnIndexAtX(e.getX());

                int column = tableView.convertColumnIndexToModel(viewColumn);

                if (e.getClickCount() == 1 && column != -1) {

                    int shiftPressed = e.getModifiers() & InputEvent.SHIFT_MASK;

                    sorter.sortByColumn(column, (shiftPressed == 0));

                }

            }
