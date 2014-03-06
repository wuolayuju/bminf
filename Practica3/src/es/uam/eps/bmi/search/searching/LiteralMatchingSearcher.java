/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.search.searching;

import es.uam.eps.bmi.search.ScoredTextDocument;
import es.uam.eps.bmi.search.indexing.Index;
import es.uam.eps.bmi.search.indexing.Posting;
import static es.uam.eps.bmi.search.searching.BooleanSearcher.AND_OPERATOR;
import static es.uam.eps.bmi.search.searching.BooleanSearcher.OR_OPERATOR;
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
        List<List<ScoredTextDocument>> listResults = new ArrayList<>();
        
        for (String clause : queryArray) {
            // Por cada cláusula, se construye una lista de documentos puntuados
            // a 1 (modelo booleano).
            List<Posting> postingList = index.getTermPostings(clause);
            List<ScoredTextDocument> docsList = new ArrayList<>();
            for (Posting postClause : postingList) {
                ScoredTextDocument scoredDoc = new ScoredTextDocument(postClause.getDocumentId(), 1);
                docsList.add(scoredDoc);
            }
            listResults.add(docsList);
        }
        
        if (listResults.get(0).isEmpty()) return listScorDocs;
        
     
        // En caso de conjunción, intersección sucesiva de listas
        listScorDocs.addAll(listResults.get(0));
        for (List<ScoredTextDocument> listClause : listResults) {
            listScorDocs = BooleanSearcher.intersection(listScorDocs, listClause);
        }
        
        return listScorDocs;
    }
    
}
