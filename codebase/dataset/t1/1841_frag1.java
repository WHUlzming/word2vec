    public void sortByColumn(int column, boolean ascending) {

        this.ascending = ascending;

        sortingColumns.removeAllElements();

        sortingColumns.addElement(new Integer(column));

        sort(this);

        super.tableChanged(new TableModelEvent(this));

    }
