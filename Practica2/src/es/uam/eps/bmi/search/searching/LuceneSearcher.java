/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.search.searching;

import es.uam.eps.bmi.search.ScoredTextDocument;
import es.uam.eps.bmi.search.indexing.Index;
import es.uam.eps.bmi.search.indexing.LuceneIndex;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.Version;

/**
 *
 * @author e185318
 */
public class LuceneSearcher implements Searcher{

    
    IndexSearcher searcher;
    int MAX_RES = 5;
    
    @Override
    public void build(Index index)
    {  
        searcher = new IndexSearcher(((LuceneIndex)index).getIndexReader());
    }

    @Override
    public List<ScoredTextDocument> search(String query) {
        List<ScoredTextDocument> listScored = new ArrayList<>();
        try {
            /* ayayay tu bajaste desde el cielooo*/
            /*que kohonen hacemos con el campo fieeeld*/
            Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
            QueryParser queryParser = new QueryParser(Version.LUCENE_36, "contents", analyzer);
            //Query query = parser.parse(query);
            
            Query q = queryParser.parse(query);
            System.out.println("Searching for: " + q.toString("contents"));
            TopDocs results = searcher.search(q, MAX_RES);
            ScoreDoc[] hits =  results.scoreDocs;
            
            
            int numTotalHits = results.totalHits;
            System.out.println(numTotalHits + " total matching documents");
         
            for(ScoreDoc d : hits)
            {
                listScored.add(new ScoredTextDocument(d.doc,d.score));
            }
            return listScored;
            
        } catch (ParseException | IOException ex) {
            Logger.getLogger(LuceneSearcher.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return listScored;
    }
    
    public static void  main(String[] args) throws Exception {
        
        String usage =
          "Usage:\tjava org.apache.lucene.demo.SearchFiles [-index dir]\nSee http://lucene.apache.org/java/4_0/demo.html for details.";
        String index = "index";
        LuceneSearcher searcher = new LuceneSearcher();
        LuceneIndex indexer = new LuceneIndex();
        
        
         
        if (args.length > 0 && ("-h".equals(args[0]) || "-help".equals(args[0]))) {
          System.out.println(usage);
          System.exit(0);
        }
        
        for(int i = 0;i < args.length;i++) {
            if ("-index".equals(args[i])) {
              index = args[i+1];
              i++;  
            }
        }
        
        indexer.load(index);
        searcher.build(indexer);
        
        while (true) {
            System.out.println("Enter query: ");
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
            String line = in.readLine();

            if (line == null || line.length() == -1) {
              break;
            }

            line = line.trim();
            if (line.length() == 0) {
              break;
            }
            
            ListIterator<ScoredTextDocument> itr =searcher.search(line).listIterator();
            
            while(itr.hasNext())
            {
                ScoredTextDocument doc = itr.next();
                System.out.println("DocId: "+doc.getDocumentId()+ " Score: "+
                        doc.getScore());
            }
        }
    
    }
    
}
