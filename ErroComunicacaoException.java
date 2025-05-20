// Exceção para erros de comunicação

public class ErroComunicacaoException extends Exception {

    public ErroComunicacaoException(String mensagem) {

        super(mensagem);
    }
}