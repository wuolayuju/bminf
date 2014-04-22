/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.recom.impl.similarity;

import es.uam.eps.bmi.recom.model.DataModel;
import es.uam.eps.bmi.recom.similarity.VectorSimilarity;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 *
 * @author uam
 */
public class UserItemSimilarity implements VectorSimilarity{

    private File itemFile;
    private HashMap<Long, ItemFeature> itemFeatures;
    private DataModel dataModel;

    public UserItemSimilarity(File itemFile, DataModel dataModel) {
        this.itemFile = itemFile;
        this.dataModel = dataModel;
    }
    
    private void buildSimilarity() throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(itemFile));
        
        String line = br.readLine(); // Headers
        
        while((line = br.readLine())!= null) {
            StringTokenizer st = new StringTokenizer(line);
            long itemID = Long.parseLong(st.nextToken());
            long featID = Long.parseLong(st.nextToken());
            int weight = Integer.parseInt(st.nextToken());
            
            if (itemFeatures.containsKey(itemID)) {
                
            } else {
                
            }
        }
    }
    
    @Override
    public double vectorSimilarity(long userID, long itemID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
