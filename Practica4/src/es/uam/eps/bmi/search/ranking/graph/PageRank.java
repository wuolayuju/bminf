/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.search.ranking.graph;

import edu.uci.ics.jung.graph.*;
import edu.uci.ics.jung.graph.util.EdgeType;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author uam
 */
public class PageRank {
    
    private SparseGraph graph;
    private HashMap <String, List<Object>> nodes;
	
	private static final int OUT_LINKS_INDEX = 0;
	private static final int PAGERANK_INDEX = 1;
    
    public PageRank (String fileGraph) throws FileNotFoundException, IOException {
    
         graph = new SparseGraph();
         nodes = new HashMap<>();
    }
    
    public void loadFromfile (String fileGraph) throws FileNotFoundException, IOException {
        
        BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new DataInputStream(
                                new FileInputStream(fileGraph))));

        int numNodes = countLinesFile(fileGraph);
        String line;
        int edgeCnt = 0;
        while ((line = br.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(line);
            
            String source = st.nextToken();
            
            graph.addVertex(source);
            
            int outlinks = Integer.parseInt(st.nextToken());
            List<Object> outrank = new ArrayList<>();
            outrank.add(outlinks);
            outrank.add(1 / numNodes);
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
    
    private int countLinesFile(String filename) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(filename));
        try {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean empty = true;
            while ((readChars = is.read(c)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }
            return (count == 0 && !empty) ? 1 : count;
        } finally {
            is.close();
        }
    }
    
    public double getScoreOf(String documentId) {
        return 0.0;
    }
    
    public void calculatePageRank(double tolerance, double damping) {
        
        do {
            Iterator<String> itr = graph.getVertices().iterator();
            while(itr.hasNext()) {
                String id = itr.next();
                double pr = damping / graph.getVertexCount();
				Collection predecessorsList = graph.getPredecessors(id);
				double sumPr = 0;
				for (Object predObj : predecessorsList) {
					String pred = (String) predObj;
					double pr_old = (Double) nodes.get(pred).get(OUT_LINKS_INDEX);
					int outlinks = (Integer) nodes.get(pred).get(PAGERANK_INDEX);
					sumPr += pr_old / outlinks;
				}
				
                //graph.getPredecessors(id);
            }
        } while(true);
    }
}
