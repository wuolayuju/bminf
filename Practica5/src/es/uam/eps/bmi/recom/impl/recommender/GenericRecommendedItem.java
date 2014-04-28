/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.recom.impl.recommender;

import es.uam.eps.bmi.recom.recommender.RecommendedItem;

/**
 *
 * @author chus
 */
public class GenericRecommendedItem implements RecommendedItem{

    private long itemID;
    private float value;

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
