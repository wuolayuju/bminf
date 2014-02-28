/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.search.indexing;

import es.uam.eps.bmi.search.TextDocument;
import es.uam.eps.bmi.search.parsing.TextParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import it.unimi.dsi.fastutil.io.BinIO;

/**
 *
 * @author Ari Handler - Adrián Lorenzo
 */
public class BasicIndex implements Index{

    private static int CUR_DOC_ID = 0;
    private HashMap<String, List<Posting>> indexMap;
    private List<TextDocument> documents;
    
    @Override
    public void build(String inputCollectionPath, String outputIndexPath, TextParser textParser) {
        try {
            // Apertura de la carpeta de documentos
            if (inputCollectionPath == null){
                System.err.println("No documents directory provided");
                return;
            }
            
            final File docDir = new File(inputCollectionPath);
            if (!docDir.exists() || !docDir.canRead()) {
                System.err.println("Document directory '" +docDir.getAbsolutePath()+ "' does not exist or is not readable, please check the path");
                return;
            }
            
            // Creación de la carpeta de indexación
            if (outputIndexPath == null) {
                System.err.println("No index path provided");
                return;
            }
            
            System.out.println("Indexing to directory '" + outputIndexPath + "'...");
            
            // Creacion del HashMap de términos
            indexMap = new HashMap<>();
            // Creacion de la lista de documentos indexados
            documents = new ArrayList<>();
            try (ZipInputStream zis = new ZipInputStream(new FileInputStream(docDir))) {
                ZipEntry entry;
                while ((entry = zis.getNextEntry())!=null)
                {
                    System.out.println("Indexing document "+entry.getName()+" ...");
                    // make a new, empty document
                    TextDocument doc = new TextDocument(Integer.toString(CUR_DOC_ID++), entry.getName());
                    documents.add(doc);
                    
                    // Lectura del contenido del documento.
                    // Se eliminan los simbolos de puntuación y cualquier otro
                    // término no alfanumérico.
                    String[] contents = parseEntryToArray(zis, textParser);
                    
                    // Indexación de los contenidos del documento
                    indexDoc(doc, contents);
                }
            }
            
            // Escritura del indice en disco con FASTUTIL
            indexToDisk(outputIndexPath);
            
            // Eliminar las referencias en RAM
            indexMap = null;
            documents = null;
            
        } catch (IOException ex) {
            Logger.getLogger(BasicIndex.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void load(String indexPath) {
        try {
            this.indexMap = 
                    (HashMap<String, List<Posting>>) BinIO.loadObject(
                            new File(indexPath+File.separator+"index.st"));
            this.documents = 
                    (List<TextDocument>) BinIO.loadObject(
                            new File(indexPath+File.separator+"docs.st"));
        } catch (ClassNotFoundException | IOException ex) {
            Logger.getLogger(BasicIndex.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<String> getDocumentIds() {
        ArrayList<String> docIds = new ArrayList<>();
        Iterator<TextDocument> itr = documents.listIterator();
        while(itr.hasNext()) {
            docIds.add(itr.next().getId());
        }
        
        return docIds.isEmpty() ? null : docIds;
    }

    @Override
    public TextDocument getDocument(String documentId) {
        return documents.get(documents.indexOf(new TextDocument(documentId, null)));
    }

    @Override
    public List<String> getTerms() {
        return new ArrayList<>(indexMap.keySet());
    }

    @Override
    public List<Posting> getTermPostings(String term) {
        if (indexMap == null)
            return null;
        
        return indexMap.get(term);
    }
    
    private String[] parseEntryToArray(ZipInputStream zis, TextParser parser) throws IOException {
        int len;
        byte[] buffer = new byte[4096];
        String text = "";
        while ((len = zis.read(buffer)) > 0) {
            String chunk = new String(buffer, 0, len);
            text += chunk;
        }

        text = parser.parse(text);
        
        return text.split(" ");
    }
    
    private void indexDoc(TextDocument doc, String[] contents) {
        for ( int i = 0; i < contents.length ; i++ ) {
            String term = contents[i];
            if (term.isEmpty()) continue;

            // Comprobamos si el token ya existia
            List<Posting> oldPostings = indexMap.get(term);

            // Si no es así, lo creamos
            if (oldPostings == null) {
                oldPostings = new ArrayList<>();
                ArrayList<Long> positions = new ArrayList<>();
                positions.add((long) i);
                oldPostings.add(new Posting(doc.getId(), 1, positions));
            }
            // Si existe, se actualiza la lista de postings
            else {
                // Por cada posting en la lista antigua
                Iterator<Posting> itrPost = oldPostings.iterator();
                boolean updated = false;
                while (itrPost.hasNext()) {
                    Posting post = itrPost.next();
                    // Se comprueba si el posting para el documento
                    // actual ya existia
                    if (post.getDocumentId().equals(doc.getId())) {

                        // Si es asi, se actualizan las frecuencias y posiciones
                        oldPostings.remove(post);
                        post.incrFrequency(i);
                        oldPostings.add(post);
                        updated = true;
                        break;
                    }
                }

                // Si no se encontraba el posting del documento actual,
                // se crea uno nuevo
                if (updated == false) {
                    ArrayList<Long> positions = new ArrayList<>();
                    positions.add((long) i);
                    oldPostings.add(new Posting(doc.getId(), 1, positions));
                }
            }

            // Por último, añadimos el término y su lista de postings
            // al HashMap
            indexMap.put(term, oldPostings);
        }
    }
    
    private void indexToDisk(String indexPath) throws IOException {
        
        BinIO.storeObject(this.indexMap, new File(indexPath+File.separator+"index.st"));
        BinIO.storeObject(this.documents, new File(indexPath+File.separator+"docs.st"));
    }
}
