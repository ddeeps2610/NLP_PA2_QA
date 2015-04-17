/**
 * 
 */
package qa.QuestionProcessing;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Queue;

import qa.AnswerType;
import qa.IQuestion;
import qa.QuestionType;


/**
 * @author Deepak
 *
 */
public class QuestionProcessor implements IQuestionProcessor {
	private Queue<IQuestion> inputQuestions;
	private Queue<IQuestion> questionsQueue;
	private String questionsFileName;
	private IQuestionReader questionReader;
	private HashMap<QuestionType,AnswerType> qaTypesMap;
	
	public QuestionProcessor(Queue<IQuestion> questionsQueue, String questionsFileName) {
		this.questionsQueue = questionsQueue;
		this.questionsFileName = questionsFileName;
		this.questionReader = new QuestionReader(questionsFileName);
		this.inputQuestions = this.questionReader.readQuestions();
		this.qaTypesMap = new HashMap<QuestionType, AnswerType>();
		this.updateQATypes();
	}
	
	private void updateQATypes()
	{
		// TODO
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		for(IQuestion question : this.inputQuestions)
		{
			for(Entry<QuestionType, AnswerType> entry:this.qaTypesMap.entrySet())
			{
				if(question.getQuestion().toUpperCase().contains(entry.getKey().toString()))
				{
					question.setQuestionType(entry.getKey());
					question.setAnswerType(entry.getValue());
				}
			}
		}
		
	}

}
