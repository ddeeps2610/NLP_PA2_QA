/**
 * 
 */
package qa.AnswerFormation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import qa.IQuestion;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;

/**
 * @author Deepak
 *
 */
public class AnswerGenerator implements IAnswerGenerator {

	/**
	 * 
	 */
	private static String serializedClassifier;// = "classifiers/english.all.7class.distsim.crf.ser.gz";
	
	/**
	 * 
	 */
	private static AbstractSequenceClassifier<CoreLabel> classifier;// = CRFClassifier.getClassifier(serializedClassifier);

	/**
	 * 
	 */
	static {
		serializedClassifier = "classifiers/english.all.7class.distsim.crf.ser.gz";
		try {
			classifier = CRFClassifier.getClassifier(serializedClassifier);
		} catch (ClassCastException | ClassNotFoundException | IOException ex) {
			System.err.println(ex.getMessage());
		}			  	
	}
	
	/**
	 * 
	 * @param processedQuestionsQueue
	 */
	public AnswerGenerator(Queue<IQuestion> processedQuestionsQueue) {
		this.processedQuestionsQueue = processedQuestionsQueue;
	}

	/**
	 * 
	 */
	@Override
	public void run() {
		while(!processedQuestionsQueue.isEmpty()) {
			IQuestion question = processedQuestionsQueue.poll();
			for(String passage : question.getRelevantPassages()) {
				String nerTaggedPassage = getNERTagging(passage);
				List<String> output = getDataFromNEROutput(nerTaggedPassage, question.getAnswerTypes());
				if(output.size() == 1) {
					question.addAnswer(output.get(0));
				} else {
					// do something
				}
			}
		}
	}

	/**
	 * 
	 * @param sentence
	 * @return
	 */
	private String getNERTagging(String sentence) {
		return classifier.classifyToString(sentence);
	}
	
	/**
	 * 
	 * @param nerOutput
	 * @param tagName
	 * @return
	 */
	private List<String> getDataFromNEROutput(String nerOutput, String tagName) {
		List<String> retVal = new ArrayList<String>();
		StringBuilder temp = new StringBuilder();
		
		String[] nerOutputArray = nerOutput.split("[/\\s]");
		String[] tags = tagName.split("|");
		
		for(String tag : tags) {		
			for(int arrayIndex = 1; arrayIndex < nerOutputArray.length; arrayIndex+=2) {
				if(nerOutputArray[arrayIndex].trim().equals(tag.trim())) {
					temp.append(nerOutputArray[arrayIndex - 1] + " ");				
				} else {
					if(!temp.toString().equals("")) {
						retVal.add(temp.toString().trim());
					}
					temp = new StringBuilder();
				}
			}
			
			if(!temp.toString().equals("") ) {
				retVal.add(temp.toString().trim());
			}
		}
		return retVal;
	}
	
	/**
	 * 
	 */
	private Queue<IQuestion> processedQuestionsQueue;
}
