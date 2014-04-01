/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.search.ranking.graph.aggregation;

import es.uam.eps.bmi.search.ScoredTextDocument;
import es.uam.eps.bmi.search.TextDocument;
import es.uam.eps.bmi.search.indexing.AdvancedIndex;
import es.uam.eps.bmi.search.indexing.Index;
import es.uam.eps.bmi.search.ranking.graph.PageRank;
import es.uam.eps.bmi.search.searching.LiteralMatchingSearcher;
import es.uam.eps.bmi.search.searching.ProximalSearcher;
import es.uam.eps.bmi.search.searching.Searcher;
import es.uam.eps.bmi.search.searching.TFIDFSearcher;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author uam
 */
public class AggregatorTest {
    
    public static void main(String[] args) {
        String indexPath = "";
        String linksFile = "";
        String queriesFile = "";
        String relevanceFile = "";
        
        String usage = "USAGE: java es.uam.eps.bmi.search.ranking.graph.aggregation.AggregatorTest"
                 + " -index INDEX_PATH -links LINKS_FILE -queries QUERIES_FILE -relevance RELEVANCE_FILE\n"
                 + "Compara resultados de varios buscadores dado un indice y el fichero de enlaces de dicha coleccion.";
        
        for(int i=0;i<args.length;i++) {
            if(args[i].compareTo("-index")==0) {
                indexPath = args[i+1];
                i++;
            }
            if(args[i].compareTo("-links")==0) {
                linksFile = args[i+1];
                i++;
            }
            if(args[i].compareTo("-queries")==0) {
                queriesFile = args[i+1];
                i++;
            }
            if(args[i].compareTo("-relevance")==0) {
                relevanceFile = args[i+1];
                i++;
            }
        }
        
        String errorCause = "";
        
        if (indexPath.isEmpty()) errorCause += "No index directory provided\n";
        
        if (linksFile.isEmpty()) errorCause += "No links file provided\n";
        
        if (queriesFile.isEmpty()) errorCause += "No queries file provided\n";
        
        if (relevanceFile.isEmpty()) errorCause += "No relevances file provided\n";
        
        if (!errorCause.isEmpty()) {
            System.err.println(errorCause);
            System.out.println(usage);
            System.exit(0);
        }
        
        if (!indexPath
                .substring(indexPath.lastIndexOf("/"))
                .contains("advanced"))
        {
            System.err.println("The provided index must be of type 'Advanced'.");
            System.exit(0);
        }
        
        // Indice
        AdvancedIndex index = new AdvancedIndex();
        index.load(indexPath);
        
        /*
        Buscadores
        */
        TFIDFSearcher tfSearcher = new TFIDFSearcher();
        tfSearcher.build(index);
        tfSearcher.setTopResults(10);
        
        LiteralMatchingSearcher literalSearcher = new LiteralMatchingSearcher();
        literalSearcher.build(index);
        literalSearcher.setTopResults(10);
        
        ProximalSearcher proxSearcher = new ProximalSearcher();
        proxSearcher.build(index);
        proxSearcher.setTopResults(10);
        
        PageRank pageRank = new PageRank();
        try {
            pageRank.loadFromfile(linksFile);
        } catch (IOException ex) {
            Logger.getLogger(AggregatorTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        pageRank.calculatePageRank(0.01, 0.8, false);
        
        /*
        Lectura de las consultas y relevancias
        */
        List<String> queries = null;
        List<List<String>> relevance = null;
        try {
            queries = readQueries(queriesFile);
            relevance = readRelevance(relevanceFile);
        } catch (Exception ex) {
            Logger.getLogger(AggregatorTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for (String query : queries) {
            List<ScoredTextDocument> topTf = tfSearcher.search(query);
            System.out.println("--- TFIDFSearcher ---");
            calcPrecisions(index, topTf, queries, relevance);
            
            List<ScoredTextDocument> topLiteral = literalSearcher.search(query);
            System.out.println("--- LiteralSearcher ---");
            calcPrecisions(index, topLiteral, queries, relevance);
            
            List<ScoredTextDocument> topProximal = proxSearcher.search(query);
            System.out.println("--- ProximalSearcher ---");
            calcPrecisions(index, topProximal, queries, relevance);
            
            //List<ScoredTextDocument> topPageRank = pageRank.getTopNPages(10);
        }
        
    }
    
    private static List<String> readQueries(String queriesPath) throws Exception {
        BufferedReader inQueries = new BufferedReader(
                     new InputStreamReader(new FileInputStream(queriesPath), "UTF-8"));
        
        List<String> queries = new ArrayList<>();
        String line;
        while ((line = inQueries.readLine()) != null) {
            queries.add(line.substring(line.indexOf(":")+1));
        }
        
        return queries;
    }
    
    private static List<List<String>> readRelevance(String relevancePath) throws Exception {
        BufferedReader inRelevance = new BufferedReader(
                     new InputStreamReader(new FileInputStream(relevancePath), "UTF-8"));
        
        List<List<String>> listsRelevance = new ArrayList<>();
        String line;
        while ((line = inRelevance.readLine()) != null) {
            List<String> relev = Arrays.asList(line.split("\t"));
            listsRelevance.add(relev);
        }
        
        return listsRelevance;
    }
    
    private static void calcPrecisions(Index index, List<ScoredTextDocument> ranking, List<String> queries, List<List<String>> relevances) {
        int i = 0;
        double totalPat5 = 0;
        double totalPat10 = 0;
        for (String query : queries) {
            List<String> listRelevance = relevances.get(i);
            double hits5 = getNumHits(index, ranking, listRelevance, 5);
            double hits10 = getNumHits(index, ranking, listRelevance, 10);
            double pat5 = hits5 / 5;
            double pat10 = hits10 / 10;
            totalPat5 += pat5;
            totalPat10 += pat10;
                        
            i++;
        }
        totalPat5 = totalPat5 / queries.size();
        totalPat10 = totalPat10 / queries.size();
        
        System.out.println("\nPrecisión total: \tP@5 = " + totalPat5 + "\tP@10 = " + totalPat10 + "\n");
    }
    
    private static double getNumHits(Index index, List<ScoredTextDocument> listResults, List<String> listRelevance, int top) {
        if (listResults.isEmpty()) return 0;
        ListIterator<ScoredTextDocument> itr = listResults.listIterator();
        double numHits = 0;
        int i = 0;
        while(i++ < top) {
            ScoredTextDocument scoredDoc = itr.next();
            TextDocument doc = index.getDocument(scoredDoc.getDocumentId());
            String docName = doc.getName().substring(0, doc.getName().indexOf(".html"));
            if (listRelevance.contains(docName)) {
                numHits ++;
            }
        }
        
        return numHits;
    }
}
