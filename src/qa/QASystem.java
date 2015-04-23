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
	private String testQuestionFilePath = "qadata/test/questions.txt";
	private String trainingQuestionFilePath = "qadata/dev/questions.txt";
	private String relevantDocsPath = "topdocs/test/";
	

	/*********************** Getters and Setters ************************/
	public QASystem() {
		this.questionsQueue = new LinkedList<IQuestion>();
		this.processedQuestionsQueue = new LinkedList<IQuestion>();
		this.questionProccessor = new QuestionProcessor(this.questionsQueue, this.trainingQuestionFilePath, this.testQuestionFilePath);
		this.passageRetriever = new PassageRetriever(this.questionsQueue, this.processedQuestionsQueue, this.relevantDocsPath+"top_docs.");
		this.processedQuestions = new ArrayList<IQuestion>();
		this.answerGenerator = new AnswerGenerator(this.processedQuestionsQueue, this.processedQuestions);
	}
	
	/*********************** Business Logic ************************/
	@Override
	public void execute() 
	{
//		Utility.initialize();
//		this.questionProccessor.run();
//		this.passageRetriever.run();
//		this.answerGenerator.run();
		try {
			Utility.initialize();
			(new Thread(this.questionProccessor)).start();
			Thread.sleep(5000);
			(new Thread(this.passageRetriever)).start();
			Thread.sleep(5000);
			(new Thread(this.answerGenerator)).start();
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
	}
}
