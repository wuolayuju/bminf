package es.uam.eps.bmi.recom.impl.neighborhood;

import es.uam.eps.bmi.recom.exceptions.GenericRecommendationException;
import es.uam.eps.bmi.recom.model.DataModel;
import es.uam.eps.bmi.recom.neighborhood.UserNeighborhood;
import es.uam.eps.bmi.recom.similarity.UserSimilarity;

/**
 * Clase abstracta que define la creación de un proveedor de vecinos próximos
 * basándose en un modelo de datos y una función de similitud entre usuarios.
 * @author Ari Handler - Adrián Lorenzo
 */
public abstract class AbstractUserNeighborhood implements UserNeighborhood{

    private final UserSimilarity userSimilarity;
    private final DataModel dataModel;

	/**
	 * Asigna al proveedor de vecinos próximos el modelo de datos y la función
	 * de similitud entre usuarios de la que se hará uso.
	 * @param userSimilarity función de similitud.
	 * @param dataModel modelo de datos.
	 * @throws GenericRecommendationException si la función de similitud o el
	 * modelo de datos no existen.
	 */
    public AbstractUserNeighborhood(UserSimilarity userSimilarity, DataModel dataModel) throws GenericRecommendationException{
        if (userSimilarity == null) throw new GenericRecommendationException("userSimilarity is null.");
        if (dataModel == null) throw new GenericRecommendationException("dataModel is null.");
        this.userSimilarity = userSimilarity;
        this.dataModel = dataModel;
    }

	/**
	 * Obtiene la función de similitud.
	 * @return función de similitud entre usuarios.
	 */
    public UserSimilarity getUserSimilarity() {
        return userSimilarity;
    }

	/**
	 * Obtiene el modelo de datos.
	 * @return modelo de datos.
	 */
    public DataModel getDataModel() {
        return dataModel;
    }
        
}
