/**
 * 
 */
package qa;

import java.util.HashMap;

/**
 * @author Deepak
 *
 */
public class InformationExtraction {
		
	private static String keywordsPattern = "[?(){}\\[\\]\\/]" ; 
	private static HashMap<String, String> posHashMap = new HashMap<String, String>();
	static {		
		posHashMap.put("NN", "Noun");
		posHashMap.put("NNP", "Proper Noun");
		posHashMap.put("JJ", "Adjective");
		posHashMap.put("VB", "Verb");
	}
	
	public static String extractKeywords(String question) {
		String keywordsString = "";
		question = question.replaceAll(keywordsPattern, " ");
			
		String taggedQues = Utility.Tagger.tagString(question);
		String [] quesTags = taggedQues.split("_|\\s");
		
		String start = "\"";
		int startIndex = question.indexOf(start);
		int lastIndex = question.lastIndexOf(start);		
		
		for (int i = 1; i < quesTags.length; i++) {
			if(posHashMap.containsKey(quesTags[i])) {
				keywordsString = keywordsString + quesTags[i-1] + " ";
			}
		}
	
		if(startIndex != -1) {
			keywordsString += question.substring(startIndex, lastIndex + 1);
		}
		
		return keywordsString;
	}

}
