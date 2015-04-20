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


/**
 * @author Deepak
 *
 */
public class PassageRetriever implements IPassageRetriever {

	private String filename;
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
		
		while(!this.questionsQueue.isEmpty()) {
		
			IQuestion question = this.questionsQueue.poll();
			
			List<String> TopDocs = new LinkedList<String>();
			TopDocs = PassageReader.splitDocuments(filename + question.getqID());
			
				
			if (!TopDocs.isEmpty()) {
				
//				}
				
				List<String> candidateDocuments = new ArrayList<String>();
				candidateDocuments = Lucene.LuceneComputation(TopDocs, 10, question.getKeywords());
				
//				System.out.println("**********RELEVANT DOCUMENTS**********");
				
				if (!candidateDocuments.isEmpty()) {
//					for (int k = 0; k < candidateDocuments.size(); k++) {
//						System.out.println(k+1 + ":\t"+candidateDocuments.get(k));
//					}
					
					List<String> relevantPassages = getRelevantPassages(candidateDocuments, question.getKeywords());
//					System.out.println("**********RELEVANT PASSAGES**********");
					question.addAllRelevantPassages(relevantPassages);
					this.processedQuestionsQueue.add(question);
					if (!relevantPassages.isEmpty()) {
						for (int k = 0; k < relevantPassages.size(); k++) {
							System.out.println(k+1 + ":\t"+relevantPassages.get(k));
						}					
					}
					
//					List<String> relevantSentences = PostPassageRetrieval.sentenceTokenizer(relevantPassages);
//					System.out.println("**********RELEVANT SENTENCES**********");
//					if (!relevantSentences.isEmpty()) {
//						for (int k = 0; k < relevantSentences.size(); k++) {
//							System.out.println(k+1 + ":\t"+relevantSentences.get(k));
//						}
//										
//						List<String> rankedSentences = new ArrayList<String>();
//						rankedSentences = Lucene.LuceneComputation(relevantSentences,relevantSentences.size(),question.getKeywords());
//						System.out.println("**********RANKED SENTENCES**********");
//						//question.addAllRelevantPassages(rankedSentences);	
//						
//						if (!rankedSentences.isEmpty()) {
//							for (int k = 0; k < rankedSentences.size(); k++) {
//								System.out.println(k+1 + ":\t"+rankedSentences.get(k));
//							}			
//						}
//					}
				}
				else {
					System.out.println("Candidate Passages List is empty.");	
				}
			} else {
					System.out.println("Relevant Documents List is empty");
			}
		}
	}
	
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
	
	private List<String> getXMLOutputforAllDocs(String eachDocument)
	{		
	
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
	
	private List<String> getRelevantPassages (List<String> top10Documents, String keywords) {
		List <String> passagesList = new ArrayList<String>();
		List <String> relevantPassages = new ArrayList<>();
		
		if (!top10Documents.isEmpty()) {
			for (int i = 0; i < top10Documents.size(); i++) {
				//System.out.println("__________DOCUMENT "+(i+1)+"_________");
				//System.out.println(top10Documents.get(i));
				
				passagesList.addAll(getXMLOutputforAllDocs(top10Documents.get(i).trim()));
			}	
			    //System.out.println("passagesList: "+passagesList.size());
				if (!passagesList.isEmpty()) {
//					for (int j = 0; j < passagesList.size(); j++) {
//						//System.out.println(passagesList.get(j));
//						//System.out.print("\n");
//					}	
//						
					List<String> candidatePassages = new ArrayList<String>();
					candidatePassages = Lucene.LuceneComputation(passagesList,10, keywords);
					relevantPassages.addAll(candidatePassages);
					if (!candidatePassages.isEmpty()) {
//						for (int k = 0; k < candidatePassages.size(); k++) {
//						System.out.println(k+1 + ":\t"+candidatePassages.get(k));
//						} 
					}
					else
						System.out.println("CandidatePassagesList for Document "+" is empty.");	
					} 
				else
					System.out.println("PassagesList for Document "+" is empty.");
			}
			
			else
				System.out.println("Top10DocumentsList is empty");
		
	return relevantPassages;
	}
}
