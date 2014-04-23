/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.recom.test;

import es.uam.eps.bmi.recom.exceptions.GenericRecommendationException;
import es.uam.eps.bmi.recom.impl.model.FileDataModel;
import es.uam.eps.bmi.recom.impl.neighborhood.KNNUserNeighborhood;
import es.uam.eps.bmi.recom.impl.similarity.CosineSimilarity;
import es.uam.eps.bmi.recom.model.DataModel;
import es.uam.eps.bmi.recom.neighborhood.UserNeighborhood;
import es.uam.eps.bmi.recom.similarity.UserSimilarity;
import java.io.File;
import java.io.IOException;
import java.util.List;
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
        
        String usage = "java es.uam.eps.bmi.recom.test.ContentBasedRecommenderTest"
                 + " [-ratings RATINGS_PATH] [-tags MOVIE_TAGS_PATH] [-userTags USER_TAGS_PATH]\n\n"
                 + "RATINGS_PATH fichero de ratings, MOVIE_TAGS_PATH fichero de tags para los items, USER_TAGS_PATH fichero de tags de usuarios a items.";
        
        String ratingsFile = null;
        
        for(int i=0;i<args.length;i++) {
            if(args[i].compareTo("-ratings")==0) {
                ratingsFile = args[i+1];
                i++;
            }
        }
        
        try {
            DataModel dataModel = new FileDataModel(new File(ratingsFile));
            UserSimilarity userSimilarity = new CosineSimilarity(dataModel);
            UserNeighborhood neighborhood = new KNNUserNeighborhood(dataModel.getNumUsers(), userSimilarity, dataModel);
            List<Long> userNeighborhood = neighborhood.getUserNeighborhood(1);
            for (Long id : userNeighborhood) {
                System.out.print(id + ", ");
            }
        } catch (IOException | GenericRecommendationException ex) {
            Logger.getLogger(KNNTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
