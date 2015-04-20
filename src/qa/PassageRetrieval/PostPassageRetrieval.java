package qa.PassageRetrieval;

import java.util.ArrayList;
import java.util.List;



public class PostPassageRetrieval {
	
	public static List<String> sentenceTokenizer(List<String> passage){

		System.out.println("In Sentence Tokenizer...");
		List<String> relevantSentences = new ArrayList<String>();
		for(String s: passage)
		{
			s=s.replace('(', ' ');

			s=s.replace(')', ' ');

			s=s.replace('{', ' ');

			s=s.replace('}', ' ');

			s=s.replace('[', ' ');

			s=s.replace(']', ' ');

			s=s.replace('"', ' ');

			s=s.replace('\'', ' ');
			
			String[] passagesSplit = s.split("\\.|\\?|\\!");
			
			for (int i = 0; i<passagesSplit.length;i++) {
				
				
				if (!passagesSplit[i].trim().isEmpty())
					relevantSentences.add(passagesSplit[i]);
		/*		int count = 0;
			for(String key: keyWords)
				{
					if (count == 2) {
						relevantSentences.add(passagesSplit[i]);
						count = 0;
						break;
					}
						
					if(passagesSplit[i].toLowerCase().contains(key)){						
						relevantSentences.add(passagesSplit[i]);	
						//count++;  
					}
				    	
				} */
			}

		}
		return relevantSentences;
		}
}

