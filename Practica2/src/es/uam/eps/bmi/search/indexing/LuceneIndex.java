/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.search.indexing;

import es.uam.eps.bmi.search.TextDocument;
import es.uam.eps.bmi.search.parsing.Parser;
import es.uam.eps.bmi.search.parsing.TextParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 *
 * @author e185318
 */
public class LuceneIndex implements Index{
    
    
    private Analyzer analyzer;
    private IndexWriterConfig iwc;
    private IndexWriter writer;
    private IndexReader indexReader;
    
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
            
            analyzer = new StandardAnalyzer(Version.LUCENE_36);
            iwc = new IndexWriterConfig(Version.LUCENE_36, analyzer);
            writer = new IndexWriter(dir, iwc);
            
            indexDocs(writer, docDir, textParser);
            writer.close();
            
        } catch (IOException ex) {
            System.err.println("An exception "+ex.getClass()+" was fired with"
                    + " message "+ex.getMessage());
        }
        
      
    }

    @Override
    public void load(String indexPath) {
        try {
            indexReader = IndexReader.open(FSDirectory.open(new File(indexPath)));
        } catch (CorruptIndexException ex) {
            Logger.getLogger(LuceneIndex.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LuceneIndex.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<String> getDocumentIds() {
        try {
            List<String> docIds = new ArrayList<>();
            TermDocs td = indexReader.termDocs();
            while(td.next()) {
                int docId = td.doc();
                docIds.add(Integer.toString(docId));
            }
            return docIds;
        } catch (IOException ex) {
            Logger.getLogger(LuceneIndex.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
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
    
    private void indexDocs(IndexWriter writer, File file, TextParser parser) throws IOException{
     // do not try to index files that cannot be read
    if (file.canRead()) {
      if (file.isDirectory()) {
        String[] files = file.list();
        // an IO error could occur
        if (files != null) {
          for (int i = 0; i < files.length; i++) {
            indexDocs(writer, new File(file, files[i]), parser);
          }
        }
      } else {

        FileInputStream fis;
        try {
          fis = new FileInputStream(file);
        } catch (FileNotFoundException fnfe) {
          // at least on windows, some temporary files raise this exception with an "access denied" message
          // checking if the file can be read doesn't help
          return;
        }

        try {

          // make a new, empty document
          Document doc = new Document();

          // Add the path of the file as a field named "path".  Use a
          // field that is indexed (i.e. searchable), but don't tokenize 
          // the field into separate words and don't index term frequency
          // or positional information:
          Field pathField = new Field("path", file.getPath(), Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS);
          pathField.setIndexOptions(FieldInfo.IndexOptions.DOCS_ONLY);
          doc.add(pathField);

          // Add the last modified date of the file a field named "modified".
          // Use a NumericField that is indexed (i.e. efficiently filterable with
          // NumericRangeFilter).  This indexes to milli-second resolution, which
          // is often too fine.  You could instead create a number based on
          // year/month/day/hour/minutes/seconds, down the resolution you require.
          // For example the long value 2011021714 would mean
          // February 17, 2011, 2-3 PM.
          NumericField modifiedField = new NumericField("modified");
          modifiedField.setLongValue(file.lastModified());
          doc.add(modifiedField);

          // Add the contents of the file to a field named "contents".  Specify a Reader,
          // so that the text of the file is tokenized and indexed, but not stored.
          // Note that FileReader expects the file to be in UTF-8 encoding.
          // If that's not the case searching for special characters will fail.
          BufferedReader read = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
           String text = "", line;
            
            do {
                line = read.readLine();
                text += line;
            } while (line != null);
            
            
          doc.add(new Field("contents",parser.parse(text), Field.Store.YES, Field.Index.ANALYZED));

          if (writer.getConfig().getOpenMode() == IndexWriterConfig.OpenMode.CREATE) {
            // New index, so we just add the document (no old document can be there):
            System.out.println("adding " + file);
            writer.addDocument(doc);
          } else {
            // Existing index (an old copy of this document may have been indexed) so 
            // we use updateDocument instead to replace the old one matching the exact 
            // path, if present:
            System.out.println("updating " + file);
            writer.updateDocument(new Term("path", file.getPath()), doc);
          }
          
        } finally {
          fis.close();
        }
      }
    }
  }       
    
    
    public static void  main(String[] args) {
        
        String usage = "java org.apache.lucene.demo.IndexFiles"
                     + " [-index INDEX_PATH] [-docs DOCS_PATH]\n\n"
                     + "This indexes the documents in DOCS_PATH, creating a Lucene index"
                     + "in INDEX_PATH that can be searched with SearchFiles";
        String indexPath = "index";
        LuceneIndex index = new LuceneIndex();
        Parser p = new Parser();
        String docsPath = null;
        boolean create = true;
        for(int i=0;i<args.length;i++) {
            switch (args[i]) {
                case "-index":
                    indexPath = args[i+1];
                    i++;
                    break;
                case "-docs":
                    docsPath = args[i+1];
                    i++;
                    break;
            }
        }
      //  index.build(docsPath,indexPath,p);
        
        index.load(indexPath);
        
        List<String> docIds = index.getDocumentIds();
        
        System.out.println();
    
    }
    
}
