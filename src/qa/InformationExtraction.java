/**
 * 
 */
package qa;

import java.util.HashMap;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

/**
 * @author Deepak
 *
 */
public class InformationExtraction {
		
	private static String keywordsPattern = "[?(){}\\[\\]\\/]" ; 
	private static HashMap<String, String> posHashMap = new HashMap<String, String>();
	private static MaxentTagger tagger;
	static {
		tagger = new MaxentTagger("External Lib/taggers/english-left3words-distsim.tagger");
		
		posHashMap.put("NN", "Noun");
		posHashMap.put("NNP", "Proper Noun");
		posHashMap.put("JJ", "Adjective");
		posHashMap.put("VB", "Verb");
	}
	
	public static String extractKeywords(String question) {
		String keywordsString = "";
		question = question.replaceAll(keywordsPattern, " ");
			
		String taggedQues = tagger.tagString(question);
		String [] quesTags = taggedQues.split("_|\\s");
		
		String start = "\"";
		int startIndex = question.indexOf(start);
		int lastIndex = question.indexOf(start);		
			
		for (int i = 1; i < quesTags.length; i++) {
			if(posHashMap.containsKey(quesTags[i])) {
				keywordsString = keywordsString + quesTags[i-1] + " ";
			}
		}
	
		if(startIndex != -1) {
			keywordsString += question.substring(startIndex, lastIndex);
		}
		
		return keywordsString;
	}

}
