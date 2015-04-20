/**
 * 
 */
package qa.QuestionProcessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

import qa.IQuestion;
import qa.Question;
import qa.QuestionType;

/**
 * @author Deepak Awari
 * Reads the file that contains questions and creates the question object 
 * with QID and Question stored in the object.
 *
 */
public class QuestionReader implements IQuestionReader{
	private String questionsFileName;
	private LinkedList<IQuestion> questions;
	
	public QuestionReader(String questionsFileName) {
		this.questionsFileName = questionsFileName;
		this.questions = new LinkedList<IQuestion>();
	}

	@Override
	public LinkedList<IQuestion> readQuestions() 
	{
		File questionsFile = new File(this.questionsFileName);
		if(questionsFile == null || !questionsFile.exists())
		{
			System.out.println("Questions file is not found...!!!");
			System.exit(0);
		}	
		else if(!questionsFile.isFile())
		{
			System.out.println(questionsFile.getAbsoluteFile());
			System.out.println("Input is not a file...!!!");
			System.exit(0);
		}
		
		try 
		{
			BufferedReader questionFileReader = new BufferedReader(new FileReader(this.questionsFileName));
			
			// Read each line from the file and process the same.
			String line;
			try {
				IQuestion newQuestion = null;
				while((line = questionFileReader.readLine())!= null)
				{
					// Skip the line if there is nothing in the line
					if(line.isEmpty())
						continue;
					
					// If the line contains the QID, search the qid and store the same.
					else if(line.contains("Number:"))
					{
						newQuestion = new Question();
						newQuestion.setqID(Integer.parseInt(line.split(" ")[1]));
						//System.out.print("QID:" +newQuestion.getqID());
					}
					else if(!line.isEmpty() && !line.contains("Number"))
					{
						newQuestion.setQuestion(line);
//						System.out.print("QID:" +newQuestion.getqID());
//						System.out.println(" :: "+newQuestion.getQuestion());
						this.questions.add(newQuestion);
						newQuestion = null;
					}
						
				}
			} 
			catch (IOException e) {
				System.out.println("IOException occurred...!!!");
				e.printStackTrace();
			}
		} 
		catch (FileNotFoundException e) 
		{
			System.out.println("File Not Found Exception occurred..!!!");
			e.printStackTrace();
		}
		return this.questions;
		
	}
	
	public void classifyQuestions()
	{
		QuestionType[] qTypes = {	QuestionType.WHAT,
									QuestionType.WHICH,
									QuestionType.NAME,
									QuestionType.HOW 	};
		//String[] qTypes = {"WHAT", "WHERE", "NAME", "HOW"};
		//for(String qType : qTypes)
		for(QuestionType qType : qTypes)
		{
			System.out.println(qType.toString());
			for(IQuestion question : this.questions)
			{
				if(question.getQuestion().toUpperCase().contains(qType.toString()))
					System.out.println(question.getqID() + ":" +question.getQuestion());
			}
			System.out.println("\n\n");
		}
	}

}
