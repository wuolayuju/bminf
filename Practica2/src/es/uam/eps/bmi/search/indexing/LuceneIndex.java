/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.search.indexing;

import es.uam.eps.bmi.search.TextDocument;
import es.uam.eps.bmi.search.parsing.TextParser;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 *
 * @author e185318
 */
public class LuceneIndex implements Index{

    @Override
    public void build(String inputCollectionPath, String outputIndexPath, TextParser textParser) {
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
        try {
            Directory dir = FSDirectory.open(new File(outputIndexPath));
        } catch (IOException ex) {
            System.err.println("An exception "+ex.getClass()+" was fired with"
                    + " message "+ex.getMessage());
        }
        
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_31);
        IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_31, analyzer);
    }

    @Override
    public void load(String indexPath) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> getDocumentIds() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TextDocument getDocument(String documentId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> getTerms() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Posting> getTermPostings(String term) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Posting> getDocumentPostings(String documentId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
