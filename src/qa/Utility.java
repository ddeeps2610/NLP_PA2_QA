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
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;

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
	private static StanfordCoreNLP pipeline;
	
	/**
	 * 
	 */
	private static Tree tree = null ;
	
	/**
	 * 
	 */
	private static Properties props = new Properties();

	/**
	 * 
	 */
	private static String keywordsPattern = "[?(){}\\[\\]\\/]";
	
	/**
	 * 
	 */
	private static String tokenizePattern = "[(){}\\[\\]]";
	
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
		props.put("annotators", "tokenize, ssplit, pos, lemma, parse");
		pipeline = new StanfordCoreNLP(props);
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
			keywordsString += question.substring(startIndex, lastIndex + 1) + " ";
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
	public static List<String> documentPreprocessorTokenizer(List<String> passages) {
		List<String> retVal = new ArrayList<String>();
		
		for(String passage : passages) {
			Reader reader = new StringReader(passage);
		passage = passage.replaceAll(tokenizePattern, "");
			DocumentPreprocessor dp = new DocumentPreprocessor(reader);	
			Iterator<List<HasWord>> it = dp.iterator();
		
			while (it.hasNext()) {
			   StringBuilder sentenceSb = new StringBuilder();
			   List<HasWord> sentence = it.next();
			   
			   for (HasWord token : sentence) {
			      if(sentenceSb.length() > 1) {
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
	 * @param passages
	 * @return
	 */
	public static List<String> ptbTokenizer(List<String> passages) {
		List<String> retVal = new ArrayList<String>();
		
		for(String passage : passages) {
			StringBuilder sentence = new StringBuilder();
			try {
				PTBTokenizer<CoreLabel> ptbTokenizer = new PTBTokenizer<CoreLabel>(new StringReader(passage), new CoreLabelTokenFactory(), "tokenizeNLs=true,ptb3Escaping=false,normalizeParentheses=false,normalizeOtherBrackets=false");
				for(CoreLabel label; ptbTokenizer.hasNext();) {
					label = ptbTokenizer.next();
					sentence.append(label.toString() + " ");
				}
			} catch (Exception ex) {
				System.err.println(ex.getMessage());
			}
			retVal.add(sentence.toString().trim());
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
	    
	    List<Tree> phraseList = new ArrayList<Tree>();
	    List<String> nounPhrase = new ArrayList<String>();
		Annotation document = new Annotation(text);
	    pipeline.annotate(document);
	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);
	    
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
	    		if(!flag) {
	    			str = str.toLowerCase().replace("who\\s", "").replace("which\\s", "").replace("how\\s", "").replace("what\\s", "").replace("when\\s", "").replace("where\\s", "").replace("how\\s", "");	    		
	    		}
	    		
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


	/**
	 * 
	 * @param paragraph
	 * @return
	 */
	public static String lemmatize(String paragraph)
    {
        List<String> lemmas = new ArrayList<String>();
        StringBuilder lemmatizedPassage=new StringBuilder();
        Annotation document = new Annotation(paragraph);
        pipeline.annotate(document);
        
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        for(CoreMap sentence: sentences) {
            for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
                lemmas.add(token.get(LemmaAnnotation.class));
                lemmatizedPassage.append(token.getString(LemmaAnnotation.class)+" ");
            }
        }
        System.out.println("lemmatized passage"+ lemmatizedPassage.toString());
        return lemmatizedPassage.toString();
    }
	
	/**
	 * 
	 * @param question
	 * @return
	 */
	public static String getQuestionStructure(String question) {
		StringBuilder questionStructs = new StringBuilder();
		
		int i = 0;
		for(String word: Utility.getPOSTagging(question).split(" ")) {
			//How_WRB many_JJ
			//How_WRB much_RB
			if(word.contains("WP") ||word.contains("WRB") || word.contains("WDT"))// ||word.contains("NN") && !word.contains("NNS") && !word.contains("NNP"))
				//questionStructure.add(word.split("_")[0]);//.split("|")[0]);
				questionStructs.append(word.split("_")[0]+" ");
			if(word.contains("NN") && !word.contains("NNS") && !word.contains("NNP")) {
				i++;
				questionStructs.append(word.split("_")[0]+" ");
			}
			if(i==1)
				break;
		}
		
		return questionStructs.toString().trim();
	}

	/**
	 * 
	 * @param sentence
	 * @param keywords
	 * @return
	 */
	public static String removeKeywords(String sentence, String keywords) {
		StringBuilder retVal = new StringBuilder();	
		String[] sentenceArray = sentence.split(" ");
		
		for(String string : sentenceArray) {
			if(string != null && !string.equals("")){
				if(keywords.contains(string + " ")) {
					continue;
				}
				retVal.append(string + " ");
			}
		}
		
		return retVal.toString();
	}
	
	/**
	 * 
	 */
	public static void initialize() {
		
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		System.out.println("**************************");
		String question1 = "Which is the tallest building?";
		System.out.println(question1);
		String posTaggedQuestion = Utility.getPOSTagging(question1);
		String nerTaggedQuestion = Utility.getNERTagging(question1);
		List<String> headWords = Utility.getNounPhrases(question1,true);
		String keywords = Utility.extractKeywords(question1);
		String quesStru = Utility.getQuestionStructure(question1);
		
		
		System.out.println("POS TAGS:   " + posTaggedQuestion);
		System.out.println("NER TAGS:   " + nerTaggedQuestion);
		System.out.println("Head words: " + headWords);
		System.out.println("Keywords:   " + keywords);
		System.out.println("Que Stru:   " + quesStru);
		
		for(String keyword : keywords.split(" ")) {
			question1 = question1.replace(keyword, "");
		}
		
		question1 = question1.replaceAll("the", "");
		question1 = question1.replace("is", "");
		question1 = question1.replace("of", "");
		System.out.println(question1);
	}	
}
