/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.recom.impl.similarity;

/**
 * Clase que representa el par Ítem-Característica acompañado de un peso
 * que mide la importancia de dicha característica.
 * @author Ari Handler - Adrián Lorenzo
 */
public class ItemFeature {
 
    private final long itemID;
    private final long featID;
    private final int weight;

	/**
	 * Construye un par Ítem-Característica asignándole un peso.
	 * @param itemID indetificador del ítem.
	 * @param featID identificador de la característica.
	 * @param weight peso de la característica.
	 */
    public ItemFeature(long itemID, long featID, int weight) {
        this.itemID = itemID;
        this.featID = featID;
        this.weight = weight;
    }

	/**
	 * Obtiene el identificador del ítem.
	 * @return identificador del ítem.
	 */
    public long getItemID() {
        return itemID;
    }

	/** 
	 * Obtiene el identificador de la característica.
	 * @return identificador de la característica.
	 */
    public long getFeatID() {
        return featID;
    }

	/**
	 * Obtiene el peso de la característica para el ítem.
	 * @return peso de la característica.
	 */
    public int getWeight() {
        return weight;
    }

	/**
	 * Determina si dos características son iguales si los identificadores
	 * de ítem coinciden.
	 * @param obj característica con la que comparar.
	 * @return true si son iguales, false en caso contrario.
	 */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ItemFeature other = (ItemFeature) obj;
        return this.itemID == other.itemID;
    }
    
    
}
