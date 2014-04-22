/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.recom.impl.model;

import es.uam.eps.bmi.recom.model.Preference;

/**
 *
 * @author chus
 */
public class GenericPreference implements Preference{

    private long userID;
    private long itemID;
    private float value;

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
