/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
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
                    + " [-index INDEX_PATH] [-queries QUERIES_PATH] [-output OUTPUT_PATH]\n\n"
                    + "in INDEX_PATH, bringing it then into RAM and, finally, "
               + "writing statistics of indexing in a file in OUTPUT_PATH.";

       String indexPath = "index";
       String outputFile = "output";
       String queriesFile = "queries";

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
       }
       LuceneIndex index = new LuceneIndex();
       LuceneSearcher searcher = new LuceneSearcher();

       index.load(indexPath);
       BufferedWriter writerTop;
       BufferedWriter writerTop10;
       searcher.build(index);
       for(int i=0;i<2;i++)
       {
           try {
                if(i==0)
                {
                    writerTop = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile+"top5"), "utf-8"));
                    searcher.setTopResults(5);
                }
                else
                {
                    writerTop = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile+"top10"), "utf-8"));
                    searcher.setTopResults(10);
                }

                BufferedReader in = new BufferedReader(
                     new InputStreamReader(new FileInputStream(queriesFile), "UTF-8"));
                int indexQuery = 1;
                while (true) {

                    String line = in.readLine();

                    if(line == null)
                            break;

                    QueryParser parser = new QueryParser(Version.LUCENE_31, "contents", 
                            new StandardAnalyzer(Version.LUCENE_31));

                    Query query = parser.parse(line);




                    ListIterator<ScoredTextDocument> itr =searcher.search(line).listIterator();
                    

                    // Escritura en fichero de las estadísticas

                    writerTop.write(""+indexQuery);
                    while(itr.hasNext())
                    {
                        ScoredTextDocument doc = itr.next();
                        writerTop.write("\t"+index.getDocument(doc.getDocumentId()).getName()+ " Score: "+
                                doc.getScore()+"\n");
                    }
                    indexQuery++;
                    
                }
                writerTop.close();
            }catch(UnsupportedEncodingException | FileNotFoundException ex) {
                Logger.getLogger(TestIndex.class.getName()).log(Level.SEVERE, null, ex);
            }

       }

    }
}
