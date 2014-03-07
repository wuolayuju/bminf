/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.search.searching;

import es.uam.eps.bmi.search.ScoredTextDocument;
import es.uam.eps.bmi.search.indexing.BasicIndex;
import java.util.List;

/**
 *
 * @author chus
 */
public class TestTFIDFSearcher {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String indexPath = "index";
        String queriesPath = "queries";
        
        for(int i=0;i<args.length;i++) {
            if(args[i].compareTo("-index")==0) {
                indexPath = args[i+1];
                i++;
            }
            if(args[i].compareTo("-queries")==0) {
                queriesPath = args[i+1];
                i++;
            }
        }
        
        BasicIndex basicIndex = new BasicIndex();
        
        basicIndex.load(indexPath);
        
        TFIDFSearcher tfidfSearcher = new TFIDFSearcher();
        
        tfidfSearcher.build(basicIndex);
        
        tfidfSearcher.setTopResults(5);
        
        List<ScoredTextDocument> results = tfidfSearcher.search("obama family tree");

        for (ScoredTextDocument doc : results) {
            String docId = doc.getDocumentId();
            System.out.println("Doc = " + basicIndex.getDocument(docId).getName() + ". Score = " + doc.getScore());
        }
    }
    
}