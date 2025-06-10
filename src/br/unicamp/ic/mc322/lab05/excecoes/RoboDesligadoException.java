package br.unicamp.ic.mc322.lab05.excecoes;
// RoboDesligadoException.java

/**
 * Exceção lançada quando uma ação é tentada em um {@link Robo} que está
 * no estado {@link EstadoRobo#DESLIGADO} e a ação requer que ele esteja ligado.
 */
public class RoboDesligadoException extends Exception {
    /**
     * Construtor que cria uma nova exceção de robô desligado com uma mensagem detalhada.
     * @param message A mensagem explicando que o robô está desligado e não pode realizar a ação.
     */
    public RoboDesligadoException(String message) {
        super(message);
    }
}