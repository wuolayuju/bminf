package es.uam.eps.bmi.recom.impl.recommender;

import es.uam.eps.bmi.recom.exceptions.GenericRecommendationException;
import es.uam.eps.bmi.recom.model.DataModel;
import es.uam.eps.bmi.recom.recommender.RecommendedItem;
import es.uam.eps.bmi.recom.similarity.VectorSimilarity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Clase que representa un recomendador basado en contenido que hace uso de
 * una función de similitud entre usuario e ítem y produce ítems recomendados
 * para un usuario.
 * @author Ari Handler - Adrián Lorenzo
 */
public class ContentBasedRecommender extends AbstractRecommender{

    private VectorSimilarity similarity;

	/**
	 * Construye el recomendador basado en contenido dado un modelo de datos
	 * y una función de similutd entre usuario e ítem.
	 * @param dataModel modelo de datos.
	 * @param similarity función de similitud entre usuario e ítem.
	 * @throws GenericRecommendationException si la función de similitud no existe.
	 */
    public ContentBasedRecommender(DataModel dataModel, VectorSimilarity similarity) throws GenericRecommendationException {
        super(dataModel);
        if (similarity == null) throw new GenericRecommendationException("vectorSimilarity is null.");
        this.similarity = similarity;
    }
    
    @Override
    public List<RecommendedItem> recommend(long userID, int top) throws GenericRecommendationException {
        List<Long> itemsRatedByUser = this.getDataModel().getItemIDsFromUser(userID);
        if (itemsRatedByUser == null)
            throw new GenericRecommendationException("User " + userID + " does not exist.");
        
        PriorityQueue<RecommendedItem> heapRecommended = 
                new PriorityQueue<>(top, new RecommendedItemComparator());
        
        for (Long itemId : this.getDataModel().getItemIDs()) {
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
    public double estimatePreference(long userID, long itemID, double train) throws GenericRecommendationException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
