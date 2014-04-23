/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.recom.impl.recommender;

import es.uam.eps.bmi.recom.exceptions.GenericRecommendationException;
import es.uam.eps.bmi.recom.model.DataModel;
import es.uam.eps.bmi.recom.neighborhood.UserNeighborhood;
import es.uam.eps.bmi.recom.recommender.RecommendedItem;
import es.uam.eps.bmi.recom.similarity.UserSimilarity;
import java.util.List;

/**
 *
 * @author uam
 */
public class GenericUserBasedRecommender extends AbstractRecommender{

    private final UserNeighborhood neighborhood;
    private final UserSimilarity similarity;
    
    public GenericUserBasedRecommender(DataModel dataModel, UserNeighborhood neighborhood, UserSimilarity similarity) throws GenericRecommendationException {
        super(dataModel);
        if (neighborhood == null) throw new GenericRecommendationException("neighborhood is null.");
        if (similarity == null) throw new GenericRecommendationException("similarity is null.");
        this.neighborhood = neighborhood;
        this.similarity = similarity;
    }

    @Override
    public List<RecommendedItem> recommend(long userID, int top) throws GenericRecommendationException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double estimatePreference(long userID, long itemID) throws GenericRecommendationException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public UserNeighborhood getNeighborhood() {
        return neighborhood;
    }

    public UserSimilarity getSimilarity() {
        return similarity;
    }
}
