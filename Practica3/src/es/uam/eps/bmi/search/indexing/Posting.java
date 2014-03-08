package es.uam.eps.bmi.search.indexing;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Clase que representa un <i>posting</i> asociado a un determinado término.
 * <p>
 * La estructura básica de un <i>posting</i> viene dada por el término asociado
 * al <i>posting</i> (en este caso la relación término/posting es indirecta), 
 * así como el documento al que se hace referencia, la frecuencia dentro de 
 * dicho documento y las posiciones en este (tantas como indique la frecuencia):
 * <p>
 * Normalmente, un término tiene asociada una lista de <i>postings</i>, uno
 * por cada documento en los que aparece:
 * <p>
 * término -> | d0 | freq0 | pos1 pos2 pos3 | | d1 | freq1 | pos1 pos2 pos3 | ...
 *             
 * @author Ari Handler - Adrián Lorenzo
 */
public class Posting implements Serializable{
    
    private final String documentId;
    private int termFrequency;
    private final List<Long> termPositions;

    public Posting(String documentId, int termFrequency, List<Long> termPositions) {
        this.documentId = documentId;
        this.termFrequency = termFrequency;
        this.termPositions = termPositions;
    }

    /**
     * Devuelve una lista con las posiciones del término asociado en el documento
     * en Long.
     *
     * @return Lista con las posiciones.
     */
    public List<Long> getTermPositions() {
        return termPositions;
    }

    /**
     * Devuelve la frecuencia de aparición del término asociado en el documento
     *
     * @return Entero que representa la frecuencia.
     */
    public int getTermFrequency() {
        return termFrequency;
    }

    /**
     * Devuelve el identificador único del documento
     *
     * @return Cadena de caracteres con el identificador del documento.
     */
    public String getDocumentId() {
        return documentId;
    }
    
    /**
     * Añade una nueva posición del término, incrementando en 1 la frecuencia
     * del término
     * @param newPosition Siguiente aparición del término en el documento.
     */
    public void incrFrequency(long newPosition) {
        termPositions.add(newPosition);
        termFrequency++;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Posting other = (Posting) obj;
        if (!Objects.equals(this.documentId, other.documentId)) {
            return false;
        }
        return true;
    }


}
