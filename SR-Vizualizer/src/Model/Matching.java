package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * @class Matching
 * @author Hubert Furmanek <254315@stud.umk.pl>
 * @brief Class represents matching between roommates in form of Java List.
 * It has form of 3D List (xyz), where x defines person with his preference list,
 * y defines next proposition on his list and z defines element in next proposition.
 */
public class Matching extends ArrayList<ArrayList<ArrayList<Integer>>> {

	private static final long serialVersionUID = 1L;
	
	/**
	 * @fn Matching
	 * @brief Constructor of Matching class.
	 * @param list - 3D list of preferences
	 */
	public Matching() {
		super();
	}
	
	/**
	 * @fn Matching
	 * @brief Constructor of Matching class.
	 * @param list - 2D list of preferences
	 */
	public Matching(List<List<Integer>> list) {
		super();
		for (int i = 0; i < list.size(); i++) {
			ArrayList<ArrayList<Integer>> y = new ArrayList<ArrayList<Integer>>();
			for (int j = 0; j < list.get(i).size(); j++) {
				ArrayList<Integer> z = new ArrayList<Integer>();
				z.add(list.get(i).get(j));
				y.add(z);
			}
			this.add(y);
		}
	}
	
	/**
	 * @fn Matching
	 * @brief Constructor of Matching class.
	 * @param list - 3D list of preferences
	 */
	public Matching(ArrayList<List<List<Integer>>> list) {
		super();
		for (int i = 0; i < list.size(); i++) {
			ArrayList<ArrayList<Integer>> y = new ArrayList<ArrayList<Integer>>();
			for (int j = 0; j < list.get(i).size(); j++) {
				ArrayList<Integer> z = new ArrayList<Integer>();
				for (int k = 0; k < list.get(i).get(j).size(); k++) {
					z.add(list.get(i).get(j).get(k));
				}
				y.add(z);
			}
			this.add(y);
		}
	}

	/**
	 * @fn Matching
	 * @brief Constructor of Matching class.
	 * @param list - 2D array of preferences
	 */
	public Matching(int[][] list) {
		super();
		for (int i = 0; i < list.length; i++) {
			ArrayList<ArrayList<Integer>> y = new ArrayList<ArrayList<Integer>>();
			for (int j = 0; j < list[i].length; j++) {
				ArrayList<Integer> z = new ArrayList<Integer>();
				z.add(list[i][j]);
				y.add(z);
			}
			this.add(y);
		}
	}
	
	/**
	 * @fn Matching
	 * @brief Constructor of Matching class.
	 * @param list - 3D array of preferences
	 */
	public Matching(int[][][] list) {
		super();
		for (int i = 0; i < list.length; i++) {
			ArrayList<ArrayList<Integer>> y = new ArrayList<ArrayList<Integer>>();
			for (int j = 0; j < list[i].length; j++) {
				ArrayList<Integer> z = new ArrayList<Integer>();
				for (int k = 0; k < list[i][j].length; k++) {
					z.add(list[i][j][k]);
				}
				y.add(z);
			}
			this.add(y);
		}
	}
	
	/**
	 * @fn cloneMatching
	 * @brief Copy all content of 3D List and return it as new Object.
	 * @return Exactly copy of current List of matches.
	 */
	public ArrayList<List<List<Integer>>> cloneMatching() {
		ArrayList<List<List<Integer>>> clone = new ArrayList<List<List<Integer>>>();
	    for(ArrayList<ArrayList<Integer>> subList : this) {
	    	List<List<Integer>> x = new ArrayList<List<Integer>>();
	    	for(List<Integer> subSubList : subList) {
	    		List<Integer> y = new ArrayList<Integer>();
	    		for(Integer item : subSubList) {
	    			y.add(item);
	    		}
	    		x.add(y);
	    	}
	    	clone.add(x);
	    }
	    return clone;
	}
	
	/**
	 * @fn updateMatching
	 * @brief Increase all elements in matching by 1.
	 * This method is necessary in SuperSR algorithm to get correct index element from Matching.
	 * By default List and Arrays start counting from 0 - not from 1 like input given from user.
	 */
	public void updateMatching() {
		for (int i = 0; i < this.size(); i++) {
			for (int j = 0; j < this.get(i).size(); j++) {
				for (int k = 0; k < this.get(i).get(j).size(); k++) {
					this.get(i).get(j).set(k, this.get(i).get(j).get(k)+1);
				}
			}
		}
	}
	
	/**
	 * @fn reduceMatching
	 * @brief Decrease all elements in matching by 1.
	 * This method is necessary in SuperSR algorithm to get correct index element from Matching.
	 * By default List and Arrays start counting from 0 - not from 1 like input given from user.
	 */
	public void reduceMatching() {
		for (int i = 0; i < this.size(); i++) {
			for (int j = 0; j < this.get(i).size(); j++) {
				for (int k = 0; k < this.get(i).get(j).size(); k++) {
					this.get(i).get(j).set(k, this.get(i).get(j).get(k)-1);
				}
			}
		}
	}
	
	/**
	 * @fn printMatching
	 * @brief Prints on standard output all elements from matching in readable form. 
	 */
	public void printMatching() {
		for (int i = 0; i < this.size(); i++) {
			System.out.print(i+1+": ");
			for (int j = 0; j < this.get(i).size(); j++) {
				if (this.get(i).get(j).size() > 1) {
					String row = "[";
					for (int k = 0; k < this.get(i).get(j).size(); k++) {
						row += this.get(i).get(j).get(k) + ",";
					}
					row = row.substring(0, row.length()-1) + "] ";
					System.out.print(row);
				} else {
					System.out.print(this.get(i).get(j).get(0) + " ");
				}
			}
			System.out.println("");
		}
	}
	
	/**
	 * @fn toString
	 * @brief Overrided method returning readable string with all elements in matching
	 * @return String
	 */
	@Override
    public String toString() {
		String content = "";
		for (int i = 0; i < this.size(); i++) {
			content += i+1+": ";
			for (int j = 0; j < this.get(i).size(); j++) {
				if (this.get(i).get(j).size() > 1) {
					String row = "[";
					for (int k = 0; k < this.get(i).get(j).size(); k++) {
						row += this.get(i).get(j).get(k) + ",";
					}
					row = row.substring(0, row.length()-1) + "] ";
					content += row;
				} else {
					content += this.get(i).get(j).get(0) + " ";
				}
			}
			content += "\n";
		}
		return content;
    }
	
	/**
	 * @fn indexInSubList
	 * @brief Find given element in chosen sublist.
	 * If element exists, method return both indexes to this element in Entry class.
	 * If not, method return null Object.
	 * @param index - index of row in which we are looking for
	 * @param head - element which we are looking for position
	 * @return Entry with both indexes of given element or null if not exist.
	 */
	public Entry<Integer, Integer> indexInSubList(int index, int head) {
		for (int i = 0; i < this.get(index).size(); i++) {
			for (int j = 0; j < this.get(index).get(i).size(); j++) {
				if (this.get(index).get(i).get(j) == head) {
					return new Entry<Integer, Integer>(i, j);
				}
			}
		}
		return null;
	}
	
	/**
	 * @fn deleteIndexFromList
	 * @brief Delete element on specified index from class.
	 * @param index1 - first index of Matching class
	 * @param index2 - second index of Matching class
	 * @param deleteIndex - last index of Matching class with element to remove.
	 */
	public void deleteIndexFromList(int index1, int index2, int deleteIndex) {
		this.get(index1).get(index2).remove(deleteIndex);
		if (this.get(index1).get(index2).size() == 0) {
			this.get(index1).remove(index2);
		}
	}
	
	/**
	 * @fn deleteElementFromList
	 * @brief Delete specified element from class.
	 * @param index1 - first index of Matching class
	 * @param index2 - second index of Matching class
	 * @param element - element in Matching class to remove.
	 */
	public void deleteElementFromList(int index1, int index2, Object element) {
		this.get(index1).get(index2).remove(element);
		if (this.get(index1).get(index2).size() == 0) {
			this.get(index1).remove(index2);
		}
	}
	
	/**
	 * @fn removeTies
	 * @brief Remove all ties without deleting object within it.
	 */
	public void removeTies() {
		for (int i = 0; i < this.size(); i++) {
			for (int j = 0; j < this.get(i).size(); j++) {
				if (this.get(i).get(j).size() > 1) {
					for (int k = this.get(i).get(j).size()-1; k >= 0; k--) {
						ArrayList<Integer> list = new ArrayList<Integer>();
						list.add(this.get(i).get(j).get(k));
						this.get(i).add(i+1, list);
					}
					this.get(i).remove(j);
				}
			}
		}
	}
}
