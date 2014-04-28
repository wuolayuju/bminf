/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.social.metrics;

import edu.uci.ics.jung.graph.util.Pair;
import es.uam.eps.bmi.social.graph.ScoredNode;
import es.uam.eps.bmi.social.graph.SocialGraph;
import es.uam.eps.bmi.social.graph.exceptions.SocialException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Clase que permite obtener estadísticas de una serie de redes sociales.
 * @author Ari Handler - Adrián Lorenzo
 */
public class MetricsWriter {
    
    private List<SocialGraph> networks;
    //private File writeFile;
    
    private final int TOP = 10;
    
	/**
	 * Construye el evaluador de redes sociales pasadas como argumento.
	 * @param networks lista de redes sociales a medir.
	 * @throws SocialException si la lista de redes no existe o está vacía.
	 * @throws IOException si existe algún problema con el fichero de salida.
	 */
    public MetricsWriter(List<SocialGraph> networks) throws SocialException, IOException {
        if (networks == null) throw new SocialException("networks is null.");
        if (networks.isEmpty()) throw new SocialException("networks is empty");
        this.networks = networks;
    }
    
	/**
	 * Escribe en fichero un el TOP10 de nodos con el mayor valor de coeficiente de
	 * clustering local de cada red.
	 * @param writeFile fichero de salida.
	 * @throws SocialException si se produce algún error con las redes.
	 * @throws IOException si se produce algún error con el fichero.
	 */
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
    
	/**
	 * Escribe en un fichero el TOP10 de aristas con el mayor valor de arraigo 
	 * de cada red.
	 * @param writeFile fichero de salida.
	 * @throws SocialException si se produce algún error con las redes.
	 * @throws IOException si se produce algún error con el fichero.
	 */
    public void writeEdgeEmbeddedness(File writeFile) throws SocialException, IOException {
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
    
	/**
	 * Escribe en un fichero el valor de asortatividad de cada red social.
	 * @param writeFile fichero de salida.
	 * @throws IOException si se produce algún error con el fichero.
	 */
    public void writeAssortativity(File writeFile) throws IOException {
        BufferedWriter br = 
                new BufferedWriter(new OutputStreamWriter(new FileOutputStream(writeFile), "utf-8"));
        
        for (SocialGraph g : networks) {
            double assortativity = g.getAssortativity();
            br.write(g.getName()+"\t"+assortativity+"\n");
        } /* graphs */
        
        br.close();
    }
    
	/**
	 * Escribe en un fichero el valor de coeficiente de clustering global
	 * de cada red social.
	 * @param writeFile fichero de salida.
	 * @throws SocialException si se produce algún error con las redes.
	 * @throws IOException si se produce algún error con el fichero.
	 */
    public void writeGlobalClusteringCoefficient(File writeFile) throws SocialException, IOException {
        BufferedWriter br = 
                new BufferedWriter(new OutputStreamWriter(new FileOutputStream(writeFile), "utf-8"));
        
        for (SocialGraph g : networks) {
            double gC = g.getGlobalClusteringCoefficient();
            br.write(g.getName()+"\t"+gC+"\n");
        } /* graphs */
        
        br.close();        
    }
    
	/**
	 * Escribe en fichero un el TOP10 de nodos con el mayor valor de PageRank de cada red.
	 * @param writeFile fichero de salida.
	 * @throws SocialException si se produce algún error con las redes.
	 * @throws IOException si se produce algún error con el fichero.
	 */
    public void writePageRank(File writeFile) throws SocialException, IOException {
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
	
	public void writeDegreeDistribution() throws FileNotFoundException, UnsupportedEncodingException, IOException {
		for (SocialGraph g : networks) {
			BufferedWriter br = 
                new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(g.getName()+"_dist.txt")), "utf-8"));
			HashMap<Integer,Integer> hm = new HashMap<>();
			Collection<String> nodes = g.getNodes();
			for (String n : nodes) {
				int degree = g.getGraph().degree(n);
				if (hm.get(degree) == null) {
					hm.put(degree, 1);
				} else {
					int newv = hm.get(degree);
					hm.put(degree, ++newv);
				}
			}
			for (Integer i : hm.keySet()) {
				br.write(i+" "+hm.get(i)+"\n");
			}
			br.close();
		}
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
