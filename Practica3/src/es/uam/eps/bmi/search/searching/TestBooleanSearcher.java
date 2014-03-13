/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.search.searching;

import es.uam.eps.bmi.search.ScoredTextDocument;
import es.uam.eps.bmi.search.indexing.BasicIndex;
import es.uam.eps.bmi.search.indexing.StemIndex;
import java.util.List;

/**
 * 
 * @author Ari Handler - Adrián Lorenzo
 */
public class TestBooleanSearcher {

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
        
        StemIndex stemIndex = new StemIndex();
        
        stemIndex.load(indexPath);
        
        BooleanSearcher boolSearcher = new BooleanSearcher();
        
        boolSearcher.build(stemIndex);
        
        boolSearcher.setQueryOperator(BooleanSearcher.AND_OPERATOR);
        
        List<ScoredTextDocument> results = boolSearcher.search("obama family tree");

        for (ScoredTextDocument doc : results) {
            String docId = doc.getDocumentId();
            System.out.println("DocID = " + docId + "(" + stemIndex.getDocument(docId).getName() + ").");
        }
    }
    
}
