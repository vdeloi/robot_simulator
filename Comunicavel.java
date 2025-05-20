// Interface Sensoriavel

// REVER // Aqui falta terminar!!
public interface Comunicavel {

    void enviarMensagem(Comunicavel destinatario, String mensagem, CentralComunicacao central) throws RoboDesligadoException, ErroComunicacaoException; // [cite: 175]
    void receberMensagem(String remetente, String mensagem) throws RoboDesligadoException; // [cite: 176]
    String getIdComunicacao(); // Para identificar o robô na comunicação

}