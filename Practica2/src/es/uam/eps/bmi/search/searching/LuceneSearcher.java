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
 * Clase que implementa la interfaz Searcher mediante el uso de los métodos
 * provistos por la API de <a href="https://lucene.apache.org/core/3_6_2/api/all">Lucene 3.6.2</a>.
 * @author Ari Handler - Adrián Lorenzo
 * @see <a href="https://lucene.apache.org/core/3_6_2/api/all">Lucene 3.6.2 API</a>
 */
public class LuceneSearcher implements Searcher{

    
    private IndexSearcher searcher;
    private int MAX_RES = 5;
    

    @Override
    public void build(Index index)
    {  
        searcher = new IndexSearcher(((LuceneIndex)index).getIndexReader());
    }

    @Override
    public List<ScoredTextDocument> search(String query) {
        List<ScoredTextDocument> listScored = new ArrayList<>();
        try {
            
            Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
            QueryParser queryParser = new QueryParser(Version.LUCENE_36, "contents", analyzer);
            
            
            Query q = queryParser.parse(query);
            System.out.println("Searching for: " + q.toString("contents"));
            TopDocs results = searcher.search(q, MAX_RES);
            ScoreDoc[] hits =  results.scoreDocs;
            
            
            int numTotalHits = results.totalHits;
            System.out.println(numTotalHits + " total matching documents");
         
            for(ScoreDoc d : hits)
            {
                listScored.add(new ScoredTextDocument(String.valueOf(d.doc),d.score));
            }
            return listScored;
            
        } catch (ParseException | IOException ex) {
            Logger.getLogger(LuceneSearcher.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return listScored;
    }

    /**
     * Define la cantidad de resultados devueltos al realizar una búsqueda.
     * @param n Nuevo límite de resultados.
     */
    public void setTopResults(int n) {
        this.MAX_RES = n;
    }
    /**
     * La aplicación recibe como argumento de entrada la ruta de la carpeta que contenga
     * un índice Lucene, y de forma iterativa pide al usuario consultas a ejecutar por el buscador
     * sobre el índice, mostrando por pantalla el top 5 documentos
     * 
     * @param args
     * @throws Exception 
     */
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
            System.out.println("\n(q)uit or enter a query:");
            
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
            String line = in.readLine();

            if (line == null || line.length() <=0)
              break;
            
            if(line.charAt(0)=='q')
                break;

            line = line.trim();
            if (line.length() == 0) {
              break;
            }
            
            searcher.setTopResults(3);
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
