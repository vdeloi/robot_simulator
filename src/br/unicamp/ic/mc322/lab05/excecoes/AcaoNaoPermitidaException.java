package br.unicamp.ic.mc322.lab05.excecoes;
// AcaoNaoPermitidaException.java

/**
 * Exceção lançada quando uma ação solicitada não pode ser executada
 * por não ser permitida pelas regras do sistema ou pelo estado atual
 * de uma entidade. Por exemplo, tentar mover um robô que não deveria
 * se mover verticalmente para uma nova coordenada Z.
 */
public class AcaoNaoPermitidaException extends Exception {
    /**
     * Construtor que cria uma nova exceção com uma mensagem detalhada.
     * @param message A mensagem explicando o motivo da ação não ser permitida.
     */
    public AcaoNaoPermitidaException(String message) {
        super(message);
    }
}