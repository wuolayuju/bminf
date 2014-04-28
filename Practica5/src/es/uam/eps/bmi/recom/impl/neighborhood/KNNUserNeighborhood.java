package es.uam.eps.bmi.recom.impl.neighborhood;

import es.uam.eps.bmi.recom.exceptions.GenericRecommendationException;
import es.uam.eps.bmi.recom.impl.model.GenericPreference;
import es.uam.eps.bmi.recom.model.DataModel;
import es.uam.eps.bmi.recom.model.Preference;
import es.uam.eps.bmi.recom.similarity.UserSimilarity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Clase que representa un proveedor de vecinos próximos haciendo uso del
 * algoritmo kNN (k Nearest Neighbours).
 * @author Ari Handler - Adrián Lorenzo
 */
public class KNNUserNeighborhood extends AbstractUserNeighborhood{

    private final int k;
    
	/**
	 * Construye un proveedor de vecinos próximos con un número de vecinos
	 * por defecto de k = 3.
	 * @param userSimilarity función de similitud entre usuarios.
	 * @param dataModel modelo de datos.
	 * @throws GenericRecommendationException {@link AbstractUserNeighborhood#this}
	 */
    public KNNUserNeighborhood(UserSimilarity userSimilarity, DataModel dataModel) throws GenericRecommendationException {
        this(3, userSimilarity, dataModel);
    }
    
	/**
	 * Construye un proveedor de vecinos próximos con un número de vecinos dado.
	 * @param k número de vecinos a calcular.
	 * @param userSimilarity función de similitud.
	 * @param dataModel modelo de datos.
	 * @throws GenericRecommendationException si k es <= 1.
	 */
    public KNNUserNeighborhood(int k, UserSimilarity userSimilarity, DataModel dataModel) throws GenericRecommendationException {
        super(userSimilarity, dataModel);
        if (k <= 1) throw new GenericRecommendationException("k must be > 1.");
        int numUsers = dataModel.getNumUsers();
        this.k = k > numUsers ? numUsers : k;
    }

    @Override
    public List<Long> getUserNeighborhood(long userID) throws GenericRecommendationException {
        List<Long> usersIds = this.getDataModel().getUserIDs();
        
        // Using Preference as User-Simil structure
        PriorityQueue<Preference> heapNeighbours = 
                new PriorityQueue<>(k, new KNNUserNeighborhood.SimilUserComparator());
        
        for (Long vID : usersIds) {
            if (vID == userID) continue; // Not myself
            double simil = this.getUserSimilarity().userSimilarity(userID, vID);
            Preference userSimil = new GenericPreference(vID, 0, (float) simil); // Item 0 (not necessary)
                       
            /***********HEAP**************/
            if (heapNeighbours.size() == k) {
                if (heapNeighbours.peek().getValue() < userSimil.getValue()){
                    heapNeighbours.poll();
                    heapNeighbours.offer(userSimil);
                }
            } else {
                heapNeighbours.offer(userSimil);
            }
            /***********END HEAP**********/
        }
        
        // Heap to ordered list
        List<Preference> listNeighbours = new ArrayList<>();
        listNeighbours.addAll(heapNeighbours);
        
        Collections.sort(listNeighbours, new KNNUserNeighborhood.SimilUserComparator());

        Collections.reverse(listNeighbours);
        
        // Preference list to IDs list
        List<Long> listIDs = new ArrayList<>();
        for (Preference p : listNeighbours ) {
            listIDs.add(p.getUserID());
        }
        
        return listIDs;
    }
    
    private class SimilUserComparator implements Comparator<Preference> {

        @Override
        public int compare(Preference o1, Preference o2) {
            if (o1.getValue() > o2.getValue())
                return 1;
            if (o1.getValue() < o2.getValue())
                return -1;
            return 0;
        }
        
    }
    
    
}
