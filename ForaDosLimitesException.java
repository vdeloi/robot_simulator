// Exceção para ação fora dos limites

public class ForaDosLimitesException extends Exception {

    public ForaDosLimitesException(String mensagem) {

        super(mensagem);
    }
}