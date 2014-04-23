/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.recom.impl.neighborhood;

import es.uam.eps.bmi.recom.exceptions.GenericRecommendationException;
import es.uam.eps.bmi.recom.model.DataModel;
import es.uam.eps.bmi.recom.neighborhood.UserNeighborhood;
import es.uam.eps.bmi.recom.similarity.UserSimilarity;

/**
 *
 * @author uam
 */
public abstract class AbstractUserNeighborhood implements UserNeighborhood{

    private final UserSimilarity userSimilarity;
    private final DataModel dataModel;

    public AbstractUserNeighborhood(UserSimilarity userSimilarity, DataModel dataModel) throws GenericRecommendationException{
        if (userSimilarity == null) throw new GenericRecommendationException("userSimilarity is null.");
        if (dataModel == null) throw new GenericRecommendationException("dataModel is null.");
        this.userSimilarity = userSimilarity;
        this.dataModel = dataModel;
    }

    public UserSimilarity getUserSimilarity() {
        return userSimilarity;
    }

    public DataModel getDataModel() {
        return dataModel;
    }
        
}
