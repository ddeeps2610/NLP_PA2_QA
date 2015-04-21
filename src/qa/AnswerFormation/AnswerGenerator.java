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
			for(String passage : question.getRelevantPassages()) 
			{
				String nerTaggedPassage = Utility.getNERTagging(passage);
				String posTaggedPassage = Utility.getPOSTagging(passage);
				List<String> output = getDataFromNEROutput(nerTaggedPassage, question.getAnswerTypes());
				output.addAll(getDataFromNEROutput(posTaggedPassage, question.getAnswerTypes()));
				
				/************************* Jugaad to select most relevant answers********************/
				// Process the output to select few most relevant answers
				for(String answer : output) 
				{
					
					// Select the most relevant answer that is closer to the keywords in the passage - Can it be done? This need not be done.		
					
					// Ignore the outputs which are part of question, so, it is not an answer
					if(question.getQuestion().toLowerCase().contains(answer.toLowerCase())) 
						continue;
					
					// Ignore duplicates
					if(answers.contains(answer))
						continue;
					
					// Select answers that are superset of another answer - Ravi and Ravi Dugar - Select Ravi Dugar
					for(String ans : answers)
					{
						// Remove the already added subsets and add the superset
						if(answer.contains(ans))
						{
							answers.remove(ans);
							answers.add(answer);
						}
					}
					
					// Add some answers before you start processing so that we have at least some answers.
					if(answers.size() < 10)
					{
						answers.add(answer);
						continue;
					}
					answers.add(answer);
				}
				// Add all answsers
				question.addMultipleAnswers(answers);
				
				/***************************************** Jugaad ends ******************************/
//				if(output.size() == 1) {
//					question.addAnswer(output.get(0));
//				} else {
//					// do something
//				}
			}
			// Process all the answers once again amongst all answers 
			
			// Final Collection of QAs
			this.processedQuestions.add(question);
		}
		AnswerWriter writer = new AnswerWriter("answer.txt");
		writer.writeAnswers(processedQuestions);
	}
	
	/**
	 * 
	 * @param nerOutput
	 * @param tagName
	 * @return
	 */
	private List<String> getDataFromNEROutput(String nerOutput, String tagName) {
		List<String> answers = new ArrayList<String>();
		StringBuilder temp = new StringBuilder();
		
		String[] nerOutputArray = nerOutput.split("[/_\\s]");
		String[] tags = tagName.split("\\|");
		
		for(String tag : tags) {
			String[] tagsArray = tag.split(":");
			for(int arrayIndex = 1; arrayIndex < nerOutputArray.length; arrayIndex+=2) {
				if(nerOutputArray[arrayIndex].trim().equals(tagsArray[1].trim())) {
					temp.append(nerOutputArray[arrayIndex - 1] + " ");				
				} 
				else 
				{
					if(!temp.toString().equals("")) 
					{
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
