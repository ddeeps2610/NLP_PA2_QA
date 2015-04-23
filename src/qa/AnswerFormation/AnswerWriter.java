/**
 * 
 */
package qa.AnswerFormation;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import qa.IQuestion;

/**
 * @author Ravi
 *
 */
public class AnswerWriter {
	
	/**
	 * 
	 * @param answerFileName
	 */
	public AnswerWriter(String answerFileName) {
		this.answerFileName = answerFileName;
	}
	
	/**
	 * 
	 * @param questions
	 */
	public void writeAnswers(List<IQuestion> questions) {
		if(questions != null && questions.size() > 0) {
			try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(answerFileName), "utf-8"))) {
				for(IQuestion question : questions) {
					writer.write("qid " + question.getqID() + "\n");
					int answerCounter = 1;
					for(String answer : question.getAnswers()) {
						writer.write(answerCounter++ + " " + answer + "\n");
						if(answerCounter > 10) break;
					}
				}
			} catch (UnsupportedEncodingException ex) {
				System.err.println(ex.getMessage());
			} catch (FileNotFoundException ex) {
				System.err.println(ex.getMessage());
			} catch (IOException ex) {
				System.err.println(ex.getMessage());
			} catch (Exception ex) {
				System.err.println(ex.getMessage());
			}
		}
	}
	
	/**
	 * 
	 */
	private String answerFileName;
}
