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
	private Integer qID;
	private String question;
	private LinkedList<String> answerPatterns;
	private LinkedList<String> relevantPassages;
	private LinkedList<String> answers;
	private QuestionType questionType;
	//private LinkedList<AnswerType> answerTypes;
	private String answerTypes;
	private String keywords;
	
	/*********************** Getters and Setters ************************/
	
	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}
	
	public QuestionType getQuestionType() {
		return questionType;
	}

	public void setQuestionType(QuestionType questionType) {
		this.questionType = questionType;
	}

	/*public LinkedList<AnswerType> getAnswerTypes() {
		return this.answerTypes;
	}

	public void addAnswerType(AnswerType answerType) {
		if(this.answerTypes == null)
			this.answerTypes = new LinkedList<AnswerType>();
		this.answerTypes.add(answerType);
	}
	
	*/
	
	
	public Integer getqID() {
		return qID;
	}

	

	public String getAnswerTypes() {
		return answerTypes;
	}

	public void setAnswerTypes(String answerTypes) {
		this.answerTypes = answerTypes;
	}

	public void setqID(Integer qID) {
		this.qID = qID;
	}

	public LinkedList<String> getAnswerPatterns() {
		return answerPatterns;
	}
	
	public void addAnswerPattern(String answerPattern)
	{
		if(this.answerPatterns == null)
			this.answerPatterns = new LinkedList<String>();
		this.answerPatterns.add(answerPattern);
	}

	public LinkedList<String> getRelevantPassages() {
		return relevantPassages;
	}
	
	public void addrelevantPassage(String relevantPassage)
	{
		if(this.relevantPassages == null)
			this.relevantPassages = new LinkedList<String>();
		this.relevantPassages.add(relevantPassage);
	}

	public LinkedList<String> getAnswers() {
		return answers;
	}
	
	public void addAnswer(String answer)
	{
		if(this.answers== null)
			this.answers= new LinkedList<String>();
		this.answers.add(answer);
	}
	
	public String getKeywords() {
		return keywords;
	}
	
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

  
}
