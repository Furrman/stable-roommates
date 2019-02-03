package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * @class MainWindow
 * @author Hubert Furmanek <254315@stud.umk.pl>
 * @brief Class represents main window of application.
 * It contains definition of all components, initial values 
 * and method allowing user to change few elements in view.
 */
public class MainWindow {
    static final int WINDOW_SIZE_X = 800;
    static final int WINDOW_SIZE_Y = 600;
    
    private int labelSize = 20;
    
    private ClassLoader classLoader;
    private InputStream input;
    private GridBagLayoutHelper layout;
    private JComboBox<String> algorithmComboBox;
	private JFrame frame;
	private JMenuBar menuBar;
	private JPanel container;
	private JPanel consolePanel;
	private JPanel matchingPanel;
	private JPanel preferencePanel;
	private JScrollPane consoleScrollPane;
	private JScrollPane matchingScrollPane;
	private JScrollPane preferenceScrollPane;
    private JSplitPane verticalSplitPane;
    private JSplitPane horizontalSplitPane;
    private JButton startButton;
    private JButton slideButton;
    private JButton stopButton;
    private JTextArea matchingArea;

    private ExtendedConsole console;
    private ExtendedTable preferences;
	
    /**
     * @fn MainWindow
     * @brief Constructor of class MainWindow.
     */
	public MainWindow() {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		classLoader = Thread.currentThread().getContextClassLoader();
		
		frame = new JFrame("Super-Stable Matching Visualizer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new GridBagLayout());
        frame.setLocationRelativeTo(null);
        frame.setMinimumSize(new Dimension(WINDOW_SIZE_X, WINDOW_SIZE_Y));
        frame.setSize(WINDOW_SIZE_X, WINDOW_SIZE_Y);
		frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
		
		container = new JPanel();
        container.setLayout(new GridBagLayout());
        container.setBorder(null);

        layout = new GridBagLayoutHelper();
		layout.addComponentTo(container, frame, 0, 0, 1, 1, 1, 1, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0));
		
		try {
			prepareMenuBar();
	        prepareMatchingPanel();
			preparePreferencePanel();
			prepareConsolePanel();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		matchingPanel.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				int notches = e.getWheelRotation();
				if (matchingArea != null) {
					if (notches < 0) {
						if (labelSize < 50 && labelSize >= 10) {
							labelSize += 2;
							setMatching(matchingArea.getText());
						}
					} else {
						if (labelSize <= 50 && labelSize > 10) {
							labelSize -= 2;
							setMatching(matchingArea.getText());
						}
					}
				}
			}
		});

		frame.setVisible(true);
	}
	
	/**
	 * @fn getAlgorithmBox
	 * @brief Get combo box element containing all available algorithms.
	 * @return JComboBox<String>
	 */
	public JComboBox<String> getAlgorithmBox() {
		return algorithmComboBox;
	}
	
	/**
	 * @fn getFrame
	 * @brief Get frame element containing all elements in application.
	 * @return JFrame 
	 */
	public JFrame getFrame() {
		return frame;
	}

	/**
	 * @fn getMenuBar
	 * @brief Get menu bar from frame.
	 * @return JMenuBar
	 */
	public JMenuBar getMenuBar() {
		return menuBar;
	}
	
	/**
	 * @fn getStartButton
	 * @brief Get startButton component.
	 * @return JButton
	 */
	public JButton getStartButton() {
		return startButton;
	}

	/**
	 * @fn getSlideButton
	 * @brief Get slideButton component.
	 * @return JButton
	 */
	public JButton getSlideButton() {
		return slideButton;
	}

	/**
	 * @fn getStopButton
	 * @brief Get stopButton component.
	 * @return JButton
	 */
	public JButton getStopButton() {
		return stopButton;
	}

	/**
	 * @fn getConsolePanel
	 * @brief Get console element.
	 * @return JPanel 
	 */
	public ExtendedConsole getConsole() {
		return console;
	}

	/**
	 * @fn getMatchingPanel
	 * @brief Get panel with matching object.
	 * @return JPanel 
	 */
	public JPanel getMatchingPanel() {
		return matchingPanel;
	}
	
	/**
	 * @fn getPreference
	 * @brief Get list of preferences.
	 * @return ExtendedTable 
	 */
	public ExtendedTable getPreferences() {
		return preferences;
	}
	
	/**
	 * @fn setMatchingPanel
	 * @brief Set content of matching panel with new text gotten from Matching.
	 * @param text - String representation of Matching class.
	 */
	public void setMatching(String text) {
		matchingPanel.removeAll();
		matchingPanel.revalidate();
	    
	    matchingArea = new JTextArea(text);
	    Font font = new Font("Verdana", Font.BOLD, labelSize);
	    matchingArea.setFont(font);
	    matchingArea.setEditable(false);
	    matchingPanel.add(matchingArea);
	    
		// refresh the panel.
	    matchingPanel.updateUI();
		matchingPanel.revalidate();
		matchingPanel.repaint();
	}
	
	/**
	 * @fn setEnabledButtonsAfterLoadMatching
	 * @brief Set enabled condition of buttons after matching loading.
	 */
	public void setButtonsAfterLoadMatching() {
		menuBar.getMenu(0).getItem(0).setEnabled(true);
		menuBar.getMenu(0).getItem(1).setEnabled(true);
		menuBar.getMenu(0).getItem(2).setEnabled(false);
		menuBar.getMenu(1).getItem(0).setEnabled(true);
		menuBar.getMenu(1).getItem(1).setEnabled(true);
		
		menuBar.getComponents()[4].setEnabled(true);
		menuBar.getComponents()[5].setEnabled(true);

		startButton.setEnabled(true);
		slideButton.setEnabled(true);
		stopButton.setEnabled(false);
		
		console.clear();
		preferences.clear();
	}
	
	/**
	 * @fn setEnabledButtonsAfterStartAlghorithm
	 * @brief Set enabled condition of buttons after starting algorithm.
	 */
	public void setButtonsAfterStartAlghorithm() {
		menuBar.getMenu(0).getItem(0).setEnabled(false);
		menuBar.getMenu(0).getItem(1).setEnabled(false);
		menuBar.getMenu(0).getItem(2).setEnabled(false);
		menuBar.getMenu(1).getItem(0).setEnabled(false);
		menuBar.getMenu(1).getItem(1).setEnabled(false);

		menuBar.getComponents()[4].setEnabled(false);
		menuBar.getComponents()[5].setEnabled(false);
		
		startButton.setEnabled(false);
		slideButton.setEnabled(false);
		stopButton.setEnabled(true);
	}
	
	/**
	 * @fn setEnabledButtonsAfterStartStepAlghorithm
	 * @brief Set enabled condition of buttons after starting step algorithm.
	 */
	public void setButtonsAfterStartStepAlghorithm() {
		menuBar.getMenu(0).getItem(0).setEnabled(false);
		menuBar.getMenu(0).getItem(1).setEnabled(false);
		menuBar.getMenu(0).getItem(2).setEnabled(false);
		menuBar.getMenu(1).getItem(0).setEnabled(false);
		menuBar.getMenu(1).getItem(1).setEnabled(false);

		menuBar.getComponents()[4].setEnabled(false);
		menuBar.getComponents()[5].setEnabled(false);

		startButton.setEnabled(true);
		slideButton.setEnabled(true);
		stopButton.setEnabled(true);
		
	}
	
	/**
	 * @fn setButtonsAfterFinishedAlgorithm
	 * @brief Set enabled condition of buttons after finished algorithm.
	 */
	public void setButtonsAfterFinishedAlgorithm() {
		menuBar.getMenu(0).getItem(0).setEnabled(true);
		menuBar.getMenu(0).getItem(1).setEnabled(true);
		menuBar.getMenu(0).getItem(2).setEnabled(true);
		menuBar.getMenu(1).getItem(0).setEnabled(true);
		menuBar.getMenu(1).getItem(1).setEnabled(true);

		menuBar.getComponents()[4].setEnabled(true);
		menuBar.getComponents()[5].setEnabled(true);
		
		startButton.setEnabled(true);
		slideButton.setEnabled(true);
		stopButton.setEnabled(false);
	}
	
	/**
	 * @throws IOException 
	 * @fn prepareMenuBar
	 * @brief Set all components in menu bar.
	 */
	private void prepareMenuBar() throws IOException {
		JMenu menu;
        JMenuItem menuItem;

        menuBar = new JMenuBar();
        
        // Prepare file menu section
        menu = new JMenu("Plik");
        menuItem = new JMenuItem("Otworz");
        menu.add(menuItem);
        menuItem = new JMenuItem("Zapisz dopasowanie");
        menuItem.setEnabled(false);
        menu.add(menuItem);
        menuItem = new JMenuItem("Zapisz wynik z konsoli");
        menuItem.setEnabled(false);
        menu.add(menuItem);
        menuItem = new JMenuItem("Zamknij");
        menu.add(menuItem);
        
        menuBar.add(menu);

        // Prepare action menu section
        menu = new JMenu("Akcja");
        menuItem = new JMenuItem("Uruchom algorytm");
        menuItem.setEnabled(false);
        menu.add(menuItem);
        menuItem = new JMenuItem("Uruchom krokowy algorytm");
        menuItem.setEnabled(false);
        menu.add(menuItem);
        
        menuBar.add(menu);
        
        // Prepare help menu section
        menu = new JMenu("Pomoc");
        menuItem = new JMenuItem("O aplikacji");
        menu.add(menuItem);
        menuItem = new JMenuItem("Instrukcja");
        menu.add(menuItem);
        menuBar.add(menu);

        // Prepare buttons section
        JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
        menuBar.add(separator);
        
        JLabel label = new JLabel("Wybierz algorytm:");
        label.setEnabled(false);
        menuBar.add(label);

        algorithmComboBox = new JComboBox<String>();
        algorithmComboBox.setEnabled(false);
        algorithmComboBox.addItem(Alghorithms.SR);
        algorithmComboBox.addItem(Alghorithms.SRI);
        algorithmComboBox.addItem(Alghorithms.SRT);
        algorithmComboBox.addItem(Alghorithms.SRTI);
        menuBar.add(algorithmComboBox);
        
        input = classLoader.getResourceAsStream("Start.png");
        startButton = new JButton(new ImageIcon(ImageIO.read(input).getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH)));
        startButton.setEnabled(false);
        menuBar.add(startButton);

        input = classLoader.getResourceAsStream("Forward.png");
        slideButton = new JButton(new ImageIcon(ImageIO.read(input).getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH)));
        slideButton.setEnabled(false);
        menuBar.add(slideButton);

        input = classLoader.getResourceAsStream("Stop.png");
        stopButton = new JButton(new ImageIcon(ImageIO.read(input).getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH)));
        stopButton.setEnabled(false);
        menuBar.add(stopButton);
        
        menuBar.setVisible(true);
        frame.setJMenuBar(menuBar);
	}
	
	/**
	 * @fn prepareMatchingPanel
	 * @brief Set all components in matching panel.
	 */
	private void prepareMatchingPanel() {
		matchingPanel = new JPanel();
		matchingPanel.setBackground(Color.white);
		matchingPanel.setLayout(new GridBagLayout());
		matchingScrollPane = new JScrollPane(matchingPanel);
		matchingScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		matchingScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		
        layout.addComponentTo(matchingScrollPane, container, 0, 0, 1, 1, 1, 1, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0));
	}

	/**
	 * @fn preparePreferencePanel
	 * @brief Set all components in preference panel.
	 */
	private void preparePreferencePanel() {
		preferences = new ExtendedTable();
		
		preferencePanel = new JPanel();
		preferencePanel.setBackground(Color.white);
		preferencePanel.setLayout(new GridBagLayout());

		preferenceScrollPane = preferences.getScrollPane();
		preferenceScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		preferenceScrollPane.setPreferredSize(new Dimension(200, 50));
		preferenceScrollPane.setBackground(Color.white);
		preferenceScrollPane.setAutoscrolls(true);
		
		horizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, matchingScrollPane, preferenceScrollPane);
		horizontalSplitPane.setBorder(null);
		horizontalSplitPane.setOneTouchExpandable(true);
		horizontalSplitPane.setDividerLocation(0.95);
		horizontalSplitPane.setResizeWeight(1);
		horizontalSplitPane.setToolTipText("Przeciągnij aby zmienić rozmiar elementu");
		horizontalSplitPane.setDividerSize(5);
        
        layout.addComponentTo(horizontalSplitPane, container, 1, 0, 1, 1, 1, 1, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0));
	}
	
	/**
	 * @fn prepareConsolePanel
	 * @brief Set all components in console panel.
	 */
	private void prepareConsolePanel() {
        console = new ExtendedConsole();
        
        consolePanel = new JPanel();
        consolePanel.setBackground(Color.white);
        consolePanel.setLayout(new GridBagLayout());

        consoleScrollPane = console.getScrollPane();
		consoleScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        consoleScrollPane.setPreferredSize(new Dimension(200, 50));
		consoleScrollPane.setBackground(Color.white);
		consoleScrollPane.setAutoscrolls(true);
		
		verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, horizontalSplitPane, consoleScrollPane);
		verticalSplitPane.setBorder(null);
		verticalSplitPane.setOneTouchExpandable(true);
		verticalSplitPane.setDividerLocation(0.8);
		verticalSplitPane.setResizeWeight(0.8);
		verticalSplitPane.setToolTipText("Przeciągnij aby zmienić rozmiar elementu");
		verticalSplitPane.setDividerSize(5);
        
        layout.addComponentTo(verticalSplitPane, container, 0, 1, 1, 1, 1, 1, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0));
	}
	
}
