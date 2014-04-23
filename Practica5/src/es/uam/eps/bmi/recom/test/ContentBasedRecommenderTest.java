/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.recom.test;

import es.uam.eps.bmi.recom.exceptions.GenericRecommendationException;
import es.uam.eps.bmi.recom.impl.model.FileDataModel;
import es.uam.eps.bmi.recom.impl.recommender.ContentBasedRecommender;
import es.uam.eps.bmi.recom.impl.recommender.UserProfiler;
import es.uam.eps.bmi.recom.impl.similarity.ItemFeature;
import es.uam.eps.bmi.recom.impl.similarity.UserItemSimilarity;
import es.uam.eps.bmi.recom.model.DataModel;
import es.uam.eps.bmi.recom.recommender.RecommendedItem;
import es.uam.eps.bmi.recom.recommender.Recommender;
import es.uam.eps.bmi.recom.similarity.VectorSimilarity;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author chus
 */
public class ContentBasedRecommenderTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        String usage = "java es.uam.eps.bmi.recom.test.ContentBasedRecommenderTest"
                 + " [-ratings RATINGS_PATH] [-tags MOVIE_TAGS_PATH] [-userTags USER_TAGS_PATH]\n\n"
                 + "RATINGS_PATH fichero de ratings, MOVIE_TAGS_PATH fichero de tags para los items, USER_TAGS_PATH fichero de tags de usuarios a items.";
        
        String dataPath = null;
        
        for(int i=0;i<args.length;i++) {
            if(args[i].compareTo("-data")==0) {
                dataPath = args[i+1];
                i++;
            }
        }
        
        if (dataPath == null) {
            System.err.println("Usage: " + usage);
            System.exit(0);
        }
        
        String ratingsFile = dataPath + "user_ratedmovies.dat";
        String tagsFile = dataPath + "movie_tags.dat";
        String userTagsFile = dataPath + "user_taggedmovies.dat";
        
        try {
            System.out.print("Enter the ID of the user: ");
            Scanner in = new Scanner(System.in);
            long userID = in.nextLong();
            
            System.out.println("\nProfile of the user " + userID + ":");
            for (ItemFeature itf : UserProfiler.getProfileFromUser(userID, new File(userTagsFile))) {
                System.out.println("Item " + itf.getItemID() + " Tag " + itf.getFeatID());
            }
            
            DataModel dataModel = new FileDataModel(new File(ratingsFile));
            VectorSimilarity similarity = new UserItemSimilarity(new File(tagsFile), dataModel);
            Recommender contentBasedRecommender = new ContentBasedRecommender(dataModel, similarity);
            List<RecommendedItem> toRecommend = contentBasedRecommender.recommend(userID, 10);
            
            System.out.println("\n10 top recommended items for this user:");
            int i = 1;
            for (RecommendedItem item : toRecommend) {
                System.out.println(i++ + ". " + item.getItemID() + ", val = " + item.getValue());
            }
        } catch (IOException | GenericRecommendationException ex) {
            Logger.getLogger(ContentBasedRecommenderTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
}
