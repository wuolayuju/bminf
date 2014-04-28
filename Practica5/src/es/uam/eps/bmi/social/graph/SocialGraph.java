package es.uam.eps.bmi.social.graph;

import edu.uci.ics.jung.algorithms.scoring.PageRank;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;
import es.uam.eps.bmi.social.graph.exceptions.SocialException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Clase que representa un grafo de una determinada red social. <br>
 * La clase provee una serie de métodos de cálculo de métricas tanto de los nodos 
 * y arcos como de la red entera.
 * @author Ari Handler - Adrián Lorenzo
 */
public class SocialGraph {
    
    private File dataFile;
    private Graph<String, Long> graph;
    private String name;

	/**
	 * Construye un grafo de red social dado un grafo creado con JUNG y un nombre
	 * para dicho grafo.
	 * @param graph grafo creado con JUNG.
	 * @param name nombre del grafo.
	 * @throws SocialException si el grafo es nulo.
	 */
    public SocialGraph(Graph<String, Long> graph, String name) throws SocialException {
        if (graph == null) throw new SocialException("graph is null.");
        this.graph = graph;
        this.name = name;
    }
    
	/**
	 * Construye una grafo de red social dado un fichero que representa las conexiones
	 * entre los nodos y un nombre para dicho grafo. El delimitador por defecto
	 * es ','.
	 * @param dataFile fichero del grafo.
	 * @param name nombre de la red.
	 * @throws SocialException {@link #this}
	 * @throws IOException si ocurre algún error con el fichero.
	 */
    public SocialGraph(File dataFile, String name) throws SocialException, IOException{
        this(dataFile, name, ",");
    }
    
	/**
	 * Construye una grafo de red social dado un fichero que representa las conexiones
	 * entre los nodos y un nombre para dicho grafo. Se define además el delimitador
	 * que se ha utlizado en el fichero.
	 * @param dataFile fichero del grafo.
	 * @param name nombre de la red.
	 * @param delimiter delimitador entre nodos que se usa en el fichero.
	 * @throws SocialException si el fichero de datos no existe.
	 * @throws IOException si ocurre algún error con el fichero.
	 */
    public SocialGraph(File dataFile, String name, String delimiter) throws SocialException, IOException {
        if (dataFile == null) throw new SocialException("dataFile is null.");
        this.dataFile = dataFile;
        this.name = name;
        buildGraph(delimiter);
    }
    
    private void buildGraph(String delimiter) throws FileNotFoundException, IOException {
        
        graph = new UndirectedSparseGraph();
        
        BufferedReader br = new BufferedReader(new FileReader(dataFile));
        
        String line;
        long edgeCnt = 0;
        while ((line = br.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(line, delimiter);
            
            String source = st.nextToken();
            
            graph.addVertex(source);
            
            String destination = st.nextToken();
            
            graph.addVertex(destination);
            
            graph.addEdge(edgeCnt++, source, destination, EdgeType.UNDIRECTED);
        }
        br.close();
    }

	/**
	 * Obtiene el fichero de datos de nodos.
	 * @return fichero de datos.
	 */
    public File getDataFile() {
        return dataFile;
    }

	/**
	 * Obtiene el grafo de la red social.
	 * @return grafo de la red.
	 */
    public Graph getGraph() {
        return graph;
    }
    
	/**
	 * Obtiene el nombre de la red social.
	 * @return nombre de la red.
	 */
    public String getName() {
        return name;
    }
    
	/**
	 * Obtiene la colección de aristas del grafo.
	 * @return colección de identificadores de aristas.
	 */
    public Collection<Long> getEdges() {
        return graph.getEdges();
    }
    
	/**
	 * Obtiene el par de nodos de una determinada arista.
	 * @param e identificador de la arista.
	 * @return par de nodos de la arista.
	 */
    public Pair<String> getVerticesFromEdge(Long e) {
        return graph.getEndpoints(e);
    }
    
	/**
	 * Obtiene la arista que conectados dos nodos.
	 * @param node1 primer nodo.
	 * @param node2 segundo nodo.
	 * @return identificador de la arista que uno los nodos.
	 */
    public Long getEdgeFromNodes(String node1, String node2) {
        return graph.findEdge(node1, node2);
    }
    
	/**
	 * Calcula el valor del coeficiente de clustering local de un determinado nodo.
	 * @param node identificador del nodo.
	 * @return coeficiente de clustering local del nodo.
	 * @throws SocialException si el nodo no existe en la red.
	 */
    public double getLocalClusteringCoefficient(String node) throws SocialException {
        if ( !graph.containsVertex(node) ) 
            throw new SocialException("node does not exist.");
        
        int degree = graph.degree(node);
        if (degree < 2) return 0.0;
        
        Collection<String> neighbors = graph.getNeighbors(node);
        double commonEdgesCnt = 0.0F;
        for (String u : neighbors) {
            for (String v : neighbors) {
                if (u.equals(v)) continue;
                if (graph.findEdge(u, v) != null)
                    commonEdgesCnt++;
            }
        }

        return (commonEdgesCnt / (degree * (degree - 1)));
    }
    
	/**
	 * Obtiene el valor de PageRank de un determinado nodo.
	 * @param node identificador del nodo.
	 * @param damping factor de amortiguamiento.
	 * @return PageRank del nodo
	 * @throws SocialException si el nodo no existe o el factor de amortiguamiento no es válido.
	 */
    public double getPageRankNode(String node, double damping) throws SocialException {
        if ( !graph.containsVertex(node) ) 
            throw new SocialException("node does not exist.");
        
        if (damping >= 1.0 || damping <= 0.0)
            throw new SocialException("damping factor must be in (0,1).");
        
        PageRank<String,Long> pr = new PageRank(graph, damping);
        double prScore = pr.getVertexScore(node);
        
        return prScore;
    }

	/**
	 * Calcula el valor de arraigo de la arista que une dos nodos. 
	 * @param node1 primer nodo.
	 * @param node2 segundo nodo.
	 * @return valor de arraigo de la arista.
	 * @throws SocialException si los nodos no están conectados.
	 */
    public double getEmbeddedness(String node1, String node2) throws SocialException {
        Long edge = graph.findEdge(node1, node2);
        if (edge == null) throw new SocialException("nodes " + node1 + " " + node2 + " are not connected.");
        
        Collection<String> neighborsNode1 = graph.getNeighbors(node1);
        Collection<String> neighborsNode2 = graph.getNeighbors(node2);
        
        List<String> intersection = new ArrayList<>();
        intersection.addAll(neighborsNode1);
        intersection.retainAll(neighborsNode2);
        if (intersection.isEmpty()) return 0.0;
        
        Set union = new HashSet(neighborsNode1);
        union.addAll(neighborsNode2);
        if (union.isEmpty()) return 0.0;
        
        return ((double) intersection.size() / (double) (union.size() - 2));
    }
    
	/**
	 * Calcula el coeficiente del clustering global de la red social.
	 * @return coeficiente global de clustering.
	 * @throws SocialException si existe algún error en el proceso.
	 */
    public double getGlobalClusteringCoefficient() throws SocialException {
        
        double sumCC = 0.0;
        for (String n : graph.getVertices()) {
            sumCC += this.getLocalClusteringCoefficient(n);
        }
        
        return sumCC / graph.getVertexCount();
    }
    
	/**
	 * Obtiene el grado de asortatividad de la red social
	 * @return asortatividad del grafo.
	 */
    public double getAssortativity() {
        
        double sumDegrees = 0.0;
        double sumSqrDegrees = 0.0;
        double sumCubDegrees = 0.0;
        double sumProductDegrees = 0.0;
        for (String u : graph.getVertices()) {
            int degree = graph.degree(u);
            sumDegrees += degree;
            sumSqrDegrees += Math.pow(degree, 2);
            sumCubDegrees += Math.pow(degree, 3);
            /*for (String v : graph.getNeighbors(u)) {
                sumProductDegrees += degree * graph.degree(v);
            }*/
        }
        
        sumSqrDegrees = Math.pow(sumSqrDegrees, 2);
        
        for (Long e : graph.getEdges()) {
            Pair<String> endpoints = graph.getEndpoints(e);
            sumProductDegrees += graph.degree(endpoints.getFirst()) * graph.degree(endpoints.getSecond());
        }
        
        double assortativity = 
                ((2 * sumDegrees * sumProductDegrees) - sumSqrDegrees)
                /
                ((sumDegrees * sumCubDegrees) - sumSqrDegrees);
        
        return assortativity;
    }
    
    @Override
    public String toString() {
        return name + "\n" + graph.toString();
    }
    
	/**
	 * Obtiene una colección de todos los nodos del grafo.
	 * @return colección de nodos.
	 */
    public Collection<String> getNodes() {
        return graph.getVertices();
    }
}
