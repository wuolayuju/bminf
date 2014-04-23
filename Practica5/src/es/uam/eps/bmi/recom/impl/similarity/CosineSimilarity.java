/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.recom.impl.similarity;

import es.uam.eps.bmi.recom.exceptions.GenericRecommendationException;
import es.uam.eps.bmi.recom.impl.model.GenericPreference;
import es.uam.eps.bmi.recom.model.DataModel;
import es.uam.eps.bmi.recom.model.Preference;
import es.uam.eps.bmi.recom.similarity.UserSimilarity;
import java.util.List;

/**
 *
 * @author uam
 */
public class CosineSimilarity implements UserSimilarity{

    private final DataModel dataModel;

    public CosineSimilarity(DataModel dataModel) throws GenericRecommendationException{
        if (dataModel == null) throw new GenericRecommendationException("dataModel is null.");
        this.dataModel = dataModel;
    }
    
    @Override
    public double userSimilarity(long userID1, long userID2) throws GenericRecommendationException {
        List<Preference> prefsU1 = dataModel.getPreferencesFromUser(userID1);
        List<Preference> prefsU2 = dataModel.getPreferencesFromUser(userID2);
        
        float numerator = 0.0F;
        for (Preference pU1 : prefsU1) {
            pU1 = new GenericPreference(userID2, pU1.getItemID(), pU1.getValue());
            
            if ( !prefsU2.contains(pU1) ) continue; 
            numerator += pU1.getValue() * prefsU2.get(prefsU2.indexOf(pU1)).getValue();
            
            pU1 = new GenericPreference(userID1, pU1.getItemID(), pU1.getValue());
        }
        
        double moduleU1 = computeModuleUser(prefsU1);
        double moduleU2 = computeModuleUser(prefsU2);
        
        return (numerator / (moduleU1 * moduleU2));
    }
    
    private double computeModuleUser(List<Preference> preferences) {
        float sum = 0.0F;
        for (Preference p : preferences) {
            sum += Math.pow(p.getValue(), 2);
        }
        return Math.sqrt(sum);
    }
    
}
