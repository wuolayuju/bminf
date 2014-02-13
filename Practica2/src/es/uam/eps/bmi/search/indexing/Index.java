/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.search.indexing;

import es.uam.eps.bmi.search.TextDocument;
import es.uam.eps.bmi.search.parsing.TextParser;
import java.util.List;

/**
 * Interfaz que contiene los métodos básicos de un indexador sobre Lucene;
 * construir el índice en disco, cargarlo a RAM, obtener la ruta de un documento,
 * los postings de un determinado término, etc...
 * @author Ari Handler - Adrián Lorenzo
 */
public interface Index {
    
    /**
     * Construye en disco un índice dado un directorio de documentos que indexar.
     * Si ocurre algún error (i.e. la ruta a los documentos no existe), se mostrará
     * por el canal de errores la causa de dicho error. El modo de creación del 
     * índice es siempre de creación, por lo que si existe un índice previo en 
     * la ruta ouputIndexPath, se sobreescribirá dicho índice.
     * @param inputCollectionPath Ruta a la colección de documentos a indexar.
     * @param outputIndexPath Ruta al directorio donde se almacenará el índice.
     * @param textParser Parser que se utilizará para leer los documentos.
     */
    public void build(String inputCollectionPath, String outputIndex, TextParser textParser);
    
    /**
     * Carga en memoria un lector del índice almacenado en la ruta pasada
     * como argumento al método. Se puede provocar la excepción CorruptIndexException si existe algún
     * error con el índice.
     * @param indexPath Ruta al índice en disco.
     * @see <a href="http://lucene.apache.org/core/3_6_2/api/all/org/apache/lucene/index/CorruptIndexException.html">CorruptIndexException</a>
     */
    public void load(String indexPath);
    
    /**
     * Devuelve una lista con todos los identificadores de documentos del índice.
     *  Si no hay documentos en el índice, se devuelve una lista vacía.
     * @return Lista de cadenas de caracteres que representan todos los 
     * identificadores de documentos.
     */
    public List<String> getDocumentIds();

    /**
     * Devuelve un documento con su ruta dado su identificador único.
     * @param documentId Identificador del documento.
     * @return TextDocument contruido para ese identificador.
     * @see TextDocument
     */
    public TextDocument getDocument(String documentId);
    
    /**
     * Devuelve la lista de términos del índice.
     * <p>
     * <strong>NOTA:</strong> El orden de los términos devueltos no es determinable.
     * @return Una lista con el texto de todos los términos del índice.
     */
    public List<String> getTerms();
    
    /**
     * Devuelve la lista de {@link Posting} de un determinado término.
     * @param term Término del que obtener su lista de postings.
     * @return Una lista con los postings asociados a dicho término.
     * @see Posting
     */
    public List<Posting> getTermPostings (String term);
    
    public List<Posting> getDocumentPostings (String documentId);
}
