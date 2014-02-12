package es.uam.eps.bmi.search;

/**
 * Guarda la puntuación de un documento generalmente en consecuencia de una
 * consulta anterior.
 * @author Ari Handler - Adrián Lorenzo
 */
public class ScoredTextDocument implements Comparable{

    private final String documentId;
    private double score;

    /**
     * Construye la puntuación de un documento
     * @param documentId Identificador del documento
     * @param score Puntuación de este documento
     */
    public ScoredTextDocument(String documentId, double score) {
        this.documentId = documentId;
        this.score = score;
    }

    /**
     * Devuelve la puntuación del documento
     *
     * @return La puntuación en formato de doble precisión
     */
    public double getScore() {
        return score;
    }
    
    /**
     * Devuelve el identificador del documento
     * @return Cadena de caracteres del identificador
     */
    public String getDocumentId() {
        return documentId;
    }
    
    /**
     * Compara las puntuaciones de dos documentos según el orden lexicográfico
     * de sus identificadores
     * @param o Documento puntuado con el que comparar.
     * @return 0 si si son iguales, un valor negativo si el argumento pasado
     * es mayor lexicográficamente que el objeto invocante y un valor positivo
     * si este último es mayor que el argumento.
     * @see String
     */
    @Override
    public int compareTo(Object o) {
        if (o == null) {
            return 1;
        }
        if (getClass() != o.getClass()) {
            return 1;
        }
        final ScoredTextDocument other = (ScoredTextDocument) o;
        return this.documentId.compareTo(other.getDocumentId());
    }
    
}
