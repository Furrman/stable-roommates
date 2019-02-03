package Controller;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker.StateValue;

import Model.Matching;
import Model.SuperSR;
import Model.SuperSRI;
import Model.SuperSRT;
import Model.SuperSRTI;
import View.Alghorithms;
import View.MainWindow;

/**
 * @class ViewController
 * @author Hubert Furmanek <254315@stud.umk.pl>
 * @brief Class which is a mediator between view and model objects.
 * It implement all action and handle all event triggered by model and send them to view.
 */
public class ViewController {
	private MainWindow window;
	private Matching matching;
	private SuperSR algorithm;
	private Boolean algorithmStarted = false;
	
	/**
	 * @fn ViewController
	 * @brief Constructor of class ViewController
	 */
	public ViewController() {
		window = new MainWindow();
		pinMenuActions();
	}
	
	/**
	 * @fn makeNextStep
	 * @brief Do next step in algorithm if step algorithm is enabled
	 */
	public void makeNextStep() {
		if (algorithmStarted && window.getStartButton().isEnabled()) {
			window.getStartButton().doClick();
		}
	}
	
	/**
	 * @fn pinMenuActions
	 * @brief Pin actions to all click-able components in menu bar.
	 */
	private void pinMenuActions() {
		// File section
		window.getMenuBar().getMenu(0).getItem(0).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Matching match = FileManager.loadFile();
				if (match != null) {
					matching = match;
					window.setMatching(matching.toString());
					window.setButtonsAfterLoadMatching();
				}
			}
        });
		window.getMenuBar().getMenu(0).getItem(1).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				FileManager.saveFile(matching);
			}
        });
		window.getMenuBar().getMenu(0).getItem(2).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				FileManager.saveFile(window.getConsole().getTable());
			}
        });
		window.getMenuBar().getMenu(0).getItem(3).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
        });
		
		// Action section
		window.getMenuBar().getMenu(1).getItem(0).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				pinStartAlghorithm();
			}
        });
        window.getMenuBar().getMenu(1).getItem(1).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				pinStartStepAlghorithm();
			}
        });
		
		// Help section
        window.getMenuBar().getMenu(2).getItem(0).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String html = "<html><center>Uniwersytet Mikołaja Kopernika<br>" +
						"Wydział Matematyki i Informatyki</center><br>" +
						"<br><center>Autor: Hubert Furmanek<br>" + 
						"nr. albumu: 254315<br>Informatyka</center><br>" +
						"<br><center>Praca magisterska</center><br>" +
						"<br><center><b>Dziedziczenie problemów<br>" +
						"dopasowań na podstawie<br>Stabilnych Współlokatorów</b></center><br>" + 
						"<br><div align=\"right\">Opiekun pracy dyplomowej<br>" +
						"prof. dr. hab. Maciej Sysło</div><br>" + 
						"<br><center>Toruń 2016</center></html>";
				JOptionPane.showMessageDialog(null, html, 
						"O aplikacji", JOptionPane.INFORMATION_MESSAGE);
			}
        });
        window.getMenuBar().getMenu(2).getItem(1).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//openHelpFile("./documents/instruction.pdf");

				String html = "<html>" +
						"Aplikacja desktopowa stworzona w ramach pracy magisterkiej. <br>" +
						"<br>Jej podstawowym zadaniem jest rozwiazywanie problemów dopasowań na podstawie zadanych list preferencji. <br>" +
						"W celu wczytania listy preferencji aplikacja potrzebuje pliku tekstowego zapisanego w odpowiednim formacie. <br>" +
						"Po rodzaj formatu odwołuje do Dodatku A opisanym w pracy magisterkiej. <br>" +
						"Wczytanie pliku odbywa sie poprzez menu kontekstowe wybierajac Plik->Otworz. <br>" +
						"W przypadku poprawnego wczytaniu danych odblokowywuja sie kolejne funkcje dostepne w programie. <br>" +
						"Za pomoca opcji Plik->Zapisz dopasowanie mozna zapisac aktualna postac list preferencji, <br>" +
						"dzieki opcji Akcja->Uruchom algorytm/Uruchom krokowy algorytm mozna uruchomic analogicznie algorytm zwykly lub krokowy <br>" +
						"a przy pomocy przyciskow mozemy kontrolowac przebieg dzialania algorytmu. <br>" +
						"Przycisk zielonej strzalki odpowiada za wykonanie kolejnego kroku w algorytmie krokowym, <br>" +
						"przycisk białej podwójnej strzalki służy do pominiecia krokow w algorytmie i uruchomienia algorytmu bez zatrzymywania oraz <br>" +
						"przycisk czerwonego kwadratu pozwala zatrzymac obecnie dzialajaca procedure algorytmu. <br>" +
						"<br>Widok ekranu zawiera dodatkowe obiekty pomocnicze przy analizowaniu algorytmu: <br>" +
						"- panelu w centrum ekranu zawierajacego aktualny status list dopasowań," +
						"<br>- tabeli z prawej czesci ekranu zawierajacej aktualne znalezione pary do dopasowania," +
						"<br>- tabeli z logami zawierajacymi wszystkie wykonane kroki algorytmu do danego momentu.<br>" +
						"<br>Oprócz możliwości zapisania aktualnej listy dopasowań możemy również zapisac aktualne wyniki z konsoli. <br>" +
						"Możemy to uzyskac poprzez wybranie opcji Plik->Zapisz wynik z konsoli." +
						"</html>";
				JOptionPane.showMessageDialog(null, html, 
						"O aplikacji", JOptionPane.INFORMATION_MESSAGE);
			}
        });
        
        // Buttons section
        window.getStartButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (algorithmStarted) {
			        algorithm.nextStep();
				} else {
					pinStartStepAlghorithm();
				}
			}
        });
        window.getSlideButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (algorithmStarted) {
			        algorithm.enableSteps(false);
			        algorithm.nextStep();
				} else {
					pinStartAlghorithm();
				}
			}
        });
        window.getStopButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (algorithm != null) {
					algorithm.cancel(true);
				}
				window.setButtonsAfterFinishedAlgorithm();
			}
        });
	}
	
	/**
	 * @fn openHelpFile
	 * @brief Open file with documentation in pdf.
	 * @param path
	 */
	private void openHelpFile(String path) {
        if (Desktop.isDesktopSupported()) {
            try {
                File file = new File(path);
                Desktop.getDesktop().open(file);
            } catch (IOException ex) {
				JOptionPane.showMessageDialog(window.getFrame(),
	        		    "Nie udalo sie znalezc szukanego pliku.",
	        		    "Blad",
	        		    JOptionPane.ERROR_MESSAGE);
            } catch(Exception ex) {
				JOptionPane.showMessageDialog(window.getFrame(),
	        		    "Nie udalo sie znalezc szukanego pliku.",
	        		    "Blad",
	        		    JOptionPane.ERROR_MESSAGE);
            }
        }
	}
	
	/**
	 * @fn pinStartAlghorithm
	 * @brief Implementation of starting step algorithm.
	 */
	private void pinStartAlghorithm() {
		algorithmStarted = true;
		window.setButtonsAfterStartAlghorithm();
		window.getConsole().clear();
		window.getPreferences().clear();
		
		switch ((String)window.getAlgorithmBox().getSelectedItem()) {
        case Alghorithms.SR:
        	algorithm = new SuperSR(matching);
            break;
        case Alghorithms.SRI:
        	algorithm = new SuperSRI(matching);
            break;
        case Alghorithms.SRT:
        	algorithm = new SuperSRT(matching);
            break;
        case Alghorithms.SRTI:
        	algorithm = new SuperSRTI(matching);
            break;
		}
		algorithm.setConsole(window.getConsole());
		algorithm.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(final PropertyChangeEvent event) {
				switch (event.getPropertyName()) {
		        	case "progress":
		        		break;
			        case "state":
			        	switch ((StateValue) event.getNewValue()) {
			        		case DONE:
			        			matching = algorithm.getMatching();
			        			window.setButtonsAfterFinishedAlgorithm();
			        			window.setMatching(matching.toString());
			        			window.getPreferences().clear();
			        			for (int i = 0; i < algorithm.getProposedFrom().size(); i++) {
			        				Vector<String> row = new Vector<String>();
			        				row.add(Integer.toString(i+1));

			        				String text = "";
			        				for (int j = 0; j < algorithm.getProposedFrom().get(i).size(); j++) {
			        					text += Integer.toString(algorithm.getProposedFrom().get(i).get(j)+1) + ",";
			        				}
			        				if (text.length() > 1) {
			        					text = text.substring(0, text.length()-1);
			        				}
			        				row.addElement(text);
			        				text = "";
			        				for (int j = 0; j < algorithm.getProposedTo().get(i).size(); j++) {
			        					text += Integer.toString(algorithm.getProposedTo().get(i).get(j)+1) + ",";
			        				}
			        				if (text.length() > 1) {
			        					text = text.substring(0, text.length()-1);
			        				}
			        				row.addElement(text);
			        				
			        				window.getPreferences().addMessage(row);
			        				algorithmStarted = false;
			        			}
			        			break;
			        		default:
			        			break;
			        	}
			        	break;
				}
			}
		});
		algorithm.execute();
	}
	
	/**
	 * @fn pinStartStepAlghorithm
	 * @brief Implementation action of starting step algorithm.
	 */
	private void pinStartStepAlghorithm() {
		algorithmStarted = true;
		window.setButtonsAfterStartStepAlghorithm();
		window.getConsole().clear();
		window.getPreferences().clear();
		
		switch ((String)window.getAlgorithmBox().getSelectedItem()) {
        case Alghorithms.SR:
        	algorithm = new SuperSR(matching);
            break;
        case Alghorithms.SRI:
        	algorithm = new SuperSRI(matching);
            break;
        case Alghorithms.SRT:
        	algorithm = new SuperSRT(matching);
            break;
        case Alghorithms.SRTI:
        	algorithm = new SuperSRTI(matching);
            break;
		}
		algorithm.setConsole(window.getConsole());
		algorithm.setPanel(window.getMatchingPanel());
		algorithm.setTable(window.getPreferences());
		algorithm.enableSteps(true);
		algorithm.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(final PropertyChangeEvent event) {
				switch (event.getPropertyName()) {
		        	case "progress":
		        		break;
			        case "state":
			        	switch ((StateValue) event.getNewValue()) {
			        		case DONE:
			        			matching = algorithm.getMatching();
			        			window.setButtonsAfterFinishedAlgorithm();
		        				window.getMenuBar().getMenu(0).getItem(2).setEnabled(true);
			        			window.setMatching(matching.toString());
			        			window.getPreferences().clear();
			        			for (int i = 0; i < algorithm.getProposedFrom().size(); i++) {
			        				Vector<String> row = new Vector<String>();
			        				row.add(Integer.toString(i+1));

			        				String text = "";
			        				for (int j = 0; j < algorithm.getProposedFrom().get(i).size(); j++) {
			        					text += Integer.toString(algorithm.getProposedFrom().get(i).get(j)+1) + ",";
			        				}
			        				if (text.length() > 1) {
			        					text = text.substring(0, text.length()-1);
			        				}
			        				row.addElement(text);
			        				text = "";
			        				for (int j = 0; j < algorithm.getProposedTo().get(i).size(); j++) {
			        					text += Integer.toString(algorithm.getProposedTo().get(i).get(j)+1) + ",";
			        				}
			        				if (text.length() > 1) {
			        					text = text.substring(0, text.length()-1);
			        				}
			        				row.addElement(text);
			        				
			        				window.getPreferences().addMessage(row);
			        				algorithmStarted = false;
			        			}
			        			break;
			        		default:
			        			break;
			        	}
			        	break;
				}
			}
		});
		algorithm.execute();
	}
	
}
