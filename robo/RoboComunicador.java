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
        /**
         * Construtor para RoboComunicador.
         * @param id Identificador do robô.
         * @param x Posição inicial X.
         * @param y Posição inicial Y.
         * @param direcao Direção inicial.
         * @param velocidadeMaxima Velocidade máxima do robô terrestre.
         */
        public RoboComunicador(String id, int x, int y, String direcao, int velocidadeMaxima) {
            super(id, x, y, direcao, velocidadeMaxima);
        }

        /**
         * Envia uma mensagem para outro robô comunicável.
         * @param central A {@link CentralComunicacao} para registrar a mensagem.
         * @param destinatario O {@link Comunicavel} que receberá a mensagem.
         * @param mensagem O conteúdo da mensagem.
         * @throws RoboDesligadoException Se o remetente ou o destinatário estiverem desligados.
         * @throws ErroComunicacaoException Se o destinatário não for um robô válido ou houver outro erro.
         */
        @Override
        public void enviarMensagem(CentralComunicacao central, Comunicavel destinatario, String mensagem) throws RoboDesligadoException, ErroComunicacaoException {
            if (getEstado() == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(getId() + " desligado.");
            // Verifica se o destinatário é um Robô (necessário para checar estado)
            if (!(destinatario instanceof Robo)) throw new ErroComunicacaoException("Destinatário não é um robô válido.");
            // Verifica se o robô destinatário está desligado
            if (((Robo)destinatario).getEstado() == EstadoRobo.DESLIGADO) throw new ErroComunicacaoException("Destinatário " + ((Robo)destinatario).getId() + " está desligado.");
            
            System.out.println(getId() + " (Comunicador) enviando para " + ((Robo)destinatario).getId() + ": " + mensagem);
            central.registrarMensagem(this.getId(), ((Robo)destinatario).getId(), mensagem); // Registra na central
            destinatario.receberMensagem(this.getId(), mensagem); // Entrega a mensagem ao destinatário
        }

        /**
         * Recebe uma mensagem de outro robô.
         * @param remetenteId O ID do robô que enviou a mensagem.
         * @param mensagem O conteúdo da mensagem recebida.
         * @throws RoboDesligadoException Se este robô (receptor) estiver desligado.
         */
        @Override
        public void receberMensagem(String remetenteId, String mensagem) throws RoboDesligadoException {
            if (getEstado() == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(getId() + " desligado.");
            System.out.println(getId() + " (Comunicador) recebeu de " + remetenteId + ": " + mensagem);
        }
        
        /**
         * Executa a tarefa específica do robô comunicador: procurar outro robô comunicável e enviar uma saudação.
         * @param ambiente O ambiente de simulação.
         * @throws RoboDesligadoException Se o robô estiver desligado.
         * @throws AcaoNaoPermitidaException (Declarada para compatibilidade).
         * @throws ColisaoException (Declarada para compatibilidade).
         * @throws ForaDosLimitesException (Declarada para compatibilidade).
         * @throws RecursoInsuficienteException (Declarada para compatibilidade).
         * @throws ErroComunicacaoException Se falhar ao enviar a mensagem.
         */
        @Override 
        // A assinatura corresponde a RoboTerrestre.executarTarefa (que agora inclui ErroComunicacaoException de Robo.java)
        public void executarTarefa(Ambiente ambiente) throws RoboDesligadoException, AcaoNaoPermitidaException, ColisaoException, ForaDosLimitesException, RecursoInsuficienteException, ErroComunicacaoException {
            if (getEstado() == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(getId() + " desligado.");
            System.out.println(getId() + " (Comunicador) está ocioso, procurando alguém para conversar...");
            
            // Procura por outros robôs que são Comunicavel, estão ligados e não são ele mesmo
            List<Robo> outrosComunicaveis = ambiente.getEntidades().stream()
                .filter(e -> e instanceof Robo && e instanceof Comunicavel && e != this && ((Robo)e).getEstado() == EstadoRobo.LIGADO)
                .map(e -> (Robo)e)
                .collect(Collectors.toList());

            if (!outrosComunicaveis.isEmpty()) { // Se encontrou alguém
                Comunicavel destinatario = (Comunicavel) outrosComunicaveis.get(0); // Pega o primeiro da lista
                enviarMensagem(centralComunicacao, destinatario, "Olá de " + getId() + "!"); // Envia uma saudação
            } else {
                System.out.println(getId() + " não encontrou ninguém para conversar agora.");
            }
        }
        /**
         * Retorna a representação visual do robô comunicador.
         * @return 'C' para Comunicador.
         */
        @Override
        public char getRepresentacao() { return 'C';} 
    }