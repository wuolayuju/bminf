package es.uam.eps.bmi.search;

import es.uam.eps.bmi.search.indexing.LuceneIndex;
import es.uam.eps.bmi.search.indexing.Posting;
import es.uam.eps.bmi.search.parsing.HTMLSimpleParser;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Realiza un test de creación de un índice a través de un directorio de documentos 
 * comprimidos en formato ZIP.
 * introducido como parámetro
 * @author Ari Handler - Adrián Lorenzo
 */
public class TestIndex {
    

    public static void  main(String[] args) throws IOException {
        
        class OrderedEntry implements Comparable{
            public final String term;
            public final int frequency;
            public final int numDocs;

            public OrderedEntry(String term, int frequency, int numDocs) {
                this.term = term;
                this.frequency = frequency;
                this.numDocs = numDocs;
            }

            @Override
            public int compareTo(Object o) {
                OrderedEntry oe2 = (OrderedEntry) o;
                return oe2.frequency - this.frequency;
            }

        }
    
        String usage = "java es.uam.eps.bmi.search.TestIndex"
                     + " [-index INDEX_PATH] [-docs DOCS_PATH] [-output OUTPUT_PATH]\n\n"
                     + "This indexes the documents in DOCS_PATH, creating a Lucene index"
                     + "in INDEX_PATH, bringing it then into RAM and, finally, "
                + "writing statistics of indexing in a file in OUTPUT_PATH.";
        
        String indexPath = "index";
        String docsPath = "docs";
        String outputFile = "output";
        
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
        }
        
        // Creación y carga en RAM del índice
        LuceneIndex index = new LuceneIndex();
        index.build(docsPath, indexPath, new HTMLSimpleParser());
        index.load(indexPath);
        
        
        List<String> terms = index.getTerms();
        ListIterator<String> itrTerms = terms.listIterator();
        Writer writer;
        
        TreeSet<OrderedEntry> treeStats = new TreeSet<>();
        
        while(itrTerms.hasNext()) 
        {
            String term = itrTerms.next();
            
            List<Posting> postings = index.getTermPostings(term);
            
            // Sumamos todas las frecuencias de los postings del término
            int totalFreq = 0;
            for (Posting post : postings) {
                totalFreq += post.getTermFrequency();
            }
            
            // Numero de documentos en los que aparece el término
            int totalDocsTerm = postings.size();
            
            // Inserción en la lista de estadísticas
            OrderedEntry entry = new OrderedEntry(term, totalFreq, totalDocsTerm);
            treeStats.add(entry);
        }
        
        // Escritura en fichero de las estadísticas
        writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "utf-8"));

        for (OrderedEntry e : treeStats) {
            try {
                writer.write(e.term+" "+e.frequency+" "+e.numDocs+"\n");
            } catch (IOException ex) {
                Logger.getLogger(TestIndex.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        writer.close();
    }
}
