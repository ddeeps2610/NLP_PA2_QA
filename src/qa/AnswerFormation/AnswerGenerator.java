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
		
		while(!processedQuestionsQueue.isEmpty() || !Utility.IsPassageRetrivalDone) {
			IQuestion question = processedQuestionsQueue.poll();
			if(question == null) continue;
			HashSet<String> answers = new HashSet<String>();
			for(String passage : question.getRelevantPassages()) {
				List<String> output = new ArrayList<String>(); 
				String passageWithoutKeywords = null;
				String nerTaggedPassage = Utility.getNERTagging(passage);
				String posTaggedPassage = Utility.getPOSTagging(passage);
				output.addAll(getDataFromOutput(nerTaggedPassage, question.getAnswerTypes()));
				
				output.addAll(getDataFromOutput(posTaggedPassage, question.getAnswerTypes()));
				
				for(String answer : output) {
					if(!question.getQuestion().toLowerCase().contains(answer.toLowerCase()) && !answers.contains(answer)) {
						answers.add(answer);
						passageWithoutKeywords = Utility.removeKeywords(answer, question.getKeywords());
						question.addAnswer(passageWithoutKeywords);
					}
				}
			}
			
			for(String passage : question.getRelevantPassages()) {
				List<String> output = new ArrayList<String>(); 
				String passageWithoutKeywords = null;
				if(answers.size() >= 10) break;
				try{
					output.addAll(Utility.getNounPhrases(passage, false));
				} catch (Exception ex) {
					System.out.println(ex.getMessage());
				}
				
				for(String answer : output) {
					if(!question.getQuestion().toLowerCase().contains(answer.toLowerCase()) && !answers.contains(answer)) {
						answers.add(answer);
						passageWithoutKeywords = Utility.removeKeywords(answer, question.getKeywords());
						question.addAnswer(passageWithoutKeywords);
					}
				}
			}
			
//			for(String answer : answers) {
//				boolean flag = true;
//				for(String answer1 : answers) {
//					if(!answer.equals(answer1)) {
//						if(answer1.toLowerCase().contains(answer.toLowerCase())) {
//							flag = false;
//							break;
//						}
//					}
//				}
//				if(flag) {
//					question.addAnswer(answer);
//				}
//			}
			
			this.processedQuestions.add(question);
		}
		AnswerWriter writer = new AnswerWriter("answer.txt");
		writer.writeAnswers(processedQuestions);
	}
	
	/**
	 * 
	 * @param output
	 * @param tagName
	 * @return
	 */
	private List<String> getDataFromOutput(String output, String tagName) {
		List<String> answers = new ArrayList<String>();
		StringBuilder temp = new StringBuilder();
		
		String[] outputArray = output.split("[/_\\s]");
		String[] tags = tagName.split("\\|");
		
		for(String tag : tags) {
			if(tag == null || tag.equals("")) continue;
			String[] tagsArray = tag.split(":");
			for(int arrayIndex = 1; arrayIndex < outputArray.length; arrayIndex+=2) {
				if(outputArray[arrayIndex].trim().equals(tagsArray[1].trim())) {
					temp.append(outputArray[arrayIndex - 1] + " ");				
				} else {
					if(!temp.toString().equals("")) {
						answers.add(temp.toString().trim());
					}
					temp = new StringBuilder();
				}
			}
			
			if(!temp.toString().equals("") ) {
				answers.add(temp.toString().trim());
			}
		}
		return answers;
	}
	
	/**
	 * 
	 */
	private Queue<IQuestion> processedQuestionsQueue;
	private List<IQuestion> processedQuestions;
}
