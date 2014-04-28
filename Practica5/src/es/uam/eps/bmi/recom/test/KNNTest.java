package es.uam.eps.bmi.recom.test;

import es.uam.eps.bmi.recom.exceptions.GenericRecommendationException;
import es.uam.eps.bmi.recom.impl.model.FileDataModel;
import es.uam.eps.bmi.recom.impl.neighborhood.KNNUserNeighborhood;
import es.uam.eps.bmi.recom.impl.recommender.GenericUserBasedRecommender;
import es.uam.eps.bmi.recom.impl.similarity.CosineSimilarity;
import es.uam.eps.bmi.recom.model.DataModel;
import es.uam.eps.bmi.recom.model.Preference;
import es.uam.eps.bmi.recom.neighborhood.UserNeighborhood;
import es.uam.eps.bmi.recom.recommender.RecommendedItem;
import es.uam.eps.bmi.recom.recommender.Recommender;
import es.uam.eps.bmi.recom.similarity.UserSimilarity;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author uam
 */
public class KNNTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        String usage = "java es.uam.eps.bmi.recom.test.KNNTest"
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
            
            System.out.print("Enter the ID of the user: ");
            Scanner in = new Scanner(System.in);
            long userID = in.nextLong();
            
            System.out.println("\nProfile of the user " + userID + ":");
            for (Preference p : dataModel.getPreferencesFromUser(userID)) {
                System.out.println("Item " + p.getItemID() + " Rating " + p.getValue());
            }
            
            //System.out.println("Setting Similarity...");
            UserSimilarity userSimilarity = new CosineSimilarity(dataModel);
            //System.out.println("Calculating neighbours...");
            UserNeighborhood neighborhood = new KNNUserNeighborhood(5, userSimilarity, dataModel);
            Recommender recommender = new GenericUserBasedRecommender(dataModel, neighborhood, userSimilarity);
            //System.out.println("Calculating Recommendations...");
            List<RecommendedItem> recommendations = recommender.recommend(userID, 10);
            
            System.out.println("\n10 top recommended items for this user:");
            int i = 1;
            for (RecommendedItem item : recommendations) {
                System.out.println(i++ + ". " + item.getItemID() + ", val = " + item.getValue());
            }
        } catch (IOException | GenericRecommendationException ex) {
            Logger.getLogger(KNNTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
