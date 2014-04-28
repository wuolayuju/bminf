package es.uam.eps.bmi.social.graph;

import edu.uci.ics.jung.algorithms.generators.random.BarabasiAlbertGenerator;
import edu.uci.ics.jung.algorithms.generators.random.ErdosRenyiGenerator;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import es.uam.eps.bmi.social.graph.exceptions.SocialException;
import java.util.HashSet;
import org.apache.commons.collections15.Factory;

/**
 * Clase que permite generar grafos aleatorios mediante una serie de algoritmos
 * definidos en JUNG.
 * @author Ari Handler - Adrián Lorenzo
 */
public class RandomSocialGraphGenerator {
    
	/**
	 * Genera un grafo aleatorio de Barabasi-Albert dado un nombre, un número
	 * inicial de vértices, el número de nodos con los que se conecta inicialmente
	 * un nodo nuevo en la red y el número de pasos del algooritmo.
	 * @param name nombre de la red.
	 * @param initialVertices número inicial de nodos.
	 * @param nodesAttached número de conexiones inicial de un nodo nuevo en la red.
	 * @param numSteps número de iteraciones que se aplica el algoritmo Barabasi-Albert.
	 * @return la red social generada.
	 * @throws SocialException {@link SocialGraph#this}
	 */
    public static SocialGraph generateBarabasiAlbert(String name, int initialVertices, int nodesAttached, int numSteps) throws SocialException {
        
        Factory<Graph<String, Long>> gF = new Factory<Graph<String, Long>>() {

            @Override
            public Graph<String, Long> create() {
                return new UndirectedSparseGraph<>();
            }
            
        };
        
        Factory<String> vF = new Factory<String>() {
            long count;
            @Override
            public String create() {
                return Long.toString(count++);
            }
        };
        
        Factory<Long> eF = new Factory<Long>() {
            long count;
            @Override
            public Long create() {
                return count++;
            }
        };
        
        BarabasiAlbertGenerator gen = new BarabasiAlbertGenerator(gF, vF, eF, initialVertices, nodesAttached, new HashSet<String>());
        
        gen.evolveGraph(numSteps);
        
        return new SocialGraph(gen.create(), name);
    }
    
	/**
	 * Genera un grafo aleatorio de Ërdos-Renyi dado un nombre, el número
	 * de nodos que debe tener el grafo y la probabilidad de que dos nodos
	 * se conecten.
	 * @param name nombre del grafo.
	 * @param numVertices número de vértices del grafo.
	 * @param edgeProbability probabilidad de que dos nodos aleatorios estén conectados.
	 * @return la red social generada.
	 * @throws SocialException {@link SocialGraph#this}
	 */
    public static SocialGraph generateErdosRenyi(String name, int numVertices, double edgeProbability) throws SocialException {
        
        Factory<Graph<String, Long>> gF = new Factory<Graph<String, Long>>() {

            @Override
            public Graph<String, Long> create() {
                return new UndirectedSparseGraph<>();
            }
            
        };
        
        Factory<String> vF = new Factory<String>() {
            long count;
            @Override
            public String create() {
                return Long.toString(count++);
            }
        };
        
        Factory<Long> eF = new Factory<Long>() {
            long count;
            @Override
            public Long create() {
                return count++;
            }
        };
        
        ErdosRenyiGenerator gen = new ErdosRenyiGenerator(gF, vF, eF, numVertices, edgeProbability);
        
        return new SocialGraph(gen.create(), name);
    }
}
