/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.search.searching;

import es.uam.eps.bmi.search.ScoredTextDocument;
import es.uam.eps.bmi.search.indexing.LuceneIndex;
import es.uam.eps.bmi.search.indexing.Posting;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author pinwi
 */
public class TestIndex {
    
    public static void  main(String[] args) {
    
        String usage = "java org.apache.lucene.demo.IndexFiles"
             + " [-index INDEX_PATH] [-output OUTPUTFILE]\n\n";
        String indexPath ="index";
        String outputFile ="output";
        for(int i=0;i<args.length;i++) {
            if(args[i].compareTo("-index")==0) {
                    indexPath = args[i+1];
                    i++;
            }
            if(args[i].compareTo("-output")==0) {
                outputFile = args[i+1];
                    i++;
            }
        }
        LuceneIndex index = new LuceneIndex();
        index.load(indexPath);
        
        List<String> terms = index.getTerms();
        
        ListIterator<String> itrTerm = terms.listIterator();
        
        Writer writer = null;
        
        //while(itrTerm.hasNext())
        //{
            String term = "obama";//itrTerm.next();
            List<Posting> postings = index.getTermPostings(term);
            ListIterator<Posting> itrPost = postings.listIterator();
            int freqTotal =0;
            while(itrPost.hasNext())
            {
                Posting post = itrPost.next();
                freqTotal +=post.getTermFrequency();
            }
            
            try {
                writer = new BufferedWriter(new OutputStreamWriter(
                      new FileOutputStream(outputFile), "utf-8"));
                writer.write("Term: "+term+" Ndoc: "+postings.size()+
                    " FreqTot: " + freqTotal+"\n");
            } catch (IOException ex) {
              // report
            } finally {
               try {writer.close();} catch (Exception ex) {}
            }
            //System.out.println("Term: "+term+" Ndoc: "+postings.size()+
                    //" FreqTot: " + freqTotal);
            
            

        //}
        
        /*
            Map<String, Integer> tm;
            tm = new TreeMap<String,Integer>(new Comparator<String>(){
                @Override
                public int compare(String o1, String o2) {
                    // implement logic here
                    // sample implementation below
                    return o1.compareTo(o2);
                }
            });
            tm.put("ojete", 1);
            tm.put("asd", 2);
            
            System.out.println(tm.toString());
        */
    }
    
}
