package Model;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

import javax.swing.JTextArea;

import View.MessageType;

/**
 * @class SuperSRT
 * @author Hubert Furmanek <254315@stud.umk.pl>
 * @brief Class represents Super Stable Roommates algorithm with Ties.
 */
public class SuperSRT extends SuperSR {

	protected List<List<Integer>> setProposedTo;
	protected List<List<Integer>> setProposedFrom;
	
	/**
	 * @fn SuperSRT
	 * @brief Constructor of SR algorithm class.
	 * @param matching roommates matching in format of Matching class.
	 */
	public SuperSRT(Matching matching) {
		this.matching = matching;

		// Declare lists with propositions
		this.setProposedTo = new ArrayList<List<Integer>>();
		this.setProposedFrom = new ArrayList<List<Integer>>();
		for (int i = 0; i < matching.size(); i++) {
			this.setProposedTo.add(new ArrayList<Integer>());
			this.setProposedFrom.add(new ArrayList<Integer>());
		}
	}
	
	/**
	 * @fn SuperSRT
	 * @brief Constructor of SRT algorithm class.
	 * @param matrix - roommates matching in format of Java Lists.
	 */
	public SuperSRT(ArrayList<List<List<Integer>>> matching) {
		this.matching = new Matching(matching);

		// Declare lists with propositions
		this.setProposedTo = new ArrayList<List<Integer>>();
		this.setProposedFrom = new ArrayList<List<Integer>>();
		for (int i = 0; i < matching.size(); i++) {
			this.setProposedTo.add(new ArrayList<Integer>());
			this.setProposedFrom.add(new ArrayList<Integer>());
		}
	}
	
	/**
	 * @fn SuperSRT
	 * @brief Constructor of SRT algorithm class.
	 * @param matrix - roommates matching in format of Java Arrays.
	 */
	public SuperSRT(int[][][] matching) {
		this.matching = new Matching(matching);

		// Declare lists with propositions
		this.setProposedTo = new ArrayList<List<Integer>>();
		this.setProposedFrom = new ArrayList<List<Integer>>();
		for (int i = 0; i < matching.length; i++) {
			this.setProposedTo.add(new ArrayList<Integer>());
			this.setProposedFrom.add(new ArrayList<Integer>());
		}
	}

	/**
	 * @fn getProposedFrom
	 * @brief return list of proposition from other persons.
	 * @return List of accepted propositions from other persons.
	 */
    @Override
	public List<List<Integer>> getProposedFrom() {
		return setProposedFrom;
	}
	
	/**
	 * @fn getProposedTo
	 * @brief return list of proposition to other persons.
	 * @return List of accepted propositions to other persons.
	 */
    @Override
	public List<List<Integer>> getProposedTo() {
		return setProposedTo;
	}
	
	/**
	 * @fn firstPhase
	 * @brief Run first phase of SRT algorithm.
	 * After this method matching should be reduced to maximum opportunities.
	 * @return True if matching is super stable or require to run second phase.
	 * False if matching is not super stable.
	 * @throws InterruptedException 
	 */
	@Override
	protected boolean firstPhase() throws InterruptedException {
		// Declare stack of undone persons 
		Stack<Integer> stack = new Stack<Integer>();
		for (int key = this.matching.size()-1; key >= 0; key--) {
			if (this.setProposedTo.get(key).size() == 1) {
				int val = this.setProposedTo.get(key).get(0);
					if (this.setProposedTo.get(val).size() == 1 &&
							this.setProposedTo.get(val).get(0) == key) {
							continue;
					}
			}
			stack.add(key);
			this.setProposedTo.get(key).clear();
			this.setProposedFrom.get(key).clear();
		}
		
		// Loop until stack will be empty
		while (!stack.isEmpty()) {	
			// Get next person from stack
			int person = (int)stack.pop();
			saveLog(MessageType.INFO, "Wez osobe " + Integer.toString(person+1));
			int personListSize = this.matching.get(person).size();
			// Check if current person list is not empty
			if (this.endOfAlgorithm() && !this.stable) {
				saveLog(MessageType.WARNING, "Znaleziono pusta liste - przerwij faze 1");
				return false;
			}
			// Go over every propose in current person list
			for (int i = 0; i < personListSize; i++) {
				// Loop over all person in tie
				int personTieSize = this.matching.get(person).get(0).size();
				for (int j = 0; j < personTieSize; j++) {
					// Get next person's choice in his/her list
					int nextChoice = this.matching.get(person).get(0).get(j);
					saveLog(MessageType.INFO, "Zbadaj propozycje osoby " + Integer.toString(nextChoice+1) + " dla osoby " + Integer.toString(person+1));
					
					// Check, if person is not removed in his/her proposition's list
					saveLog(MessageType.INFO, "Sprawdz, czy osoba " + Integer.toString(person+1) + " istnieje na liscie osoby " + Integer.toString(nextChoice+1));
					if (this.matching.indexInSubList(nextChoice, person) != null) {
						// Check, if nextChoice has any other proposition
						saveLog(MessageType.INFO, "Sprawdz, czy osoba " + Integer.toString(nextChoice+1) + " posiada inna propozycje niz od osoby " + Integer.toString(person+1));
						Entry<Integer, Integer> entry = this.matching.indexInSubList(nextChoice, person);
						if (this.setProposedFrom.get(nextChoice).size() == 1 &&
								this.matching.get(nextChoice).get(entry.getKey()).size() == 1) {
							int oldPropose = this.setProposedFrom.get(nextChoice).get(0);
							
							// Remove the current best proposition from person who has propose person
							saveLog(MessageType.INFO, "Usun stara propozycje (" + Integer.toString(oldPropose+1) + ") u osoby " + Integer.toString(nextChoice+1));
							int size = this.matching.get(oldPropose).get(0).size();
							for (int k = 0; k < size; k++) {
								// Delete only this element, where was that proposition
								if (this.matching.get(oldPropose).get(0).get(k) == nextChoice) {
									this.matching.deleteIndexFromList(oldPropose, 0, k);
									k = size;
								}
							}
							
							// Delete proposition from proposition lists
							if (!this.setProposedTo.get(person).isEmpty()) {
								this.setProposedTo.get(person).remove(
										(Object)this.matching.indexInSubList(person, oldPropose).getKey());
							}
							this.setProposedFrom.get(nextChoice).clear();
							
							for (int k = 0; k < this.setProposedTo.get(oldPropose).size(); k++) {
								if (this.setProposedTo.get(oldPropose).get(k) == nextChoice) {
									this.setProposedTo.get(oldPropose).remove(k);
									k = this.setProposedTo.get(oldPropose).size();
								}
							}
							
							// Remove old proposition from current person list
							saveLog(MessageType.INFO, "Usun pare {" + Integer.toString(oldPropose+1) + "," +  Integer.toString(person+1) + "} z dopasowania");
							entry = this.matching.indexInSubList(person, oldPropose);
							if (entry != null) {
								this.matching.deleteIndexFromList(person, (int)entry.getKey(), (int)entry.getValue());
							}
							entry = this.matching.indexInSubList(oldPropose, person);
							if (entry != null) {
								this.matching.deleteIndexFromList(oldPropose, (int)entry.getKey(), (int)entry.getValue());
							}
							
							// Add old propose again on top of stack if he doesn't have any other accepted propositions
							if (this.setProposedTo.get(oldPropose).size() == 0) {
								saveLog(MessageType.INFO, "Dodaj osobe " + Integer.toString(oldPropose+1) + " z powrotem na stos");
								stack.push(oldPropose);
							}
						}
						// Assign current person to propose
						saveLog(MessageType.INFO, "Dodaj propozycje " + Integer.toString(person+1) + "=>" + Integer.toString(nextChoice+1) + " do tabeli rozpatrywanych rozwiazan");
						this.setProposedTo.get(person).add(nextChoice);
						this.setProposedFrom.get(nextChoice).add(person);
						
						// If there is more than one proposition from other people to next choice
						// then delete all proposition and remove these elements in matrix
						saveLog(MessageType.INFO, "Sprawdz, czy osoba " + Integer.toString(nextChoice+1) + " posiada wiecej propozycji od innych osob");
						if (this.setProposedFrom.get(nextChoice).size() > 1) {
							saveLog(MessageType.INFO, "Takie osoby istnieja, wiec usun je z list propozycji i z odpowiednich pozycji w macierzy");
							for (int k : this.setProposedFrom.get(nextChoice)) {
								// Delete next element from setProposedFrom
								this.setProposedTo.get(k).remove((Object)nextChoice);
								// Delete next choice in old proposition row
								this.matching.deleteElementFromList(k, 0, (Object)nextChoice);
								
								// Delete old proposition in next choice row
								int firstIndex = this.matching.indexInSubList(nextChoice, k).getKey();
								this.matching.deleteElementFromList(nextChoice, firstIndex, (Object)k);
								
								// Check if nextPerson contain proposition to these persons
								for (int x = 0; x < this.setProposedTo.get(nextChoice).size(); x++) {
									if (this.setProposedTo.get(nextChoice).get(x) == k) {
										this.setProposedTo.get(nextChoice).remove(x);
										for (int y = 0; y < this.setProposedFrom.get(k).size(); y++) {
											if (this.setProposedFrom.get(k).get(y) == nextChoice) {
												this.setProposedFrom.get(k).remove(y);
											}
										}
									}
								}
								
								// If old proposition has not any other proposition to, push old proposition on stack again
								if (this.setProposedTo.get(k).isEmpty()) {
									stack.add(k);
								}
								
							}
							this.setProposedFrom.get(nextChoice).clear();
						}
						
						// Reducing matrix in propose row from worse elements than current person
						saveLog(MessageType.INFO, "Usuwanie gorszych propozycji od osoby " + Integer.toString(person+1) + " na liscie " + Integer.toString(nextChoice+1) 
							+ " oraz osoby " + Integer.toString(nextChoice+1) + " z listy gorszych osób jesli takie istnieja");
						if (this.setProposedTo.get(person).contains(nextChoice)) {
							int index = this.matching.indexInSubList(nextChoice, person).getKey()+1;
							int size = this.matching.get(nextChoice).size();
							for (int k = index; k < size; k++) {
								// Delete nextChoice from worse proposition lists
								for (int l = 0; l < this.matching.get(nextChoice).get(index).size(); l++) {
									int removingElement = this.matching.get(nextChoice).get(index).get(0);
									entry = this.matching.indexInSubList(removingElement, nextChoice);
									if (entry != null) {
										this.matching.deleteIndexFromList(removingElement, entry.getKey(), entry.getValue());
									}
								}
								// Delete worse proposition in nextChoice list
								this.matching.get(nextChoice).remove(index);
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
						saveLog(MessageType.WARNING, "Znaleziono pusta liste - przerwij faze 1");
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * @fn secondPhase
	 * @brief Run second phase of SRT algorithm.
	 * After this method we get final information, that we can get super stable matching from these data.
	 * @return True if matching is super stable.
	 * False if matching is not stable.
	 * @throws InterruptedException 
	 */
    @Override
    protected boolean secondPhase() throws InterruptedException {
    	// Create matching backup
    	ArrayList<List<List<Integer>>> backup = this.matching.cloneMatching();
    	int p = -1, r = -1, q = -1;
    	for (int i = 0; i < this.matching.size(); i++) {
    		if (this.matching.get(i).size() > 1) {
    			p = i;
    			r = this.matching.get(p).get(0).get(0);
    			q = this.matching.get(p).get(this.matching.get(p).size()-1).get(0);
    			break;
    		}
    	}
    	
    	// Reduce matrix by removing pair p-r
    	saveLog(MessageType.INFO, "Zredukuj macierze dopasowan poprzez usuniecie pary " + Integer.toString(p+1) + "=>" + Integer.toString(r+1));
    	int tmp = this.matching.indexInSubList(p, r).getKey();
    	this.matching.get(p).remove(tmp);
    	tmp = this.matching.indexInSubList(r, p).getKey();
    	this.matching.get(r).remove(tmp);

    	// Run phase 1
    	saveLog(MessageType.INFO, "Uruchom ponownie faze 1");
    	firstPhase();
    	
    	// Check if phase 1 resolve matching
    	if (!endOfAlgorithm()) {
    		secondPhase();
    	}
    	
    	// Check result
    	endOfAlgorithm();
    	if (this.stable) {
    		saveLog(MessageType.INFO, "Otrzymane rozwiazanie jest stabilne");
    		return true;
    	} else {
    		// Undo changes on matrix
    		saveLog(MessageType.INFO, "Otrzymane rozwiazanie nie bylo stabilne - cofnij zmiany z przed pierwszego uruchomienia fazy 1");
    		this.matching = new Matching(backup);

    		// Reduce matrix by removing pair p-q
    		saveLog(MessageType.INFO, "Zredukuj macierze dopasowan poprzez usuniecie pary " + Integer.toString(p+1) + "=>" + Integer.toString(q+1));
    		tmp = this.matching.indexInSubList(p, q).getKey();
    		this.matching.get(p).remove(tmp);
    		tmp = this.matching.indexInSubList(q, p).getKey();
    		this.matching.get(q).remove(tmp);
    		
    		// Run again first phase of algorithm
    		saveLog(MessageType.INFO, "Uruchom ponownie faze 1");
    		firstPhase();
    		
    		// Check if phase 1 resolve matching
    		if (!endOfAlgorithm()) {
    			secondPhase();
    		}
    		
    		// Check result
    		endOfAlgorithm();
    		if (this.stable) {
    			saveLog(MessageType.INFO, "Otrzymane rozwiazanie jest stabilne");
    			return true;
    		} else {
    			saveLog(MessageType.INFO, "Otrzymane rozwiazanie nie jest stabilne");
    			return false;
    		}
    	}
    }
    
    /**
	 * @fn saveLog
	 * @brief Save log and current status of matching and preferences lists.
	 * @param type - level type of saving log.
	 * @param text - message of log.
	 * @throws InterruptedException
	 */
    @Override
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
}
