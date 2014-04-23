/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.recom.impl.recommender;

import es.uam.eps.bmi.recom.exceptions.GenericRecommendationException;
import es.uam.eps.bmi.recom.model.DataModel;
import es.uam.eps.bmi.recom.recommender.Recommender;

/**
 *
 * @author uam
 */
public abstract class AbstractRecommender implements Recommender{
    
    private final DataModel dataModel;
    
    protected AbstractRecommender(DataModel dataModel) throws GenericRecommendationException{
        if (dataModel == null) throw new GenericRecommendationException("dataModel is null.");
        this.dataModel = dataModel;
    }

    @Override
    public DataModel getDataModel() {
        return dataModel;
    }
    
}
