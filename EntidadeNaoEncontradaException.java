// Exceção para entidade não encontrada

public class EntidadeNaoEncontradaException extends Exception {

    public EntidadeNaoEncontradaException(String mensagem) {

        super(mensagem);
    }
}