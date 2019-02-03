package View;

import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 * @class ExtendedTable
 * @author Hubert Furmanek <254315@stud.umk.pl>
 * @brief Table object built on JTable object with supported methods.
 */
public class ExtendedTable {
    private final JScrollPane scrollPane;
    private final JTable extendedTable;
    private final DefaultTableModel tableModel;

    /**
     * @fn ExtendedTable
     * @brief Constructor of class ExtendedTable.
     */
    public ExtendedTable() {
        extendedTable = new JTable() {
			private static final long serialVersionUID = 1L;

			@Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        extendedTable.getTableHeader().setReorderingAllowed(false);
        extendedTable.getTableHeader().setReorderingAllowed(false);
        extendedTable.getTableHeader().setResizingAllowed(false);
        extendedTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        extendedTable.setAutoscrolls(false);
        extendedTable.setFillsViewportHeight(true);
        
        scrollPane = new JScrollPane(extendedTable);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("Osoba");
        tableModel.addColumn("Od");
        tableModel.addColumn("Do");

        extendedTable.setModel(tableModel);
        extendedTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
    }

    /**
     * @fn getScrollPane
     * @brief Get scroll panel object with table within it.
     * @return JScrollPane
     */
    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    /**
     * @fn addMessage
     * @brief Add new text to table.
     * @param row - vector with text added to table.
     */
    public void addMessage(Vector<String> row) {
        tableModel.addRow(row);
        tableModel.fireTableDataChanged();
    }

    /**
     * @fn remove
     * @brief Remove given row from table.
     * @param i - index of removed row.
     */
    public void remove(int i) {
        tableModel.removeRow(i);
        tableModel.fireTableDataChanged();
    }

    /**
     * @fn getRowCount
     * @brief Get number of added table.
     * @return Integer
     */
    public int getRowCount() {
        return tableModel.getRowCount();
    }

    /**
     * @fn clear
     * @brief Clear console from all rows.
     */
    public void clear() {
        tableModel.setRowCount(0);
        tableModel.fireTableDataChanged();
    }

    /**
     * @fn getTable
     * @brief Get table with all rows within it.
     * @return JTable
     */
    public JTable getTable() {
        return extendedTable;
    }
}
