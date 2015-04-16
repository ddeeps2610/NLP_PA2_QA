/**
 * 
 */
package qa.QuestionProcessing;

import java.io.File;
import java.util.LinkedList;

import qa.IQuestion;

/**
 * @author Deepak
 *
 */
public class QuestionReader implements IQuestionReader{
	private String questionsFileName;
	private LinkedList<IQuestion> questions;
	
	public QuestionReader(String questionsFileName) {
		this.questionsFileName = questionsFileName;
	}

	@Override
	public void readQuestions() {
		File questionsFile = new File(this.questionsFileName);
		
		
	}

}
