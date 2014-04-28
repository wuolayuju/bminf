package es.uam.eps.bmi.recom.recommender;

/**
 * Define la interfaz del par Ítem-Valor recomendado para un determinado usuario.<br>
 * Este tipo de objetos es lo que devolverán los recomendadores.
 * @author Ari Handler - Adrián Lorenzo
 */
public interface RecommendedItem {
	/**
	 * Obtiene el identificador de ítem.
	 * @return identificador del ítem.
	 */
    long getItemID();
    
	/**
	 * Obtiene la puntuación estimada del ítem.
	 * @return puntuación estimada.
	 */
    float getValue();
}
