/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.search.searching;

import es.uam.eps.bmi.search.ScoredTextDocument;
import es.uam.eps.bmi.search.indexing.Index;
import es.uam.eps.bmi.search.indexing.LuceneIndex;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import static java.lang.System.in;
import java.util.List;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;

/**
 *
 * @author e185318
 */
public class LuceneSearcher implements Searcher{

    
    IndexSearcher searcher;
    
    @Override
    public void build(Index index) {
      
        //searcher = new IndexSearcher(((LuceneIndex)index).getIndexReader());
    }

    @Override
    public List<ScoredTextDocument> search(String query) {
        
        /* ayayay tu bajaste desde el cielooo*/
        /*que kohonen hacemos con el campo fieeeld*/
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
        //QueryParser parser = new QueryParser(Version.LUCENE_36, field, analyzer);
        //Query query = parser.parse(query);
        
        return null;
    }
    
    public static void  main(String[] args) throws Exception {
        
        String usage =
          "Usage:\tjava org.apache.lucene.demo.SearchFiles [-index dir]\nSee http://lucene.apache.org/java/4_0/demo.html for details.";
        String index = "index";
        
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
        }
    
    }
    
}
