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
import java.util.HashMap;
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
public class ProximalSearcher implements Searcher{

    
    private Index index;
    
    @Override
    public void build(Index index) {
        this.index = index;
    }

    @Override
    public List<ScoredTextDocument> search(String query) {
        
        if (index == null) 
            return null;
        
        HashMap<String,Double> docTFIDF = new HashMap<>();
        List<ScoredTextDocument> listScorDocs = new ArrayList<>();
        
        String[] queryArray = query.split(" ");
        if (queryArray.length == 0) 
            return listScorDocs;
        
        // Lista de tantas listas como cláusulas de la consulta
        List<List<Posting>> listQueryPostings = new ArrayList<>();
        
        for (String clause : queryArray) {
            // Por cada cláusula, se construye una lista de postings
            List<Posting> postingList = index.getTermPostings(clause);
            listQueryPostings.add(postingList);
        }
    
        return listScorDocs;
    }
    
    
    private List<Posting> intersection(List<List<Posting>> list1) {
        
        List<Posting> list = new ArrayList();
        List <String> listdocumentIds = new ArrayList();
        int i;
        
        for (Posting t : list1.get(0))
        {
            for(i=1;i<list1.size();i++)
            {
            
                if(!list1.get(i).contains(t))
                    break;
                
            }
            if(i==list1.size())
                listdocumentIds.add(t.getDocumentId());
        }
        
        for(i=0;i<list1.size();i++)
        {

            for(int j=0;j<list1.get(i).size();j++)
            {
                if(listdocumentIds.contains(list1.get(i).get(j).getDocumentId()))
                {
                    list.add(list1.get(i).get(j));
                    break;
                }
            }
        }
        return list;
    }   
}
