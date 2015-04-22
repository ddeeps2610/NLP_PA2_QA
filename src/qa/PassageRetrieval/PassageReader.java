/**
 * 
 */
package qa.PassageRetrieval;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;



public class PassageReader implements IPassageReader {
	
	public static List<String> splitDocuments(String filename) {

		List<String> topDocs = new LinkedList<String>();
	
		BufferedReader br = null;
		String xmlOutput = "";
		try {
			br = new BufferedReader(new FileReader (filename));
			String currentLine = null;
			while ((currentLine = br.readLine()) != null) {
				//	System.out.println(CurrentLine);
				if (!currentLine.contains("Qid")) {
					xmlOutput = xmlOutput + " " + fixXML(currentLine);
				}
				else {
					if (!xmlOutput.trim().isEmpty()) {
						topDocs.add(xmlOutput.trim());
						xmlOutput = "";
					}
				}				
			}
			if (!xmlOutput.trim().isEmpty()) 
				topDocs.add(xmlOutput.trim());
			
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException ex) {
				System.err.println(ex.getMessage());
			}
		}
		return topDocs;
	}

	/**
	 * 
	 * @param documentData
	 * @return
	 */
	private static String fixXML(String documentData) {
		int index = 0;
		StringBuilder retVal = new StringBuilder();
		char temp;
		while(index < documentData.length()) {
			temp = documentData.charAt(index);
			if(temp == '<' && documentData.charAt(index + 1) != '/') {
				retVal.append(temp);
				++index;
				while(true) {
					temp = documentData.charAt(index);
					if(temp != ' ' && temp != '>') {
						retVal.append(temp);
						++index;
					} else {
						break;
					}
				}
				while(true) {
					temp = documentData.charAt(index);
					++index;
					if(temp == '>') {
						break;
					}
				}
				retVal.append('>');
			} else {
				retVal.append(temp);
				++index;
			}
		}
		
		String tempString = retVal.toString();
		
		if(tempString.contains("& ")) {
			tempString = tempString.replaceAll("& ", "&amp;");
		}
		
		String[] regex = {",", "!"};
		for(String str : regex) {
			tempString = tempString.replaceAll(str, " " + str);
		}
		return tempString;
	}
}
		




