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
public class LiteralMatchingSearcher implements Searcher {

    
    private Index index;
    
    
    @Override
    public void build(Index index) {
        this.index = index;
    }

    @Override
    public List<ScoredTextDocument> search(String query) {
        
        if (index == null) return null;
        HashMap<String,Double> docTFIDF = new HashMap<>();
        List<ScoredTextDocument> listScorDocs = new ArrayList<>();
        
        String[] queryArray = query.split(" ");
        if (queryArray.length == 0) return listScorDocs;
        
        // Lista de tantas listas como cláusulas de la consulta
        List<List<Posting>> listQueryPostings = new ArrayList<>();
        
        for (String clause : queryArray) {
            // Por cada cláusula, se construye una lista de postings
            List<Posting> postingList = index.getTermPostings(clause);
            listQueryPostings.add(postingList);
        }
        
       
        
        return listScorDocs;
    }
    
    public static List<Posting> intersection(List<List<Posting>> list1) {
        
        List<Posting> list = new ArrayList(list1.get(0));
        
        for (Posting t : list1.get(0))
        {
            for(int i=1;i<list1.size();i++)
            {
            
                if(!list1.get(i).contains(t))
                {
                    list.remove(t);
                    break;
                }
                else
                {
                    Posting taux = list1.get(i).get(list1.get(i).indexOf(t));
                    for(Long l : taux.getTermPositions())
                    {
                        
                    }
                }
            }
        }
       /* for(int i=1;i<list1.size();i++)
        {
            if(list1.get()
            {
                list.remove(t);
                break;
            }            
        }*/
        
        return list;
    }  
    
    public static void main(String[] args) {
        
        List<Long> pos2 = new ArrayList();
        List<Long> pos1 = new ArrayList();
        List<Long> pos = new ArrayList();
        pos.add((long)1);
        pos.add((long) 6);
        pos1.add((long)2);
        pos1.add((long) 8);
        pos2.add((long)3);
        pos2.add((long) 2);
        List <Posting> postings = new ArrayList();
        List <Posting> postings1 = new ArrayList();
        List <Posting> postings2 = new ArrayList();
        List <List<Posting>> post = new ArrayList<>();
        Posting p1 = new Posting("01",4,pos);
        Posting p2 = new Posting("02",3,pos);
        Posting p4 = new Posting("04",4,pos1);
        Posting p3 = new Posting("02",5,pos1);    
        postings.add(p1);
        postings.add(p2);
        postings1.add(p4);
        post.add(postings);
        postings1.add(p3);
        post.add(postings1);
        postings2.add(new Posting("02",6,pos2));
        post.add(postings2);
        List<Posting> ojete = intersection(post);
        
      

    }
}
