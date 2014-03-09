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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author uam
 */
public class SearcherTest {

    private static BasicIndex basicIndex = null;
    private static StopwordIndex stopwordIndex = null;
    private static StemIndex stemIndex = null;
    private static AdvancedIndex advancedIndex = null;
    
    private static BufferedWriter writerPrecs = null;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException, Exception {
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
        
        writerPrecs = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(path_1k +"precision.txt"), "utf-8"));
        
        testCollection(path_1k);
        //testCollection(path_10k);
        //testCollection(path_100k);
        
        writerPrecs.close();
    }

    private static void testCollection(String collectionPath) throws Exception {
        
        // Path a cada tipo de índice
        String basicIndexPath = collectionPath + "basic";
        
        String stopwordIndexPath = collectionPath + "stopword";
        
        String stemIndexPath = collectionPath + "stem";
        
        String advancedIndexPath = collectionPath + "advanced";
        
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
        String relevancePath = collectionPath + "relevance" + collectionName + ".txt";

        // Test de cada tipo de indice con los buscadores
        writerPrecs.write("Basic\n");
        System.out.print("Basic\n");
        basicIndex.load(basicIndexPath);
        testIndex(basicIndex, queriesPath, relevancePath);

        writerPrecs.write("Stopword\n");
        System.out.print("Stopword\n");
        stopwordIndex.load(stopwordIndexPath);
        testIndex(stopwordIndex, queriesPath, relevancePath);
        
        writerPrecs.write("Stem\n");
        System.out.print("Stem\n");
        stemIndex.load(stemIndexPath);
        testIndex(stemIndex, queriesPath, relevancePath);
        
        writerPrecs.write("Advanced\n");
        System.out.print("Advanced\n");
        advancedIndex.load(advancedIndexPath);
        testIndex(advancedIndex, queriesPath, relevancePath);
    }

    private static void testIndex(Index index, String queriesPath, String relevancePath) throws Exception {
        // Lectura de las consultas y las relevancias
        List<String> listQueries = readQueries(queriesPath);
        List<List<String>> listsRelevance = readRelevance(relevancePath);
        
        writerPrecs.write("Boolean\n");
        System.out.print("Boolean\n");
        BooleanSearcher booleanSearcher = new BooleanSearcher();
        booleanSearcher.build(index);
        calcPrecisions(booleanSearcher, listQueries, listsRelevance);
        
        writerPrecs.write("TF-IDF\n");
        System.out.print("TF-IDF\n");
        TFIDFSearcher tfidfSearcher = new TFIDFSearcher();
        tfidfSearcher.build(index);
        tfidfSearcher.setTopResults(10);
        calcPrecisions(tfidfSearcher, listQueries, listsRelevance);
        
        writerPrecs.write("Literal\n");
        System.out.print("Literal\n");
        //LiteralMatchingSearcher literalSearcher = new LiteralMatchingSearcher();
        //literalSearcher.build(index);
        //literalSearcher.setTopResults(10);
        //calcPrecisions(literalSearcher, listQueries, listsRelevance);
    }

    private static void calcPrecisions(Searcher searcher, List<String> queries, List<List<String>> relevances) throws IOException {
        
        int i = 1;
        for (String query : queries) {
            writerPrecs.write(i + ":\t");
            System.out.print(i + ":\t");
            List<ScoredTextDocument> listResults = searcher.search(query);
            List<String> listRelevance = relevances.get(i);
            int hits5 = getNumHits(listResults, listRelevance, 5);
            int hits10 = getNumHits(listResults, listRelevance, 10);
            double pat5 = hits5 / 5;
            double pat10 = hits10 / 10;
            writerPrecs.write(pat5 + "\t" + pat10 + "\n");
            System.out.print(pat5 + "\t" + pat10 + "\n");
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

    private static int getNumHits(List<ScoredTextDocument> listResults, List<String> listRelevance, int top) {
        ListIterator<ScoredTextDocument> itr = listResults.listIterator();
        int numHits = 0;
        int i = 0;
        while(i++ < top) {
            ScoredTextDocument doc = itr.next();
            if (listRelevance.contains(doc.getDocumentId())) {
                numHits ++;
            }
        }
        
        return numHits;
    }
    
}