package robo;

import ambiente.*;
import sensores.Sensor;
import sensores.Sensoreavel;

/**
     * Representa um robô aéreo genérico.
     * Pode se mover no espaço 3D até uma altitude máxima.
     * Implementa {@link Sensoreavel} para interagir com sensores.
     */
    public static class RoboAereo extends Robo implements Sensoreavel {
        protected final int altitudeMaxima; // Altitude máxima que o robô aéreo pode atingir
        protected int numHelices;           // Número de hélices do robô aéreo

        /**
         * Construtor para RoboAereo.
         * @param id Identificador do robô.
         * @param x Posição inicial X.
         * @param y Posição inicial Y.
         * @param z Posição inicial Z (altitude).
         * @param direcao Direção inicial.
         * @param altitudeMaxima Altitude máxima de voo.
         * @param numHelices Número de hélices.
         */
        public RoboAereo(String id, int x, int y, int z, String direcao, int altitudeMaxima, int numHelices) {
            super(id, x, y, z, direcao);
            this.altitudeMaxima = Math.max(0, altitudeMaxima); // Garante que a altitude máxima não seja negativa
            this.numHelices = numHelices;
            // Ajusta a altitude inicial se estiver fora dos limites permitidos
            if (z > this.altitudeMaxima) {
                 System.out.println("Aviso: Alt inicial ("+z+") do RoboAereo "+id+" excede max ("+this.altitudeMaxima+"). Ajustando.");
                 this.z = this.altitudeMaxima; // Define para altitude máxima se exceder
            }
            if (z < 0) {
                 System.out.println("Aviso: Alt inicial ("+z+") do RoboAereo "+id+" negativa. Ajustando para 0.");
                 this.z = 0; // Define para 0 se for negativa
            }
        }
        
        /**
         * Faz o robô aéreo subir uma determinada quantidade de metros.
         * @param metros Quantidade de metros para subir (deve ser positivo).
         * @throws RoboDesligadoException Se o robô estiver desligado.
         * @throws AcaoNaoPermitidaException Se `metros` não for positivo ou se exceder a altitude máxima.
         * @throws ColisaoException Se houver colisão ao subir.
         * @throws ForaDosLimitesException Se o movimento levar para fora dos limites do ambiente.
         */
        public void subir(int metros) throws RoboDesligadoException, AcaoNaoPermitidaException, ColisaoException, ForaDosLimitesException {
            if (getEstado() == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(getId() + " desligado.");
            if (metros <=0) throw new AcaoNaoPermitidaException("Metros para subir deve ser positivo.");
            // Chama moverRelativamente da superclasse (Robo) para efetuar o movimento vertical
            super.moverRelativamente(ambiente, 0,0, metros); 
        }

        /**
         * Faz o robô aéreo descer uma determinada quantidade de metros.
         * @param metros Quantidade de metros para descer (deve ser positivo).
         * @throws RoboDesligadoException Se o robô estiver desligado.
         * @throws AcaoNaoPermitidaException Se `metros` não for positivo ou se tentar descer abaixo de 0.
         * @throws ColisaoException Se houver colisão ao descer.
         * @throws ForaDosLimitesException Se o movimento levar para fora dos limites do ambiente.
         */
        public void descer(int metros) throws RoboDesligadoException, AcaoNaoPermitidaException, ColisaoException, ForaDosLimitesException {
            if (getEstado() == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(getId() + " desligado.");
            if (metros <=0) throw new AcaoNaoPermitidaException("Metros para descer deve ser positivo.");
            // Chama moverRelativamente da superclasse (Robo) para efetuar o movimento vertical negativo
            super.moverRelativamente(ambiente, 0,0, -metros);
        }

        /**
         * Sobrescreve o método para mover o robô aéreo relativamente.
         * Adiciona verificações específicas para altitude máxima e mínima (0).
         * @param ambiente O ambiente de simulação.
         * @param dx Deslocamento em X.
         * @param dy Deslocamento em Y.
         * @param dz Deslocamento em Z.
         * @throws ColisaoException Se o movimento causar colisão.
         * @throws ForaDosLimitesException Se o movimento for para fora dos limites do ambiente.
         * @throws RoboDesligadoException Se o robô estiver desligado.
         * @throws AcaoNaoPermitidaException Se o movimento violar restrições de altitude.
         */
        @Override
        public void moverRelativamente(Ambiente ambiente, int dx, int dy, int dz) throws ColisaoException, ForaDosLimitesException, RoboDesligadoException, AcaoNaoPermitidaException {
            if (this.getEstado() == EstadoRobo.DESLIGADO) {
                throw new RoboDesligadoException("Robô " + getId() + " está desligado.");
            }
            int futuroZ = this.getZ() + dz; // Calcula a altitude futura
            // Verifica se a altitude futura excede a máxima permitida
            if (futuroZ > altitudeMaxima) {
                throw new AcaoNaoPermitidaException(getId() + " não pode se mover para Z=" + futuroZ + " (acima da alt max: "+altitudeMaxima+").");
            }
            // Verifica se a altitude futura é menor que zero
            if (futuroZ < 0) {
                throw new AcaoNaoPermitidaException(getId() + " não pode se mover para Z=" + futuroZ + " (abaixo de 0).");
            }
            // Chama o método da superclasse para realizar o movimento se as verificações passarem
            super.moverRelativamente(ambiente, dx, dy, dz); 
        }

        /**
         * Executa a tarefa específica de um robô aéreo: patrulha aérea.
         * O robô tenta mover-se um passo na direção atual e depois muda sua direção para SUL.
         * @param ambiente O ambiente de simulação.
         * @throws RoboDesligadoException Se o robô estiver desligado.
         * @throws AcaoNaoPermitidaException Se a ação não for permitida.
         * @throws ColisaoException Se houver colisão.
         * @throws ForaDosLimitesException Se sair dos limites.
         * @throws RecursoInsuficienteException (Declarada para compatibilidade com a superclasse, não usada diretamente aqui).
         * @throws ErroComunicacaoException (Declarada para compatibilidade com a superclasse, não usada diretamente aqui).
         */
        @Override
        // CORRIGIDO: Adicionadas todas as exceções de Robo.executarTarefa para segurança ao sobrescrever
        public void executarTarefa(Ambiente ambiente) throws RoboDesligadoException, AcaoNaoPermitidaException, ColisaoException, ForaDosLimitesException, RecursoInsuficienteException, ErroComunicacaoException {
            if (getEstado() == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(getId() + " desligado.");
            System.out.println("Robô Aéreo " + getId() + " está realizando patrulha aérea em Z=" + getZ() + ".");
            
            int dx_patrulha = 0, dy_patrulha = 0;
            // Determina o deslocamento com base na direção atual
            switch (getDirecao().toUpperCase()) {
                case "NORTE": dy_patrulha = 1; break;
                case "SUL": dy_patrulha = -1; break;
                case "LESTE": dx_patrulha = 1; break;
                case "OESTE": dx_patrulha = -1; break;
            }
            // Tenta mover um passo na direção calculada
            if (dx_patrulha != 0 || dy_patrulha != 0) {
                 System.out.println(getId() + " (Aereo) tentando mover 1 passo para " + getDirecao());
                 // Esta chamada deve ser compatível com sua própria cláusula throws
                 moverRelativamente(ambiente, dx_patrulha, dy_patrulha, 0); // Movimento apenas no plano XY
            }
            setDirecao("SUL"); // Muda a direção para SUL após a patrulha
        }

        /**
         * Aciona todos os sensores acoplados a este robô aéreo.
         * @param ambiente O ambiente para os sensores monitorarem.
         * @throws RoboDesligadoException Se o robô estiver desligado.
         */
        @Override
        public void acionarSensores(Ambiente ambiente) throws RoboDesligadoException {
            if (getEstado() == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(getId() + " desligado.");
            System.out.println("\n--- Sensores do Robô Aéreo " + getId() + " ---");
            if (getSensores().isEmpty()) {
                System.out.println(getId() + " não possui sensores."); return;
            }
            // Itera sobre os sensores e chama o método monitorar de cada um
            for (Sensor s : getSensores()) {
                System.out.println(s.monitorar(ambiente, this));
            }
        }
        /**
         * Retorna a representação visual do robô aéreo.
         * @return 'V' para Robô Aéreo (Voador).
         */
        @Override
        public char getRepresentacao() { return 'V';} 
    }