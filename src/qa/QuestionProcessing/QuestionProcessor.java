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
	private HashMap<String, String> trainedMap;
	
	public QuestionProcessor(Queue<IQuestion> questionsQueue, String trainingQuestionFile, String testQuestionFile) {
		this.questionsQueue = questionsQueue;
		this.trainQuestionProcessor(trainingQuestionFile);
		
		this.questionReader = new QuestionReader(testQuestionFile);
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
		this.qaTypesMap.put(QuestionType.WHERE, 	"NER:LOCATION|NER:ORGANIZATION");		
		this.qaTypesMap.put(QuestionType.WHO, 		"NER:PERSON|NER:ORGANIZATION|POS:NP");
		this.qaTypesMap.put(QuestionType.WHOM, 		"NER:PERSON|NER:ORGANIZATION");
		
		// Multiple possible Answer Types.
		this.qaTypesMap.put(QuestionType.WHEN, 		"NER:DATE|NER:TIME");		
		
		// Need to handle for sub categories
		this.qaTypesMap.put(QuestionType.WHAT, 		"NER:LOCATION|POS:NP|POS:NNP|POS:NN|POS:NNS|POS:VB|NER:DEFINITION|NER:NUMBER");
		this.qaTypesMap.put(QuestionType.WHICH, 	"NER:LOCATION|POS:NP|POS:NNP|POS:NN|POS:NNS|POS:VB|NER:DEFINITION|NER:NUMBER");
		this.qaTypesMap.put(QuestionType.NAME, 		"NER:LOCATION|POS:NP|POS:NNP|POS:NN|POS:NNS|POS:VB|NER:DEFINITION|NER:NUMBER");
		this.qaTypesMap.put(QuestionType.HOW_MANY, 	"NER:NUMBER");
		this.qaTypesMap.put(QuestionType.HOW, 		"POS:NNP|POS:NNPS|NER:DEFINITION");

		// Non Standard Answer Types
		this.qaTypesMap.put(QuestionType.WHY, 		"NER:REASON");
		this.qaTypesMap.put(QuestionType.DESCRIBE,	"NER:DESCRIPTION");
		this.qaTypesMap.put(QuestionType.DEFINE, 	"NER:DEFINITION");
	}
	
	public void trainQuestionProcessor(String fileName)
	{
		System.out.println("Training the Question Processor based on the developer Questions Set.");
		this.trainedMap = QuestionReader.trainQuestionTypes(fileName);
		int i = 0;
		for(Entry<String,String> entry : trainedMap.entrySet())
		{
			System.out.println(i++ + "\t" + entry.getKey() +  "\t" + entry.getValue());
		}
	}

	@Override
	public void run() {
		for(IQuestion question : this.inputQuestions)
		{
			// Check if the question belongs to any question structure.
			String questionStructure = Utility.getQuestionStructure(question.getQuestion());
			if((this.trainedMap != null) && (this.trainedMap.containsKey(questionStructure)))
			{
				if((this.trainedMap.get(questionStructure) != null) && (!this.trainedMap.get(questionStructure).isEmpty()))
				{
					for(Entry<QuestionType, String> entry:this.qaTypesMap.entrySet())
					{
						// Identify the question and answer types
						if(question.getQuestion().toUpperCase().contains(entry.getKey().toString()))
						{
							question.setQuestionType(entry.getKey());
							break;
						}
					}
					if(question.getQuestion() == null)
						question.setQuestionType(QuestionType.OTHERS);
					question.setAnswerTypes(this.trainedMap.get(questionStructure));
				}
			}
			
			
			// Else, check for standard question types
			else {
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
			}
			// Extract keywords
			question.setKeywords(Utility.extractKeywords(question.getQuestion()));
			this.questionsQueue.add(question);
			System.out.println(question.getqID()+":"+question.getQuestion()+":" + question.getQuestionType()+":"+question.getAnswerTypes()+":"+question.getKeywords());
		}
		
		Utility.IsQuestionProcessingDone = true;
	}
}
