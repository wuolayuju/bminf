/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.recom.impl.model;

import es.uam.eps.bmi.recom.model.DataModel;
import es.uam.eps.bmi.recom.model.Preference;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author chus
 */
public class FileDataModel implements DataModel{

    private HashMap<Long, List<Preference>> usersPreferences;
    
    private File dataFile;
    private char delimiter;
    
    private List<Long> itemIDs;
    
    private float max_preference = Float.MIN_VALUE;
    private float min_preference = Float.MAX_VALUE;

    public FileDataModel(File dataFile) throws IOException {
        this(dataFile, '\t');
    }
    
    public FileDataModel(File dataFile, char delimiter) throws IOException {
        this.dataFile = dataFile;
        
        this.usersPreferences = new HashMap<>();
        this.delimiter = delimiter;
        
        this.itemIDs = new ArrayList<>();
        
        this.max_preference = Float.MIN_VALUE;
        this.min_preference = Float.MAX_VALUE;
        
        buildModel(dataFile, delimiter);
    }
    
    protected void buildModel(File dataFile, char delimiter) throws FileNotFoundException, IOException {
        
        BufferedReader br = new BufferedReader(new FileReader(dataFile));
        
        String line = br.readLine(); // Headers
        
        while((line = br.readLine())!= null) {
            StringTokenizer st = new StringTokenizer(line);
            long userID = Long.parseLong(st.nextToken());
            long itemID = Long.parseLong(st.nextToken());
            float ratValue = Float.parseFloat(st.nextToken());
            
            if (ratValue > this.max_preference) this.max_preference = ratValue;
            if (ratValue < this.min_preference) this.min_preference = ratValue;
            
            if (usersPreferences.containsKey(userID)) {
                usersPreferences.get(userID).add(new GenericPreference(userID, itemID, ratValue));
            } else {
                ArrayList<Preference> prefs = new ArrayList<>();
                prefs.add(new GenericPreference(userID, itemID, ratValue));
                usersPreferences.put(userID, prefs);
            }
            
            if (!itemIDs.contains(itemID)) itemIDs.add(itemID);
        }
    }
    
    @Override
    public List<Long> getUserIDs() {
        return new ArrayList<>(usersPreferences.keySet());
    }

    @Override
    public List<Preference> getPreferencesFromUser(long userID) {
        return usersPreferences.get(userID);
    }

    @Override
    public List<Long> getItemIDsFromUser(long userID) {
        List<Preference> listPrefs = this.getPreferencesFromUser(userID);
        List<Long> listIds = new ArrayList<>();
        for (Preference p : listPrefs) {
            listIds.add(p.getItemID());
        }
        
        return listIds;
    }

    @Override
    public List<Long> getItemIDs() {
        return itemIDs;
    }

    @Override
    public List<Preference> getPreferencesForItem(long itemID) {
        List<Preference> list = new ArrayList<>();
        for (Long userID : usersPreferences.keySet()) {
            for (Preference p : usersPreferences.get(userID)) {
                if (p.getItemID() == itemID)
                    list.add(p);
            }
        }
        return list;
    }

    @Override
    public float getPreferenceValue(long userID, long itemID) {
        for (Preference p : usersPreferences.get(userID)) {
            if (p.getItemID() == itemID)
                return p.getValue();
        }
        return -1;
    }

    @Override
    public int getNumItems() {
        return itemIDs.size();
    }

    @Override
    public int getNumUsers() {
        return usersPreferences.keySet().size();
    }

    @Override
    public int getNumUsersWithPreferenceFor(long itemID) {
        int total = 0;
        for (Long userID : usersPreferences.keySet()) {
            for (Preference p : usersPreferences.get(userID)) {
                if (p.getItemID() == itemID)
                    total++;
            }
        }
        return total;
    }

    @Override
    public int getNumUsersWithPreferenceFor(long itemID1, long itemID2) {
        int total = 0;
        for (Long userID : usersPreferences.keySet()) {
            boolean found = false;
            for (Preference p : usersPreferences.get(userID)) {
                if (p.getItemID() == itemID1 || p.getItemID() == itemID2)
                    if (found) total++;
            }
        }
        return total;
    }

    @Override
    public void setPreference(long userID, long itemID, float value) {
        List<Preference> prefs = usersPreferences.get(userID);
        for (Preference p : prefs) {
            if (p.getItemID() == itemID) {
                p.setValue(value);
                return;
            }
        }
    }

    @Override
    public void removePreference(long userID, long itemID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public float getMaxPreference() {
        return this.max_preference;
    }

    @Override
    public float getMinPreference() {
        return this.min_preference;
    }
    
}
