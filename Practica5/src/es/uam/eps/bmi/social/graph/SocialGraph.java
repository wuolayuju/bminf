/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.social.graph;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
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
 *
 * @author chus
 */
public class SocialGraph {
    
    private File dataFile;
    private Graph<String, Long> graph;
    private String name;

    public SocialGraph(File dataFile, String name) throws SocialException, IOException{
        this(dataFile, name, ",");
    }
    
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

    public File getDataFile() {
        return dataFile;
    }

    public Graph getGraph() {
        return graph;
    }
    
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
    
    public double getPageRankNode(String node, double damping) throws SocialException {
        if ( !graph.containsVertex(node) ) 
            throw new SocialException("node does not exist.");
        
        if (damping >= 1.0 || damping <= 0.0)
            throw new SocialException("damping factor must be in (0,1).");
        
        int N = graph.getVertexCount();
        double pr = 0.0;
        for (String v : graph.getNeighbors(node)) {
            int tempDegree = graph.degree(v);
            tempDegree = tempDegree > 0 ? tempDegree : (N - 1);
            pr += (1/N) / tempDegree;
        }
        
        return ((damping / N) + (1 - damping) * pr);
    }

    public double getEmbeddedness(String node1, String node2) throws SocialException {
        Long edge = graph.findEdge(node1, node2);
        if (edge == null) throw new SocialException("nodes " + node1 + " " + node2 + " are not connected.");
        
        Collection<String> neighborsNode1 = graph.getNeighbors(node1);
        Collection<String> neighborsNode2 = graph.getNeighbors(node2);
        
        List<String> intersection = new ArrayList<>();
        intersection.addAll(neighborsNode1);
        intersection.retainAll(neighborsNode2);
        
        Set union = new HashSet(neighborsNode1);
        union.addAll(neighborsNode2);
        
        return ((double) intersection.size() / (double) (union.size() - 2));
    }
    
    @Override
    public String toString() {
        return name + " " + graph.toString();
    }
    
    public Collection<String> getNodes() {
        return graph.getVertices();
    }
}
