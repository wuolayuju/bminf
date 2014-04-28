package es.uam.eps.bmi.recom.recommender;

import es.uam.eps.bmi.recom.exceptions.GenericRecommendationException;
import es.uam.eps.bmi.recom.model.DataModel;
import java.util.List;

/**
 * Interfaz común para todas las clases de recomendador.
 * @author Ari Handler - Adrián Lorenzo
 */
public interface Recommender {
	/**
	 * Calcula el top de ítems que el recomendador sugiere a un determinado usuario.
	 * @param userID identificador del usuario.
	 * @param top numero máximo de resultados devueltos.
	 * @return lista de {@link RecommendedItem} que representan los ítems recomendados.
	 * @throws GenericRecommendationException si no existe el usuario.
	 */
    List<RecommendedItem> recommend(long userID, int top) throws GenericRecommendationException;
    
	/**
	 * Obtiene el valor estimado de preferencia de un usuario hacia un ítem.
	 * @param userID identificador del usuario.
	 * @param itemID identificador del ítem.
	 * @return preferencia estimada.
	 * @throws GenericRecommendationException si no existe el usuario.
	 */
    double estimatePreference(long userID, long itemID) throws GenericRecommendationException;
    
	/**
	 * Igual que {@link #estimatePreference(long, long) } pero deifiniendo el
	 * porcentaje de datos de ratings a tener en cuenta a la hora de calcular
	 * la preferencia.
	 * @param userID identificador del usuario.
	 * @param itemID identificador del ítem.
	 * @param train porcentaje de datos a utilizar.
	 * @return preferencia estimada.
	 * @throws GenericRecommendationException si no existe el usuario.
	 */
    double estimatePreference(long userID, long itemID, double train) throws GenericRecommendationException;
    
	/**
	 * Obtiene el modelo de datos que se está utilizando.
	 * @return modelo de datos.
	 */
    DataModel getDataModel();
}
