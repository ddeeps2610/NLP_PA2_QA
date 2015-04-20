/**
 * 
 */
package qa;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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
public class QASystem implements IQASystem 
{
	/*********************** State ************************/
	private Queue<IQuestion> questionsQueue;
	private Queue<IQuestion> processedQuestionsQueue;
	private List<IQuestion> processedQuestions;
	private IQuestionProcessor questionProccessor;
	private IPassageRetriever passageRetriever;
	private IAnswerGenerator answerGenerator;
	private String QUESTIONFILENAME= "qadata/dev/questions.txt";
	private String RELEVANTDOCSPATH= "topdocs/dev/";
	

	/*********************** Getters and Setters ************************/
	public QASystem() {
		this.questionsQueue = new LinkedList<IQuestion>();
		this.processedQuestionsQueue = new LinkedList<IQuestion>();
		this.questionProccessor = new QuestionProcessor(this.questionsQueue, QUESTIONFILENAME);
		this.passageRetriever = new PassageRetriever(this.questionsQueue,this.processedQuestionsQueue,RELEVANTDOCSPATH+"top_docs.");
		this.processedQuestions = new ArrayList<IQuestion>();
		this.answerGenerator = new AnswerGenerator(this.processedQuestionsQueue, this.processedQuestions);
	}
	
	/*********************** Business Logic ************************/
	@Override
	public void execute() 
	{
		this.questionProccessor.run();
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		this.passageRetriever.run();
//		try {
//			Thread.sleep(70000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
		this.answerGenerator.run();
		
	}
}
