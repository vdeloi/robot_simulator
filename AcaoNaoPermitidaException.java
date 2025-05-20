// Exceção para ações não permitidas

public class AcaoNaoPermitidaException extends Exception {

    public AcaoNaoPermitidaException(String mensagem) {

        super(mensagem);
    }
}