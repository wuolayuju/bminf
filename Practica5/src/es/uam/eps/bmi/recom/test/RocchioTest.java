/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.recom.test;

import es.uam.eps.bmi.recom.impl.model.FileDataModel;
import es.uam.eps.bmi.recom.impl.recommender.ContentBasedRecommender;
import es.uam.eps.bmi.recom.impl.similarity.UserItemSimilarity;
import es.uam.eps.bmi.recom.model.DataModel;
import es.uam.eps.bmi.recom.recommender.RecommendedItem;
import es.uam.eps.bmi.recom.recommender.Recommender;
import es.uam.eps.bmi.recom.similarity.VectorSimilarity;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author chus
 */
public class RocchioTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        String ratingsFile = "ratingsFile";
        String tagsFile = "tagsFile";
        
        for(int i=0;i<args.length;i++) {
            if(args[i].compareTo("-ratings")==0) {
                ratingsFile = args[i+1];
                i++;
            }
            if(args[i].compareTo("-tags")==0) {
                tagsFile = args[i+1];
                i++;
            }
        }
        
        try {
            DataModel dataModel = new FileDataModel(new File(ratingsFile));
            VectorSimilarity similarity = new UserItemSimilarity(new File(tagsFile), dataModel);
            Recommender contentBasedRecommender = new ContentBasedRecommender(dataModel, similarity);
            List<RecommendedItem> toRecommend = contentBasedRecommender.recommend(75, 3);
            
            int i = 1;
            for (RecommendedItem item : toRecommend) {
                System.out.println(i++ + ". " + item.getItemID() + ", val = " + item.getValue());
            }
        } catch (IOException ex) {
            Logger.getLogger(RocchioTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
}
