package tableutils;

import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import uk.co.antroy.latextools.parsers.IRowTableModel;

/**
 * Wraps and sorts the BibTeXTableModel.
 * It makes it possible to sort the table
 * of BibTeX entries by reference/title/author/journal.
 * 
 * @see uk.co.antroy.latextools.parsers.IRowTableModel
 * @see uk.co.antroy.latextools.BibTeXTablePanel
 * @param <T> Class of a row element of the underlying model
 */
public class TableSorter<T> extends TableMap implements IRowTableModel<T> {

    /**
	 * A list of indices to the underlying model
	 * sorted with respect to some column.
	 * (We don't sort the model itself - instead, we only 
	 * sort the indices, so for example indexes[0] = 5 means 
	 * that the row number 5 in the underlying model should 
	 * be displayed as the first one.) 
	 */
    int indexes[];

    Vector<ColumnInfo> sortingColumns = new Vector<ColumnInfo>();

    int compares;

    public TableSorter() {
        indexes = new int[0];
    }

    public TableSorter(IRowTableModel<?> model) {
        setModel(model);
    }

    public void setModel(IRowTableModel<?> model) {
        super.setModel(model);
        reallocateIndexes();
    }

    public IRowTableModel<T> getModel() {
        return (IRowTableModel<T>) super.getModel();
    }

    /**
     * Determine the sort order of the 2 rows with respect 
     * to the indicated column.
     * 
     * Is the value of the given column in the 1st row
     * &lt;, = or &gt; in the 2nd row? 
     * 
     * @see Comparable#compareTo
     */
    public int compareRowsByColumn(int row1, int row2, int column) {
        Class type = getModel().getColumnClass(column);
        TableModel data = getModel();
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
            String s2 = (String) data.getValueAt(row2, column);
            int result = s1.compareTo(s2);
            if (result < 0) {
                return -1;
            } else if (result > 0) {
                return 1;
            } else {
                return 0;
            }
        } else if (type == Boolean.class) {
            Boolean bool1 = (Boolean) data.getValueAt(row1, column);
            boolean b1 = bool1.booleanValue();
            Boolean bool2 = (Boolean) data.getValueAt(row2, column);
            boolean b2 = bool2.booleanValue();
            if (b1 == b2) {
                return 0;
            } else if (b1) {
                return 1;
            } else {
                return -1;
            }
        } else {
            Object v1 = data.getValueAt(row1, column);
            String s1 = v1.toString();
            Object v2 = data.getValueAt(row2, column);
            String s2 = v2.toString();
            int result = s1.compareTo(s2);
            if (result < 0) {
                return -1;
            } else if (result > 0) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    /**
     * Determine the sort order of the 2 rows with respect 
     * to all sort columns (1 or more).
     * 
     * @see Comparable#compareTo
     * @see #compareRowsByColumn(int, int, int)
     */
    public int compare(int row1, int row2) {
        compares++;
        for (int level = 0; level < sortingColumns.size(); level++) {
            ColumnInfo column = sortingColumns.elementAt(level);
            int result = compareRowsByColumn(row1, row2, column.getColumn());
            if (result != 0) {
                return column.isAscending() ? result : -result;
            }
        }
        return 0;
    }

    /**
     * Set up a new array of indices with the right number of elements
     * for the new data model.
     */
    public void reallocateIndexes() {
        int rowCount = getModel().getRowCount();
        indexes = new int[rowCount];
        for (int row = 0; row < rowCount; row++) {
            indexes[row] = row;
        }
    }

    /**
     * Invoked when a change to the underlying model occures.
     * @see javax.swing.event.TableModelListener
     */
    public void tableChanged(TableModelEvent e) {
        reallocateIndexes();
        super.tableChanged(e);
    }

    /**
     * Check indexes.length == model.getRowCount(). 
     */
    private void checkModel() {
        if (indexes == null) {
            throw new NullPointerException("Error: indexes is null; model is: " + model);
        }
        if (indexes.length != getRowCount()) {
            System.err.println("Sorter not informed of a change in model.");
        }
    }

    /**
     * Sort the array of indices with respect to the list 
     * of sort columns stored in this.sortingColumns.
     * <br/>
     * We don't sort the underlying model itself but 
     * we sort indices into the model with respect to the
     * sorting columns - they take less 
     * space so we don't spend so much by moving them.
     * @param sender (Not used so far.)
     */
    public void sort(Object sender) {
        checkModel();
        compares = 0;
        shuttlesort((int[]) indexes.clone(), indexes, 0, indexes.length);
    }

    private void shuttlesort(int from[], int to[], int low, int high) {
        if (high - low < 2) {
            return;
        }
        int middle = (low + high) / 2;
        shuttlesort(to, from, low, middle);
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
                to[i] = from[p++];
            } else {
                to[i] = from[q++];
            }
        }
    }

    /**
     * Get the column's value from the given row of the sorted view. 
     * @param aRow Index of the row in the soted view of the model.
     */
    public Object getValueAt(int aRow, int aColumn) {
        checkModel();
        return getModel().getValueAt(indexes[aRow], aColumn);
    }

    public void setValueAt(Object aValue, int aRow, int aColumn) {
        checkModel();
        getModel().setValueAt(aValue, indexes[aRow], aColumn);
    }

    public void sortByColumn(int column) {
        sortByColumn(column, true);
    }

    public void sortByColumn(int column, boolean ascending) {
        sortingColumns.removeAllElements();
        sortingColumns.addElement(new ColumnInfo(column, ascending));
        sort(this);
        super.tableChanged(new TableModelEvent(this));
    }

    /** 
     * Add a mouse listener to the Table to trigger a table sort 
     * when a column heading is clicked in the JTable.
     */
    public void addMouseListenerToHeaderInTable(JTable table) {
        final TableSorter sorter = this;
        final JTable tableView = table;
        tableView.setColumnSelectionAllowed(false);
        MouseAdapter listMouseListener = new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                TableColumnModel columnModel = tableView.getColumnModel();
                int viewColumn = columnModel.getColumnIndexAtX(e.getX());
                int column = tableView.convertColumnIndexToModel(viewColumn);
                if (e.getClickCount() == 1 && column != -1) {
                    int shiftPressed = e.getModifiers() & InputEvent.SHIFT_MASK;
                    boolean ascending = true;
                    if (shiftPressed == 0) {
                        Vector<ColumnInfo> sortingCols = sorter.getSortingColumns();
                        for (ColumnInfo columnInfo : sortingCols) {
                            if (columnInfo.getColumn() == column) {
                                ascending = !columnInfo.isAscending();
                            }
                        }
                    }
                    sorter.sortByColumn(column, ascending);
                }
            }
        };
        JTableHeader th = tableView.getTableHeader();
        th.addMouseListener(listMouseListener);
    }

    public T getRowEntry(int row) {
        return getModel().getRowEntry(indexes[row]);
    }

    /**
	 * Vector of the columns with respect to which we are sorted. 
	 */
    public Vector<ColumnInfo> getSortingColumns() {
        return sortingColumns;
    }

    /** Info about a column w.r.t. which we sort. */
    class ColumnInfo {

        final int column;

        boolean ascending;

        /**
		 * New info about a column with respect to which we sort.
		 * Ascending order assumed.
		 * @param column Index of the column
		 * descending (false) order?
		 */
        public ColumnInfo(int column) {
            this(column, true);
        }

        /**
		 * New info about a column with respect to which we sort.
		 * @param column Index of the column
		 * @param ascending Is it sorted in the ascending (true) or
		 * descending (false) order?
		 */
        public ColumnInfo(int column, boolean ascending) {
            this.column = column;
            this.ascending = ascending;
        }

        public boolean isAscending() {
            return ascending;
        }

        public void setAscending(boolean ascending) {
            this.ascending = ascending;
        }

        private int getColumn() {
            return column;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            } else if (obj instanceof TableSorter.ColumnInfo) {
                ColumnInfo ci2 = (ColumnInfo) obj;
                return getColumn() == ci2.getColumn();
            } else {
                return false;
            }
        }
    }
}
