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
    
    public static void main(String[] args) throws AggregatorException {
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
        
        int relevanceIndex = 0;
        double pat5Tf = 0, pat10Tf = 0;
        double pat5Lit = 0, pat10Lit = 0;
        double pat5Prox = 0, pat10Prox = 0;
        double pat5TfPr = 0, pat10TfPr = 0;
        double pat5LitPr = 0, pat10LitPr = 0;
        double pat5TfProxPr = 0, pat10TfProxPr = 0;
        for (String query : queries) {
            
            /*
            TF-IDF SEARCHER
            */
            List<ScoredTextDocument> topTf = tfSearcher.search(query);
            pat5Tf += getNumHits(index, topTf, relevance.get(relevanceIndex), 5) / 5;
            pat10Tf += getNumHits(index, topTf, relevance.get(relevanceIndex), 10) / 10;
            
            /*
            LITERAL MATCHING
            */
            List<ScoredTextDocument> topLiteral = literalSearcher.search(query);
            pat5Lit += getNumHits(index, topLiteral, relevance.get(relevanceIndex), 5) / 5;
            pat10Lit += getNumHits(index, topLiteral, relevance.get(relevanceIndex), 10) / 10;
            
            /*
            PROXIMAL SEARCHER
            */
            List<ScoredTextDocument> topProximal = proxSearcher.search(query);
            pat5Prox += getNumHits(index, topProximal, relevance.get(relevanceIndex), 5) / 5;
            pat10Prox += getNumHits(index, topProximal, relevance.get(relevanceIndex), 10) / 10;
            
            List<ScoredTextDocument> topPageRank = pageRank.getTopNPages(10);
            
            /*
            TF-IDF + PAGERANK
            */
            List<List<ScoredTextDocument>> rankingsTfPr = new ArrayList<>();
            rankingsTfPr.add(topTf);rankingsTfPr.add(topPageRank);
            List<Double> weightsTfPr = new ArrayList<>();
            weightsTfPr.add(0.5);weightsTfPr.add(0.5);
            WeightedSumRankAggregator aggregator = new WeightedSumRankAggregator(rankingsTfPr, weightsTfPr);
            List<ScoredTextDocument> topTfPr = aggregator.aggregateRankings();
            pat5TfPr += getNumHits(index, topTfPr, relevance.get(relevanceIndex), 5) / 5;
            pat10TfPr += getNumHits(index, topTfPr, relevance.get(relevanceIndex), 10) / 10;
            
            /*
            LITERAL + PAGERANK
            */
            List<List<ScoredTextDocument>> rankingsLitPr = new ArrayList<>();
            rankingsLitPr.add(topTf);rankingsLitPr.add(topPageRank);
            List<Double> weightsLitPr = new ArrayList<>();
            weightsLitPr.add(0.5);weightsLitPr.add(0.5);
            aggregator = new WeightedSumRankAggregator(rankingsLitPr, weightsLitPr);
            List<ScoredTextDocument> topLitPr = aggregator.aggregateRankings();
            pat5LitPr += getNumHits(index, topLitPr, relevance.get(relevanceIndex), 5) / 5;
            pat10LitPr += getNumHits(index, topLitPr, relevance.get(relevanceIndex), 10) / 10;
            
            /*
            TF-IDF + PROXIMAL + PAGERANK
            */
            List<List<ScoredTextDocument>> rankingsTfProxPr = new ArrayList<>();
            rankingsTfProxPr.add(topTf);rankingsTfProxPr.add(topPageRank);
            List<Double> weightsTfProxPr = new ArrayList<>();
            weightsTfProxPr.add(0.3);weightsTfProxPr.add(0.4);weightsTfProxPr.add(0.3);
            aggregator = new WeightedSumRankAggregator(rankingsTfProxPr, weightsTfProxPr);
            List<ScoredTextDocument> topTfProxPr = aggregator.aggregateRankings();
            pat5TfProxPr += getNumHits(index, topTfProxPr, relevance.get(relevanceIndex), 5) / 5;
            pat10TfProxPr += getNumHits(index, topTfProxPr, relevance.get(relevanceIndex), 10) / 10;
        }
        
        System.out.println("--- TFIDFSearcher ---");
        System.out.println("P@5 = " + pat5Tf / queries.size() + "\tP@10 = " + pat10Tf / queries.size() + "\n");
        System.out.println("--- LiteralSearcher ---");
        System.out.println("P@5 = " + pat5Lit / queries.size() + "\tP@10 = " + pat10Lit / queries.size() + "\n");
        System.out.println("--- ProximalSearcher ---");
        System.out.println("P@5 = " + pat5Prox / queries.size() + "\tP@10 = " + pat10Prox / queries.size() + "\n");
        System.out.println("--- TF-IDF + PageRank ---");
        System.out.println("P@5 = " + pat5TfPr / queries.size() + "\tP@10 = " + pat10TfPr / queries.size() + "\n");
        System.out.println("--- Literal + PageRank ---");
        System.out.println("P@5 = " + pat5LitPr / queries.size() + "\tP@10 = " + pat10LitPr / queries.size() + "\n");
        System.out.println("--- Tf-IDF + Proximal + PageRank ---");
        System.out.println("P@5 = " + pat5TfProxPr / queries.size() + "\tP@10 = " + pat10TfProxPr / queries.size() + "\n");
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
