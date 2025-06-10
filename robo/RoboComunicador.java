package robo;

import ambiente.*;
import comunicacao.CentralComunicacao;
import comunicacao.Comunicavel;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Representa um robô terrestre com capacidade de comunicação.
 * Estende {@link RoboTerrestre} e implementa {@link Comunicavel}.
 */
public class RoboComunicador extends RoboTerrestre implements Comunicavel {
    
    // CORRIGIDO: Adicionado campo para guardar a referência da central de comunicação
    private CentralComunicacao centralComunicacao;

    /**
     * Construtor para RoboComunicador.
     * CORRIGIDO: Recebe a CentralComunicacao como parâmetro.
     */
    public RoboComunicador(String id, int x, int y, String direcao, int velocidadeMaxima, CentralComunicacao central) {
        super(id, x, y, direcao, velocidadeMaxima);
        this.centralComunicacao = central; // Armazena a central
    }

    @Override
    public void enviarMensagem(CentralComunicacao central, Comunicavel destinatario, String mensagem) throws RoboDesligadoException, ErroComunicacaoException {
        if (getEstado() == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(getId() + " desligado.");
        if (!(destinatario instanceof Robo)) throw new ErroComunicacaoException("Destinatário não é um robô válido.");
        if (((Robo)destinatario).getEstado() == EstadoRobo.DESLIGADO) throw new ErroComunicacaoException("Destinatário " + ((Robo)destinatario).getId() + " está desligado.");
        
        System.out.println(getId() + " (Comunicador) enviando para " + ((Robo)destinatario).getId() + ": " + mensagem);
        central.registrarMensagem(this.getId(), ((Robo)destinatario).getId(), mensagem);
        destinatario.receberMensagem(this.getId(), mensagem);
    }

    @Override
    public void receberMensagem(String remetenteId, String mensagem) throws RoboDesligadoException {
        if (getEstado() == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(getId() + " desligado.");
        System.out.println(getId() + " (Comunicador) recebeu de " + remetenteId + ": " + mensagem);
    }
    
    @Override 
    public void executarTarefa(Ambiente ambiente) throws RoboDesligadoException, AcaoNaoPermitidaException, ColisaoException, ForaDosLimitesException, RecursoInsuficienteException, ErroComunicacaoException {
        if (getEstado() == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(getId() + " desligado.");
        System.out.println(getId() + " (Comunicador) está ocioso, procurando alguém para conversar...");
        
        List<Robo> outrosComunicaveis = ambiente.getEntidades().stream()
            .filter(e -> e instanceof Robo && e instanceof Comunicavel && e != this && ((Robo)e).getEstado() == EstadoRobo.LIGADO)
            .map(e -> (Robo)e)
            .collect(Collectors.toList());

        if (!outrosComunicaveis.isEmpty()) {
            Comunicavel destinatario = (Comunicavel) outrosComunicaveis.get(0);
            // CORRIGIDO: Usa a central armazenada no campo 'this.centralComunicacao'
            enviarMensagem(this.centralComunicacao, destinatario, "Olá de " + getId() + "!");
        } else {
            System.out.println(getId() + " não encontrou ninguém para conversar agora.");
        }
    }
    
    @Override
    public char getRepresentacao() { return 'C';} 
}