
package es.uam.eps.bmi.search;

import es.uam.eps.bmi.search.indexing.LuceneIndex;
import es.uam.eps.bmi.search.searching.LuceneSearcher;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.queryParser.ParseException;

/**
 * Realiza un test de consultas sobre el motor de búsqueda imprimiendo el top5 
 * y el top10 de cada una de las consultas, asi como el cálculo de precisión
 * tanto por query como total.
 * @author Ari Handler - Adrián Lorenzo
 */
public class TestSearcher {
    private static String field;
    
    public static void  main(String[] args) throws IOException, ParseException {

        String usage = "java es.uam.eps.bmi.search.TestIndex"
                    + " -index INDEX_PATH -output OUTPUT_PATH [-queries QUERIES_PATH_FILE] [-relevance RELEVANCE_PATH_FILE]\n\n"
                    + "in INDEX_PATH, bringing it then into RAM and, finally, "
               + "writing statistics of indexing in a file in OUTPUT_PATH.";

       String indexPath = "index";
       String outputFile = "output";
       String queriesFile = "queries";
       String relevanceFile = "relevance";

       for(int i=0;i<args.length;i++) {
           if(args[i].compareTo("-index")==0) {
               indexPath = args[i+1];
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
           if(args[i].compareTo("-relevance")==0) {
             relevanceFile = args[i+1];
               i++;
           }
       }
       
       // Instanciación del índice y el buscador
       LuceneIndex index = new LuceneIndex();
       index.load(indexPath);
       LuceneSearcher searcher = new LuceneSearcher();       
       searcher.build(index);
       
       BufferedWriter writerTop;
       BufferedReader readerRelevance;
       BufferedReader inQueries;
       for(int i=0;i<2;i++)
       {
           try {
                if(i==0) // TOP 5
                {
                    writerTop = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile+"top5"), "utf-8"));
                    searcher.setTopResults(5);
                }
                else // TOP 10
                {
                    writerTop = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile+"top10"), "utf-8"));
                    searcher.setTopResults(10);
                }

                readerRelevance = new BufferedReader(
                     new InputStreamReader(new FileInputStream(relevanceFile), "UTF-8"));
                
                inQueries = new BufferedReader(
                     new InputStreamReader(new FileInputStream(queriesFile), "UTF-8"));
                
                int indexQuery = 1;
                double totalPatk = 0;
                
                while (true) {

                    String line = inQueries.readLine();
                    if(line == null)
                            break;
                    line = line.substring(line.indexOf(":")+1); // Limpiamos el numero de query

                    List<ScoredTextDocument> listResults = searcher.search(line);
                    ListIterator<ScoredTextDocument> itr = listResults.listIterator();
                    
                    String lineRelevString = readerRelevance.readLine();
                    List<String> relev = Arrays.asList(lineRelevString.split("\t"));
                    
                    // Escritura en fichero de las estadísticas
                    double n_conj = 0;
                    writerTop.write(""+indexQuery);
                    while(itr.hasNext())
                    {
                        ScoredTextDocument doc = itr.next();
                        
                        String fullDocPath = index.getDocument(doc.getDocumentId()).getName();
                        String docPath = fullDocPath.substring(fullDocPath.indexOf("zip")+4).replace(".html", "");
                        
                        writerTop.write("\t"+docPath);
                        
                        if (relev.contains(docPath)) {
                            n_conj++;
                        }
                    }
                    
                    double patk = n_conj / (i==0 ? 5 : 10);
                    totalPatk += patk;
                    writerTop.write("\nPrecision: "+patk+"\n");
                    indexQuery++;
                }
                
                totalPatk /= indexQuery-1;
                writerTop.write("\n\nPrecision total: "+totalPatk+"\n");
                
                readerRelevance.close();
                inQueries.close();
                writerTop.close();
                
            }catch(UnsupportedEncodingException | FileNotFoundException ex) {
                Logger.getLogger(TestIndex.class.getName()).log(Level.SEVERE, null, ex);
            }

       }

    }
}
