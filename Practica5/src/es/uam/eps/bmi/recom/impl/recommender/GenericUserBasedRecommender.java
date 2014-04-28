/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.recom.impl.recommender;

import es.uam.eps.bmi.recom.exceptions.GenericRecommendationException;
import es.uam.eps.bmi.recom.model.DataModel;
import es.uam.eps.bmi.recom.neighborhood.UserNeighborhood;
import es.uam.eps.bmi.recom.recommender.RecommendedItem;
import es.uam.eps.bmi.recom.similarity.UserSimilarity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 *
 * @author uam
 */
public class GenericUserBasedRecommender extends AbstractRecommender{

    private final UserNeighborhood neighborhood;
    private final UserSimilarity similarity;
    
    public GenericUserBasedRecommender(DataModel dataModel, UserNeighborhood neighborhood, UserSimilarity similarity) throws GenericRecommendationException {
        super(dataModel);
        if (neighborhood == null) throw new GenericRecommendationException("neighborhood is null.");
        if (similarity == null) throw new GenericRecommendationException("similarity is null.");
        this.neighborhood = neighborhood;
        this.similarity = similarity;
    }

    @Override
    public List<RecommendedItem> recommend(long userID, int top) throws GenericRecommendationException {
        //System.out.println("Calculating Neighborhood...");
        List<Long> userNeighborhood = neighborhood.getUserNeighborhood(userID);
        
        PriorityQueue<RecommendedItem> heapRecommended = 
                new PriorityQueue<>(top, new GenericUserBasedRecommender.RecommendedItemComparator());
        
        int itemsProcessed = 0;
        for (Long itemId : this.getDataModel().getItemIDs()) {
            //System.out.println("Calculating likeliness for item" + itemId + "...");
            float recommendedScore = (float) estimatePreference(userID, itemId);
            
            RecommendedItem recItem = new GenericRecommendedItem(itemId, recommendedScore);
            
            /***********HEAP**************/
            if (heapRecommended.size() == top) {
                if (heapRecommended.peek().getValue() < recItem.getValue()){
                    heapRecommended.poll();
                    heapRecommended.offer(recItem);
                }
            } else {
                heapRecommended.offer(recItem);
            }
            /***********FIN HEAP**********/
            
            if (itemsProcessed % 1000 == 0)
                System.out.println("Processed " + itemsProcessed + " items.");
            itemsProcessed++;
        }/********* items */
        
        // Conversión a lista del heap de puntuaciones
        List<RecommendedItem> listRecom = new ArrayList<>();
        listRecom.addAll(heapRecommended);
        
        Collections.sort(listRecom, new RecommendedItemComparator());

        Collections.reverse(listRecom);
        
        return listRecom;
    }

    @Override
    public double estimatePreference(long userID, long itemID) throws GenericRecommendationException {
        List<Long> userNeighborhood = neighborhood.getUserNeighborhood(userID);
        
        float recommendedScore = 0.0F;
        float normalizer = 0.0F;
        for (Long v : userNeighborhood) {

            float preferenceValue;
            try {
                preferenceValue = this.getDataModel().getPreferenceValue(v, itemID);

            } catch (GenericRecommendationException ex) {
                preferenceValue = 0.0F;
            }

            float simil = (float) similarity.userSimilarity(userID, v);
            
            normalizer += simil;
            
            recommendedScore += preferenceValue * simil;
        }
        
        return recommendedScore / normalizer;
    }
    
    @Override
    public double estimatePreference(long userID, long itemID, double train) throws GenericRecommendationException {
        List<Long> userNeighborhood = neighborhood.getUserNeighborhood(userID);
        
        float recommendedScore = 0.0F;
        float normalizer = 0.0F;
        for (Long v : userNeighborhood) {

            float preferenceValue;
            try {
                preferenceValue = this.getDataModel().getPreferenceValue(v, itemID);

            } catch (GenericRecommendationException ex) {
                preferenceValue = 0.0F;
            }

            float simil = (float) similarity.userSimilarity(userID, v, train);
            
            normalizer += simil;
            
            recommendedScore += preferenceValue * simil;
        }
        
        return recommendedScore / normalizer;
    }

    public UserNeighborhood getNeighborhood() {
        return neighborhood;
    }

    public UserSimilarity getSimilarity() {
        return similarity;
    }
    
    private class RecommendedItemComparator implements Comparator<RecommendedItem> {

        @Override
        public int compare(RecommendedItem o1, RecommendedItem o2) {
            if (o1.getValue() > o2.getValue())
                return 1;
            if (o1.getValue() < o2.getValue())
                return -1;
            return 0;
        }
        
    }
}
