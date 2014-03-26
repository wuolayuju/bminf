/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.search.ranking.graph.aggregation;

import es.uam.eps.bmi.search.ScoredTextDocument;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author uam
 */
public class ScoreNormalizer {

    List<ScoredTextDocument> data;

    public ScoreNormalizer(List<ScoredTextDocument> data) {
        this.data = data;
    }
    
    public List<ScoredTextDocument> normalizeZScore() {
        double mean = getMean();
        double sd = getSD(mean);
        for (ScoredTextDocument doc : data) {
            doc.setScore((doc.getScore() - mean) / sd);
        }
        
        return data;
    }
    
    public List<ScoredTextDocument> normalizeMinMax() {
        Comparator comparator = new ScoreNormalizer.ScoredTextDocumentComparator();
        // s'(d) = (s(d) - min{s(d')}) / (max{s(d')} - min{s(d')})
        double min = Collections.min(data, comparator).getScore();
        double max = Collections.max(data, comparator).getScore();
        double denominator = max - min;
        for (ScoredTextDocument doc : data) {
            double normScore = (doc.getScore() - min) / denominator;
            doc.setScore(normScore);
        }
        return data;
    }
 
    private double getMean() {
        double sum = 0.0;
        for(ScoredTextDocument doc : data) {
            sum += doc.getScore();
        }
        return sum / data.size();
    }
    
    private double getSD(double mean) {
        double sd = 0.0;
        for (ScoredTextDocument doc : data) {
            sd += (Math.pow(doc.getScore() - mean, 2));
        }
        return Math.sqrt(sd / data.size());
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
