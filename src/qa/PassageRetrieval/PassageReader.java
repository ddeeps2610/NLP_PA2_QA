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
	
	static List<String> splitDocuments(String filename) {
		

		List<String> TopDocs = new LinkedList<String>();
	
		BufferedReader br = null;
		String XMLOutput = "";
		try {
			br = new BufferedReader(new FileReader (filename));
			String CurrentLine = null;
			while ((CurrentLine = br.readLine()) != null) {
				if (!CurrentLine.contains("Qid")) {
				//	System.out.println(CurrentLine);
					XMLOutput = XMLOutput + " " + fixXML(CurrentLine);
				}
				else {
					//System.out.println(XMLOutput);
					if (!XMLOutput.trim().isEmpty()) {
						TopDocs.add(XMLOutput.trim());
						XMLOutput = "";
					}
				}				
			}
			if (!XMLOutput.trim().isEmpty()) 
				TopDocs.add(XMLOutput.trim());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (br != null)
					br.close();
				//System.out.println("Number of documents fed : "+TopDocs.size());
				
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return TopDocs;
	}

	
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
		return retVal.toString();
	}
	
	
	
}
		




