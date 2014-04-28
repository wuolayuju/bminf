package es.uam.eps.bmi.social.graph;

/**
 * Clase que representa un nodo de un grafo al que se le ha asignado una 
 * determinada puntuación.
 * @author Ari Handler - Adrián Lorenzo
 */
public class ScoredNode {
    
    private final String node;
    private final double score;

	/**
	 * Construye un nodo puntuado.
	 * @param node identificador del nodo.
	 * @param score puntuación del nodo.
	 */
    public ScoredNode(String node, double score) {
        this.node = node;
        this.score = score;
    }

	/**
	 * Obtiene el identificador del nodo.
	 * @return identificador del nodo.
	 */
    public String getNode() {
        return node;
    }

	/**
	 * Obtiene la puntuación del nodo.
	 * @return puntuación del nodo.
	 */
    public double getScore() {
        return score;
    }
    
    
}
