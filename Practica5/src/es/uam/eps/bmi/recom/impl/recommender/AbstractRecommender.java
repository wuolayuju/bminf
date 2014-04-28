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
 * Clase abstracta que determina como se debe crear un recomendador dado un
 * modelo de datos.
 * @author Ari Handler - Adrián Lorenzo
 */
public abstract class AbstractRecommender implements Recommender{
    
    private final DataModel dataModel;
    
	/**
	 * Asigna a este recomendador un modelo de datos.
	 * @param dataModel el modelo de datos
	 * @throws GenericRecommendationException si el modelo de datos no existe.
	 */
    protected AbstractRecommender(DataModel dataModel) throws GenericRecommendationException{
        if (dataModel == null) throw new GenericRecommendationException("dataModel is null.");
        this.dataModel = dataModel;
    }

    @Override
    public DataModel getDataModel() {
        return dataModel;
    }
    
}
