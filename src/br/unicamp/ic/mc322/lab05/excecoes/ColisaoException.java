// ColisaoException.java

/**
 * Exceção lançada quando ocorre uma colisão entre entidades ou
 * quando uma entidade tenta se mover para uma posição já ocupada.
 */
public class ColisaoException extends Exception {
    /**
     * Construtor que cria uma nova exceção de colisão com uma mensagem detalhada.
     * @param message A mensagem explicando a natureza da colisão.
     */
    public ColisaoException(String message) {
        super(message);
    }
}