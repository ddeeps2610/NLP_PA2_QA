package qa.PassageRetrieval;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
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

	public static List<String> LuceneComputation( List<String> passagesList, int noOfReturns, String question) {
		//System.out.println("*********In Lucene Computation...");
		List<String> retList = new ArrayList<String>();
		try {
		
		//Lucene Analyser
	    StandardAnalyzer analyzer = new StandardAnalyzer();
	    
	    // Creating the index
	    Directory index = new RAMDirectory();

	    IndexWriterConfig config = new IndexWriterConfig(analyzer);

	    IndexWriter w;
			w = new IndexWriter(index, config);
		
		for (String passage: passagesList) {
//			System.out.println(passage);
			addDocuments(w, passage);
		}
		w.close();
		
		//Query Words
		String querystr = //"Which Hockey team did Wayne Gretzky play for?";
				"owns CNN?";
		//args.length > 0 ? args[0] : "action";

		Query q = new QueryParser("description", analyzer).parse(question);


		int hitsPerPage = noOfReturns;
		IndexReader reader = DirectoryReader.open(index);
		IndexSearcher searcher = new IndexSearcher(reader);
		TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
		searcher.search(q, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;

		//System.out.println(hits.length + " most Lucene relevant.");
		for(int i=0;i<hits.length;++i) {
				int docId = hits[i].doc;
				Document d = searcher.doc(docId);
				//System.out.println((i + 1) + ". " + d.get("description"));
				retList.add(i,d.get("description"));
				
		}

		reader.close();
		
		} catch (IOException | ParseException e) {
			
			e.printStackTrace();
		}
		return retList;
	};  
	
	public static void addDocuments(IndexWriter w, String description) throws IOException {
		    Document doc = new Document();
		    doc.add(new TextField("description", description, Field.Store.YES));
		    w.addDocument(doc);
		  }
}
