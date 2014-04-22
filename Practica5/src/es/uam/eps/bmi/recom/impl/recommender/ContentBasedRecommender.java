/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.recom.impl.recommender;

import es.uam.eps.bmi.recom.exceptions.GenericRecommendationException;
import es.uam.eps.bmi.recom.model.DataModel;
import es.uam.eps.bmi.recom.recommender.RecommendedItem;
import es.uam.eps.bmi.recom.recommender.Recommender;
import es.uam.eps.bmi.recom.similarity.VectorSimilarity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 *
 * @author chus
 */
public class ContentBasedRecommender implements Recommender{

    private DataModel dataModel;
    private VectorSimilarity similarity;

    public ContentBasedRecommender(DataModel dataModel, VectorSimilarity similarity) {
        this.dataModel = dataModel;
        this.similarity = similarity;
    }
    
    @Override
    public List<RecommendedItem> recommend(long userID, int top) throws GenericRecommendationException {
        List<Long> itemsRatedByUser = dataModel.getItemIDsFromUser(userID);
        if (itemsRatedByUser == null)
            throw new GenericRecommendationException("User " + userID + " does not exist.");
        
        PriorityQueue<RecommendedItem> heapRecommended = 
                new PriorityQueue<>(top, new RecommendedItemComparator());
        
        for (Long itemId : dataModel.getItemIDs()) {
            if ( !itemsRatedByUser.contains(itemId)) {
                RecommendedItem recItem = new GenericRecommendedItem(itemId, (float) estimatePreference(userID, itemId));
                
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
            }
        }
        // Conversión a lista del heap de puntuaciones
        List<RecommendedItem> listRecom = new ArrayList<>();
        listRecom.addAll(heapRecommended);
        
        Collections.sort(listRecom, new RecommendedItemComparator());

        Collections.reverse(listRecom);
        
        return listRecom;
    }

    @Override
    public double estimatePreference(long userID, long itemID) throws GenericRecommendationException{
        return similarity.vectorSimilarity(userID, itemID);
    }

    @Override
    public DataModel getDataModel() {
        return dataModel;
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
