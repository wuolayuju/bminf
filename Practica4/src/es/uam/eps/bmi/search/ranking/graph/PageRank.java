package es.uam.eps.bmi.search.ranking.graph;

import edu.uci.ics.jung.graph.*;
import edu.uci.ics.jung.graph.util.EdgeType;
import es.uam.eps.bmi.search.ScoredTextDocument;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

/**
 * Clase que implementa el algoritmo iterativo PageRank dado un fichero de enlaces.<br>
 * Para usar correctamente esta clase, es necesario primero instanciarla, llamar a
 * {@link PageRank#loadFromfile(java.lang.String) } para crear el grafo de enlaces y
 * por último llamar a {@link PageRank#calculatePageRank(double, double, boolean) }
 * para realizar el cálculo del score de los nodos.
 * @author Ari Handler y Adrián Lorenzo
 */
public class PageRank {
    
    private SparseGraph graph;
    private HashMap <String, List<Object>> nodes;
	
    private static final int OUT_LINKS_INDEX = 0;
    private static final int PAGERANK_INDEX = 1;
    
    /**
     * Construye la clase PageRank, inicializando el grafo de enlaces y la lista de nodos
     */
    public PageRank () {
    
         graph = new SparseGraph();
         nodes = new HashMap<>();
    }
    
    /**
     * Construye el grafo de enlaces dado el nombre de un fichero que sigue el 
     * formato:<br>
     * id_cluweb n_outlinks [id_clueweb_link]*
     * @param fileGraph fichero que contiene los nodos y sus enlaces
     * @return el número de nodos leídos
     * @throws FileNotFoundException si no se encuentra el fichero fileGraph
     * @throws IOException si se produce algún error con el fichero
     */
    public int loadFromfile (String fileGraph) throws FileNotFoundException, IOException {
        
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
            outrank.add(1.0 / numNodes);
            if (nodes.containsKey(source)) { nodes.remove(source); }
            nodes.put(source, outrank);
            
            while(st.hasMoreTokens()) {
                String destination = st.nextToken();
                graph.addVertex(destination);
                graph.addEdge(edgeCnt++, source, destination, EdgeType.DIRECTED);
            }
        }
        br.close();
        
        return graph.getVertexCount();
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
    
    /**
     * Devuelve el valor PageRank de un determinado documento
     * @param documentId el identificador del documento
     * @return la puntuación PageRank del documento
     */
    public double getScoreOf(String documentId) {
        return (Double) nodes.get(documentId).get(PAGERANK_INDEX);
    }
    
    /**
     * Implementa el método iterativo para calcular el valor de PageRank de cada
     * uno de los nodos en el grafo montado mediante {@link PageRank#loadFromfile(java.lang.String) }.<br>
     * El algoritmo se detiene si se ha llegado al criterio de convergencia pasado
     * como uno de los argumentos o bien, si el número de iteraciones que se han
     * realizado son un total de 50.<br>
     * Para el tratamiento de nodos sumidero se ha hecho uso de (1 - P'[i]) / |links|).
     * @param tolerance criterio de convergencia. Si el cambio mayor de score
     * en una iteración entera es menor que este valor, el algoritmo se detiene.
     * @param damping factor de amortiguamiento.
     * @param verbose si está activado, imprimirá por salida estándar el desarrollo
     * del algoritmo.
     */
    public void calculatePageRank(double tolerance, double damping, boolean verbose) {
        
        double maxDelta;
        int nIterations = 0;
        do {
            maxDelta = 0;
            HashMap<String, Double> newRanks = new HashMap<>();
            Iterator<String> itr = graph.getVertices().iterator();
            
            if (verbose) System.out.println("=====Iteration " + nIterations + "=====");
            
            /*
             * P(dj) = r + (1 - r) * sum{ P(di)/#out(di) ; di->dj }
             */
            double sumNewPrs = 0;
            while(itr.hasNext()) {
                String id = itr.next();
                double pr = damping;
                Collection predecessorsList = graph.getPredecessors(id);
                double sumPr = 0;
                for (Object predObj : predecessorsList) {
                    String pred = (String) predObj;
                    double pr_old = (Double) nodes.get(pred).get(PAGERANK_INDEX);
                    int outlinks = (Integer) nodes.get(pred).get(OUT_LINKS_INDEX);
                    // sum{ P(di)/#out(di) ; di->dj }
                    sumPr += pr_old / outlinks;
                }
                // (1 - r) * sum
                pr += ( 1 - damping) * sumPr;
                sumNewPrs += pr;
                newRanks.put(id, pr);
            }
            
            // Actualizacion de los PR de los nodos
            itr = newRanks.keySet().iterator();
            while(itr.hasNext()) {
                String id = itr.next();
                double newPr = newRanks.get(id);
                // Calculo del maximo cambio de PR
                List<Object> value = nodes.get(id);
                double oldPr = (Double) value.get(PAGERANK_INDEX);
                
                // Nodos sumidero : (1 - sum{P'[i]}) / N
                newPr += (1 - sumNewPrs) / nodes.size();
                
                if (verbose) {
                    System.out.println("Node '" + id + ""
                            + "' => PR_old = " + oldPr + ""
                            + " PR_new = " + newPr);
                }
                
                if (Math.abs(oldPr - newPr) > maxDelta) {
                    maxDelta = (Double) value.get(PAGERANK_INDEX);
                }
                // Actualizacion de PR
                value.set(PAGERANK_INDEX, newPr);
                nodes.put(id, value);
            }
            nIterations ++;
            
            if (verbose) { System.out.println("\nMax Delta = " + maxDelta + "\n");}
            
        } while(maxDelta > tolerance && nIterations < 50);
    }
    
    /**
     * Devuelve el top de documentos dado su PageRank
     * @param top numero máximo de documentos devueltos
     * @return lista de documentos puntuados en orden descendiente
     */
    public List<ScoredTextDocument> getTopNPages(int top) {
        
        PriorityQueue<ScoredTextDocument> heap = 
                new PriorityQueue<>(top, new PageRank.ScoredTextDocumentComparator());
        
        Iterator<String> itr = nodes.keySet().iterator();
        
        while (itr.hasNext()) {
            String id = itr.next();
            ScoredTextDocument doc = new ScoredTextDocument(id, (Double) nodes.get(id).get(PAGERANK_INDEX));
            /***********HEAP**************/
            if (heap.size() == top) {
                if (heap.peek().getScore() < doc.getScore()){
                    heap.poll();
                    heap.offer(doc);
                }
            } else {
                heap.offer(doc);
            }
            /***********FIN HEAP**********/
        }
        
        // Conversión a lista del heap de puntuaciones
        List<ScoredTextDocument> listScorDocs = new ArrayList<>();
                listScorDocs.addAll(heap);
        
        Collections.sort(listScorDocs, new PageRank.ScoredTextDocumentComparator());

        Collections.reverse(listScorDocs);
        
        return listScorDocs;
    }
    
    private class ScoredTextDocumentComparator implements Comparator<ScoredTextDocument> {

        @Override
        public int compare(ScoredTextDocument o1, ScoredTextDocument o2) {
            if (o1.getScore() > o2.getScore())
                return 1;
            if (o1.getScore() < o2.getScore())
                return -1;
            return 0;
        }
    }
}

