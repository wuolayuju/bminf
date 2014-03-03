/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.search.searching;

import es.uam.eps.bmi.search.ScoredTextDocument;
import es.uam.eps.bmi.search.TextDocument;
import es.uam.eps.bmi.search.indexing.Index;
import es.uam.eps.bmi.search.indexing.Posting;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
 
/**
 *
 * Buscador que implementa el modelo de recuperación de información Booleano.
 * Permite establecer si los términos de una consulta 
 * están relacionados con operadores OR o AND. 
 * 
 * @author Ari Handler - Adrián Lorenzo
 */
public class BooleanSearcher implements Searcher {

    public static int OR_OPERATOR = 0;
    public static int AND_OPERATOR = 1;
    
    private Index index;
    private int operator = OR_OPERATOR;
    
    @Override
    public void build(Index index) {
        this.index = index;
    }

    @Override
    public List<ScoredTextDocument> search(String query) {
        
        if (index == null) return null;
        
        List<ScoredTextDocument> listScorDocs = new ArrayList<>();
        
        String[] queryArray = query.split(" ");
        
        List<List<ScoredTextDocument>> listResults = new ArrayList<>();
        
        for (String clause : queryArray) {
            List<Posting> postingList = index.getTermPostings(clause);
            List<ScoredTextDocument> docsList = new ArrayList<>();
            for (Posting postClause : postingList) {
                ScoredTextDocument scoredDoc = new ScoredTextDocument(postClause.getDocumentId(), 1);
                docsList.add(scoredDoc);
            }
            listResults.add(docsList);
        }
        
        if (operator == OR_OPERATOR) {
            listScorDocs.addAll(listResults.get(0));
            for (List<ScoredTextDocument> listClause : listResults) {
                listScorDocs = union(listScorDocs, listClause);
            }
        }
        else if (operator == AND_OPERATOR) {
            listScorDocs.addAll(listResults.get(0));
            for (List<ScoredTextDocument> listClause : listResults) {
                listScorDocs = intersection(listScorDocs, listClause);
            }
        }
        
        return listScorDocs;
    }
    
    public void setQueryOperator(int operator) {
        this.operator = operator;
    }
    /**
     * 
     * Retorna la union de dos listas genéricas
     * 
     * @param <T>
     * @param list1
     * @param list2
     * @return 
     */
    private <T> List<T> union(List<T> list1, List<T> list2) {
        Set<T> set = new HashSet<>();

        set.addAll(list1);
        set.addAll(list2);

        return new ArrayList<>(set);
    }
    /**
     * 
     * Retorna la intersección de dos listas genéricas
     * 
     * @param <T>
     * @param list1
     * @param list2
     * @return 
     */
    public <T> List<T> intersection(List<T> list1, List<T> list2) {
        List<T> list = new ArrayList<>();

        for (T t : list1) {
            if(list2.contains(t)) {
                list.add(t);
            }
        }

        return list;
    }
}
