package View;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @fn GridBagLayoutHelper
 * @author Hubert Furmanek <254315@stud.umk.pl>
 * @brief Extension of class GridBagConstraints which simplify aligning of components in application.
 */
public class GridBagLayoutHelper {
    GridBagConstraints gc;

    /**
     * @fn GridBagLayoutHelper
     * @brief Constructor of class GridBagLayoutHelper.
     */
    public GridBagLayoutHelper() {
        gc = new GridBagConstraints();
        
        gc.ipadx = 0;
        gc.ipady = 0;
    }
    
    /**
     * @fn addComponentTo
     * @brief Method which add component to panel with given attributes and coordinates.
     * @param element - new component added to panel.
     * @param panel - object containing new added element.
     * @param x - coordinate X of grid panel.
     * @param y - coordinate Y of grid panel.
     * @param wx - percentage coordinate specifies how to distribute extra horizontal space. 
     * @param wy - percentage coordinate specifies how to distribute extra vertical space. 
     * @param gw - specifies the number of cells in a row for the component's display area. 
     * @param gh - specifies the number of cells in a column for the component's display area. 
     * @param fill - resize element with chosen attribute for ex. Vertical/Horizontal/Both.
     * @param padding - it determines whether to resize the component, and if so, how.
     */
    public void addComponentTo(Component element, JPanel panel, int x, int y, double wx, double wy, int gw, int gh, int fill, Insets padding) {
        gc.fill = fill;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.weightx = wx;
        gc.weighty = wy;
        gc.gridwidth = gw;
        gc.gridheight = gh;
        gc.gridx = x;
        gc.gridy = y;
        gc.insets = padding;

        panel.add(element, gc);
    }

    /**
     * @fn addComponentTo
     * @brief Method which add panel to panel with given attributes and coordinates.
     * @param panel - new component added to panel.
     * @param container - object containing new added panel.
     * @param x - coordinate X of grid panel.
     * @param y - coordinate Y of grid panel.
     * @param wx - percentage coordinate specifies how to distribute extra horizontal space. 
     * @param wy - percentage coordinate specifies how to distribute extra vertical space. 
     * @param gw - specifies the number of cells in a row for the component's display area. 
     * @param gh - specifies the number of cells in a column for the component's display area. 
     * @param fill - resize element with chosen attribute for ex. Vertical/Horizontal/Both.
     * @param padding - it determines whether to resize the component, and if so, how.
     */
    public void addComponentTo(JPanel panel, JPanel container, int x, int y, double wx, double wy, int gw, int gh, int fill, Insets padding) {
        gc.fill = fill;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.weightx = wx;
        gc.weighty = wy;
        gc.gridwidth = gw;
        gc.gridheight = gh;
        gc.gridx = x;
        gc.gridy = y;
        gc.insets = padding;

        container.add(panel, gc);
    }

    /**
     * @fn addComponentTo
     * @brief Method which add panel to panel with given attributes and coordinates.
     * @param panel - new component added to panel.
     * @param container - object containing new added panel.
     * @param x - coordinate X of grid panel.
     * @param y - coordinate Y of grid panel.
     * @param wx - percentage coordinate specifies how to distribute extra horizontal space. 
     * @param wy - percentage coordinate specifies how to distribute extra vertical space. 
     * @param gw - specifies the number of cells in a row for the component's display area. 
     * @param gh - specifies the number of cells in a column for the component's display area.
     * @param start - determines where, within the display area, to place the component.
     * @param fill - resize element with chosen attribute for ex. Vertical/Horizontal/Both.
     * @param padding - it determines whether to resize the component, and if so, how.
     */
    public void addComponentTo(JPanel panel, JPanel container, int x, int y, double wx, double wy, int gw, int gh, int start, int fill, Insets padding) {
        gc.fill = fill;
        gc.anchor = start;
        gc.weightx = wx;
        gc.weighty = wy;
        gc.gridwidth = gw;
        gc.gridheight = gh;
        gc.gridx = x;
        gc.gridy = y;
        gc.insets = padding;

        container.add(panel, gc);
    }

    /**
     * @fn addComponentTo
     * @brief Method which add component to panel with given attributes and coordinates.
     * @param element - new component added to panel.
     * @param container - object containing new added component.
     * @param x - coordinate X of grid panel.
     * @param y - coordinate Y of grid panel.
     * @param wx - percentage coordinate specifies how to distribute extra horizontal space. 
     * @param wy - percentage coordinate specifies how to distribute extra vertical space. 
     * @param gw - specifies the number of cells in a row for the component's display area. 
     * @param gh - specifies the number of cells in a column for the component's display area. 
     * @param start - determines where, within the display area, to place the component.
     * @param fill - resize element with chosen attribute for ex. Vertical/Horizontal/Both.
     * @param padding - it determines whether to resize the component, and if so, how.
     */
    public void addComponentTo(Component element, JPanel container, int x, int y, double wx, double wy, int gw, int gh, int start, int fill, Insets padding) {
        gc.fill = fill;
        gc.anchor = start;
        gc.weightx = wx;
        gc.weighty = wy;
        gc.gridwidth = gw;
        gc.gridheight = gh;
        gc.gridx = x;
        gc.gridy = y;
        gc.insets = padding;

        container.add(element, gc);
    }

    /**
     * @fn addComponentTo
     * @brief Method which add panel to frame with given attributes and coordinates.
     * @param panel - new panel added to frame.
     * @param frame - object containing new added panel.
     * @param x - coordinate X of grid frame.
     * @param y - coordinate Y of grid frame.
     * @param wx - percentage coordinate specifies how to distribute extra horizontal space. 
     * @param wy - percentage coordinate specifies how to distribute extra vertical space. 
     * @param gw - specifies the number of cells in a row for the component's display area. 
     * @param gh - specifies the number of cells in a column for the component's display area. 
     * @param fill - resize element with chosen attribute for ex. Vertical/Horizontal/Both.
     * @param padding - it determines whether to resize the component, and if so, how.
     */
    public void addComponentTo(JPanel panel, JFrame frame, int x, int y, double wx, double wy, int gw, int gh, int fill, Insets padding) {
        gc.fill = fill;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.weightx = wx;
        gc.weighty = wy;
        gc.gridwidth = gw;
        gc.gridheight = gh;
        gc.gridx = x;
        gc.gridy = y;
        gc.insets = padding;

        frame.add(panel, gc);
    }

    /**
     * @fn addComponentTo
     * @brief Method which add component to frame with given attributes and coordinates.
     * @param panel - new component added to frame.
     * @param frame - object containing new added component.
     * @param x - coordinate X of grid frame.
     * @param y - coordinate Y of grid frame.
     * @param wx - percentage coordinate specifies how to distribute extra horizontal space. 
     * @param wy - percentage coordinate specifies how to distribute extra vertical space. 
     * @param gw - specifies the number of cells in a row for the component's display area. 
     * @param gh - specifies the number of cells in a column for the component's display area. 
     * @param fill - resize element with chosen attribute for ex. Vertical/Horizontal/Both.
     * @param padding - it determines whether to resize the component, and if so, how.
     */
    public void addComponentTo(Component panel, JFrame frame, int x, int y, double wx, double wy, int gw, int gh, int fill, Insets padding) {
        gc.fill = fill;
        gc.anchor = GridBagConstraints.FIRST_LINE_END;
        gc.weightx = wx;
        gc.weighty = wy;
        gc.gridwidth = gw;
        gc.gridheight = gh;
        gc.gridx = x;
        gc.gridy = y;
        gc.insets = padding;

        frame.add(panel, gc);
    }
      
}
