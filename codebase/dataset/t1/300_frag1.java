public class TableSorter extends TableMap {



    int indexes[];



    Vector sortingColumns = new Vector();



    boolean ascending = true;



    int compares;



    public TableSorter() {

        indexes = new int[0];

    }



    public TableSorter(TableModel model) {

        setModel(model);

    }



    public void setModel(TableModel model) {

        super.setModel(model);

        reallocateIndexes();
