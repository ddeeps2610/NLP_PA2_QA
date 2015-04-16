/**
 * 
 */
package qa;

import java.util.LinkedList;

/**
 * @author Deepak
 * This is a Question Object that contains the question, 
 * answer patterns, relevant passage and Answers. 
 */
public class Question implements IQuestion
{
	/*********************** State ************************/
	private String question;
	private LinkedList<String> answerPatterns;
	private LinkedList<String> relevantPassages;
	private LinkedList<String> answers;
	
	/*********************** Getters and Setters ************************/
	public String getQuestion() {
		return question;
	}
	
	public LinkedList<String> getAnswerPatterns() {
		return answerPatterns;
	}

	public LinkedList<String> getRelevantPassages() {
		return relevantPassages;
	}

	public LinkedList<String> getAnswers() {
		return answers;
	}

	public void setQuestion(String question) {
		this.question = question;
	}
	
	public void addAnswerPattern(String answerPattern)
	{
		if(this.answerPatterns == null)
			this.answerPatterns = new LinkedList<String>();
		this.answerPatterns.add(answerPattern);
	}
	
	public void addAnswer(String answer)
	{
		if(this.answers== null)
			this.answers= new LinkedList<String>();
		this.answers.add(answer);
	}
	
	public void addrelevantPassage(String relevantPassage)
	{
		if(this.relevantPassages == null)
			this.relevantPassages = new LinkedList<String>();
		this.relevantPassages.add(relevantPassage);
	}
  
}
