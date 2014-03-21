/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.search.searching;

import es.uam.eps.bmi.search.ScoredTextDocument;
import es.uam.eps.bmi.search.indexing.Index;
import es.uam.eps.bmi.search.indexing.Posting;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

/**
 *
 *  Buscador que implementa una búsqueda literal. Esto es, una búsqueda en la 
 * que las palabras de la consulta van entre comillas, y los documentos 
 * devueltos deben contener estos términos de forma consecutiva y en el mismo
 * orden en que se dan en la consulta. 
 * 
 * @author Ari Handler - Adrián Lorenzo
 */
public class ProximalSearcher implements Searcher{

    
    private Index index;
    private int TOP_RES = 5;
    
    @Override
    public void build(Index index) {
        this.index = index;
    }

    /**
     * Define la cantidad de resultados devueltos al realizar una búsqueda.
     * @param n Nuevo límite de resultados.
     */
    public void setTopResults(int n) {
        
        if(n>0) this.TOP_RES = n;
    }
    
    @Override
    public List<ScoredTextDocument> search(String query) {
        
        if (index == null) 
            return null;
        
        HashMap<String,Double> docTFIDF = new HashMap<>();
        
        String[] queryArray = query.split(" ");
        if (queryArray.length == 0) 
            return null;
        
        // Lista de tantas listas como cláusulas de la consulta
        List<List<Posting>> listQueryPostings = new ArrayList<>();
        
        for (String clause : queryArray) {
            // Por cada cláusula, se construye una lista de postings
            List<Posting> postingList = index.getTermPostings(clause);
            listQueryPostings.add(postingList);
        }
        
        // Intersección de las listas de Posting de cada cláusula
        listQueryPostings = intersection(listQueryPostings);
        
        // Si no existe la interseccion entre dichas listas, devolvemos vacio
        if (listQueryPostings.isEmpty())
            return new ArrayList<>();
        else if (listQueryPostings.get(0).isEmpty())
            return new ArrayList<>();
        
        /*
         * Al estar ordenadas las listas de Posting por su docId, los Posting 
         * de cada documento se encuentran en las mismas posiciones en cada lista de clausula.
         * 
         * Cada una de las listas de Posting de cada clausula deben tener
         * el mismo tamaño, por lo que iterando sobre los indices de la primera
         * de ellas es suficiente para recorrer las de todas las demas
         */
        PriorityQueue<ScoredTextDocument> heapScores = 
                new PriorityQueue<>(TOP_RES, new ProximalSearcher.ScoredTextDocumentComparator());
        List<Posting> listPostingsPerDoc = new ArrayList<>();
        for (int indexDoc = 0; indexDoc < listQueryPostings.get(0).size() ; indexDoc++) 
        {
            // Obtenemos los Posting de cada clausula por cada documento
            listPostingsPerDoc.clear();
            for (List<Posting> listPostingClause : listQueryPostings) {
                listPostingsPerDoc.add(listPostingClause.get(indexDoc));
            }
            
            double score = getProximalScore(listPostingsPerDoc);

            ScoredTextDocument scoredDoc = new ScoredTextDocument(
                    listQueryPostings.get(0).get(indexDoc).getDocumentId(),
                    score);
            
            /***********HEAP**************/
            if (heapScores.size() == TOP_RES) {
                if (heapScores.peek().getScore() < scoredDoc.getScore()){
                    heapScores.poll();
                    heapScores.offer(scoredDoc);
                }
            } else {
                heapScores.offer(scoredDoc);
            }
            /***********FIN HEAP**********/
        }
        
        // Conversión a lista del heap de puntuaciones
        List<ScoredTextDocument> listScorDocs = new ArrayList<>();
                listScorDocs.addAll(heapScores);
        
        Collections.sort(listScorDocs, new ProximalSearcher.ScoredTextDocumentComparator());

        Collections.reverse(listScorDocs);
        
        return listScorDocs;
    }
    
    
    public static List<List<Posting>> intersection(List<List<Posting>> list1) {
        
        List<List<Posting>> intersectionList = new ArrayList();
        List <Posting> auxlist = new ArrayList();
        List <String> listdocumentIds = new ArrayList();
        int i;
        
        for (Posting t : list1.get(0))
        {
            for(i=1;i<list1.size();i++)
            {
            
                if(!list1.get(i).contains(t))
                    break;
                
            }
            if(i==list1.size())
                listdocumentIds.add(t.getDocumentId());
        }
        
        for(i=0;i<list1.size();i++)
        {

            for(int j=0;j<list1.get(i).size();j++)
            {
                
                if(listdocumentIds.contains(list1.get(i).get(j).getDocumentId()))
                {
                    auxlist.add(list1.get(i).get(j));
                }
            }
            if(!auxlist.isEmpty())
            {
                intersectionList.add(auxlist);
                 auxlist = new ArrayList();
            }
            
        }
        return intersectionList;
    }

    
    private static double getProximalScore(List<Posting> listPostingsPerDoc) {
        //2. a ← −∞
        //3. b ← max min l ∩ (a, ∞) |l ∈ P d
        //4. Si “b = ∞” fin // O bien antes del paso 3, a = min long l l ∈ P d
        //5. a ← min max l ∩ (0, b) | l ∈ P d
        //6. Devolver [a, b], volver a 3
        
        long a = -1;
        long b;
        boolean isBInfinite = false;
        double score = 0.0;
        do{
            // Calculo de B
            long tempB = -1;
            for (Posting post : listPostingsPerDoc) {
                List<Long> l = post.getTermPositions();
                // min l ∩ (a, ∞) 
                int indexCeil = getIndexCeilOf(l, a);
                if (indexCeil < 0) {
                    /* Condicion de parada */
                    isBInfinite = true;
                    break;
                }
                long currentMin = Collections.min(l.subList(getIndexCeilOf(l, a), l.size()));
                // b ← max min l ∩ (a, ∞) 
                if (currentMin > tempB) {
                    tempB = currentMin;
                }
            }
            
            if (isBInfinite) break; /* Flag de condicion de parada */
            
            b = tempB;
            // Calculo de A
            long tempA = Long.MAX_VALUE;
            for (Posting post : listPostingsPerDoc) {
                List<Long> l = post.getTermPositions();
                // max l ∩ (0, b) 
                int indexFloor = getIndexFloorOf(l, b);
                indexFloor = indexFloor == 0 ? 1 : indexFloor;
                long currentMax = Collections.max(l.subList(0, indexFloor));
                // a ← min max l ∩ (0, b) 
                if (currentMax < tempA) {
                    tempA = currentMax;
                }
            }
            a = tempA;
            
            // Calculo del score y acumulacion del mismo
            //System.out.println("[" + a + "," + b + "]");
            
            double denominator = b - a - listPostingsPerDoc.size() + 2;
            score += 1 / denominator;
            
            //System.out.println("denominator = " + denominator);
            
        }while(true);
        
        //System.out.println("score = " + score);
        
        return score;
    }
    
    /* Devuelve el indice de una lista ordenada a partir del cual sus elementos
     * son mayores que un valor
    */
    private static int getIndexCeilOf(List<Long> list, long a) {
        for(int i = 0; i < list.size() ; i++) {
            if (list.get(i) > a)
                return i;
        }
        return -1;
    }
    
    /* Devuelve el indice de una lista ordenada hasta el cual sus elementos
     * son menores que un valor
    */
    private static int getIndexFloorOf(List<Long> list, long b) {
        for(int i = 0; i < list.size() ; i++) {
            if (list.get(i) > b) {
                return i;
            }
        }
        return list.size();
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
    public static void main(String[] args) {

        List<Long> pos1 = new ArrayList<>();
        List<Long> pos2 = new ArrayList<>();
        List<Long> pos3 = new ArrayList<>();

        pos1.add((long)0);pos1.add((long)11);pos1.add((long)22);
        pos2.add((long)5);pos2.add((long)7);pos2.add((long)15);pos2.add((long)31);pos2.add((long)36);pos2.add((long)39);pos2.add((long)41);pos2.add((long)43);pos2.add((long)46);
        pos3.add((long)8);pos3.add((long)14);pos3.add((long)42);pos3.add((long)44);pos3.add((long)56);

        Posting posting1 = new Posting("0", 3, pos1);
        Posting posting2 = new Posting("1", 9, pos2);
        Posting posting3 = new Posting("2", 5, pos3);

        List<Posting> postingList = new ArrayList<>();
        postingList.add(posting1);
        postingList.add(posting2);
        postingList.add(posting3);

        getProximalScore(postingList);
        
        System.out.println("=============================");
        
        pos1.clear();
        pos1.add((long)5);pos1.add((long)7);pos1.add((long)15);pos1.add((long)31);pos1.add((long)36);pos1.add((long)39);pos1.add((long)41);pos1.add((long)43);pos1.add((long)46);
        pos2.clear();
        pos2.add((long)8);pos2.add((long)14);pos2.add((long)42);pos2.add((long)44);pos2.add((long)56);

        posting1 = new Posting("0", 9, pos1);
        posting2 = new Posting("1", 5, pos2);
        
        postingList.clear();
        postingList.add(posting1);
        postingList.add(posting2);
        
        getProximalScore(postingList);
    }
}
