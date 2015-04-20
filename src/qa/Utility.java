/**
 * 
 */
package qa;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.NERClassifierCombiner;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

/**
 * @author Ravi
 *
 */
public class Utility {

	/**
	 * 
	 */
	public static boolean IsQuestionProcessingDone = false;
	
	/**
	 * 
	 */
	public static boolean IsPassageRetrivalDone = false;
	
	/**
	 * 
	 */
	private static MaxentTagger tagger;
	
	/**
	 * 
	 */
	private static String serializedNERClassifier;
	
	/**
	 * 
	 */
	private static AbstractSequenceClassifier<CoreLabel> nerClassifier;

	/**
	 * 
	 */
	private static String keywordsPattern = "[?(){}\\[\\]\\/]" ; 
	
	/**
	 * 
	 */
	private static HashMap<String, String> posHashMap = new HashMap<String, String>();
	
	/**
	 * 
	 */
	static {
		serializedNERClassifier = "External Lib/NER Classifiers/english.all.7class.distsim.crf.ser.gz";
		try {
			nerClassifier = new NERClassifierCombiner(serializedNERClassifier);
		} catch (ClassCastException | IOException ex) {
			System.err.println(ex.getMessage());
		}			  	
	}
	
	/**
	 * 
	 */
	static {
		tagger = new MaxentTagger("External Lib/taggers/english-left3words-distsim.tagger");
	}

	/**
	 * 
	 */
	static {		
		posHashMap.put("NN", "Noun");
		posHashMap.put("NNP", "Proper Noun");
		posHashMap.put("JJ", "Adjective");
		posHashMap.put("VB", "Verb");
		posHashMap.put("FW", "Foreign Word");
		posHashMap.put("JJR", "Compartive Adjective");
		posHashMap.put("JJS", "Adjective Singular");
		posHashMap.put("NNS", "Noun Plural");
		posHashMap.put("NNPS", "Proper Noun");
		posHashMap.put("VBD", "Verb Past Tense");
		posHashMap.put("VBG", "Present Participle");
		posHashMap.put("VBN", "Verb Parsed");
		posHashMap.put("VBP", "Verb Personal");
		posHashMap.put("VBZ", "Verb Singular");
	}
	
	/**
	 * 
	 * @param question
	 * @return
	 */
	public static String extractKeywords(String question) {
		String keywordsString = "";
		question = question.replaceAll(keywordsPattern, " ");
			
		String taggedQues = tagger.tagString(question);
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


	/**
	 * 
	 * @param sentence
	 * @return
	 */
	public static String getNERTagging(String sentence) {
		return nerClassifier.classifyToString(sentence);
	}
	
	/**
	 * 
	 * @param sentence
	 * @return
	 */
	public static String getPOSTagging(String sentence) {
		return tagger.tagString(sentence);
	}
	
	/**
	 * 
	 * @param passage
	 * @return
	 */
	public static List<String> sentenceTokenizer(List<String> passage){

		List<String> relevantSentences = new ArrayList<String>();
		for(String string : passage)
		{
			//(){}\\[\\]\"\'
			string = string.replaceAll("[(){}\\[\\]\"']", " ");
			String[] passagesSplit = string.split("\\.|\\?|\\!");
			for (int i = 0; i<passagesSplit.length;i++) {
				if (!passagesSplit[i].trim().isEmpty())
					relevantSentences.add(passagesSplit[i]);
			}
		}
		return relevantSentences;
	}
}
