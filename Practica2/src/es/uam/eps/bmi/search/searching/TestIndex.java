/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.search.searching;

import es.uam.eps.bmi.search.ScoredTextDocument;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pinwi
 */
public class TestIndex {
    
    static <K,V extends Comparable<? super V>> SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
            SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
                new Comparator<Map.Entry<K,V>>() {
                    @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
                        List<Integer> l1 = (List<Integer>) e1.getValue();
                        List<Integer> l2 = (List<Integer>) e2.getValue();
                        int res = l1.get(0).compareTo(l2.get(0));
                        return res != 0 ? res : 1; // Special fix to preserve items with equal values
                    }
                }
            );
            sortedEntries.addAll(map.entrySet());
            return sortedEntries;
    }

    public static void  main(String[] args) {
    
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
        //index.build(docsPath, indexPath, new HTMLSimpleParser());
        index.load(indexPath);
        
        
        List<String> terms = index.getTerms();
        ListIterator<String> itrTerms = terms.listIterator();
        Writer writer = null;
        
        Map<String, List<Integer>> mapTermFreqDocs = new TreeMap<>();
        
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
            
            // Inserción en el árbol de estadísticas
            List<Integer> values = new ArrayList<>();
            values.add(totalFreq); values.add(totalDocsTerm);
            mapTermFreqDocs.put(term, values);
        }
        
        // Escritura en fichero de las estadísticas
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "utf-8"));
        } catch (UnsupportedEncodingException | FileNotFoundException ex) {
            Logger.getLogger(TestIndex.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for (Entry<String, List<Integer>> entry = entriesSortedByValues(mapTermFreqDocs)) {
            
        }
        
        itrTerms = terms.listIterator();
        while (itrTerms.hasNext()) {
            String term = itrTerms.next();
            List<Integer> freqDocs = mapTermFreqDocs.get(term);
            try {
                writer.write(term+" "+freqDocs.get(0)+" "+freqDocs.get(1)+"\n");
            } catch (IOException ex) {
                Logger.getLogger(TestIndex.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
