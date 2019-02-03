package Model;

import java.util.ArrayList;
import java.util.List;

import View.MessageType;

/**
 * @class SuperSRT
 * @author Hubert Furmanek <254315@stud.umk.pl>
 * @brief Class represents Super Stable Roommates algorithm with Ties.
 */
public class SuperSRTI extends SuperSRT {
	
	/**
	 * @fn SuperSRI
	 * @brief Constructor of SR algorithm class.
	 * @param matching roommates matching in format of Matching class.
	 */
	public SuperSRTI(Matching matching) {
		super(matching);
	}
	
	/**
	 * @fn SuperSRTI
	 * @brief Constructor of SRT algorithm class.
	 * @param matrix - roommates matching in format of Java Lists.
	 */
	public SuperSRTI(ArrayList<List<List<Integer>>> matching) {
		super(matching);
	}
	
	/**
	 * @fn SuperSRTI
	 * @brief Constructor of SRT algorithm class.
	 * @param matrix - roommates matching in format of Java Arrays.
	 */
	public SuperSRTI(int[][][] matching) {
		super(matching);
	}
	
	/**
	 * @fn secondPhase
	 * @brief Run second phase of SRTI algorithm.
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
    	super.firstPhase();
    	
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
    		super.firstPhase();
    		
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
	 * @fn endOfAlgorithm
	 * @brief Check if algorithm meets condition to finish reduction.
	 * It check also if matching is stable.
	 * @return True if someone prefers to person with empty preference list.
	 */
    @Override
	protected boolean endOfAlgorithm() {
		for (int i = 0; i < this.setProposedFrom.size(); i++) {
			List<Integer> index = this.setProposedFrom.get(i);
			if (index.size() != 0) {
				for (int j = 0; j < index.size(); j++) {
					if (this.matching.get(index.get(j)).size() == 0) {
						return true;
					}
				}
			}
		}
		
		boolean zeroOrOneElementInList = true;
		for (int i = 0; i < this.matching.size(); i++) {
			if (this.matching.get(i).size() > 1) {
				zeroOrOneElementInList = false;
			} else if (this.matching.get(i).size() == 1) {
				if (this.matching.get(i).get(0).size() != 1) {
					zeroOrOneElementInList = false;
				}
			}
		}
		if (zeroOrOneElementInList) {
			this.stable = true;
			return true;
		}
		
		return false;
    }
}
