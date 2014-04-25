/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.social.test;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import es.uam.eps.bmi.social.graph.RandomSocialGraphGenerator;
import es.uam.eps.bmi.social.graph.SocialGraph;
import es.uam.eps.bmi.social.graph.exceptions.SocialException;
import java.awt.Dimension;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author chus
 */
public class GraphTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String dataFile = null;
        
        for(int i=0;i<args.length;i++) {
            if(args[i].compareTo("-data")==0) {
                dataFile = args[i+1];
                i++;
            }
        }
        
        try {
            //String netName = dataFile.substring(dataFile.lastIndexOf("/")+1);
            //SocialGraph graph = new SocialGraph(new File(dataFile), netName);
            //System.out.println(graph.toString());
            /*for (String n : graph.getNodes()) {
            System.out.println("CC("+n+") = "+graph.getLocalClusteringCoefficient(n));
            System.out.println("PR("+n+") = "+graph.getPageRankNode(n, 0.8));
            }*/
            //System.out.println("E(1,2) = " + graph.getEmbeddedness("1", "2"));
            //System.out.println("GC = " + graph.getGlobalClusteringCoefficient());
            //System.out.println("Ast = " + graph.getAssortativity());
            /*BufferedWriter br = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream("nodeClustering.txt"), "utf-8"));
            
            for (String u : graph.getNodes()) {
                br.write(netName + "\t" + u + "\t" + graph.getLocalClusteringCoefficient(u) + "\n");
            }
            
            br.close();*/
            
            SocialGraph barGraph = RandomSocialGraphGenerator.generateBarabasiAlbert("barabasi", 10, 10, 50);
            System.out.println(barGraph.toString());
            
            SocialGraph erdGraph = RandomSocialGraphGenerator.generateErdosRenyi("erdos", 50, 0.5);
            System.out.println(erdGraph.toString());
            
            // The Layout<V, E> is parameterized by the vertex and edge types
            Layout<Integer, String> layout = new CircleLayout(barGraph.getGraph());
            layout.setSize(new Dimension(600,600)); // sets the initial size of the space
            // The BasicVisualizationServer<V,E> is parameterized by the edge types
            BasicVisualizationServer<Integer,String> vv =
            new BasicVisualizationServer<Integer,String>(layout);
            vv.setPreferredSize(new Dimension(600,600)); //Sets the viewing area size
            JFrame frame = new JFrame("Simple Graph View");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(vv);
            frame.pack();
            frame.setVisible(true); 
            
            
        } catch (SocialException ex) {
            Logger.getLogger(GraphTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
