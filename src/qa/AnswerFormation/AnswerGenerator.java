/**
 * 
 */
package qa.AnswerFormation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;

import qa.IQuestion;
import qa.Utility;

/**
 * @author Deepak
 *
 */
public class AnswerGenerator implements IAnswerGenerator {

	/**
	 * 
	 * @param processedQuestionsQueue
	 */
	public AnswerGenerator(Queue<IQuestion> processedQuestionsQueue, List<IQuestion> processedQuestions) {
		this.processedQuestionsQueue = processedQuestionsQueue;
		this.processedQuestions = processedQuestions;
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
				String posTaggedPassage = getPOSTagging(passage);
				HashSet<String> answers = new HashSet<String>();
				List<String> output = getDataFromNEROutput(nerTaggedPassage, question.getAnswerTypes());
				output.addAll(getDataFromNEROutput(posTaggedPassage, question.getAnswerTypes()));
				// not to be added to final code
				for(String answer : output) {
					if(!question.getQuestion().toLowerCase().contains(answer.toLowerCase()) && !answers.contains(answer)) {
						answers.add(answer);
						question.addAnswer(answer);
					}
				}
//				if(output.size() == 1) {
//					question.addAnswer(output.get(0));
//				} else {
//					// do something
//				}
			}
			this.processedQuestions.add(question);
		}
		AnswerWriter writer = new AnswerWriter("answer.txt");
		writer.writeAnswers(processedQuestions);
	}

	/**
	 * 
	 * @param sentence
	 * @return
	 */
	private String getNERTagging(String sentence) {
		return Utility.NERClassifier.classifyToString(sentence);
	}
	
	private String getPOSTagging(String sentence) {
		return Utility.Tagger.tagString(sentence);
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
		
		String[] nerOutputArray = nerOutput.split("[/_\\s]");
		String[] tags = tagName.split("\\|");
		
		for(String tag : tags) {
			String[] tagsArray = tag.split(":");
			for(int arrayIndex = 1; arrayIndex < nerOutputArray.length; arrayIndex+=2) {
				if(nerOutputArray[arrayIndex].trim().equals(tagsArray[1].trim())) {
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
	private List<IQuestion> processedQuestions;
}
