// Interface Comunicavel

public interface Comunicavel {

    void enviarMensagem(Comunicavel destinatario, String mensagem, CentralComunicacao central) throws RoboDesligadoException, ErroComunicacaoException;
    
    void receberMensagem(String remetenteIdComunicacao, String mensagem) throws RoboDesligadoException;
    
    String getIdComunicacao(); // Para identificar o robô na comunicação
}