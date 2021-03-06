/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.search.ranking.graph.aggregation;

import es.uam.eps.bmi.search.ScoredTextDocument;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase que implmenta un agregador de ránkings que realiza una suma pesada
 * de cada uno de los resultados pasados y devuelve un ránking combinado.
 * @author Ari Handler y Adrián Lorenzo
 */
public class WeightedSumRankAggregator {
    
    private List<List<ScoredTextDocument>> rankings;
    private List<Double> weights;

    /**
     * Instancia el agregador de ránkings dados n ránkings y los correspondientes
     * pesos que se aplicarán a la hora de combinarlos. <br>
     * Es importante recalcar que es necesario que el número de ránkings y 
     * el número de pesos coincidan.
     * @param rankings ránkings que se quieren combinar
     * @param weights pesos que se aplicarán a cada ránking, en el mismo orden
     * que el argumento anterior
     * @throws AggregatorException si se produce algún error con los argumentos
     */
    public WeightedSumRankAggregator(List<List<ScoredTextDocument>> rankings, List<Double> weights) throws AggregatorException {
        String excCause = "";
        // Las listas deben contener elementos
        if (rankings.isEmpty()) excCause = "rankings";
        if (weights.isEmpty()) excCause = "weights";
        if (!excCause.isEmpty()) throw new AggregatorException("List " + excCause + " cannot be empty.");
        
        if (rankings.size() != weights.size())
            throw new AggregatorException("List sizes of rankings and weights differ.");
        
        this.rankings = rankings;
        
        normalizeRankings();
        this.weights = weights;
    }

    /**
     * Devuelve los ránkings que se combinan
     * @return lista de ránkings original
     */
    public List<List<ScoredTextDocument>> getRankings() {
        return rankings;
    }

    /**
     * Devuelve los pesos
     * @return lista de pesos
     */
    public List<Double> getWeights() {
        return weights;
    }
    
    private void normalizeRankings() {
        for (List<ScoredTextDocument> list : rankings) {
            ScoreNormalizer norm = new ScoreNormalizer(list);
            list = norm.normalizeZScore();
        }
    }
    
    /**
     * Realiza la agregación de los ránkings mediante la suma pesada de 
     * cada uno de los documentos y los ordena en un orden descendiente
     * @return ránking combinado y pesado
     */
    public List<ScoredTextDocument> aggregateRankings() {
        
        List<ScoredTextDocument> listScorDocs = new ArrayList<>();
        
        /*
         Por cada documento en cada lista de rankings, obtenemos la puntuacion
        ponderada dado el vector de pesos
        */
        for (int i = 0; i < rankings.size() ; i++) {
            List<ScoredTextDocument> list = rankings.get(i); // Por cada lista
            for (ScoredTextDocument doc : list) {
                // Comprobacion de que el documento no este ya ponderado
                if (listScorDocs.contains(doc)) continue;
                
                // Cada documento dentro del ranking
                // Puntuacion ponderada para esta lista
                double weightedScore = doc.getScore() * weights.get(i);
                // Por cada otra lista de rankings
                for (int j = i+1; j < rankings.size() ; j++) {
                    List<ScoredTextDocument> otherList = rankings.get(j);
                    
                    // Si existe el documento en la lista, obtenemos su puntuacion
                    // En caso contrario, el valor es 0
                    double actualScore = otherList.contains(doc) ? 
                            otherList.get(otherList.indexOf(doc)).getScore() :
                            -2;
                    
                    weightedScore += actualScore * weights.get(j);
                }
                
                ScoredTextDocument weightedDoc = new ScoredTextDocument(doc.getDocumentId(), weightedScore);
                
                listScorDocs.add(weightedDoc);
            }
        }
   
        Collections.sort(listScorDocs, new WeightedSumRankAggregator.ScoredTextDocumentComparator());
        
        return listScorDocs;
    }
    
    private class ScoredTextDocumentComparator implements Comparator<ScoredTextDocument> {

        @Override
        public int compare(ScoredTextDocument o1, ScoredTextDocument o2) {
            if (o1.getScore() < o2.getScore())
                return 1;
            if (o1.getScore() > o2.getScore())
                return -1;
            return 0;
        }
    }

    public static void main(String[] args) {
        
        List<ScoredTextDocument> rank1 = new ArrayList<>();
        rank1.add(new ScoredTextDocument("doc1", 0.9));
        rank1.add(new ScoredTextDocument("doc2", 0.7));
        rank1.add(new ScoredTextDocument("doc3", 0.1));
        
        List<ScoredTextDocument> rank2 = new ArrayList<>();
        rank2.add(new ScoredTextDocument("doc4", 0.8));
        rank2.add(new ScoredTextDocument("doc1", 0.6));
        rank2.add(new ScoredTextDocument("doc5", 0.3));
        rank2.add(new ScoredTextDocument("doc6", 0.2));
        
        List<ScoredTextDocument> rank3 = new ArrayList<>();
        rank3.add(new ScoredTextDocument("doc2", 0.7));
        rank3.add(new ScoredTextDocument("doc3", 0.3));
        rank3.add(new ScoredTextDocument("doc6", 0.1));
        
        List<List<ScoredTextDocument>> listRanks = new ArrayList<>();
        listRanks.add(rank1);listRanks.add(rank2);listRanks.add(rank3);
        
        List<Double> weights = new ArrayList<>();
        weights.add(0.3);weights.add(0.5);weights.add(0.2);
        
        WeightedSumRankAggregator aggre = null;
        try {
            aggre = new WeightedSumRankAggregator(listRanks, weights);
        } catch (AggregatorException ex) {
            Logger.getLogger(WeightedSumRankAggregator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (aggre == null) {
            System.err.println("Error in aggregation");
            System.exit(0);
        }
        
        int pos = 1;
        for (ScoredTextDocument doc : aggre.aggregateRankings()) {
            System.out.println(pos++ + ". " + doc.getDocumentId() + " = " + doc.getScore());
        }
    }
    
}
