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
 * Clase que permite conocer estadísticas de precisión de un determinado
 * motor de recomendación.
 * @author Ari Handler - Adrián Lorenzo
 */
public class MetricsEvaluator {
    private Recommender recommender;
    
	/**
	 * Construye el evaluador con el recomendador que se quiere medir.
	 * @param recommender recomendador que se quiere evaluar.
	 * @throws GenericRecommendationException  si el recomendador no existe.
	 */
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
    
	/**
	 * Calcula la métrica MAE dado un porcentaje de datos que se usarán como
	 * conjunto de test.
	 * @param test porcentaje de test.
	 * @return valor MAE del recomendador.
	 * @throws GenericRecommendationException si ocurre algun error con el recomendador.
	 */
    public double evaluateMAE(double test) throws GenericRecommendationException {
        List<Long> userIDs = recommender.getDataModel().getUserIDs();
        
        double sumTotalMae = 0.0;
        for (Long u : userIDs) {
			double mae = evaluateMAEUser(u, test);
			System.out.println("MAE("+u+") = "+mae);
            sumTotalMae += mae;
        }
        
        return sumTotalMae / recommender.getDataModel().getNumUsers();
    }
	
	private double evaluateRMSEUser(long userID, double test) throws GenericRecommendationException {
        double train = 1.0 - test;
        
        List<Preference> prefs = recommender.getDataModel().getPreferencesFromUser(userID);
        int totalPrefs = prefs.size();
        prefs = prefs.subList((int) (prefs.size() * train), prefs.size());
        
        double sumMAE = 0.0;
        for (Preference p : prefs) {
            double estimated = recommender.estimatePreference(userID, p.getItemID(), train);
            float real = recommender.getDataModel().getPreferenceValue(userID, p.getItemID());
            sumMAE += Math.pow(estimated - real, 2);
        }
        
        return Math.sqrt(sumMAE / (totalPrefs - prefs.size()));
	}
	
	/**
	 * Calcula la métrica RMSE dado un porcentaje de datos que se usarán como
	 * conjunto de test.
	 * @param test porcentaje de test.
	 * @return valor RMSE del recomendador.
	 * @throws GenericRecommendationException si ocurre algun error con el recomendador.
	 */
	public double evaluateRMSE(double test) throws GenericRecommendationException {
		List<Long> userIDs = recommender.getDataModel().getUserIDs();
        
        double sumTotalRMSE = 0.0;
        for (Long u : userIDs) {
			double rmse = evaluateRMSEUser(u, test);
			System.out.println("RMSE("+u+") = "+rmse);
            sumTotalRMSE += rmse;
        }
        
        return sumTotalRMSE / recommender.getDataModel().getNumUsers();
	}
}
