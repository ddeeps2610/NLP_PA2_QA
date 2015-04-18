package qa.PassageRetrieval;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;


public class PostPassageRetrieval {
	
	public static List<String> keyWords = new ArrayList<String>();
	public static String keywordsString = "";
	
	public static String keywordExtractor(String question){

		question=question.replace('?', ' ');

		question=question.replace('(', ' ');

		question=question.replace(')', ' ');

		question=question.replace('{', ' ');

		question=question.replace('}', ' ');

		question=question.replace('[', ' ');

		question=question.replace(']', ' ');

		question=question.replace('"', ' ');

		question=question.replace('\'', ' ');


		String [] quesTokens= question.split("\\s+");



//			for (int i=0;i<quesTokens.length;i++){

//			keyWords.add(quesTokens[i]);

//			}

		MaxentTagger tagger =  new MaxentTagger("External Lib/taggers/english-left3words-distsim.tagger");

		String taggedQues=tagger.tagString(question);

		System.out.println("POS tagged sentence:  "+ taggedQues);

		//System.out.println("keywords"+ keyWords.get(1));


		String [] quesTags= taggedQues.split("_|\\s");


		for (int i=1;i<quesTags.length;i++){

		if(quesTags[i].equals("NN")||quesTags[i].equals("NNP")||quesTags[i].equals("JJ")){

		keyWords.add(quesTags[i-1]);
		keywordsString = keywordsString + quesTags[i-1] + " ";

		}

		}

		/*for(int i=0;i<keyWords.size();i++){

		System.out.println("keywords"+keyWords.size()+ keyWords.get(i));

		}*/

		return keywordsString;
		}


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

