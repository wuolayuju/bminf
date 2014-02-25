/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.search.indexing;

import es.uam.eps.bmi.search.TextDocument;
import es.uam.eps.bmi.search.parsing.HTMLSimpleParser;
import es.uam.eps.bmi.search.parsing.TextParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 * @author Ari Handler - Adrián Lorenzo
 */
public class BasicIndex implements Index{

    private List<String> terms;
    private static int CUR_DOC_ID = 0;
    
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
            
            // Se abre el stream de lectura del fichero comprimido
            ZipInputStream zis = new ZipInputStream(new FileInputStream(docDir));
            
            ZipEntry entry;
            while ((entry = zis.getNextEntry())!=null)
            {
                String filePath = docDir.getPath()+"/"+entry.getName();
                
                // make a new, empty document
                TextDocument doc = new TextDocument(Integer.toString(CUR_DOC_ID++), filePath);
                
                // Lectura del contenido del documento
                String contents = parseEntry(zis, textParser);

            }
            zis.close();
            
        } catch (IOException ex) {
            Logger.getLogger(BasicIndex.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    
    private String parseEntry(ZipInputStream zis, TextParser parser) throws IOException {
        int len;
        byte[] buffer = new byte[4096];
        String text = "";
        while ((len = zis.read(buffer)) > 0) {
            String chunk = new String(buffer, 0, len);
            text += chunk;
        }

        if (text.indexOf("<body") >= 0)
            text = text.substring(text.indexOf("<body"));

        return parser.parse(text);
    }
    
}
