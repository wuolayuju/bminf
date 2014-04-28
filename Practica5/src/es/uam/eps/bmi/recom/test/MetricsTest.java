/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.recom.test;

import es.uam.eps.bmi.recom.exceptions.GenericRecommendationException;
import es.uam.eps.bmi.recom.impl.eval.MetricsEvaluator;
import es.uam.eps.bmi.recom.impl.model.FileDataModel;
import es.uam.eps.bmi.recom.impl.neighborhood.KNNUserNeighborhood;
import es.uam.eps.bmi.recom.impl.recommender.GenericUserBasedRecommender;
import es.uam.eps.bmi.recom.impl.similarity.CosineSimilarity;
import es.uam.eps.bmi.recom.model.DataModel;
import es.uam.eps.bmi.recom.neighborhood.UserNeighborhood;
import es.uam.eps.bmi.recom.recommender.Recommender;
import es.uam.eps.bmi.recom.similarity.UserSimilarity;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author uam
 */
public class MetricsTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String usage = "java es.uam.eps.bmi.recom.test.MetricsTest"
                 + " [-data HETREC_PATH]\n\n"
                 + "HETREC_PATH ruta al repositorio HetRec.";
        
        String ratingsFile = null;
        String dataPath = null;
        
        for(int i=0;i<args.length;i++) {
            if(args[i].compareTo("-data")==0) {
                dataPath = args[i+1];
                i++;
            }
        }
        
        try {
            ratingsFile = dataPath + "user_ratedmovies.dat";
            
            //System.out.println("Building Data Model...");
            DataModel dataModel = new FileDataModel(new File(ratingsFile));
            
            //System.out.println("Setting Similarity...");
            UserSimilarity userSimilarity = new CosineSimilarity(dataModel);
            //System.out.println("Calculating neighbours...");
            UserNeighborhood neighborhood = new KNNUserNeighborhood(3, userSimilarity, dataModel);
            Recommender recommender = new GenericUserBasedRecommender(dataModel, neighborhood, userSimilarity);
            //System.out.println("Calculating Recommendations...");
            MetricsEvaluator metrics = new MetricsEvaluator(recommender);
			//double mae = metrics.evaluateMAE(0.2);
			double rmse = metrics.evaluateRMSE(0.2);
			//System.out.println("MAE = "+mae);
        } catch (IOException | GenericRecommendationException ex) {
            Logger.getLogger(KNNTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
