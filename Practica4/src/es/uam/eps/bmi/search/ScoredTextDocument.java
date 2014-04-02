package es.uam.eps.bmi.search;

import java.util.Objects;

/**
 * Guarda la puntuación de un documento generalmente en consecuencia de una
 * consulta anterior.
 * @author Ari Handler - Adrián Lorenzo
 */
public class ScoredTextDocument implements Comparable{

    private String documentId;
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
     * Incrementa en una cantidad determinada la puntuación de un documento
     * @param score cantidad a incrementar
     * @return la nueva puntuación
     */
    public double addToScore(double score) {
        this.score += score;
        return this.score;
    }
    
    /**
     * Define la puntuación del documento
     * @param score nueva puntuación
     */
    public void setScore(double score) {
        this.score = score;
    }
    
    public void setId(String documentId) {
        this.documentId = documentId;
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

    /**
     * Determina si un documento es igual a otro, comparando sus identificadores.
     * @param obj Documento con el que comparar.
     * @return True si coinciden sus respectivos identificadores, false en caso contrario.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ScoredTextDocument other = (ScoredTextDocument) obj;
        return Objects.equals(this.documentId, other.documentId);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.documentId);
        return hash;
    }
}
