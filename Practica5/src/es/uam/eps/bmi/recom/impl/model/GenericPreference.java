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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public long getItemID() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public float getValue() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setValue(float value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
