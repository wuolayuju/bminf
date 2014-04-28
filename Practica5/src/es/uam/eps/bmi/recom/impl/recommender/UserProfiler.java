/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.recom.impl.recommender;

import es.uam.eps.bmi.recom.impl.similarity.ItemFeature;
import es.uam.eps.bmi.recom.model.Preference;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author chus
 */
public class UserProfiler {

    public static List<ItemFeature> getProfileTagsFromUser(long userID, File tagsFile) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(tagsFile));
        
        String line = br.readLine(); // Headers
        
        List<ItemFeature> listItf = new ArrayList<>();
        
        boolean found = false;
        while((line = br.readLine())!= null) {
            StringTokenizer st = new StringTokenizer(line);
            long uId = Long.parseLong(st.nextToken());
            if (uId == userID) {
                ItemFeature itf = new ItemFeature(Long.parseLong(st.nextToken()), Long.parseLong(st.nextToken()), 0);
                listItf.add(itf);
            }
            if (found) break;
        }
        
        return listItf;
    }
}
