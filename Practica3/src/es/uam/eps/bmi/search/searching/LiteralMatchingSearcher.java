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
 *  Buscador que implementa una búsqueda literal. Esto es, una búsqueda en la 
 * que las palabras de la consulta van entre comillas, y los documentos 
 * devueltos deben contener estos términos de forma consecutiva y en el mismo
 * orden en que se dan en la consulta. 
 * 
 * @author Ari Handler - Adrián Lorenzo
 */
public class LiteralMatchingSearcher implements Searcher {

    
    private Index index;
    
    
    @Override
    public void build(Index index) {
        this.index = index;
    }

    @Override
    public List<ScoredTextDocument> search(String query) {
        
        if (index == null) return null;
        
        List<ScoredTextDocument> listScorDocs = new ArrayList<>();
        
        String[] queryArray = query.split(" ");
        if (queryArray.length == 0) return listScorDocs;
        
        // Lista de tantas listas como cláusulas de la consulta
        List<List<Posting>> listQueryPostings = new ArrayList<>();
        
        for (String clause : queryArray) {
            // Por cada cláusula, se construye una lista de documentos puntuados
            // a 1 (modelo booleano).
            List<Posting> postingList = index.getTermPostings(clause);
            listQueryPostings.add(postingList);
        }
        
       
        
        return listScorDocs;
    }
    
}
