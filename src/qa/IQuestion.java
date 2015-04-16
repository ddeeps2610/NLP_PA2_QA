/**
 * 
 */
package qa;

import java.util.LinkedList;

/**
 * @author Deepak
 *
 */
public interface IQuestion 
{
	public String getQuestion();
	public LinkedList<String> getAnswerPatterns();
	public LinkedList<String> getRelevantPassages();
	public LinkedList<String> getAnswers();
	
	public void setQuestion(String question);
	public void addAnswerPattern(String answerPattern);
	public void addAnswer(String answer);
	public void addrelevantPassage(String relevantPassage);

}
