/**
 * 
 */
package qa;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.NERClassifierCombiner;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;


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
	private static StanfordCoreNLP pipeline;
	
	private static Tree tree=null ;
	private static Properties props = new Properties();

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
	
	static{
//		props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
//		pipeline = new StanfordCoreNLP(props);
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
	 * @param passages
	 * @return
	 */
	public static List<String> sentenceTokenizer(List<String> passages) {
		List<String> retVal = new ArrayList<String>();
		
		for(String passage : passages) {
			Reader reader = new StringReader(passage);
			DocumentPreprocessor dp = new DocumentPreprocessor(reader);	
			Iterator<List<HasWord>> it = dp.iterator();
		
			while (it.hasNext()) {
			   StringBuilder sentenceSb = new StringBuilder();
			   List<HasWord> sentence = it.next();
			   
			   for (HasWord token : sentence) {
			      if(sentenceSb.length()>1) {
			         sentenceSb.append(" ");
			      }
			      sentenceSb.append(token);
			   }
			   
			   retVal.add(sentenceSb.toString());
			}
		}
		return retVal;
	}
	
	/**
	 * 
	 * @param text
	 * @param flag
	 * @return
	 */
	public static List<String> getNounPhrases(String text, boolean flag)
	{
		if(flag) {
			text = text.replaceAll("[?.,]", "");
		}
	    
		Annotation document = new Annotation(text);
	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);
	    List<Tree> phraseList=new ArrayList<Tree>();
	    List<String> nounPhrase= new ArrayList<String>();

	    pipeline.annotate(document);
	    
	    for(CoreMap sentence: sentences){
	    	tree = sentence.get(TreeAnnotation.class);	
	    }
	    
	    for (Tree subtree: tree) {
	    	if(subtree.value().equals("NP")||(subtree.value().equals("WHNP"))) {
	    		phraseList.add(subtree);
	    	}
	    }
    	
	    for(Tree tree: phraseList) {
	    	StringBuilder np = new StringBuilder(); 
	    	String[] token = tree.toString().split("\\s");
	    	for(String str : token) {
	    		str = str.toLowerCase().replace("who", "").replace("which", "").replace("how", "").replace("what", "").replace("when", "").replace("where", "").replace("how", "");
	    		if(!str.contains("(")) {
	    			np.append(str);
	    		}
	    	}
	    	nounPhrase.add(np.toString().replace(")"," ").trim());
	    }
	   
	    //finding head words
    	String value = nounPhrase.get(0);
    	if(!value.isEmpty()) {
		    System.out.println("head word = " + nounPhrase.get(0));
    	} else {
    		System.out.println("head word = " + nounPhrase.get(1));
    	}

    	return nounPhrase;
	}
}
