/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.social.test;

import edu.uci.ics.jung.algorithms.layout.BalloonLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.DAGLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout2;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.DefaultVertexIconTransformer;
import edu.uci.ics.jung.visualization.layout.CachingLayout;
import es.uam.eps.bmi.social.graph.RandomSocialGraphGenerator;
import es.uam.eps.bmi.social.graph.SocialGraph;
import es.uam.eps.bmi.social.graph.exceptions.SocialException;
import es.uam.eps.bmi.social.metrics.MetricsWriter;
import java.awt.Dimension;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    public static void main(String[] args) throws IOException {
        List<String> files = new ArrayList<>();
        
        files.addAll(Arrays.asList(args));
        
        try {
            List<SocialGraph> graphs = new ArrayList<>();
            SocialGraph g;
            for(String f : files) {
                String name = f.substring(0, f.indexOf("."));
                g = new SocialGraph(new File(f), name);
                graphs.add(g);
            }
            g = RandomSocialGraphGenerator.generateBarabasiAlbert("barabasi", 100, 5, 50);
            graphs.add(g);
            g = RandomSocialGraphGenerator.generateErdosRenyi("erdos", 100, 0.2);
            graphs.add(g);
            
            MetricsWriter metrics = new MetricsWriter(graphs);
            metrics.writeClusteringCoefficient(new File("clustering.txt"));
            metrics.writeEdgeEmbeddedness(new File("embeddedness.txt"));
            metrics.writeAssortativity(new File("assortativity.txt"));
            metrics.writeGlobalClusteringCoefficient(new File("global_clustering.txt"));
            metrics.writePageRank(new File("pagerank.txt"));
            
        } catch (SocialException ex) {
            Logger.getLogger(GraphTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void printGraph(SocialGraph g) {
        // The Layout<V, E> is parameterized by the vertex and edge types
        Layout<Integer, String> layout = new ISOMLayout(g.getGraph());
        layout.setSize(new Dimension(600,600)); // sets the initial size of the space
        // The BasicVisualizationServer<V,E> is parameterized by the edge types
        VisualizationViewer<Integer,String> vv = new VisualizationViewer<>(layout);
        vv.setPreferredSize(new Dimension(600,600)); //Sets the viewing area size
        JFrame frame = new JFrame(g.getName());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv);
        frame.pack();
        frame.setVisible(true);
    }
    
}
