/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.search;

import es.uam.eps.bmi.search.indexing.LuceneIndex;
import es.uam.eps.bmi.search.searching.LuceneSearcher;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ListIterator;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;

/**
 *
 * @author e185318
 */
public class TestSearcher {
    private static String field;
    
     public static void  main(String[] args) throws IOException, ParseException {
         
         String usage = "java es.uam.eps.bmi.search.TestIndex"
                     + " [-index INDEX_PATH] [-docs DOCS_PATH] [-queries QUERIES_PATH] [-output OUTPUT_PATH]\n\n"
                     + "This indexes the documents in DOCS_PATH, creating a Lucene index"
                     + "in INDEX_PATH, bringing it then into RAM and, finally, "
                + "writing statistics of indexing in a file in OUTPUT_PATH.";
        
        String indexPath = "index";
        String docsPath = "docs";
        String outputFile = "output";
        String queriesFile = "queries";
        
        for(int i=0;i<args.length;i++) {
            if(args[i].compareTo("-index")==0) {
                indexPath = args[i+1];
                i++;
            }
            if(args[i].compareTo("-docs")==0) {
                docsPath = args[i+1];
                i++;
            }
            if(args[i].compareTo("-output")==0) {
                outputFile = args[i+1];
                i++;
            }
            if(args[i].compareTo("-queries")==0) {
                queriesFile = args[i+1];
                i++;
            }
        }
        LuceneIndex index = new LuceneIndex();
        LuceneSearcher searcher = new LuceneSearcher();
        
        index.load(indexPath);
        
        while (true) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(new FileInputStream(queriesFile), "UTF-8"));
            String line = in.readLine();
            
            if(line == null)
                    break;
            
            QueryParser parser = new QueryParser(Version.LUCENE_31, "contents", 
                    new StandardAnalyzer(Version.LUCENE_31));
            
            Query query = parser.parse(line);
            System.out.println("Searching for: " + query.toString(field));
            
            searcher.build(index);
            
            searcher.setTopResults(5);
            ListIterator<ScoredTextDocument> itr =searcher.search(line).listIterator();
            int indexQuery = 1;
            System.out.print(indexQuery);
            while(itr.hasNext())
            {
                ScoredTextDocument doc = itr.next();
                System.out.println("\t"+index.getDocument(doc.getDocumentId()).getName()+ " Score: "+
                        doc.getScore());
            }
            
            searcher.setTopResults(10);
            
            
            
        }
     }
}
