// Exception para indicar que o robô está desligado

public class RoboDesligadoException extends Exception {

    public RoboDesligadoException(String mensagem) {

        super(mensagem);
    }
}