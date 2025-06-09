package src.br.unicamp.ic.mc322.lab05.excecoes;
// RecursoInsuficienteException.java

/**
 * Exceção lançada quando uma ação requer um recurso (por exemplo, energia, itens)
 * que não está disponível em quantidade suficiente.
 * Um exemplo seria um robô coletor tentando coletar mais itens do que existem
 * em um local, ou um robô tentando realizar uma ação que consome energia
 * sem ter energia suficiente.
 */
public class RecursoInsuficienteException extends Exception {
    /**
     * Construtor que cria uma nova exceção de recurso insuficiente com uma mensagem detalhada.
     * @param message A mensagem explicando qual recurso está faltando ou é insuficiente.
     */
    public RecursoInsuficienteException(String message) {
        super(message);
    }
}