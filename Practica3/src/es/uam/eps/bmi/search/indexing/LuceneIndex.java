package es.uam.eps.bmi.search.indexing;

import es.uam.eps.bmi.search.TextDocument;
import es.uam.eps.bmi.search.parsing.HTMLSimpleParser;
import es.uam.eps.bmi.search.parsing.TextParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
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
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.index.TermPositions;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * Clase que implementa la interfaz {@link Index} con las funciones de indexación
 * de la API de Lucene.
 * 
 * @see <a href="https://lucene.apache.org/core/3_6_2/api/all">Lucene 3.6.2 API</a>
 * 
 * @author Ari Handler - Adrián Lorenzo
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
            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
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

    /**
     * Devuelve el <a href="https://lucene.apache.org/core/3_6_2/api/all/org/apache/lucene/index/IndexReader.html">IndexReader</a> 
     * de este índice para realizar búsquedas.
     * @return Lector del índice
     * @see <a href="https://lucene.apache.org/core/3_6_2/api/all/org/apache/lucene/index/IndexReader.html">IndexReader</a>
     */
    public IndexReader getIndexReader() {
        return indexReader;
    }
    
    @Override
    public List<String> getDocumentIds() {
        List<String> docIds = new ArrayList<>();
        // El índice asigna identificadores desde 0 hasta un número determinado
        // por maxDoc
        int maxDocs = indexReader.maxDoc();
        for (int id=0; id < maxDocs ; id++){
            if (indexReader.isDeleted(id)) {
                continue;
            }
            docIds.add(Integer.toString(id));
        }
        return docIds;
    }

    @Override
    public TextDocument getDocument(String documentId) {
        int id = Integer.parseInt(documentId);
        try {
            // No nos interesan los inexistentes
            if (indexReader.isDeleted(id)) {
                return null;
            }
            String path = indexReader.document(id).get("path");
            return new TextDocument(documentId, path);
        } catch (IOException ex) {
            Logger.getLogger(LuceneIndex.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<String> getTerms() {
        List<String> terms = new ArrayList<>();
        try {
            // Iterar sobre un enumerador de términos
            TermEnum te = indexReader.terms();
            while(te.next()) {
                // Solo nos quedaremos con los términos
                if (!"contents".equals(te.term().field()))
                    continue;
                Term term = te.term();
                terms.add(term.text());
            }
        } catch (IOException ex) {
            Logger.getLogger(LuceneIndex.class.getName()).log(Level.SEVERE, null, ex);
        }
        return terms;
    }

    @Override
    public List<Posting> getTermPostings(String term) {
        List<Posting> listPosts = new ArrayList<>();
        try {
            TermPositions posEnum = indexReader.termPositions(new Term("contents", term));
            while (posEnum.next()) {
                // Identificador de cada documento
                String docId = String.valueOf(posEnum.doc());
                // Numero de apariciones dentro del documento
                int freq = posEnum.freq();
                // Lista con las posiciones dentro del documento
                List<Long> positions = new ArrayList<>();
                for (int i = 0; i < freq ; i++) {
                    positions.add(new Long(posEnum.nextPosition()));
                }
                listPosts.add(new Posting(docId, freq, positions));
            }
        } catch (IOException ex) {
            Logger.getLogger(LuceneIndex.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return listPosts;
    }
    
    private void indexDocs(IndexWriter writer, File file, TextParser parser) throws IOException{
        // do not try to index files that cannot be read
        if (file.canRead()) {

            ZipInputStream zis;
            try {
              zis = new ZipInputStream(new FileInputStream(file));
            } catch (FileNotFoundException fnfe) {
              // at least on windows, some temporary files raise this exception with an "access denied" message
              // checking if the file can be read doesn't help
              return;
            }

            ZipEntry entry;
            while ((entry = zis.getNextEntry())!=null)
            {
                String filePath = file.getPath()+"/"+entry.getName();

                // make a new, empty document
                Document doc = new Document();

                // Add the path of the file as a field named "path".  Use a
                // field that is indexed (i.e. searchable), but don't tokenize 
                // the field into separate words and don't index term frequency
                // or positional information:
                Field pathField = new Field("path", filePath, Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS);
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
                int len = 0;
                byte[] buffer = new byte[2048];
                String text = "";
                while ((len = zis.read(buffer)) > 0) {
                    String chunk = new String(buffer, 0, len);
                    text += chunk;
                }
                
                if (text.indexOf("<body") >= 0)
                    text = text.substring(text.indexOf("<body"));
                
                //String text2 = parser.parse(text);
                
                doc.add(new Field("contents",parser.parse(text), Field.Store.YES, Field.Index.ANALYZED));

                if (writer.getConfig().getOpenMode() == IndexWriterConfig.OpenMode.CREATE) {
                  // New index, so we just add the document (no old document can be there):
                  System.out.println("adding " + filePath);
                  writer.addDocument(doc);
                } else {
                  // Existing index (an old copy of this document may have been indexed) so 
                  // we use updateDocument instead to replace the old one matching the exact 
                  // path, if present:
                  System.out.println("updating " + filePath);
                  writer.updateDocument(new Term("path", filePath), doc);
                }
            }
            zis.close();
        }
    }      
    
    /**
     * 
     * La aplicación recibe dos argumentos de entrada: la ruta de la carpeta que contiene la colección de
     * documentos con los que crear el índice, y la ruta de la carpeta en la que almacenar el índice creado
     * 
     * @param args 
     */
    public static void  main(String[] args) {
        
        String usage = "java es.uam.eps.bmi.indexing.LuceneIndex"
                     + " [-index INDEX_PATH] [-docs DOCS_PATH]\n\n"
                     + "This indexes the documents in DOCS_PATH, creating a Lucene index"
                     + "in INDEX_PATH that can be searched with LuceneSearcher";
        String indexPath = "index";
        LuceneIndex index = new LuceneIndex();
        HTMLSimpleParser p = new HTMLSimpleParser();
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
        index.build(docsPath,indexPath,p);
        
        index.load(indexPath);
        
        List<String> docIds = index.getDocumentIds();
        
        TextDocument td = index.getDocument(docIds.get(4));
        
        System.out.println("DocID = "+td.getId()+" Path = "+td.getName());
        
        List<String> terms = index.getTerms();
//        for (int i=0; i < terms.size() ; i++) {
//            System.out.println(terms.get(i));
//        }
        List<Posting> postings = index.getTermPostings("obama");
        
        System.out.println();
    
    }

    
}
