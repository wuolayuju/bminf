package es.uam.eps.bmi.recom.model;

import es.uam.eps.bmi.recom.exceptions.GenericRecommendationException;
import java.util.List;

/**
 * Esta interfaz modela los métodos básicos que deberá tener el controlador
 * de la fuente de datos que se utilizarán para operar con los sistemas
 * de recomendación de la aplicación.<br>
 * Como tipos de datos se utilizan números Long para los identificadores tanto
 * de usuario como de ítem y {@link Preference} para las preferencias de usuario.
 * 
 * @author Ari Handler - Adrián Lorenzo
 */
public interface DataModel {
    
	/**
	 * Obtiene la lista de los IDs de todos los usuarios.
	 * @return lista de long de identificadores de usuarios.
	 */
    List<Long> getUserIDs();
    
	/**
	 * Obtiene la lista de preferencias de un determinado usuario.
	 * @param userID identificador del usuario.
	 * @return lista de {@link Preference} del usuario.
	 * @throws GenericRecommendationException si el usuario no existe.
	 */
    List<Preference> getPreferencesFromUser(long userID) throws GenericRecommendationException;
    
	/**
	 * Obtiene los IDs de los ítems por los cuáles el usuario ha mostrado interés.
	 * @param userID identificador del usuario.
	 * @return lista de identificadores de los ítems.
	 * @throws GenericRecommendationException si el usuario no existe.
	 */
    List<Long> getItemIDsFromUser(long userID) throws GenericRecommendationException;
    
	/**
	 * Obtiene la lista completa de identificadores de ítems de la colección.
	 * @return lista de identificadores de ítems.
	 */
    List<Long> getItemIDs();
    
	/**
	 * Obtiene todas las preferencias en las que el ítem en cuestión aparece.
	 * @param itemID identificador del ítem.
	 * @return lista de {@link Preference} para el ítem.
	 */
    List<Preference> getPreferencesForItem(long itemID);
    
	/**
	 * Obtiene el valor de preferencia de un usuario para un determinado ítem.
	 * @param userID identificador del usuario.
	 * @param itemID identificador del ítem.
	 * @return valor de preferencia para ese ítem.
	 * @throws GenericRecommendationException si no existe el usuario.
	 */
    float getPreferenceValue(long userID, long itemID) throws GenericRecommendationException;
    
	/**
	 * Obtiene el número total de ítems en la colección.
	 * @return total de ítems.
	 */
    int getNumItems();
    
	/**
	 * Obtiene el número total de usuarios en la colección.
	 * @return total de usuarios.
	 */
    int getNumUsers();
    
	/**
	 * Obtiene el total de usuarios que han expresado preferencia por un determinado ítem.
	 * @param itemID identificador del ítem
	 * @return número de usuarios.
	 */
    int getNumUsersWithPreferenceFor(long itemID);
    
	/**
	 * Obtiene el total de usuarios que han expresado preferencia por dos determinados ítems.
	 * @param itemID1 identificador de uno de los ítems.
	 * @param itemID2 identificador del otro ítem.
	 * @return número de usuarios.
	 */
    int getNumUsersWithPreferenceFor(long itemID1, long itemID2);
    
	/**
	 * Da una valor de prefencia de un usuario a un ítem.
	 * @param userID identificador del usuario.
	 * @param itemID identificador del ítem.
	 * @param value valor de la preferencia.
	 * @throws GenericRecommendationException si no existe el usuario.
	 */
    void setPreference(long userID, long itemID, float value) throws GenericRecommendationException;
    
	/**
	 * Elimina la preferencia de un usuario a un ítem.
	 * @param userID identificador del usuario.
	 * @param itemID identificador del ítem.
	 * @throws GenericRecommendationException si no existe el usuario o el ítem.
	 */
    void removePreference(long userID, long itemID) throws GenericRecommendationException;
    
	/**
	 * Obtiene el valor más alto de preferencia en la colección.
	 * @return valor de preferencia máximo.
	 */
    float getMaxPreference();
    
	/**
	 * Obtiene el valor más bajo de preferencia en la colección.
	 * @return valor de preferencia mínimo.
	 */
    float getMinPreference();
}
