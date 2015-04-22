package qa.PassageRetrieval;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

public class Lucene {

	public static List<String> luceneComputation( List<String> passagesList, int noOfReturns, String question) {
	
		List<String> retList = new ArrayList<String>();
		try {
			
			//Lucene Analyser
		    StandardAnalyzer analyzer = new StandardAnalyzer();
		    
		    // Creating the index
		    Directory index = new RAMDirectory();
	
		    IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);	
		    IndexWriter indexWriter = new IndexWriter(index, indexWriterConfig);
			
			for (String passage: passagesList) {
				addDocuments(indexWriter, passage);
			}
			indexWriter.close();
	
			Query q = new QueryParser("description", analyzer).parse(question);
	
	
			int hitsPerPage = noOfReturns;
			IndexReader reader = DirectoryReader.open(index);
			IndexSearcher searcher = new IndexSearcher(reader);
			TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
			searcher.search(q, collector);
			ScoreDoc[] hits = collector.topDocs().scoreDocs;
	
			for(int i=0;i<hits.length;++i) {
				int docId = hits[i].doc;
				Document d = searcher.doc(docId);
				retList.add(i,d.get("description"));
			}
	
			reader.close();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return retList;
	};  
	
	public static void addDocuments(IndexWriter indexWriter, String description) throws IOException {
	    Document doc = new Document();
	    doc.add(new TextField("description", description, Field.Store.YES));
	    indexWriter.addDocument(doc);
	}
}
