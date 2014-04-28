/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.recom.impl.similarity;

import es.uam.eps.bmi.recom.exceptions.GenericRecommendationException;
import es.uam.eps.bmi.recom.model.DataModel;
import es.uam.eps.bmi.recom.model.Preference;
import es.uam.eps.bmi.recom.similarity.VectorSimilarity;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Clase que implementa la función de similitud entre un usuario y un ítem
 * basándose en el algoritmo Rocchio de similitud por centroides de usuarios.<br>
 * Ya que normalmente el cálculo de centroide de usuario es una operación recurrente,
 * se ha implementado un centroide de usuario cacheado para acceso rápido
 * a la función de similitud.
 * @author Ari Handler - Adrián Lorenzo
 */
public class UserItemSimilarity implements VectorSimilarity{

    private File itemFile;
    private HashMap<Long, List<ItemFeature>> itemFeatures;
    private List<Long> featuresIDs;
    private DataModel dataModel;
    
    private Map<Long, Double> cachedUserCentroid;
    private long cachedUserID;

	/**
	 * Construye la función de similitud dado un fichero en el que se detallan
	 * los ítems y sus características, así como el modelo de datos del sistema
	 * de recomendación en cuestión.
	 * @param itemFile fichero de ítems con sus características.
	 * @param dataModel modelo de datos.
	 * @throws IOException si ocurre algún problema con el fichero de ítems.
	 */
    public UserItemSimilarity(File itemFile, DataModel dataModel) throws IOException {
        this.itemFile = itemFile;
        this.dataModel = dataModel;
        
        this.itemFeatures = new HashMap<>();
        this.featuresIDs = new ArrayList<>();
        
        buildItemFeatures();
    }
    
    private void buildItemFeatures() throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(itemFile));
        
        String line = br.readLine(); // Headers
        
        while((line = br.readLine())!= null) {
            StringTokenizer st = new StringTokenizer(line);
            long itemID = Long.parseLong(st.nextToken());
            long featID = Long.parseLong(st.nextToken());
            int weight = Integer.parseInt(st.nextToken());
            
            if (itemFeatures.containsKey(itemID)) {
                itemFeatures.get(itemID).add(new ItemFeature(itemID, featID, weight));
            } else {
                List<ItemFeature> list = new ArrayList<>();
                list.add(new ItemFeature(itemID, featID, weight));
                itemFeatures.put(itemID, list);
            }
            
            if (!featuresIDs.contains(featID)) featuresIDs.add(featID);
        }
    }
    
	/**
	 * Devuelve la similitud entre un usario y un ítem mediante el cálculo
	 * de centroide de usuario y aplicación del algoritmo Rocchio.
	 * @param userID identificador del usuario.
	 * @param itemID idetificador del ítem.
	 * @return similitud entre usuario e ítem.
	 * @throws GenericRecommendationException si se produce algún error en el proceso.
	 */
    @Override
    public double vectorSimilarity(long userID, long itemID) throws GenericRecommendationException{
        Map<Long, Double> userCentroid = processUserCentroid(userID);
        
        List<ItemFeature> feats = itemFeatures.get(itemID);
        if (feats == null) return -1.0;
        // Cosine
        double numerator = 0.0;
        double itemModule = 0.0;
        for (ItemFeature f : feats) {
            numerator += f.getWeight() * userCentroid.get(f.getFeatID());
            itemModule += Math.pow(f.getWeight(), 2);
        }
        itemModule = Math.sqrt(itemModule);
        
        //User module
        double userModule = 0.0;
        for (Double v : userCentroid.values()) {
            userModule += Math.pow(v, 2);
        }
        userModule = Math.sqrt(userModule);
        
        return (numerator / (itemModule * userModule));
    }
    
    private Map<Long, Double> processUserCentroid(long userID) throws GenericRecommendationException {
        
        if (this.cachedUserID == userID)
            return this.cachedUserCentroid;
        
        List<Preference> userPrefs = dataModel.getPreferencesFromUser(userID);
        if (userPrefs == null) return null;
        
        Map<Long, Double> centroid = new HashMap<>();
        
        for (Long feature : featuresIDs) {
            double numerator = 0.0;

            for (Preference p : userPrefs) {
                long itemID = p.getItemID();
                List<ItemFeature> itemVector = itemFeatures.get(itemID);
                if (itemVector == null) continue;
                for (ItemFeature itf : itemVector) {
                    if (itf.getFeatID() == feature){
                        numerator += p.getValue() * itf.getWeight();
                    }
                }
            }
            
            centroid.put(feature, numerator / userPrefs.size());
        }

        this.cachedUserCentroid = centroid;
        this.cachedUserID = userID;
        
        return centroid;
    }
}
