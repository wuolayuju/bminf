/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.search.ranking.graph.aggregation;

import es.uam.eps.bmi.search.ScoredTextDocument;
import java.util.List;

/**
 *
 * @author uam
 */
public class WeightedSumRankAggregator {
    
    private List<List<ScoredTextDocument>> rankings;
    private List<Double> weights;

    public WeightedSumRankAggregator(List<List<ScoredTextDocument>> rankings, List<Double> weights) throws AggregatorException {
        String excCause = "";
        // Las listas deben contener elementos
        if (rankings.isEmpty()) excCause = "rankings";
        if (weights.isEmpty()) excCause = "weights";
        if (!excCause.isEmpty()) throw new AggregatorException("List " + excCause + " cannot be empty.");
        
        if (rankings.size() != weights.size())
            throw new AggregatorException("List sizes of rankings and weights differ.");
        
        this.rankings = rankings;
        this.weights = weights;
    }

    public List<List<ScoredTextDocument>> getOriginalRankings() {
        return rankings;
    }

    public List<Double> getWeights() {
        return weights;
    }
    
    public List<ScoredTextDocument> aggregateRankings() {
        
        
        
        return null;
    }
}
