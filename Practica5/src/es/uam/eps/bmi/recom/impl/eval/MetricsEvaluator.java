/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.recom.impl.eval;

import es.uam.eps.bmi.recom.exceptions.GenericRecommendationException;
import es.uam.eps.bmi.recom.model.Preference;
import es.uam.eps.bmi.recom.recommender.Recommender;
import java.util.List;

/**
 *
 * @author uam
 */
public class MetricsEvaluator {
    private Recommender recommender;
    
    public MetricsEvaluator(Recommender recommender) throws GenericRecommendationException {
        if (recommender == null) throw new GenericRecommendationException("recommender is null.");
        this.recommender = recommender;
    }
    
    private double evaluateMAEUser(long userID, double test) throws GenericRecommendationException {
        double train = 1.0 - test;
        
        List<Preference> prefs = recommender.getDataModel().getPreferencesFromUser(userID);
        int totalPrefs = prefs.size();
        prefs = prefs.subList((int) (prefs.size() * train), prefs.size());
        
        double sumMAE = 0.0;
        for (Preference p : prefs) {
            double estimated = recommender.estimatePreference(userID, p.getItemID(), train);
            float real = recommender.getDataModel().getPreferenceValue(userID, p.getItemID());
            sumMAE += Math.abs(estimated - real);
        }
        
        return sumMAE / (totalPrefs - prefs.size());
    }
    
    public double evaluateMAE(double test) throws GenericRecommendationException {
        List<Long> userIDs = recommender.getDataModel().getUserIDs();
        
        double sumTotalMae = 0.0;
        for (Long u : userIDs) {
            sumTotalMae += evaluateMAEUser(u, test);
        }
        
        return sumTotalMae / recommender.getDataModel().getNumUsers();
    }
}
