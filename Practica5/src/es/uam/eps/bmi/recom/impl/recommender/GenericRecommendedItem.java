package es.uam.eps.bmi.recom.impl.recommender;

import es.uam.eps.bmi.recom.recommender.RecommendedItem;

/**
 * Clase que representa un ítem recomendado general.
 * @author Ari Handler - Adrián Lorenzo
 */
public class GenericRecommendedItem implements RecommendedItem{

    private final long itemID;
    private final float value;

	/**
	 * Construye un ítem recomendado asignándole un valor de rating.
	 * @param itemID identificador del ítem.
	 * @param value valor de la recomendación.
	 */
    public GenericRecommendedItem(long itemID, float value) {
        this.itemID = itemID;
        this.value = value;
    }
    
    @Override
    public long getItemID() {
        return itemID;
    }

    @Override
    public float getValue() {
        return value;
    }
    
}
