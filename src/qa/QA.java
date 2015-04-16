/**
 * 
 */
package qa;

import java.util.LinkedList;
import java.util.Queue;

import qa.AnswerFormation.AnswerGenerator;
import qa.AnswerFormation.IAnswerGenerator;
import qa.PassageRetrieval.IPassageRetriever;
import qa.PassageRetrieval.PassageRetriever;
import qa.QuestionProcessing.IQuestionProcessor;
import qa.QuestionProcessing.QuestionProcessor;

/**
 * @author Deepak Awari
 */
public class QA implements IQA 
{
	/*********************** State ************************/
	private Queue<IQuestion> questionsQueue;
	private Queue<IQuestion> processedQuestionsQueue;
	private IQuestionProcessor questionProccessor;
	private IPassageRetriever passageRetriever;
	private IAnswerGenerator answerGenerator;
	private static String QUESTIONFILENAME= "qadata/test/questions.txt";
	

	/*********************** Getters and Setters ************************/
	public QA() {
		this.questionsQueue = new LinkedList<IQuestion>();
		this.processedQuestionsQueue = new LinkedList<IQuestion>();
		this.questionProccessor = new QuestionProcessor(this.questionsQueue,this.QUESTIONFILENAME);
		this.passageRetriever = new PassageRetriever(this.questionsQueue,this.processedQuestionsQueue);
		this.answerGenerator = new AnswerGenerator(this.processedQuestionsQueue);
	}
	
	/*********************** Business Logic ************************/
	@Override
	public void execute() 
	{
		this.questionProccessor.run();
		this.passageRetriever.run();
		this.answerGenerator.run();
		
	}
}
