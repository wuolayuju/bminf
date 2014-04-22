/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.recom.recommender;

import java.util.List;

/**
 *
 * @author uam
 */
public interface Recommender {
    List<RecommendedItem> recommend(long userID, int top);
    
    float estimatePreference(long userID, long itemID);
}
