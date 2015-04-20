/**
 * 
 */
package qa.QuestionProcessing;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Queue;

import qa.AnswerType;
import qa.IQuestion;
import qa.InformationExtraction;
import qa.QuestionType;


/**
 * @author Deepak Awari
 *
 */
public class QuestionProcessor implements IQuestionProcessor {
	private Queue<IQuestion> inputQuestions;
	private Queue<IQuestion> questionsQueue;
	private String questionsFileName;
	private IQuestionReader questionReader;
	private HashMap<QuestionType,String> qaTypesMap;
	
	public QuestionProcessor(Queue<IQuestion> questionsQueue, String questionsFileName) {
		this.questionsQueue = questionsQueue;
		this.questionsFileName = questionsFileName;
		this.questionReader = new QuestionReader(questionsFileName);
		this.inputQuestions = this.questionReader.readQuestions();
		//this.questionReader.classifyQuestions();
		this.qaTypesMap = new HashMap<QuestionType, String>();
		this.updateQATypes();
	}
	
	private void updateQATypes()
	{
		// TODO
		//AnswerTypes: Time, Location, Organization, Person, Money, Percent, Date, REASON,DESCRIPTION,DEFINITION
		//QuestionTypes: WHEN,WHERE,WHY,DESCRIBE,DEFINE,WHO,WHOM,WHAT,WHICH,NAME,HOW,OTHERS
		
		// Definite QA Match
		this.qaTypesMap.put(QuestionType.WHERE, 	"LOCATION");		
		this.qaTypesMap.put(QuestionType.WHO, 		"PERSON");
		this.qaTypesMap.put(QuestionType.WHOM, 		"PERSON");
		
		// Multiple possible Answer Types.
		this.qaTypesMap.put(QuestionType.WHEN, 		"DATE|TIME");		
		
		// Need to handle for sub categories
		this.qaTypesMap.put(QuestionType.WHAT, 		"LOCATION|[NP]|[NNP]|POS:NN|VB|NER:DEFINITION|NUMBER");
		this.qaTypesMap.put(QuestionType.WHICH, 	"LOCATION|NP|NNP|NN|VB|DEFINITION|NUMBER");
		this.qaTypesMap.put(QuestionType.NAME, 		"LOCATION|NP|NNP|NN|VB|DEFINITION|NUMBER");
		this.qaTypesMap.put(QuestionType.HOW_MANY, 	"NUMBER");
		this.qaTypesMap.put(QuestionType.HOW, 		"NNP|DEFINITION");

		// Non Standard Answer Types
		this.qaTypesMap.put(QuestionType.WHY, 		"REASON");
		this.qaTypesMap.put(QuestionType.DESCRIBE,	"DESCRIPTION");
		this.qaTypesMap.put(QuestionType.DEFINE, 	"DEFINITION");
	}

	@Override
	public void run() {
		for(IQuestion question : this.inputQuestions)
		{
			this.questionsQueue.add(question);
			for(Entry<QuestionType, String> entry:this.qaTypesMap.entrySet())
			{
				// Identify the question and answer types
				if(question.getQuestion().toUpperCase().contains(entry.getKey().toString()))
				{
					question.setQuestionType(entry.getKey());
					//question.addAnswerType(entry.getValue());
					question.setAnswerTypes(entry.getValue());
					break;
				}
				
				// Default Case if nothing works
				question.setQuestionType(QuestionType.OTHERS);
				//question.addAnswerType(AnswerType.NONE);
				question.setAnswerTypes(AnswerType.NONE.toString());
				
			}
			
			// Extract keywords
			question.setKeywords(InformationExtraction.extractKeywords(question.getQuestion()));
			this.questionsQueue.add(question);
			
			System.out.println(question.getqID()+":"+question.getQuestion()+":" + question.getQuestionType()+":"+question.getAnswerTypes()+":"+question.getKeywords());
		}
		
	}

}
