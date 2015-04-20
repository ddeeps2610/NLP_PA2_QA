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
import qa.Utility;


/**
 * @author Deepak Awari
 *
 */
public class QuestionProcessor implements IQuestionProcessor {
	private Queue<IQuestion> inputQuestions;
	private Queue<IQuestion> questionsQueue;
	private IQuestionReader questionReader;
	private HashMap<QuestionType,String> qaTypesMap;
	
	public QuestionProcessor(Queue<IQuestion> questionsQueue, String questionsFileName) {
		this.questionsQueue = questionsQueue;
		this.questionReader = new QuestionReader(questionsFileName);
		this.inputQuestions = this.questionReader.readQuestions();
		//this.questionReader.classifyQuestions();
		this.qaTypesMap = new HashMap<QuestionType, String>();
		this.updateQATypes();
	}
	
	private void updateQATypes()
	{
		//AnswerTypes: Time, Location, Organization, Person, Money, Percent, Date, REASON,DESCRIPTION,DEFINITION
		//QuestionTypes: WHEN,WHERE,WHY,DESCRIBE,DEFINE,WHO,WHOM,WHAT,WHICH,NAME,HOW,OTHERS
		
		// Definite QA Match
		this.qaTypesMap.put(QuestionType.WHERE, 	"NER:LOCATION");		
		this.qaTypesMap.put(QuestionType.WHO, 		"NER:PERSON");
		this.qaTypesMap.put(QuestionType.WHOM, 		"NER:PERSON");
		
		// Multiple possible Answer Types.
		this.qaTypesMap.put(QuestionType.WHEN, 		"NER:DATE|NER:TIME");		
		
		// Need to handle for sub categories
		this.qaTypesMap.put(QuestionType.WHAT, 		"NER:LOCATION|POS:NP|POS:NNP|POS:NN|POS:VB|NER:DEFINITION|NER:NUMBER");
		this.qaTypesMap.put(QuestionType.WHICH, 	"NER:LOCATION|POS:NP|POS:NNP|POS:NN|POS:VB|NER:DEFINITION|NER:NUMBER");
		this.qaTypesMap.put(QuestionType.NAME, 		"NER:LOCATION|POS:NP|POS:NNP|POS:NN|POS:VB|NER:DEFINITION|NER:NUMBER");
		this.qaTypesMap.put(QuestionType.HOW_MANY, 	"NER:NUMBER");
		this.qaTypesMap.put(QuestionType.HOW, 		"POS:NNP|NER:DEFINITION");

		// Non Standard Answer Types
		this.qaTypesMap.put(QuestionType.WHY, 		"NER:REASON");
		this.qaTypesMap.put(QuestionType.DESCRIBE,	"NER:DESCRIPTION");
		this.qaTypesMap.put(QuestionType.DEFINE, 	"NER:DEFINITION");
	}

	@Override
	public void run() {
		for(IQuestion question : this.inputQuestions)
		{
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
			question.setKeywords(Utility.extractKeywords(question.getQuestion()));
			this.questionsQueue.add(question);
			System.out.println(question.getqID()+":"+question.getQuestion()+":" + question.getQuestionType()+":"+question.getAnswerTypes()+":"+question.getKeywords());
		}
		
		Utility.IsQuestionProcessingDone = true;
	}
}
