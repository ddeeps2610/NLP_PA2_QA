/**
 * 
 */
package qa;

import java.util.ArrayList;
import java.util.List;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

/**
 * @author Deepak
 *
 */
public class InformationExtraction {
	
	
	private static String p= "[?(){}\\[\\]\\/]" ; 
	
	
	public static String extractKeywords(String question)
	{
		 String keywordsString = "";
		 question=question.replaceAll(p, " ");
			
		 MaxentTagger tagger =  new MaxentTagger("External Lib/taggers/english-left3words-distsim.tagger");
		 String taggedQues=tagger.tagString(question);
		 String [] quesTags= taggedQues.split("_|\\s");
		
		String start="\"";
		int startIndex=question.indexOf(start);
		int lastIndex=question.indexOf(start);
		//keyWords.add(question.substring(startIndex, lastIndex));
		
			
		for (int i=1;i<quesTags.length;i++){
			if(quesTags[i].equals("NN")||quesTags[i].equals("NNP")||quesTags[i].equals("JJ")||quesTags[i].equals("VB")){

			keywordsString = keywordsString + quesTags[i-1] + question.substring(startIndex, lastIndex)+" ";
			
			}
		
	}
		return keywordsString;
		}

}
