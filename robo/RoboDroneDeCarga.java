package robo;

import ambiente.*;
import comunicacao.CentralComunicacao;
import comunicacao.Comunicavel;
import missao.Missao;

/**
     * Representa um drone de carga, um tipo especializado de {@link RoboAereo}.
     * Possui capacidade de carga e pode executar ações autônomas.
     * Implementa {@link Autonomo}.
     */
    public class RoboDroneDeCarga extends RoboAereo implements Autonomo {
        private int carga; // Carga atual do drone
        private final int cargaMaxima; // Capacidade máxima de carga

        /**
         * Construtor para RoboDroneDeCarga.
         * @param id Identificador do robô.
         * @param x Posição inicial X.
         * @param y Posição inicial Y.
         * @param z Posição inicial Z (altitude).
         * @param direcao Direção inicial.
         * @param altitudeMaxima Altitude máxima de voo.
         * @param numHelices Número de hélices.
         * @param cargaInicial Carga inicial do drone.
         * @param cargaMaxima Capacidade máxima de carga.
         */
        public RoboDroneDeCarga(String id, int x, int y, int z, String direcao, int altitudeMaxima,
                                int numHelices, int cargaInicial, int cargaMaxima) {
            super(id, x, y, z, direcao, altitudeMaxima, numHelices);
            this.cargaMaxima = Math.max(0, cargaMaxima); // Garante que a carga máxima não seja negativa
            // Garante que a carga inicial esteja entre 0 e a carga máxima
            this.carga = Math.max(0, Math.min(cargaInicial, this.cargaMaxima));
        }
        
        // Getters para carga
        public int getCarga() { return carga; }
        public int getCargaMaxima() { return cargaMaxima; }

        /**
         * Carrega o drone com uma determinada quantidade.
         * @param quantidade A quantidade a ser carregada (deve ser positiva).
         * @throws AcaoNaoPermitidaException Se a quantidade for inválida ou exceder a capacidade máxima.
         * @throws RoboDesligadoException Se o robô estiver desligado.
         */
        public void carregar(int quantidade) throws AcaoNaoPermitidaException, RoboDesligadoException {
            if (getEstado() == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(getId() + " desligado.");
            if (quantidade <= 0) throw new AcaoNaoPermitidaException("Quantidade para carregar deve ser positiva.");
            // Verifica se carregar a quantidade excederá a carga máxima
            if (this.carga + quantidade > this.cargaMaxima) {
                throw new AcaoNaoPermitidaException("Excederia carga máxima. Carga atual: " + this.carga + ", tentando: " + quantidade + ", Max: " + this.cargaMaxima);
            }
            this.carga += quantidade; // Aumenta a carga
            System.out.println(getId() + " carregou " + quantidade + ". Carga atual: " + this.carga + "/" + this.cargaMaxima);
        }
        
        /**
         * Descarrega uma determinada quantidade do drone.
         * @param quantidade A quantidade a ser descarregada (deve ser positiva).
         * @throws AcaoNaoPermitidaException Se a quantidade for inválida ou não houver carga suficiente.
         * @throws RoboDesligadoException Se o robô estiver desligado.
         */
        public void descarregar(int quantidade) throws AcaoNaoPermitidaException, RoboDesligadoException {
            if (getEstado() == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(getId() + " desligado.");
            if (quantidade <= 0) throw new AcaoNaoPermitidaException("Quantidade para descarregar deve ser positiva.");
            // Verifica se há carga suficiente para descarregar
            if (quantidade > this.carga) {
                throw new AcaoNaoPermitidaException("Não pode descarregar " + quantidade + ". Carga atual: " + this.carga);
            }
            this.carga -= quantidade; // Diminui a carga
            System.out.println(getId() + " descarregou " + quantidade + ". Carga atual: " + this.carga + "/" + this.cargaMaxima);
        }

        /**
         * Executa a tarefa específica do drone de carga.
         * Lógica simples: se vazio na base (0,0), tenta carregar. Se com carga e fora da base, move para a base. Se com carga na base, descarrega.
         * @param ambiente O ambiente de simulação.
         * @throws RoboDesligadoException Se o robô estiver desligado.
         * @throws AcaoNaoPermitidaException Se a ação de carregar/descarregar/mover não for permitida.
         * @throws ColisaoException Se houver colisão ao mover.
         * @throws ForaDosLimitesException Se mover para fora dos limites.
         * @throws RecursoInsuficienteException (Pode ser lançada por `carregar` se fosse implementado para pegar de um local, mas aqui `carregar` apenas aumenta o contador).
         * @throws ErroComunicacaoException (Declarada para compatibilidade, não usada aqui).
         */
        @Override
        // A assinatura corresponde a RoboAereo.executarTarefa (que agora inclui RecursoInsuficienteException implicitamente de Robo.java)
        public void executarTarefa(Ambiente ambiente) throws RoboDesligadoException, AcaoNaoPermitidaException, ColisaoException, ForaDosLimitesException, RecursoInsuficienteException, ErroComunicacaoException {
            if (getEstado() == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(getId() + " desligado.");
            System.out.println(getId() + " (Drone Carga) status: Carga " + carga + "/" + cargaMaxima);
            
            // Lógica de decisão para a tarefa do drone
            if (carga == 0 && getX() == 0 && getY() == 0) { // Se está na base (0,0) e vazio
                System.out.println(getId() + " na base e vazio, tentando carregar...");
                if (cargaMaxima > 0) { // Só tenta carregar se tiver capacidade
                    carregar(1); // Tenta carregar 1 unidade (exemplo)
                } else {
                    System.out.println(getId() + " não pode carregar (capacidade máxima é 0).");
                }
            } else if (carga > 0 && (getX() != 0 || getY() != 0)) { // Se tem carga e não está na base
                System.out.println(getId() + " tem carga, tentando mover para base (0,0) para descarregar.");
                int targetX = 0; int targetY = 0; // Coordenadas da base
                // Calcula o deslocamento para chegar à base
                int dx = Integer.compare(targetX, getX()); // Retorna -1, 0, ou 1
                int dy = Integer.compare(targetY, getY());
                if (dx !=0 || dy !=0) moverRelativamente(ambiente, dx, dy, 0); // Move um passo em direção à base
            } else if (carga > 0 && getX() == 0 && getY() == 0) { // Se tem carga e está na base
                 System.out.println(getId() + " na base com carga, descarregando...");
                 descarregar(carga); // Descarrega toda a carga
            } else {
                 System.out.println(getId() + " aguardando instruções ou em condição não prevista pela tarefa simples.");
            }
        }
        
        /**
         * Executa a próxima ação autônoma do drone de carga.
         * Lógica: Se tem carga e não está na base, move para a base. Se descarregado, move para um ponto de coleta e carrega.
         * @param ambiente O ambiente de simulação.
         * @throws RoboDesligadoException Se o robô estiver desligado.
         * @throws AcaoNaoPermitidaException Se a ação não for permitida.
         * @throws ColisaoException Se houver colisão.
         * @throws ForaDosLimitesException Se sair dos limites.
         */
        @Override
        public void executarProximaAcaoAutonoma(Ambiente ambiente) throws RoboDesligadoException, AcaoNaoPermitidaException, ColisaoException, ForaDosLimitesException {
            if (getEstado() == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(getId() + " desligado.");
            System.out.println(getId() + " (Drone Carga Autônomo) executando...");

            if (carga > 0 && (getX() != 0 || getY() != 0)) { // Se tem carga e não está na base (0,0)
                int targetX = 0; int targetY = 0; // Ponto de descarga (base)
                // Calcula o movimento para a base
                int dx = Integer.compare(targetX, getX());
                int dy = Integer.compare(targetY, getY());
                if (dx !=0 || dy !=0) { // Se não está na base
                    System.out.println("Autônomo: Movendo para base (" + dx + "," + dy + ")");
                    moverRelativamente(ambiente, dx, dy, 0); // Move um passo em direção à base
                } else { // Se chegou na base e ainda tem carga (caso raro, pois deveria descarregar)
                     try { if (carga > 0) descarregar(carga); }
                     catch (RoboDesligadoException | AcaoNaoPermitidaException e) { System.err.println("Autônomo: Falha ao descarregar - " + e.getMessage());}
                }
            } else if (carga < cargaMaxima) { // Se não está com carga máxima (prioriza carregar)
                // Define um ponto de coleta (ex: canto oposto do ambiente)
                int targetX = ambiente.getLargura() -1; 
                int targetY = ambiente.getProfundidade() -1; 
                // Calcula o movimento para o ponto de coleta
                int dx = Integer.compare(targetX, getX());
                int dy = Integer.compare(targetY, getY());
                if (dx !=0 || dy !=0) { // Se não está no ponto de coleta
                    System.out.println("Autônomo: Movendo para coleta (" + dx + "," + dy + ")");
                    moverRelativamente(ambiente, dx, dy, 0); // Move um passo em direção ao ponto de coleta
                } else { // Se chegou no ponto de coleta
                    try { if (cargaMaxima - carga > 0) carregar(cargaMaxima - carga); } // Carrega até a capacidade máxima
                    catch (RoboDesligadoException | AcaoNaoPermitidaException e) { System.err.println("Autônomo: Falha ao carregar - " + e.getMessage());}
                }
            } else { // Se está com carga máxima e na base (ou outra condição não tratada)
                 System.out.println(getId() + " (Autônomo) - Carga máxima e na base, ou outra condição.");
            }
        }
        /**
         * Retorna a representação visual do drone de carga.
         * @return 'D' para Drone de Carga.
         */
         @Override
        public char getRepresentacao() { return 'D';} 
    }
    