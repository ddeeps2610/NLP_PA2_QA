/**
 * 
 */
package qa.PassageRetrieval;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class PassageReader implements IPassageReader {
	
static List<String> splitDocuments(String filename) {

	 List<String> TopDocs = new LinkedList<String>();
	
		BufferedReader br = null;
		String XMLOutput = "";
		try {
			br = new BufferedReader(new FileReader (filename));
			String CurrentLine = null;
			while ((CurrentLine = br.readLine()) != null) {
				if (!CurrentLine.contains("Qid")) {
				//	System.out.println(CurrentLine);
					XMLOutput = XMLOutput + " " +CurrentLine;
				}
				else {
					//System.out.println(XMLOutput);
					if (!XMLOutput.trim().isEmpty()) {
						TopDocs.add(XMLOutput.trim());
						XMLOutput = "";
					}
				}				
			}
			if (!XMLOutput.trim().isEmpty()) 
				TopDocs.add(XMLOutput.trim());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (br != null)
					br.close();
				System.out.println("Number of documents fed : "+TopDocs.size());
				
				//Write into a file
				File file = new File("C:/Users/Arpitha/Documents/NLP/Lucene/SplitDocuments.txt");
				if(!file.exists())
					file.createNewFile();
				BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
				for (int i = 0; i < TopDocs.size(); i++) {
					//	System.out.println(TopDocs.get(i));
					bw.write("\nRANK: "+i+"\n"+TopDocs.get(i));
				}
				bw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return TopDocs;
	}

	
	
	
	
	
	// List of all 50 Documents read from top_docs.n file
	/*public static List<String> TopDocs = new LinkedList<String>();
	
	public static String METHOD = 
			"GET_HIGHEST_10_DOCS";
	
	//Function to split the top_docs.n file and store it as a list of 50 document strings. Written into file SplitDocuments.txt
	static void splitDocuments(String filename) {
		
		BufferedReader br = null;
		String XMLOutput = "";
		try {
			br = new BufferedReader(new FileReader (filename));
			String CurrentLine = null;
			while ((CurrentLine = br.readLine()) != null) {
				if (!CurrentLine.contains("Qid")) {
				//	System.out.println(CurrentLine);
					XMLOutput = XMLOutput + " " +CurrentLine;
				}
				else {
					//System.out.println(XMLOutput);
					if (!XMLOutput.trim().isEmpty()) {
						TopDocs.add(XMLOutput.trim());
						XMLOutput = "";
					}
				}				
			}
			if (!XMLOutput.trim().isEmpty()) 
				TopDocs.add(XMLOutput.trim());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (br != null)
					br.close();
				System.out.println("Number of documents fed : "+TopDocs.size());
				
				//Write into a file
				File file = new File("C:/Users/Arpitha/Documents/NLP/Lucene/SplitDocuments.txt");
				if(!file.exists())
					file.createNewFile();
				BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
				for (int i = 0; i < TopDocs.size(); i++) {
					//	System.out.println(TopDocs.get(i));
					bw.write("\nRANK: "+i+"\n"+TopDocs.get(i));
				}
				bw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
	}

	
	public static List<String> parseXMLDocument(Node node, List<String> passagesList ) {
		
	    NodeList nodeList = node.getChildNodes();
	    String nodeName = null;    
        nodeName = node.getNodeName();   	
	    for (int i = 0; i < nodeList.getLength(); i++) {
	    	
	        Node currentNode = nodeList.item(i);
	        if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
	        	parseXMLDocument(currentNode, passagesList);		        	
	        } else {	        	
	        	//System.out.println(nodeName);
	        	String temp = currentNode.getTextContent().trim();
	        	//System.out.println(temp);
	        	if (!temp.isEmpty()) {
	        		if (nodeName.equals("TEXT")) {
		        		//System.out.println("Inside Text Splitting..");
		        		String[] tempSplit = temp.split("\\s\\s|;");
		        		for (int j = 0; j < tempSplit.length; j++) {
							System.out.println(tempSplit[j]);
						} 
		        		List<String> splitList = Arrays.asList(tempSplit);
		        		passagesList.addAll(splitList);
	        		}
	        	else
	        		passagesList.add(temp);
	        }
	    }
	       
	    for (int j = 0; j < passagesList.size(); j++) {
			System.out.println(passagesList.get(j));
		}   
	    }
	    return passagesList;
	}
	
	static List<String> getXMLOutputforAllDocs(String eachDocument)
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
				System.out.println("Passage: "+passagesList.size());
			for (int i = 0; i < passagesList.size(); i++) {
				System.out.println(passagesList.get(i));
			}   
		
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			return passagesList;
		}
	}
	
	static List<String> getRelevantPassages (List<String> top10Documents) {
		List <String> passagesList = new ArrayList<String>();
		List <String> relevantPassages = new ArrayList<>();
		
		if (!top10Documents.isEmpty()) {
			for (int i = 0; i < top10Documents.size(); i++) {
				//System.out.println("__________DOCUMENT "+(i+1)+"_________");
				//System.out.println(top10Documents.get(i));
				
				passagesList = getXMLOutputforAllDocs(top10Documents.get(i).trim());
				
			    //System.out.println("passagesList: "+passagesList.size());
				if (!passagesList.isEmpty()) {
					for (int j = 0; j < passagesList.size(); j++) {
						//System.out.println(passagesList.get(j));
						//System.out.print("\n");
					}	
						
					List<String> candidatePassages = new ArrayList<String>();
					candidatePassages = Lucene.LuceneComputation(passagesList,2,PostPassageRetrieval.keywordsString);
					relevantPassages.addAll(candidatePassages);
					if (!candidatePassages.isEmpty()) {
						for (int k = 0; k < candidatePassages.size(); k++) {
						System.out.println(k+1 + ":\t"+candidatePassages.get(k));
						} 
					}
					else
						System.out.println("CandidatePassagesList for Document "+i+" is empty.");	
					} 
				else
					System.out.println("PassagesList for Document "+i+" is empty.");
			}
			}
			else
				System.out.println("Top10DocumentsList is empty");
		
	return relevantPassages;
	}
	
public static void main(String argv[]) {
	
	List<String> passagesList = new ArrayList<String>();
	String question = "What province is Edmonton located in?";
	String keywordString = PostPassageRetrieval.keywordExtractor(question);
	System.out.println("Keyword String: "+keywordString);
		
switch (METHOD)
{
case "GET_HIGHEST_10_DOCS": 
	splitDocuments("C:/Users/Arpitha/Documents/NLP/PA2/pa2_data/pa2-release/topdocs/dev/top_docs.102");
	if (!TopDocs.isEmpty()) {
		for (int i = 1; i < TopDocs.size(); i++) {
			System.out.println("__________DOCUMENT "+i+"_________");
			System.out.println(TopDocs.get(i));					
				} 
		List<String> candidateDocuments = new ArrayList<String>();
		candidateDocuments = Lucene.LuceneComputation(TopDocs,10,keywordString);
		System.out.println("**********RELEVANT DOCUMENTS**********");
		if (!candidateDocuments.isEmpty()) {
			for (int k = 0; k < candidateDocuments.size(); k++) {
				System.out.println(k+1 + ":\t"+candidateDocuments.get(k));
			}
			
			List<String> relevantPassages = getRelevantPassages(candidateDocuments);
			System.out.println("**********RELEVANT PASSAGES**********");
			if (!relevantPassages.isEmpty()) {
				for (int k = 0; k < relevantPassages.size(); k++) {
					System.out.println(k+1 + ":\t"+relevantPassages.get(k));
				}
			
			}
			
			List<String> relevantSentences = PostPassageRetrieval.sentenceTokenizer(relevantPassages);
			System.out.println("**********RELEVANT SENTENCES**********");
			if (!relevantSentences.isEmpty()) {
				for (int k = 0; k < relevantSentences.size(); k++) {
					System.out.println(k+1 + ":\t"+relevantSentences.get(k));
				}
								
				List<String> rankedSentences = new ArrayList<String>();
				rankedSentences = Lucene.LuceneComputation(relevantSentences,relevantSentences.size(),PostPassageRetrieval.keywordsString);
				System.out.println("**********RANKED SENTENCES**********");
				if (!rankedSentences.isEmpty()) {
					for (int k = 0; k < rankedSentences.size(); k++) {
						System.out.println(k+1 + ":\t"+rankedSentences.get(k));
					}			
				}
		}
		}
		else
			System.out.println("Candidate Passages List is empty.");	
		}
		else
			System.out.println("Relevant Documents List is empty");
	break;
default:
		break;
}
			
		}  */
}
		




