/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.search.ranking.graph;

import edu.uci.ics.jung.graph.*;
import edu.uci.ics.jung.graph.util.EdgeType;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author uam
 */
public class PageRank {
    
    private SparseGraph graph;
    private HashMap <String, List<Object>> nodes;
    
    public PageRank (String fileGraph) throws FileNotFoundException, IOException {
    
         graph = new SparseGraph();
         nodes = new HashMap<>();
        //graph = new DirectedSparseGraph<NodeGraph, Integer>();
        
        BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new DataInputStream(
                                new FileInputStream(fileGraph))));

        String line;
        int edgeCnt = 0;
        while ((line = br.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(line);
            
            String source = st.nextToken();
            
            graph.addVertex(source);
            
            int outlinks = Integer.parseInt(st.nextToken());
            List<Object> outrank = new ArrayList<>();
            outrank.add(outlinks);
            outrank.add(1);
            if (nodes.containsKey(source)) { nodes.remove(source); }
            nodes.put(source, outrank);
            
            while(st.hasMoreTokens()) {
                String destination = st.nextToken();
                graph.addVertex(destination);
                graph.addEdge(edgeCnt++, source, destination, EdgeType.DIRECTED);
            }
        }
        br.close();
    }
    
    public double getScoreOf(String documentId) {
        return 0.0;
    }
    
    public void calculatePageRank() {
        
    }
}
