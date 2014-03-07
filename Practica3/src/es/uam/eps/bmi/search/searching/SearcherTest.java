/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.search.searching;

import es.uam.eps.bmi.search.ScoredTextDocument;
import es.uam.eps.bmi.search.indexing.AdvancedIndex;
import es.uam.eps.bmi.search.indexing.BasicIndex;
import es.uam.eps.bmi.search.indexing.Index;
import es.uam.eps.bmi.search.indexing.StemIndex;
import es.uam.eps.bmi.search.indexing.StopwordIndex;
import es.uam.eps.bmi.search.parsing.HTMLStemParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author uam
 */
public class SearcherTest {

    private static BasicIndex basicIndex = null;
    private static StopwordIndex stopwordIndex = null;
    private static StemIndex stemIndex = null;
    private static AdvancedIndex advancedIndex = null;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String collectionsPath = "collections";
        String queriesPath = "queries";
        
        for(int i=0;i<args.length;i++) {
            if(args[i].compareTo("-collections")==0) {
                collectionsPath = args[i+1];
                i++;
            }
            if(args[i].compareTo("-queries")==0) {
                queriesPath = args[i+1];
                i++;
            }
        }
        
        // Path a cada una de las colecciones
        String path_1k = collectionsPath + "clueweb-1K/";
        String path_10k = collectionsPath + "clueweb-10K/";
        String path_100k = collectionsPath + "clueweb-100K/";
        
        // Instanciacion de los 4 tipos de índices
        basicIndex = new BasicIndex();
        stopwordIndex = new StopwordIndex();
        stemIndex = new StemIndex();
        advancedIndex = new AdvancedIndex();
        
        testCollection(path_1k);
        testCollection(path_10k);
        testCollection(path_100k);
    }

    private static void testCollection(String collectionPath) throws Exception {
        
        List<String> indexesPaths = new ArrayList<>();
        
        // Path a cada tipo de índice
        String basicIndexPath = collectionPath + "basic";
        indexesPaths.add(basicIndexPath);
        
        String stopwordIndexPath = collectionPath + "stopword";
        indexesPaths.add(stopwordIndexPath);
        
        String stemIndexPath = collectionPath + "stem";
        indexesPaths.add(stemIndexPath);
        
        String advancedIndexPath = collectionPath + "advanced";
        indexesPaths.add(advancedIndexPath);
        
        // Path a la colección de documentos comprimida
        String docsPath = collectionPath + "docs.zip";
        
        // Fichero de stopwords y tipo de stemmizador
        String stopPath = collectionPath + "stopwords.txt";
        String stemmerType = HTMLStemParser.ENGLISH_STEMMER;
        
        // Fichero de consultas y relevancias
        String queriesPath = collectionPath + "queries.txt";
        
        String collectionName = collectionPath.substring(
                collectionPath.lastIndexOf("-"),
                collectionPath.lastIndexOf("/"));
        String relevancePath = collectionPath + "relevance-" + collectionName + ".txt";
        
        for (String indexPath : indexesPaths) {
            basicIndex.load(indexPath);
            
            testIndex(basicIndex, queriesPath, relevancePath);
            
            /**
            * TODO : Buscar en cada indice las consultas y construir P@5 y P@10
            */ 
        }
    }

    private static void testIndex(Index index, String queriesPath, String relevancePath) throws Exception {
        // Lectura de las consultas y las relevancias
        List<String> listQueries = readQueries(queriesPath);
        List<List<String>> listsRelevance = readRelevance(relevancePath);
        
        BooleanSearcher booleanSearcher = new BooleanSearcher();
        
        List<Double> precisions = calcPrecisions(booleanSearcher, listQueries, listsRelevance);
        
    }

    private static List<Double> calcPrecisions(Searcher searcher, List<String> queries, List<List<String>> relevances) {
        
        int i = 0;
        for (String query : queries) {
            List<ScoredTextDocument> listResults = searcher.search(query);
            List<String> listRelevance = relevances.get(i);
        }
        
        
        return null;
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
    
}