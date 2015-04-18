/**
 * 
 */
package qa.QuestionProcessing;

import java.util.LinkedList;

import qa.IQuestion;

/**
 * @author Deepak
 *
 */
public interface IQuestionReader {

	public LinkedList<IQuestion> readQuestions();
	public void classifyQuestions();

}
