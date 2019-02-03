package View;

import java.util.Vector;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 * @class ExtendedConsole
 * @author Hubert Furmanek <254315@stud.umk.pl>
 * @brief Console object built on JTable object with supported methods.
 */
public class ExtendedConsole {
    private int count;
    private final JScrollPane scrollPane;
    private final JTable extendedConsole;
    private final DefaultTableModel tableModel;

    /**
     * @fn ExtendedConsole
     * @brief Constructor of class ExtendedConsole.
     */
    public ExtendedConsole() {
        count = 0;
        extendedConsole = new JTable() {
			private static final long serialVersionUID = 1L;

			@Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        extendedConsole.getTableHeader().setReorderingAllowed(false);
        extendedConsole.getTableHeader().setReorderingAllowed(false);
        extendedConsole.getTableHeader().setResizingAllowed(false);
        extendedConsole.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        extendedConsole.setAutoscrolls(false);
        extendedConsole.setFillsViewportHeight(true);
        
        scrollPane = new JScrollPane(extendedConsole);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("Krok");
        tableModel.addColumn("Opis");

        extendedConsole.setModel(tableModel);
        extendedConsole.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        extendedConsole.getColumnModel().getColumn(0).setMaxWidth(50);
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
     * @brief Add new text message to console with given status.
     * @param type - type of given message.
     * @param description - text message added to console.
     */
    public void addMessage(MessageType type, String description) {
        Vector<String> row = new Vector<String>();
        count++;
        row.add(Integer.toString(count));
        switch (type) {
            case INFO:
            	description = "<html>" + description + "</html>";
                break;
            case WARNING:
            	description = "<html><b>" + description + "</b></html>";
            	break;
            case SUCCESS:
            	description = "<html><font color=\"green\"><b>" + description + "</b></font></html>";
                break;
            case ERROR:
            	description = "<html><font color=\"red\"><b>" + description + "</b></font></html>";
                break;
        }

        row.add(description);
        tableModel.addRow(row);
        tableModel.fireTableDataChanged();
        extendedConsole.scrollRectToVisible(extendedConsole.getCellRect(extendedConsole.getRowCount()-1, 0, true));
        extendedConsole.repaint();
    }

    /**
     * @fn remove
     * @brief Remove given row from console.
     * @param i - index of removed row.
     */
    public void remove(int i) {
        tableModel.removeRow(i);
        tableModel.fireTableDataChanged();
    }

    /**
     * @fn getMsgCount
     * @brief Get number of added messages.
     * @return Integer
     */
    public int getMsgCount() {
        return tableModel.getRowCount();
    }

    /**
     * @fn clear
     * @brief Clear console from all messages.
     */
    public void clear() {
        count = 0;
        tableModel.setRowCount(0);
        tableModel.fireTableDataChanged();
    }

    /**
     * @fn getTable
     * @brief Get table with all messages within it.
     * @return JTable
     */
    public JTable getTable() {
        return extendedConsole;
    }
}
