// ForaDosLimitesException.java

/**
 * Exceção lançada quando uma entidade tenta se mover para uma posição
 * ou realizar uma ação que está fora das dimensões definidas do {@link Ambiente}.
 */
public class ForaDosLimitesException extends Exception {
    /**
     * Construtor que cria uma nova exceção de "fora dos limites" com uma mensagem detalhada.
     * @param message A mensagem explicando qual ação ou posição resultou nesta exceção.
     */
    public ForaDosLimitesException(String message) {
        super(message);
    }
}