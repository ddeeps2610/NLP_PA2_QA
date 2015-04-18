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
	private HashMap<QuestionType,AnswerType> qaTypesMap;
	
	public QuestionProcessor(Queue<IQuestion> questionsQueue, String questionsFileName) {
		this.questionsQueue = questionsQueue;
		this.questionsFileName = questionsFileName;
		this.questionReader = new QuestionReader(questionsFileName);
		this.inputQuestions = this.questionReader.readQuestions();
		this.questionReader.classifyQuestions();
		this.qaTypesMap = new HashMap<QuestionType, AnswerType>();
		this.updateQATypes();
	}
	
	private void updateQATypes()
	{
		// TODO
		//AnswerTypes: Time, Location, Organization, Person, Money, Percent, Date, REASON,DESCRIPTION,DEFINITION
		//QuestionTypes: WHEN,WHERE,WHY,DESCRIBE,DEFINE,WHO,WHOM,WHAT,WHICH,NAME,HOW,OTHERS
		
		// Definite QA Match
		this.qaTypesMap.put(QuestionType.WHERE, 	AnswerType.LOCATION);		
		this.qaTypesMap.put(QuestionType.WHO, 		AnswerType.PERSON);
		this.qaTypesMap.put(QuestionType.WHOM, 		AnswerType.PERSON);
		
		// Multiple possible Answer Types.
		this.qaTypesMap.put(QuestionType.WHEN, 		AnswerType.DATE);		
		
		// Need to handle for sub categories
		this.qaTypesMap.put(QuestionType.WHAT, 		AnswerType.NOUN);
		this.qaTypesMap.put(QuestionType.WHICH, 	AnswerType.PERSON);
		this.qaTypesMap.put(QuestionType.NAME, 		AnswerType.PERSON);
		this.qaTypesMap.put(QuestionType.HOW, 		AnswerType.PERSON);
		this.qaTypesMap.put(QuestionType.OTHERS, 	AnswerType.PERSON);
		
		// Non Standard Answer Types
		this.qaTypesMap.put(QuestionType.WHY, 		AnswerType.REASON);
		this.qaTypesMap.put(QuestionType.DESCRIBE,	AnswerType.DESCRIPTION);
		this.qaTypesMap.put(QuestionType.DEFINE, 	AnswerType.DEFINITION);
	}

	@Override
	public void run() {
		for(IQuestion question : this.inputQuestions)
		{
			for(Entry<QuestionType, AnswerType> entry:this.qaTypesMap.entrySet())
			{
				// Identify the question and answer types
				if(question.getQuestion().toUpperCase().contains(entry.getKey().toString()))
				{
					question.setQuestionType(entry.getKey());
					question.addAnswerType(entry.getValue());
					break;
				}
				
				// Default Case if nothing works
				question.setQuestionType(QuestionType.OTHERS);
				question.addAnswerType(AnswerType.NONE);
				
			}
			
			// Extract keywords
			question.setKeywords((LinkedList<String>) InformationExtraction.extractKeywords(question.getQuestion()));
		}
		
	}

}
