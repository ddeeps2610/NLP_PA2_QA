/**
 * 
 */
package qa.PassageRetrieval;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import qa.IQuestion;
import qa.Utility;


/**
 * @author Deepak
 *
 */
public class PassageRetriever implements IPassageRetriever {

	private String filename;
	private int topDocumentsCount = 10;
	private Queue<IQuestion> questionsQueue, processedQuestionsQueue;
	
	public PassageRetriever(Queue<IQuestion> questionsQueue,
			Queue<IQuestion> processedQuestionsQueue,
			String filename) {
		
		this.questionsQueue = questionsQueue;
		this.processedQuestionsQueue = processedQuestionsQueue;
		this.filename = filename;
	}

	@Override
	public void run() {
		
		while(!this.questionsQueue.isEmpty() || !Utility.IsQuestionProcessingDone) {
		
			IQuestion question = this.questionsQueue.poll();
			if(question == null) continue;
			
			List<String> topDocs = new LinkedList<String>();
			topDocs = PassageReader.splitDocuments(filename + question.getqID());
			
				
			if (!topDocs.isEmpty()) {
				List<String> candidateDocuments = new ArrayList<String>();
				candidateDocuments = Lucene.luceneComputation(topDocs, this.topDocumentsCount, question.getKeywords());
				
				if (!candidateDocuments.isEmpty()) {
					List<String> relevantPassages = getRelevantPassages(candidateDocuments, question.getKeywords());
//					System.out.println("**********RELEVANT PASSAGES**********");
//					if (!relevantPassages.isEmpty()) {
//						for (int k = 0; k < relevantPassages.size(); k++) {
//							System.out.println(k+1 + ":\t"+relevantPassages.get(k));
//						}					
//					}
					
					List<String> relevantSentences = Utility.documentPreprocessorTokenizer(relevantPassages);
					
//					System.out.println("**********RELEVANT SENTENCES**********");
					if (!relevantSentences.isEmpty()) {
//						for (int k = 0; k < relevantSentences.size(); k++) {
//							System.out.println(k+1 + ":\t"+relevantSentences.get(k));
//						}
//										
						List<String> rankedSentences = new ArrayList<String>();
						rankedSentences = Lucene.luceneComputation(relevantSentences, this.topDocumentsCount, question.getKeywords());
//						System.out.println("**********RANKED SENTENCES**********");
						
						question.addAllRelevantPassages(rankedSentences);	
						this.processedQuestionsQueue.add(question);
						
//						if (!rankedSentences.isEmpty()) {
//							for (int k = 0; k < rankedSentences.size(); k++) {
//								System.out.println(k+1 + ":\t"+rankedSentences.get(k));
//							}		
//						}
					}
				}
				else {
					System.out.println("Candidate Passages List is empty.");	
				}
			} else {
				System.out.println("Relevant Documents List is empty");
			}
		}
		Utility.IsPassageRetrivalDone = true;
	}
	
	/***
	 * 
	 * @param node
	 * @param passagesList
	 * @return
	 */
	private List<String> parseXMLDocument(Node node, List<String> passagesList ) {
		
	    NodeList nodeList = node.getChildNodes();
	    for (int i = 0; i < nodeList.getLength(); i++) {
	    	
	        Node currentNode = nodeList.item(i);
	        if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
	        	parseXMLDocument(currentNode, passagesList);		        	
	        } else {	        	
	        	String temp = currentNode.getTextContent().trim();
	        	if (!temp.isEmpty()) {
	        		if (temp.length() > 1000) {
		        		String[] tempSplit = temp.split("\\s\\s|;");
		        		List<String> splitList = Arrays.asList(tempSplit);
		        		passagesList.addAll(splitList);
	        		} else {
	        			passagesList.add(temp);
	        		}
	        	}
	        }
	    }
	    return passagesList;
	}
	
	/**
	 * 
	 * @param eachDocument
	 * @return
	 */
	private List<String> getXMLOutputforAllDocs(String eachDocument) {		
	
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		
		List<String> passagesList = new ArrayList<String>();
		try {					
			dBuilder = dbFactory.newDocumentBuilder();
			InputSource input = new InputSource(new StringReader(eachDocument));
			Document doc = dBuilder.parse(input);
			doc.getDocumentElement().normalize();
			passagesList = parseXMLDocument(doc.getDocumentElement(), passagesList);
		
		} catch (ParserConfigurationException e) {
			System.err.println(e.getMessage());
		} catch (SAXException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		return passagesList;
	}
	
	/**
	 * 
	 * @param top10Documents
	 * @param keywords
	 * @return
	 */
	private List<String> getRelevantPassages (List<String> top10Documents, String keywords) {
		List <String> passagesList = new ArrayList<String>();
		List <String> relevantPassages = new ArrayList<>();
		List <String> tempPassagesList = new ArrayList<String>();
		if (!top10Documents.isEmpty()) {
			for (int i = 0; i < top10Documents.size(); i++) {		
				tempPassagesList.addAll(getXMLOutputforAllDocs(top10Documents.get(i).trim()));
			}
			
			for(int i = 0; i < tempPassagesList.size(); ++i) {
				boolean flag = true;
				for(int j = i + 1; j < tempPassagesList.size(); ++j) {
					if(tempPassagesList.get(i).toLowerCase().trim().equals(tempPassagesList.get(j).toLowerCase().trim())) {
						flag = false;
						break;
					}
				}
				if(flag) {
					passagesList.add(tempPassagesList.get(i));
				}
			}
			
			if (!passagesList.isEmpty()) {
				List<String> candidatePassages = new ArrayList<String>();
				candidatePassages = Lucene.luceneComputation(passagesList, this.topDocumentsCount, keywords);
				relevantPassages.addAll(candidatePassages);
				if (candidatePassages.isEmpty()) {
					System.out.println("CandidatePassagesList for Document "+" is empty.");	
				}
			} else {
				System.out.println("PassagesList for Document "+" is empty.");
			}
		} else {
			System.out.println("Top10DocumentsList is empty");
		}
		
		return relevantPassages;
	}
}
