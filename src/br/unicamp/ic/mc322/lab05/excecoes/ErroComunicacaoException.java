package br.unicamp.ic.mc322.lab05.excecoes;
// ErroComunicacaoException.java

/**
 * Exceção lançada quando ocorre um erro durante a tentativa de comunicação
 * entre entidades. Por exemplo, se o destinatário não for encontrado ou
 * não puder receber mensagens.
 */
public class ErroComunicacaoException extends Exception {
    /**
     * Construtor que cria uma nova exceção de erro de comunicação com uma mensagem detalhada.
     * @param message A mensagem explicando a causa do erro de comunicação.
     */
    public ErroComunicacaoException(String message) {
        super(message);
    }
}