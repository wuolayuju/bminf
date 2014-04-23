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
import java.util.Collection;
import java.util.StringTokenizer;

/**
 *
 * @author chus
 */
public class SocialGraph {
    
    private File dataFile;
    private Graph<String, Long> graph;

    public SocialGraph(File dataFile) throws SocialException, IOException{
        this(dataFile, ",");
    }
    
    public SocialGraph(File dataFile, String delimiter) throws SocialException, IOException {
        if (dataFile == null) throw new SocialException("dataFile is null.");
        this.dataFile = dataFile;
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
        
        Collection<String> neighbors = graph.getNeighbors(node);
        
        return 0.0;
    }
}
