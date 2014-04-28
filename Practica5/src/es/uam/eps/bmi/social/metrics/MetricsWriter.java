/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.social.metrics;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Pair;
import es.uam.eps.bmi.social.graph.ScoredNode;
import es.uam.eps.bmi.social.graph.SocialGraph;
import es.uam.eps.bmi.social.graph.exceptions.SocialException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 *
 * @author chus
 */
public class MetricsWriter {
    
    private List<SocialGraph> networks;
    //private File writeFile;
    
    private final int TOP = 10;
    
    public MetricsWriter(List<SocialGraph> networks) throws SocialException, IOException {
        if (networks == null) throw new SocialException("networks is null.");
        if (networks.isEmpty()) throw new SocialException("networks is empty");
        this.networks = networks;
    }
    
    public void writeClusteringCoefficient(File writeFile) throws SocialException, IOException {
       
        BufferedWriter br = 
                new BufferedWriter(new OutputStreamWriter(new FileOutputStream(writeFile), "utf-8"));
        
        for (SocialGraph g : networks) {
            PriorityQueue<ScoredNode> heapNodes = 
                    new PriorityQueue<>(TOP, new MetricsWriter.ScoredNodeComparator());
            for (String n : g.getNodes()) {
                ScoredNode sn = new ScoredNode(n, g.getLocalClusteringCoefficient(n));
                if (heapNodes.size() == TOP) {
                    if (heapNodes.peek().getScore() < sn.getScore()){
                        heapNodes.poll();
                        heapNodes.offer(sn);
                    }
                } else {
                    heapNodes.offer(sn);
                } /* heap */
            }/* nodes */ 
            List<ScoredNode> listTop = new ArrayList<>(heapNodes);
            Collections.sort(listTop, new MetricsWriter.ScoredNodeComparator());
            Collections.reverse(listTop);
            for (ScoredNode sn : listTop) {
                br.write(g.getName()+"\t"+sn.getNode()+"\t"+sn.getScore()+"\n");
            }
        }/* graph */
        br.close();
    }
    
    public void writeEdgeEmbeddedness(File writeFile) throws SocialException, FileNotFoundException, UnsupportedEncodingException, IOException {
        BufferedWriter br = 
                new BufferedWriter(new OutputStreamWriter(new FileOutputStream(writeFile), "utf-8"));
        
        for (SocialGraph g : networks) {
            PriorityQueue<ScoredNode> heapEdges = 
                    new PriorityQueue<>(TOP, new MetricsWriter.ScoredNodeComparator());
            for (Long e : g.getEdges()) {
                Pair<String> verts = g.getVerticesFromEdge(e);
                String n1 = verts.getFirst();
                String n2 = verts.getSecond();
                double embeddedness = g.getEmbeddedness(n1, n2);
                ScoredNode se = new ScoredNode(String.valueOf(e), embeddedness);
                
                if (heapEdges.size() == TOP) {
                    if (heapEdges.peek().getScore() < se.getScore()){
                        heapEdges.poll();
                        heapEdges.offer(se);
                    }
                } else {
                    heapEdges.offer(se);
                } /* heap */
            }/* edges */

            List<ScoredNode> listTop = new ArrayList<>(heapEdges);
            Collections.sort(listTop, new MetricsWriter.ScoredNodeComparator());
            Collections.reverse(listTop);
            for (ScoredNode se : listTop) {
                Pair<String> verts = g.getVerticesFromEdge(Long.parseLong(se.getNode()));
                br.write(g.getName()+"\t"+verts.getFirst()+"\t"+verts.getSecond()+"\t"+se.getScore()+"\n");
            }
        }/* graph */
        br.close();
    }
    
    public void writeAssortativity(File writeFile) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        BufferedWriter br = 
                new BufferedWriter(new OutputStreamWriter(new FileOutputStream(writeFile), "utf-8"));
        
        for (SocialGraph g : networks) {
            double assortativity = g.getAssortativity();
            br.write(g.getName()+"\t"+assortativity+"\n");
        } /* graphs */
        
        br.close();
    }
    
    public void writeGlobalClusteringCoefficient(File writeFile) throws UnsupportedEncodingException, FileNotFoundException, SocialException, IOException {
        BufferedWriter br = 
                new BufferedWriter(new OutputStreamWriter(new FileOutputStream(writeFile), "utf-8"));
        
        for (SocialGraph g : networks) {
            double gC = g.getGlobalClusteringCoefficient();
            br.write(g.getName()+"\t"+gC+"\n");
        } /* graphs */
        
        br.close();        
    }
    
    public void writePageRank(File writeFile) throws UnsupportedEncodingException, FileNotFoundException, SocialException, IOException {
        BufferedWriter br = 
                new BufferedWriter(new OutputStreamWriter(new FileOutputStream(writeFile), "utf-8"));
        
        for (SocialGraph g : networks) {
            PriorityQueue<ScoredNode> heapNodes = 
                    new PriorityQueue<>(TOP, new MetricsWriter.ScoredNodeComparator());
            for (String n : g.getNodes()) {
                ScoredNode sn = new ScoredNode(n, g.getPageRankNode(n, 0.2));
                if (heapNodes.size() == TOP) {
                    if (heapNodes.peek().getScore() < sn.getScore()){
                        heapNodes.poll();
                        heapNodes.offer(sn);
                    }
                } else {
                    heapNodes.offer(sn);
                } /* heap */
            }/* nodes */ 
            List<ScoredNode> listTop = new ArrayList<>(heapNodes);
            Collections.sort(listTop, new MetricsWriter.ScoredNodeComparator());
            Collections.reverse(listTop);
            for (ScoredNode sn : listTop) {
                br.write(g.getName()+"\t"+sn.getNode()+"\t"+sn.getScore()+"\n");
            }
        }/* graph */
        br.close();
    }
    
    private class ScoredNodeComparator implements Comparator<ScoredNode> {

        @Override
        public int compare(ScoredNode o1, ScoredNode o2) {
            if (o1.getScore() > o2.getScore())
                return 1;
            if (o1.getScore() < o2.getScore())
                return -1;
            return 0;
        }
        
    }
}
