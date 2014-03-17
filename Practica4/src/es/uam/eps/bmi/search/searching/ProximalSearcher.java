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
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

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
    
    @Override
    public void build(Index index) {
        this.index = index;
    }

    @Override
    public List<ScoredTextDocument> search(String query) {
        
        if (index == null) 
            return null;
        
        HashMap<String,Double> docTFIDF = new HashMap<>();
        List<ScoredTextDocument> listScorDocs = new ArrayList<>();
        
        String[] queryArray = query.split(" ");
        if (queryArray.length == 0) 
            return listScorDocs;
        
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
        List<Posting> listPostingsPerDoc = new ArrayList<>();
        for (int indexDoc = 0; indexDoc < listQueryPostings.get(0).size() ; indexDoc++) 
        {
            // Obtenemos los Posting de cada clausula por cada documento
            listPostingsPerDoc.clear();
            for (List<Posting> listPostingClause : listQueryPostings) {
                listPostingsPerDoc.add(listPostingClause.get(indexDoc));
            }
            
            double scoreDoc = getProximalScore(listPostingsPerDoc, queryArray.length);
            
        }
        
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
    
    public List<ScoredTextDocument> getIntervals(List<List<Posting>> intersection)
    {
//2. a ← −∞
//3. b ← max min l ∩ (a, ∞) |l ∈ P d
//4. Si “b = ∞” fin // O bien antes del paso 3, a = min long l l ∈ P d
//5. a ← min max l ∩ (0, b) | l ∈ P d
//6. Devolver [a, b], volver a 3

        List<Long> bminList = new ArrayList(); 
        List<Long> amaxList = new ArrayList(); 
        for(int docIndex=0;docIndex<intersection.get(0).size();docIndex++)
        {
            for(int i=0;i<intersection.size();i++)
            {
                long min = Collections.min(intersection.get(i).get(docIndex)
                        .getTermPositions());
                
                bminList.add(min);
            }
            long b = Collections.max(bminList);
 
            for(int i=0;i<intersection.size();i++)
            {
                int sizeTermPositions = intersection.get(i).get(docIndex)
                        .getTermPositions().size();
                
                long max = Long.MIN_VALUE;
                
                for(int j=0;j<sizeTermPositions;j++)
                {
                    if(intersection.get(i).get(docIndex)
                        .getTermPositions().get(j) > b)
                        
                        break;
                    
                    else if(intersection.get(i).get(docIndex)
                        .getTermPositions().get(j) > max)
                        
                        max = intersection.get(i).get(docIndex)
                        .getTermPositions().get(j);
                }
                amaxList.add(max);
                
            }
            long a = Collections.min(amaxList);
            
        }
        
        
        return null;
    }
    
 public static void main(String[] args) {
        
        List<Long> pos2 = new ArrayList();
        List<Long> pos1 = new ArrayList();
        List<Long> pos = new ArrayList();
        pos.add((long)1);
        pos.add((long) 6);
        pos1.add((long)2);
        pos1.add((long) 7);
        pos2.add((long)3);
        pos2.add((long) 8);
        List <Posting> postings = new ArrayList();
        List <Posting> postings1 = new ArrayList();
        List <Posting> postings2 = new ArrayList();
        List <List<Posting>> post = new ArrayList<>();
        Posting p1 = new Posting("04",4,pos);
        Posting p2 = new Posting("03",4,pos);
        Posting p4 = new Posting("01",5,pos1);
        Posting p3 = new Posting("02",5,pos1);
        postings.add(p1);
        postings.add(p2);
        postings1.add(p4);
        post.add(postings);
        postings1.add(p3);
        post.add(postings1);
        postings2.add(new Posting("02",6,pos2));
        postings2.add(new Posting("01",6,pos2));
        post.add(postings2);
        List<List<Posting>> ojete = intersection(post);
        
        if(ojete.isEmpty())
            System.out.print("muy bien");
      

    }

    private double getProximalScore(List<Posting> listPostingsPerDoc, int numTerms) {
        List<Integer> currentIndexes = new ArrayList<>();
        // Inicializamos a 0 los índices
        for(int i = 0; i < numTerms ; i++)
            currentIndexes.add(0);
        
        int aIndex = 0;
        long b = -1;
        int listIndexOfB;
        int tempI = 0;
        do{
            // Calculo de B
            for (Posting post : listPostingsPerDoc) {
                tempI++;
                // min l ∩ (a, ∞) 
                long tempB = Collections.min(
                        post.getTermPositions().subList(aIndex, post.getTermPositions().size()));
                // b ← max min l ∩ (a, ∞) 
                if (tempB > b) {
                    b = tempB;
                    listIndexOfB = tempI++;
                }
            }
            
            
            
            
        }while(true);
        
        //return 0.0;
    }
}
