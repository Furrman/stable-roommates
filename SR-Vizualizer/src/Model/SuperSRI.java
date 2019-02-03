package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * @class SuperSRT
 * @author Hubert Furmanek <254315@stud.umk.pl>
 * @brief Class represents Super Stable Roommates algorithm with Ties.
 */
public class SuperSRI extends SuperSR {
	
	/**
	 * @fn SuperSRI
	 * @brief Constructor of SR algorithm class.
	 * @param matching roommates matching in format of Matching class.
	 */
	public SuperSRI(Matching matching) {
		super(matching);
	}
	
	/**
	 * @fn SuperSRI
	 * @brief Constructor of SR algorithm class.
	 * @param matrix - roommates matching in format of Java Lists.
	 */
	public SuperSRI(ArrayList<List<Integer>> matching) {
		super(matching);
	}
	
	/**
	 * @fn SuperSRI
	 * @brief Constructor of SR algorithm class.
	 * @param matrix - roommates matching in format of Java Arrays.
	 */
	public SuperSRI(int[][] matching) {
		super(matching);
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
			int index = this.setProposedFrom.get(i);
			if (index != -1 && 
					this.matching.get(index).size() == 0) {
				return true;
			}
		}
		
		boolean zeroOrOneElementInList = true;
		for (int i = 0; i < this.matching.size(); i++) {
			if (this.matching.get(i).size() > 1) {
				zeroOrOneElementInList = false;
			}
		}
		if (zeroOrOneElementInList) {
			this.stable = true;
			return true;
		}
		
		return false;
	}
}
