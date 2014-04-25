/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.social.graph;

import edu.uci.ics.jung.algorithms.generators.random.BarabasiAlbertGenerator;
import edu.uci.ics.jung.algorithms.generators.random.ErdosRenyiGenerator;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import es.uam.eps.bmi.social.graph.exceptions.SocialException;
import java.util.HashSet;
import org.apache.commons.collections15.Factory;

/**
 *
 * @author uam
 */
public class RandomSocialGraphGenerator {
    
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
