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

/**
 * @author Deepak
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
		if(questionsFile == null)
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
				while((line = questionFileReader.readLine())!= null)
				{
					IQuestion newQuestion = new Question();
					
					// Skip the line if there is nothing in the line
					if(line.isEmpty())
						continue;
					
					// If the line contains the QID, search the qid and store the same.
					else if(line.contains("Number:"))
					{
						newQuestion.setqID(Integer.parseInt(line.split(" ")[1]));
						System.out.print("QID:" +newQuestion.getqID());
					}
					else if(!line.isEmpty() && !line.contains("Number"))
					{
						newQuestion.setQuestion(line);
						System.out.println(" :: "+newQuestion.getQuestion());
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

}
