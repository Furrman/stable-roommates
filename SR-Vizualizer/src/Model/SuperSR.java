package Model;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

import View.ExtendedConsole;
import View.ExtendedTable;
import View.MessageType;

/**
 * @class SuperSRT
 * @author Hubert Furmanek <254315@stud.umk.pl>
 * @brief Class represents Super Stable Roommates algorithm.
 */
public class SuperSR extends SwingWorker<Integer, String> implements Runnable {
	
	protected Matching matching;
	protected List<Integer> setProposedFrom;
	protected boolean stable = false;
	protected ExtendedConsole console;
	protected ExtendedTable table;
	protected JPanel panel;
	protected Boolean stepsEnabled = false;
	protected Boolean nextStep = false;
	
	/**
	 * @fn SuperSR
	 * @brief Constructor of SR algorithm class.
	 */
	protected SuperSR() {}
	
	/**
	 * @fn SuperSR
	 * @brief Constructor of SR algorithm class.
	 * @param matching - roommates matching in format of Matching class.
	 */
	public SuperSR(Matching matching) {
		this.matching = matching;
		this.matching.removeTies();
		
		// Declare list with propositions
		this.setProposedFrom = new ArrayList<Integer>();
		for (int i = 0; i < this.matching.size(); i++) {
			this.setProposedFrom.add(-1);
		}
	}
	
	/**
	 * @fn SuperSR
	 * @brief Constructor of SR algorithm class.
	 * @param matrix - roommates matching in format of Java Lists.
	 */
	public SuperSR(ArrayList<List<Integer>> matching) {
		this.matching = new Matching(matching);
		this.matching.removeTies();
		
		// Declare list with propositions
		this.setProposedFrom = new ArrayList<Integer>();
		for (int i = 0; i < this.matching.size(); i++) {
			this.setProposedFrom.add(-1);
		}
	}
	
	/**
	 * @fn SuperSR
	 * @brief Constructor of SR algorithm class.
	 * @param matrix - roommates matching in format of Java Arrays.
	 */
	public SuperSR(int[][] matching) {
		this.matching = new Matching(matching);
		this.matching.removeTies();

		// Declare list with propositions
		this.setProposedFrom = new ArrayList<Integer>();
		for (int i = 0; i < this.matching.size(); i++) {
			this.setProposedFrom.add(-1);
		}
	}
	
	/**
	 * @fn getMatching
	 * @brief Get matching from algorithm.
	 * @return Matching
	 */
	public Matching getMatching() {
		return matching;
	}
	
	/**
	 * @fn getProposedFrom
	 * @brief Return list of proposition from other persons.
	 * @return List of accepted propositions from other persons.
	 */
	public List<List<Integer>> getProposedFrom() {
		List<List<Integer>> list = new ArrayList<List<Integer>>();
		for (int i = 0; i < this.setProposedFrom.size(); i++) {
			List<Integer> tmp = new ArrayList<Integer>();
			if (this.setProposedFrom.get(i) != -1) {
				tmp.add(this.setProposedFrom.get(i));
			}
			list.add(tmp);
		}
		
		return list;
	}
	
	/**
	 * @fn getProposedTo
	 * @brief Return list of proposition to other persons.
	 * @return List of accepted propositions to other persons.
	 */
	public List<List<Integer>> getProposedTo() {
		List<List<Integer>> list = new ArrayList<List<Integer>>();
		for (int i = 0; i < this.setProposedFrom.size(); i++) {
			List<Integer> tmp = new ArrayList<Integer>();
			list.add(tmp);
		}
		
		for (int i = 0; i < this.setProposedFrom.size(); i++) {
			if (this.setProposedFrom.get(i) != -1) {
				int tmp = this.setProposedFrom.get(i);
				list.get(tmp).add(i);
			}
		}
		
		return list;
	}
	
	/**
	 * @fn setOutput
	 * @brief Set console component to save logs.
	 * @param output - container for logs
	 */
	public void setConsole(ExtendedConsole console) {
		this.console = console;
	}
	
	/**
	 * @fn setTable
	 * @brief Set table component to save preferences.
	 * @param table - container for preferences
	 */
	public void setTable(ExtendedTable table) {
		this.table = table;
	}
	
	/**
	 * @fn setPanel
	 * @brief Set panel component to save matching.
	 * @param panel - container for mathcing
	 */
	public void setPanel(JPanel panel) {
		this.panel = panel;
	}
	
	/**
	 * @fn enableSteps
	 * @brief Enable/Disable step algorithm.
	 * @param val - enable or disable
	 */
	public void enableSteps(Boolean val) {
		stepsEnabled = val;
	}
	
	/**
	 * @fn nextStep
	 * @brief Make new step in step algorithm.
	 */
	public void nextStep() {
		nextStep = true;
	}
	
	/**
	 * @throws InterruptedException 
	 * @fn start
	 * @brief Start algorithm and show result on given console.
	 */
	public void start() throws InterruptedException {
		this.matching.reduceMatching();
		// Phase 1
		saveLog(MessageType.WARNING, "Start fazy 1");
		boolean status = firstPhase();
		saveLog(MessageType.WARNING, "Koniec fazy 1");
		if (!status) {
			saveLog(MessageType.ERROR, "Dopasowanie nie jest stabilne");
			this.matching.updateMatching();
			return;
		}
		if (this.endOfAlgorithm()) {
			saveLog(MessageType.SUCCESS, "Dopasowanie jest stabilne");
			this.matching.updateMatching();
			return;
		}
		// Phase 2
		saveLog(MessageType.WARNING, "Start fazy 2");
		status = secondPhase();
		saveLog(MessageType.WARNING, "Koniec fazy 2");
		if (!status) {
			saveLog(MessageType.ERROR, "Dopasowanie nie jest stabilne");
		} else {
			saveLog(MessageType.SUCCESS, "Dopasowanie jest stabilne");
		}
		this.matching.updateMatching();
	}
	
	/**
	 * @fn firstPhase
	 * @brief Run first phase of SR algorithm.
	 * After this method matching should be reduced to maximum opportunities.
	 * @return True if matching is super stable or require to run second phase.
	 * False if matching is not super stable.
	 * @throws InterruptedException 
	 */
	protected boolean firstPhase() throws InterruptedException {
		// Declare stack of undone persons 
		Stack<Integer> stack = new Stack<Integer>();
		for (int key = this.matching.size()-1; key >= 0; key--) {
			stack.add(key);
		}

		// Loop until stack will be empty
		while (!stack.isEmpty()) {	
			// Get next person from stack
			int person = (int)stack.pop();
			saveLog(MessageType.INFO, "Wez osobe " + Integer.toString(person+1));
			int personListSize = this.matching.get(person).size();
			// Check if current person list is not empty
			if (this.endOfAlgorithm() && !this.stable) {
				saveLog(MessageType.ERROR, "Znaleziono pusta liste - przerwij faze 1");
				return false;
			}
			// Go over every propose in current person list
			for (int i = 0; i < personListSize; i++) {
				for (int j = 0; j < this.matching.get(person).get(0).size(); j++) {
					// Get next person's choice in his/her list
					int nextChoice = this.matching.get(person).get(0).get(j);
					saveLog(MessageType.INFO, "Zbadaj propozycje osoby " + Integer.toString(nextChoice+1) + " dla osoby " + Integer.toString(person+1));
					
					// Check, if person is not removed in his/her proposition's list
					saveLog(MessageType.INFO, "Sprawdz, czy osoba " + Integer.toString(person+1) + " istnieje na liscie osoby " + Integer.toString(nextChoice+1));
					if (this.matching.indexInSubList(nextChoice, person) != null) {
						// Check, if nextChoice has any other proposition
						saveLog(MessageType.INFO, "Sprawdz, czy osoba " + Integer.toString(nextChoice+1) + " posiada inna propozycje niz od osoby " + Integer.toString(person+1));
						if (this.setProposedFrom.get(nextChoice) != -1) {
							int oldPropose = this.setProposedFrom.get(nextChoice);
							
							// Remove the current best proposition from person who has propose person
							saveLog(MessageType.INFO, "Usun stara propozycje (" + Integer.toString(oldPropose+1) + ") u osoby " + Integer.toString(nextChoice+1));
							for (int k = 0; k < this.matching.get(oldPropose).get(0).size(); k++) {
								// Delete only this element, where was that proposition
								if (this.matching.get(oldPropose).get(0).get(k) == nextChoice) {
									this.matching.deleteIndexFromList(oldPropose, 0, k);
								}
							}
							
							// Remove old proposition from current person list
							saveLog(MessageType.INFO, "Usun pare {" + Integer.toString(oldPropose+1) + "," +  Integer.toString(person+1) + "} z dopasowania");
							Entry<Integer,Integer> entry = this.matching.indexInSubList(person, oldPropose);
							if (entry != null) {
								this.matching.deleteIndexFromList(person, (int)entry.getKey(), (int)entry.getValue());
							}
							entry = this.matching.indexInSubList(oldPropose, person);
							if (entry != null) {
								this.matching.deleteIndexFromList(oldPropose, (int)entry.getKey(), (int)entry.getValue());
							}
							
							// Add old propose again on top of stack;
							saveLog(MessageType.INFO, "Dodaj osobe " + Integer.toString(oldPropose+1) + " z powrotem na stos");
							stack.push(oldPropose);
						}
						// Override old propose by new one
						saveLog(MessageType.INFO, "Zapisz propozycje " + Integer.toString(person+1) + "=>" + Integer.toString(nextChoice+1) + " do tabeli rozpatrywanych rozwiazan");
						this.setProposedFrom.set(nextChoice, person);
						
						// Reducing matrix in propose row from worse elements than current person
						saveLog(MessageType.INFO, "Usuwanie gorszych propozycji od osoby " + Integer.toString(person+1) + " na liscie " + Integer.toString(nextChoice+1) 
							+ " oraz osoby " + Integer.toString(nextChoice+1) + " z listy gorszych osób jesli takie istnieja");
						if (this.setProposedFrom.get(nextChoice) == person) {
							int index = this.matching.indexInSubList(nextChoice, person).getKey()+1;
							int size = this.matching.get(nextChoice).size();
							for (int k = index; k < size; k++) {
								// Delete nextChoice from worse proposition lists
								for (int l = 0; l < this.matching.get(nextChoice).get(index).size(); l++) {
									int removingElement = this.matching.get(nextChoice).get(index).get(0);
									Entry<Integer, Integer> entry = this.matching.indexInSubList(removingElement, nextChoice);
									if (entry != null) {
										this.matching.deleteIndexFromList(removingElement, entry.getKey(), entry.getValue());
									}
								}
								// Delete worse proposition in nextChoice list
								this.matching.get(nextChoice).remove(index);
							}
						}
						
						// Check, if propose also is assign to current person
						// If yes, remove these persons from other person's lists
						saveLog(MessageType.INFO, "Sprawdz czy propozycja " + Integer.toString(nextChoice+1) + "=>" + Integer.toString(person+1) + " juz nie istnieje");
						if (this.setProposedFrom.get(person) == nextChoice &&
								this.setProposedFrom.get(nextChoice) == person) {
							saveLog(MessageType.INFO, "Taka propozycja istnieje, wiec usun osoby ze wszystkich propozycji pozostalych osob");
							for (int k = 0; k < this.matching.size(); k++) {
								// Skip current person and his/her propose lists 
								if (k != person && k != nextChoice) {
									// Remove current person from list if is still available
									Entry<Integer, Integer> tmp = this.matching.indexInSubList(k, person);
									if (tmp != null) {
										this.matching.deleteIndexFromList(k, (int)tmp.getKey(), 
												this.matching.get(k).get(tmp.getKey()).indexOf(person));
									}
									// Remove propose from list if is still available
									tmp = this.matching.indexInSubList(k, nextChoice);
									if (tmp != null) {
										this.matching.deleteIndexFromList(k, (int)tmp.getKey(), 
												this.matching.get(k).get(tmp.getKey()).indexOf(nextChoice));
									}
								} else {
									for (int l = 1; l < this.matching.get(k).size(); l++) {
										this.matching.get(k).remove(l);
									}
								}
							}
						}
						
						// Do not loop again if person has proposition
						i = personListSize;
					} else {
						if (this.endOfAlgorithm()) {
							saveLog(MessageType.ERROR, "Nie mozna skladac propozycji osobie z pusta lista preferencji - przerwij faze 1");
							return false;
						}
						
						// Delete this propose from person row
						saveLog(MessageType.INFO, "Osoba " + Integer.toString(nextChoice+1) + " nie posiada na swojej liscie osoby " 
								+ Integer.toString(person+1) + " - usun propozycje u tej osoby");
						this.matching.deleteIndexFromList(person, 0, 0);
					}
					// Check if current person list is not empty
					if (this.endOfAlgorithm() && !this.stable) {
						saveLog(MessageType.ERROR, "Znaleziono pusta liste - przerwij faze 1");
						return false;
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * @fn secondPhase
	 * @brief Run second phase of SR algorithm.
	 * After this method we get final information, that we can get super stable matching from these data.
	 * @return True if matching is super stable.
	 * False if matching is not stable.
	 * @throws InterruptedException 
	 */
	protected boolean secondPhase() throws InterruptedException {
		saveLog(MessageType.WARNING, "Rozpoczecie fazy 2");
		
		// Clear old propositions
		for (int i = 0; i < this.setProposedFrom.size(); i++) {
			if (this.matching.get(i).size() > 1) {
				this.setProposedFrom.set(i, -1);
			}
		}
		
		while(!endOfAlgorithm()) {
			saveLog(MessageType.INFO, "Rozpocznij szukanie cyklu");
			// Declare p and q
			List<Integer> p = new ArrayList<Integer>();
			List<Integer> q = new ArrayList<Integer>();
			
			// Find p1
			for (int i = 0; i < this.matching.size(); i++) {
				if (this.matching.get(i).size() > 1) {
					p.add(i);
					i = this.matching.size();
				}
			}
			saveLog(MessageType.INFO, "Element p1 wynosi " + (p.get(0)+1));
			
			// Find cycle
			while(moreThanOnce(p) == -1) {
				// Get next q
				int element = p.get(p.size()-1);
				int index = 1;
				q.add(this.matching.get(element).get(index).get(0));
				saveLog(MessageType.INFO, "Element q" + q.size() + " wynosi " + (q.get(q.size()-1)+1));
				
				// Get next p
				element = q.get(q.size()-1);
				index = this.matching.get(element).size()-1;
				p.add(this.matching.get(element).get(index).get(0));
				saveLog(MessageType.INFO, "Element p" + p.size() + " wynosi " + (p.get(p.size()-1)+1));
			}
			
			// Get cycle data
			int cyclePerson = moreThanOnce(p);
			int cyclePersonIndex1 = p.indexOf(cyclePerson);
			int cyclePersonIndex2 = p.subList(cyclePersonIndex1+1, p.size()).indexOf(cyclePerson)+cyclePersonIndex1+1;
			int cycleLength = cyclePersonIndex2 - cyclePersonIndex1;
			saveLog(MessageType.INFO, "Znaleziono cykl w miejscu p" + (cyclePersonIndex1+1) + " o dlugosci " + cycleLength);
			
			// Get pairs to delete
			saveLog(MessageType.INFO, "Rozpocznij usuwanie przeciwległych par");
			for (int i = 0; i < cycleLength; i++) {
				int el1 = q.get(cyclePersonIndex1+i);
				int el2 = p.get(cyclePersonIndex1+i+1);
				saveLog(MessageType.INFO, "Usun pare {" + el1 + "," + el2 + "}");

				// Delete pair
				Entry<Integer, Integer> e = this.matching.indexInSubList(el1, el2);
				this.matching.get(el1).remove((int)e.getKey());
				
				e = this.matching.indexInSubList(el2, el1);
				this.matching.get(el2).remove((int)e.getKey());
			}
			
			// Find new propositions
			saveLog(MessageType.INFO, "Sprawdz czy istnieja nowe stabilne pary");
			for (int i = 0; i < this.setProposedFrom.size(); i++) {
				if (this.setProposedFrom.get(i) == -1 &&
						this.matching.get(i).size() == 1) {
					int a = this.matching.get(i).get(0).get(0);
					if (this.setProposedFrom.get(a) == -1 &&
							this.matching.get(a).size() == 1) {
						int b = this.matching.get(a).get(0).get(0);
						if (a == this.matching.get(b).get(0).get(0)) {
							saveLog(MessageType.INFO, "Znaleziono nowa stabilna pare {" + a + "," + b + "}");
							this.setProposedFrom.set(a, b);
							this.setProposedFrom.set(b, a);
						}
					}
				}
			}
		}
		if (this.stable) {
			return true;
		} else {
			return false;
		}
	}
		
	/**
	 * @fn endOfAlgorithm
	 * @brief Check if algorithm meets condition to finish reduction.
	 * It check also if matching is stable.
	 * @return True if someone preference list is empty.
	 */
	protected boolean endOfAlgorithm() {
		boolean anyEmptyList = false;
		boolean oneElementsInLists = true;
		for (int i = 0; i < this.matching.size(); i++) {
			if (this.matching.get(i).size() == 0) {
				anyEmptyList = true;
				break;
			}
			if (this.matching.get(i).size() != 1) {
				oneElementsInLists = false;
			}
		}
		if (anyEmptyList) {
			return true;
		} else if (oneElementsInLists) {
			this.stable = true;
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * @fn saveLog
	 * @brief Save log and current status of matching and preferences lists.
	 * @param type - level type of saving log.
	 * @param text - message of log.
	 * @throws InterruptedException
	 */
	protected void saveLog(MessageType type, String text) throws InterruptedException {
	    SuperSR.failIfInterrupted();
		// Check if component to saving logs is available
		if (console != null) {
			console.addMessage(type, text);
		}
		
		// Check if step algorithm is enabled
		if (stepsEnabled) {
			// Check if components to saving matching and preferences are available
			if (panel != null) {
				panel.removeAll();
				panel.revalidate();
			    
				matching.updateMatching();
				text = matching.toString();
				matching.reduceMatching();
			    JTextArea textarea = new JTextArea(text);
			    Font font = new Font("Verdana", Font.BOLD, 20);
			    textarea.setFont(font);
			    textarea.setEditable(false);
			    panel.add(textarea);
			    
				// refresh the panel.
			    panel.updateUI();
			    panel.revalidate();
			    panel.repaint();
			}
			if (table != null) {
				table.clear();
				for (int i = 0; i < setProposedFrom.size(); i++) {
					Vector<String> row = new Vector<String>();
					row.add(Integer.toString(i+1));

					text = "";
					for (int j = 0; j < this.getProposedFrom().get(i).size(); j++) {
						text += Integer.toString(this.getProposedFrom().get(i).get(j)+1) + ",";
					}
					if (text.length() > 1) {
						text = text.substring(0, text.length()-1);
					}
					row.addElement(text);
					text = "";
					for (int j = 0; j < this.getProposedTo().get(i).size(); j++) {
						text += Integer.toString(this.getProposedTo().get(i).get(j)+1) + ",";
					}
					if (text.length() > 1) {
						text = text.substring(0, text.length()-1);
					}
					row.addElement(text);
					
					table.addMessage(row);
				}
			}
			
			// Wait for user input
	         while (!nextStep) {
	            try {
	               Thread.sleep(50);
	            } catch (InterruptedException e) {
	            	this.matching.updateMatching();
	            }
	         }
	         nextStep = false;
		}
	}
	
	/**
	 * @fn failIfInterrupted
	 * @brief Stop algorithm by throwing InterruptedException
	 * @throws InterruptedException
	 */
	protected static void failIfInterrupted() throws InterruptedException {
		if (Thread.currentThread().isInterrupted()) {
			throw new InterruptedException("Interrupted");
		}
	}
	
	/**
	 * @fn doInBackground
	 * @brief Run algorithm in background.
	 * @throws Exception
	 */
	@Override
	protected Integer doInBackground() throws Exception {
		start();
		return 0;
	}
	
	/**
	 * @brief
	 * Find repeating element in list.
	 * @param list - available numbers
	 * @return Element which occurs more than once, or -1 in other case.
	 */
	private int moreThanOnce(List<Integer> list) {
	    for (int i = 0; i < list.size(); i++) {
		    for (int j = 0; j < list.size(); j++) {
		    	// Skip current item
		    	if (i == j) {
		    		continue;
		    	}
		    	// Check if items are equal and return it if yes
		        if (list.get(i) == list.get(j)) {
		            return list.get(i);
		        }
		    }
	    }

	    // Items aren't equal
	    return -1;
	}

}