package robo;

import ambiente.*;
import comunicacao.CentralComunicacao;
import comunicacao.Comunicavel;
import robo.modulos.ModuloComunicacao; 

import java.util.List;
import java.util.stream.Collectors;

/**
 * Representa um robô terrestre com capacidade de comunicação.
 * Estende {@link RoboTerrestre} e implementa {@link Comunicavel}.
 * A lógica de comunicação é delegada para um ModuloComunicacao (Composição).
 */
public class RoboComunicador extends RoboTerrestre implements Comunicavel {
    /**
     * Módulo de comunicação que contém a lógica de envio e recebimento de mensagens.
     * Este módulo é responsável por interagir com a CentralComunicacao
     * e gerenciar as mensagens entre robôs.
     */
    private final ModuloComunicacao moduloComunicacao;

    // que precisa chamar enviarMensagem com a instância da central.
    private final CentralComunicacao centralComunicacao;

    /**
     * Construtor para RoboComunicador.
     * Recebe a CentralComunicacao e a utiliza para inicializar o ModuloComunicacao interno.
     */
    public RoboComunicador(String id, int x, int y, String direcao, int velocidadeMaxima, CentralComunicacao central) {
        super(id, x, y, direcao, velocidadeMaxima);
        this.centralComunicacao = central; // Guarda a referência da central
       
        this.moduloComunicacao = new ModuloComunicacao(this, central);
    }

    /**
     * Envia uma mensagem para outro robô.
     * A chamada é DELEGADA para o módulo de comunicação, que contém a lógica real.
     * O parâmetro 'central' da interface é ignorado, pois o módulo já possui a referência correta.
     */
    @Override
    public void enviarMensagem(CentralComunicacao central, Comunicavel destinatario, String mensagem) throws RoboDesligadoException, ErroComunicacaoException {
       
        this.moduloComunicacao.enviarMensagem(destinatario, mensagem);
    }

    /**
     * Recebe uma mensagem de outro robô.
     * Esta lógica pode permanecer no robô, pois trata-se de "reagir" a um estímulo.
     */
    @Override
    public void receberMensagem(String remetenteId, String mensagem) throws RoboDesligadoException {
        if (getEstado() == EstadoRobo.DESLIGADO) {
            throw new RoboDesligadoException(getId() + " desligado.");
        }
        System.out.println(getId() + " (Comunicador) recebeu de " + remetenteId + ": " + mensagem);
    }

    /**
     * Tarefa específica do comunicador: encontrar outro robô comunicável e enviar uma mensagem.
     */
    @Override
    public void executarTarefa(Ambiente ambiente) throws RoboDesligadoException, AcaoNaoPermitidaException, ColisaoException, ForaDosLimitesException, RecursoInsuficienteException, ErroComunicacaoException {
        if (getEstado() == EstadoRobo.DESLIGADO) {
            throw new RoboDesligadoException(getId() + " desligado.");
        }
        System.out.println(getId() + " (Comunicador) está ocioso, procurando alguém para conversar...");

        // Lógica para encontrar outros robôs que podem se comunicar
        List<Robo> outrosComunicaveis = ambiente.getEntidades().stream()
            .filter(e -> e instanceof Robo && e instanceof Comunicavel && e != this && ((Robo)e).getEstado() != EstadoRobo.DESLIGADO)
            .map(e -> (Robo)e)
            .collect(Collectors.toList());

        if (!outrosComunicaveis.isEmpty()) {
            Comunicavel destinatario = (Comunicavel) outrosComunicaveis.get(0); // Pega o primeiro que encontrar
            String mensagem = "Olá de " + getId() + "!";
            System.out.println(getId() + " encontrou " + ((Robo)destinatario).getId() + " e vai enviar uma mensagem.");

            // O método enviarMensagem, por sua vez, delegará a ação para o módulo.
            this.enviarMensagem(this.centralComunicacao, destinatario, mensagem);
        } else {
            System.out.println(getId() + " não encontrou ninguém para conversar agora.");
        }
    }

    @Override
    public char getRepresentacao() {
        return 'C';
    }
}