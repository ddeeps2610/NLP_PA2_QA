/**
 * 
 */
package qa.QuestionProcessing;

import java.util.Queue;

import qa.IQuestion;


/**
 * @author Deepak
 *
 */
public class QuestionProcessor implements IQuestionProcessor {

	private Queue<IQuestion> questionsQueue;
	private String questionsFileName;
	private IQuestionReader questionReader;
	
	public QuestionProcessor(Queue<IQuestion> questionsQueue, String questionsFileName) {
		this.questionsQueue = questionsQueue;
		this.questionsFileName = questionsFileName;
		this.questionReader = new QuestionReader(questionsFileName);
		this.questionsQueue = this.questionReader.readQuestions();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
