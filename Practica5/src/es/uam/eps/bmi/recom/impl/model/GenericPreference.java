/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.recom.impl.model;

import es.uam.eps.bmi.recom.model.Preference;

/**
 * Modela una preferencia básica de un usuario hacia un ítem.
 * @author Ari Handler - Adrián Lorenzo
 */
public class GenericPreference implements Preference{

    private final long userID;
    private final long itemID;
    private float value;

	/**
	 * Construye la preferencia de un usuario hacia un ítem en un determinado grado.
	 * @param userID identificador del usuario.
	 * @param itemID identificador del ítem.
	 * @param value valor de preferencia.
	 */
    public GenericPreference(long userID, long itemID, float value) {
        this.userID = userID;
        this.itemID = itemID;
        this.value = value;
    }
    
    @Override
    public long getUserID() {
        return userID;
    }

    @Override
    public long getItemID() {
        return itemID;
    }

    @Override
    public float getValue() {
        return value;
    }

    @Override
    public void setValue(float value) {
        this.value = value;
    }

	/**
	 * Determina si una preferencia es igual a otra si tanto el usuario como el ítem coinciden.
	 * @param obj preferencia con la que comparar.
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
        final GenericPreference other = (GenericPreference) obj;
        if (this.userID != other.userID) {
            return false;
        }
        return this.itemID == other.itemID;
    }
    
    
    
}
