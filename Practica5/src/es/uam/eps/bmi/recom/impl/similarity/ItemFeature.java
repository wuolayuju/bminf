/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.recom.impl.similarity;

/**
 *
 * @author uam
 */
public class ItemFeature {
 
    private final long itemID;
    private final long featID;
    private final int weight;

    public ItemFeature(long itemID, long featID, int weight) {
        this.itemID = itemID;
        this.featID = featID;
        this.weight = weight;
    }

    public long getItemID() {
        return itemID;
    }

    public long getFeatID() {
        return featID;
    }

    public int getWeight() {
        return weight;
    }

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
