/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.search.searching;

import es.uam.eps.bmi.search.ScoredTextDocument;
import es.uam.eps.bmi.search.indexing.Index;
import es.uam.eps.bmi.search.indexing.Posting;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Buscador que implementa el modelo de recuperación de información vectorial 
 * con ponderación de términos TF-IDF.
 * 
 * @author Ari Handler - Adrián Lorenzo
 */
public class TFIDFSearcher implements Searcher {

    private Index index;
    private int TOP_RES = 5;    
    
    @Override
    public void build(Index index) {
        this.index = index;
    }
    
    /**
     * Define la cantidad de resultados devueltos al realizar una búsqueda.
     * @param n Nuevo límite de resultados.
     */
    public void setTopResults(int n) {
        
        if(n>0) this.TOP_RES = n;
    }

    @Override
    public List<ScoredTextDocument> search(String query) {
        
        double idfTerm;
        double tfTerm;
        double tf_idfTerm;
        
        if (index == null) return null;

        int docsSetSize = index.getDocumentIds().size();     
        
        List<ScoredTextDocument> listScorDocs = new ArrayList<>();
        
        String[] queryArray = query.split(" ");
        
        List<List<ScoredTextDocument>> listResults = new ArrayList<>();
        
        for (String clause : queryArray) {
            
            List<Posting> postingList = index.getTermPostings(clause);
            idfTerm = Math.log(docsSetSize/postingList.size());
            
        }
        
        return null;
        
    }

}
