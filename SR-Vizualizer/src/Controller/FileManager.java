package Controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;

import Model.Matching;

/**
 * @class FileManager
 * @author Hubert Furmanek <254315@stud.umk.pl>
 * @brief Static class with methods handle saving and loading Matching object to/from file.
 */
public final class FileManager {
	private FileManager() {}
	
	/**
	 * @fn loadFile
	 * @brief Load file from showed dialog and return encoded matching from them.
	 * @return Matching or null if file has wrong format.
	 */
	public static Matching loadFile() {		
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Pliki txt", "txt");
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setCurrentDirectory(new File("./examples"));
		fileChooser.setDialogTitle("Wybierz plik z danymi do wczytania");
		fileChooser.setFileFilter(filter);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int retVal = fileChooser.showOpenDialog(null);
        
        if (retVal == JFileChooser.APPROVE_OPTION) {
	        File file = null;
	        try {
	        	file = fileChooser.getSelectedFile();
	        	return decode(file);
	        } catch (Exception e) {}
        }
        return null;
	}
	
	/**
	 * @fn saveFile
	 * @brief Save current solution from existing Matching in application.
	 * @param matching - Matching taken from application.
	 */
	public static void saveFile(Matching matching) {
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Pliki txt", "txt");
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setCurrentDirectory(new File(""));
		fileChooser.setDialogTitle("Wybierz miejsce do zapisania danych");
		fileChooser.setFileFilter(filter);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int retVal = fileChooser.showSaveDialog(null);
        
        if (retVal == JFileChooser.APPROVE_OPTION) {
	        File file = null;
	        try {
	        	file = fileChooser.getSelectedFile();
	        	encode(file, matching);
	        } catch (Exception e) {}
        }
	}
	
	public static void saveFile(JTable console) {
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Pliki txt", "txt");
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setCurrentDirectory(new File(""));
		fileChooser.setDialogTitle("Wybierz miejsce do zapisania danych");
		fileChooser.setFileFilter(filter);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int retVal = fileChooser.showSaveDialog(null);
        
        if (retVal == JFileChooser.APPROVE_OPTION) {
	        File file = null;
	        try {
	        	file = fileChooser.getSelectedFile();
	        	saveLogs(file, console);
	        } catch (Exception e) {}
        }
	}
	
	private static void saveLogs(File file, JTable console) {
		String content = "";
		String content2 = "";
		for (int i = 0; i < console.getRowCount(); i++) {
			content += console.getModel().getValueAt(i, 0) + ". ";
			
			content2 += console.getModel().getValueAt(i, 1);
			content2 = content2.substring(0, content2.indexOf("</"));
			content2 = content2.substring(content2.lastIndexOf(">")+1);
			System.out.println(content2);
			content2 += "\n";
			
			content += content2;
			content2 = "";
		}
		content = content.replace("\n", "\r\n");
		
		BufferedWriter writer = null;
		try {
		    writer = new BufferedWriter(new FileWriter(file));
		    writer.write(content);
	        writer.close();
		}
		catch (IOException e) {}
	}

	/**
	 * @fn encode
	 * @brief Encode given matching to chosen file object.
	 * @param file - object chosen from showed dialog.
	 * @param matching - Matching taken from application.
	 */
	private static void encode(File file, Matching matching) {
		String content = "";
		for (int i = 0; i < matching.size(); i++) {
			for (int j = 0; j < matching.get(i).size(); j++) {
				for (int k = 0; k < matching.get(i).get(j).size(); k++) {
					if (matching.get(i).get(j).size() != 1) {
						content += "[";
					}
					content += Integer.toString(matching.get(i).get(j).get(k)) + ",";
				}

				if (matching.get(i).get(j).size() != 1) {
					content = content.substring(0, content.length()-1);
					content += "],";
				}
			}
			content = content.substring(0, content.length()-1);
			content += "\n";
		}
		content = content.substring(0, content.length()-2);
		
		BufferedWriter writer = null;
		try {
		    writer = new BufferedWriter(new FileWriter(file));
		    writer.write(content);
	        writer.close();
		}
		catch (IOException e) {}
	}
	
	/**
	 * @fn decode
	 * @brief Decode txt file with converted Matching.
	 * @param file - object which contain Matching object with txt format.
	 * @return Matching
	 */
	private static Matching decode(File file) {
		Matching matching = null;
		BufferedReader reader = null;
		try {
		    FileInputStream fis = new FileInputStream(file);
		    reader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
		    
		    Boolean isTie = false;
		    String line = reader.readLine();
	        List<Integer> element = new ArrayList<Integer>();
		    ArrayList<List<List<Integer>>> match = new ArrayList<List<List<Integer>>>();
		    while (line != null) {
		    	if (line.equals("")) {
		    		throw new Exception("Bad format of file");
		    	}
		        String[] parts = line.split(",");
		        List<List<Integer>> row = new ArrayList<List<Integer>>();
		        for (int i = 0; i < parts.length; i++) {
			        if (parts[i].indexOf('[') != -1) {
			        	isTie = true;
			        	parts[i] = parts[i].substring(1);
			        } else if (parts[i].indexOf(']') != -1) {
			        	parts[i] = parts[i].substring(0, parts[i].length()-1);
			        	element.add(Integer.parseInt(parts[i]));
			        	row.add(element);
			        	
			        	isTie = false;
			        	element = new ArrayList<Integer>();
			        	continue;
			        } 

			        element.add(Integer.parseInt(parts[i]));
			        if (!isTie) {
			        	row.add(element);
			        	element = new ArrayList<Integer>();
			        }
		        }
		        match.add(row);
		        line = reader.readLine();
		    }
		    matching = new Matching(match);
	    } catch (Exception e) {}
		finally {
	    	try {
				reader.close();
			} catch (IOException e) {}
		}
		return matching;
	}
}
