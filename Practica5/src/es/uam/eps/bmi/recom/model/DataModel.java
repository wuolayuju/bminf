/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.recom.model;

import java.util.List;

/**
 *
 * @author chus
 */
public interface DataModel {
    
    List<Long> getUserIDs();
    
    List<Preference> getPreferencesFromUser(long userID);
    
    List<Long> getItemIDsFromUser(long userID);
    
    List<Long> getItemIDs();
    
    List<Preference> getPreferencesForItem(long itemID);
    
    float getPreferenceValue(long userID, long itemID);
    
    int getNumItems();
    
    int getNumUsers();
    
    int getNumUsersWithPreferenceFor(long itemID);
    
    int getNumUsersWithPreferenceFor(long itemID1, long itemID2);
    
    void setPreference(long userID, long itemID, float value);
    
    void removePreference(long userID, long itemID);
    
    float getMaxPreference();
    
    float getMinPreference();
}
