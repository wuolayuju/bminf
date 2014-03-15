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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

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
        
        List<ScoredTextDocument> listIntersection = getFullIntersection(listQueryPostings);
        
        PriorityQueue<ScoredTextDocument> heapScores = 
                new PriorityQueue<>(TOP_RES, new LiteralMatchingSearcher.ScoredTextDocumentComparator());
        
        /***********HEAP**************/
        for (ScoredTextDocument scoredDoc : listIntersection) {
            int docSize = index.getDocument(scoredDoc.getDocumentId()).getSize();
            double normScore = scoredDoc.getScore() / docSize;
            scoredDoc.setScore(normScore);
            if (heapScores.size() == TOP_RES) {
                if (heapScores.peek().getScore() < normScore){
                    heapScores.poll();
                    heapScores.offer(scoredDoc);
                }
            } else {
                heapScores.offer(scoredDoc);
            }
        }
        /***********FIN HEAP**********/
        
        // Conversión a lista del heap de puntuaciones
        List<ScoredTextDocument> scoredDocs = new ArrayList<>();
        
        scoredDocs.addAll(heapScores);
        
        Collections.sort(scoredDocs, new ScoredTextDocumentComparator());

        Collections.reverse(scoredDocs);
        
        return scoredDocs;
        
    }

    private class ScoredTextDocumentComparator implements Comparator<ScoredTextDocument> {

        @Override
        public int compare(ScoredTextDocument o1, ScoredTextDocument o2) {
            if (o1.getScore() > o2.getScore())
                return 1;
            if (o1.getScore() < o2.getScore())
                return -1;
            return 0;
        }
    }
    
    private List<Posting> intersection(List<List<Posting>> list1) {
        
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

            }
        }
        return list;
    }  
    
    private List<ScoredTextDocument> getFullIntersection(List<List<Posting>> list1)
    {
        HashMap<String,Integer> hashDocFreq = new HashMap<>();
        List<ScoredTextDocument> documentlist = new ArrayList<>();
        List<Posting> intersection;
        intersection = intersection(list1);
        int i;
        //List<Posting> intersection = new ArrayList(list2);
        for(Posting p : intersection)
        {
            int freq = 0;
            for(Long position : p.getTermPositions())
            {
                for(i=1;i<list1.size();i++)
                {
                    Posting postinglist1 = list1.get(i).get(list1.get(i).indexOf(p));
                    if(!postinglist1.getTermPositions().contains(position+i))
                        break;
                }
                if(i == list1.size())
                    freq++;   
            }
            if(freq>0)
                hashDocFreq.put(p.getDocumentId(), freq);
        }
        
        double idfTerm = 0;
        if (!hashDocFreq.isEmpty())
            idfTerm = Math.log(index.getDocumentIds().size()/hashDocFreq.size());
        
        for (String key : hashDocFreq.keySet()) {
         
            double tfidf = (1 + Math.log((double)hashDocFreq.get(key)))*idfTerm;
            documentlist.add(new ScoredTextDocument(key,tfidf));
        }
        return documentlist;
    }
}
