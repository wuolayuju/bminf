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
import java.util.List;
import java.util.PriorityQueue;

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
        
        if (index == null) return null;

        int docsSetSize = index.getDocumentIds().size();     
        
        String[] queryArray = query.split(" ");
        
        //List<List<ScoredTextDocument>> listResults = new ArrayList<>();
        
        PriorityQueue<ScoredTextDocument> heapScores = 
                new PriorityQueue<>(TOP_RES, new ScoredTextDocumentComparator());
        
        List<ScoredTextDocument> tfidfDocs = new ArrayList<>();
        
        for (String clause : queryArray) {
            
            List<Posting> postingList = index.getTermPostings(clause);
            
            // Cálculo del valor IDF de cada término de la consulta
            double idf = Math.log(docsSetSize/postingList.size());
            
            // Lista de documentos puntuados correspondiente a la cláusula
            //List<ScoredTextDocument> docsList = new ArrayList<>();
            for (Posting postClause : postingList) {
                
                // Cálculo del valor de TF para la cláusula en el documento
                int freqTerm = postClause.getTermFrequency();
                double tf = 0;
                if (freqTerm > 0) {
                    tf = 1 + Math.log(freqTerm);
                }
                
                ScoredTextDocument scoredDoc = new ScoredTextDocument(postClause.getDocumentId(), tf * idf);
                if (tfidfDocs.contains(scoredDoc)) {
                    // Actualizacion del valor tf-idf del documento
                    tfidfDocs.get(tfidfDocs.indexOf(scoredDoc)).addToScore(scoredDoc.getScore());
                } else {
                    tfidfDocs.add(scoredDoc);
                }
                //docsList.add(scoredDoc);
            }
            //listResults.add(docsList);
        }
        
        /***********HEAP**************/
        for (ScoredTextDocument scoredDoc : tfidfDocs) {
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
        List<ScoredTextDocument> listScorDocs = new ArrayList<>();
        
        listScorDocs.addAll(heapScores);
        
        Collections.sort(listScorDocs, new ScoredTextDocumentComparator());

        Collections.reverse(listScorDocs);
        
        return listScorDocs;
        
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
    
}
