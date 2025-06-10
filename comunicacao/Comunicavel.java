package comunicacao;

import ambiente.RoboDesligadoException;
import ambiente.ErroComunicacaoException;

/**
 * Interface para entidades que têm a capacidade de enviar e receber mensagens.
 * Define os métodos necessários para a comunicação através de uma CentralComunicacao.
 */
public interface Comunicavel {
    /**
     * Envia uma mensagem para outra entidade comunicável através da central de comunicação.
     *
     * @param central       A central de comunicação que registrará e mediará a mensagem.
     * @param destinatario  A entidade comunicável que receberá a mensagem.
     * @param mensagem      O conteúdo da mensagem a ser enviada.
     * @throws RoboDesligadoException   Se o remetente ou o destinatário (se for um robô) estiver desligado.
     * @throws ErroComunicacaoException Se ocorrer um erro durante o processo de comunicação (ex: destinatário inválido).
     */
    void enviarMensagem(CentralComunicacao central, Comunicavel destinatario, String mensagem) throws RoboDesligadoException, ErroComunicacaoException;

    /**
     * Recebe uma mensagem de outra entidade.
     * Este método é tipicamente chamado pela central ou pelo remetente após o envio.
     *
     * @param remetenteId O ID da entidade que enviou a mensagem.
     * @param mensagem    O conteúdo da mensagem recebida.
     * @throws RoboDesligadoException Se o robô receptor estiver desligado e não puder processar a mensagem.
     */
    void receberMensagem(String remetenteId, String mensagem) throws RoboDesligadoException;
}