/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.recom.model;

import es.uam.eps.bmi.recom.exceptions.GenericRecommendationException;
import java.util.List;

/**
 *
 * @author chus
 */
public interface DataModel {
    
    List<Long> getUserIDs();
    
    List<Preference> getPreferencesFromUser(long userID) throws GenericRecommendationException;
    
    List<Long> getItemIDsFromUser(long userID) throws GenericRecommendationException;
    
    List<Long> getItemIDs();
    
    List<Preference> getPreferencesForItem(long itemID);
    
    float getPreferenceValue(long userID, long itemID) throws GenericRecommendationException;
    
    int getNumItems();
    
    int getNumUsers();
    
    int getNumUsersWithPreferenceFor(long itemID);
    
    int getNumUsersWithPreferenceFor(long itemID1, long itemID2);
    
    void setPreference(long userID, long itemID, float value) throws GenericRecommendationException;
    
    void removePreference(long userID, long itemID) throws GenericRecommendationException;
    
    float getMaxPreference();
    
    float getMinPreference();
}
