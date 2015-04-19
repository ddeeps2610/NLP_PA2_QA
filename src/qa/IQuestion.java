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
	public Integer getqID();
	public QuestionType getQuestionType();
	//public LinkedList<AnswerType> getAnswerTypes();
	public String getAnswerTypes();
	public LinkedList<String> getKeywords();
	
	public void setQuestion(String question);
	public void addAnswerPattern(String answerPattern);
	public void addAnswer(String answer);
	public void addrelevantPassage(String relevantPassage);
	public void setqID(Integer qID);
	public void setQuestionType(QuestionType questionType);
	//public void addAnswerType(AnswerType answerType);
	public void setAnswerTypes(String answerTypes);
	public void addKeywords(String keyword);
	public void setKeywords(LinkedList<String> keywords);
	
}