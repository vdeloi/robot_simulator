package br.unicamp.ic.mc322.lab05.robos;

import br.unicamp.ic.mc322.lab05.ambiente.Ambiente;
import br.unicamp.ic.mc322.lab05.excecoes.*;
import br.unicamp.ic.mc322.lab05.sensores.Sensor;
import br.unicamp.ic.mc322.lab05.sensores.Sensoreavel;

/**
 * Representa um robô terrestre, um tipo específico de {@link Robo}.
 * Robôs terrestres operam no nível Z=0 (chão) e possuem uma velocidade máxima
 * para movimentos no plano XY.
 * Implementa a interface {@link Sensoreavel}, permitindo que tenha sensores.
 */
public class RoboTerrestre extends Robo implements Sensoreavel {
    private final int velocidadeMaxima; // Velocidade máxima de deslocamento no plano XY

    /**
     * Construtor para RoboTerrestre.
     * Robôs terrestres são sempre inicializados na altitude Z=0.
     *
     * @param id                O identificador único do robô.
     * @param x                 A coordenada X inicial.
     * @param y                 A coordenada Y inicial.
     * @param direcao           A direção inicial do robô.
     * @param velocidadeMaxima  A velocidade máxima de deslocamento do robô (deve ser >= 1).
     */
    public RoboTerrestre(String id, int x, int y, String direcao, int velocidadeMaxima) {
        super(id, x, y, 0, direcao); // Robôs terrestres sempre começam em z=0
        this.velocidadeMaxima = Math.max(1, velocidadeMaxima); // Garante que a velocidade máxima seja pelo menos 1
    }

    /**
     * Retorna a velocidade máxima do robô terrestre.
     * @return A velocidade máxima.
     */
    public int getVelocidadeMaxima() {
        return velocidadeMaxima;
    }

    /**
     * Sobrescreve o método `moverPara` da classe {@link Robo}.
     * Adiciona restrições específicas para robôs terrestres:
     * - Não podem mudar de altitude (novoZ deve ser igual ao Z atual, que é 0).
     * - O movimento não pode exceder a velocidade máxima.
     *
     * @param ambiente O ambiente onde o robô se moverá.
     * @param novoX    A nova coordenada X de destino.
     * @param novoY    A nova coordenada Y de destino.
     * @param novoZ    A nova coordenada Z de destino (deve ser 0).
     * @throws ColisaoException        Se houver colisão na nova posição.
     * @throws ForaDosLimitesException Se a nova posição estiver fora dos limites.
     * @throws RoboDesligadoException  Se o robô estiver desligado.
     * @throws AcaoNaoPermitidaException Se o robô tentar mudar de altitude ou exceder a velocidade máxima.
     */
    @Override
    public void moverPara(Ambiente ambiente, int novoX, int novoY, int novoZ) throws ColisaoException, ForaDosLimitesException, RoboDesligadoException, AcaoNaoPermitidaException {
        if (getEstado() == EstadoRobo.DESLIGADO) {
            throw new RoboDesligadoException(getId() + " está desligado.");
        }
        // Robôs terrestres não podem mudar de altitude
        if (novoZ != this.getZ()) { // this.getZ() para robôs terrestres é sempre 0
            throw new AcaoNaoPermitidaException("RoboTerrestre " + getId() + " não pode mudar de altitude. Tentou mover para Z=" + novoZ + " a partir de Z=" + this.getZ());
        }
        // Calcula a distância do movimento no plano XY
        double distancia = Math.sqrt(Math.pow(novoX - getX(), 2) + Math.pow(novoY - getY(), 2));
        // Verifica se a distância excede a velocidade máxima
        if (distancia > this.velocidadeMaxima) {
            throw new AcaoNaoPermitidaException("Movimento (dist " + String.format("%.2f", distancia) + ") excede velocidade máxima (" + this.velocidadeMaxima + ") para " + getId());
        }
        // Chama o método da superclasse para realizar o movimento, mantendo Z atual (0)
        super.moverPara(ambiente, novoX, novoY, this.getZ());
    }

    /**
     * Sobrescreve o método `moverRelativamente` da classe {@link Robo}.
     * Adiciona restrições específicas para robôs terrestres:
     * - Não podem se mover verticalmente (dz deve ser 0).
     * - O movimento não pode exceder a velocidade máxima.
     *
     * @param ambiente O ambiente onde o robô se moverá.
     * @param dx       O deslocamento na direção X.
     * @param dy       O deslocamento na direção Y.
     * @param dz       O deslocamento na direção Z (deve ser 0).
     * @throws ColisaoException        Se houver colisão na nova posição.
     * @throws ForaDosLimitesException Se a nova posição estiver fora dos limites.
     * @throws RoboDesligadoException  Se o robô estiver desligado.
     * @throws AcaoNaoPermitidaException Se o robô tentar se mover verticalmente ou exceder a velocidade máxima.
     */
    @Override
    public void moverRelativamente(Ambiente ambiente, int dx, int dy, int dz) throws ColisaoException, ForaDosLimitesException, RoboDesligadoException, AcaoNaoPermitidaException {
        if (getEstado() == EstadoRobo.DESLIGADO) {
            throw new RoboDesligadoException(getId() + " está desligado.");
        }
        // Robôs terrestres não podem se mover verticalmente
        if (dz != 0) {
            throw new AcaoNaoPermitidaException("RoboTerrestre " + getId() + " não pode se mover verticalmente (dz=" + dz + ").");
        }
        // Calcula a distância do movimento relativo no plano XY
        double distancia = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
        // Verifica se a distância excede a velocidade máxima
        if (distancia > this.velocidadeMaxima) {
            throw new AcaoNaoPermitidaException("Movimento relativo (dist " + String.format("%.2f", distancia) + ") excede velocidade máxima (" + this.velocidadeMaxima + ") para " + getId());
        }
        // Chama o método da superclasse para realizar o movimento, com dz = 0
        super.moverRelativamente(ambiente, dx, dy, 0);
    }

    /**
     * Executa a tarefa específica de um robô terrestre: patrulhar a área.
     * O robô muda seu estado para EXECUTANDO_TAREFA, tenta mover-se um passo na direção atual,
     * e depois volta para o estado OCIOSO.
     *
     * @param ambiente O ambiente no qual a patrulha será executada.
     * @throws RoboDesligadoException Se o robô estiver desligado.
     * @throws AcaoNaoPermitidaException Se o movimento de patrulha não for permitido (ex: excede velocidade).
     * @throws ColisaoException Se colidir durante a patrulha.
     * @throws ForaDosLimitesException Se sair dos limites durante a patrulha.
     * @throws RecursoInsuficienteException (Declarada para compatibilidade com a superclasse, não usada diretamente aqui).
     * @throws ErroComunicacaoException (Declarada para compatibilidade, especialmente se uma subclasse como RoboComunicador a usar).
     */
    @Override
    // CORRIGIDO: Adicionadas exceções para corresponder a Robo.java e permitir que RoboComunicador lance ErroComunicacaoException
    public void executarTarefa(Ambiente ambiente) throws RoboDesligadoException, AcaoNaoPermitidaException, ColisaoException, ForaDosLimitesException, RecursoInsuficienteException, ErroComunicacaoException {
        if (getEstado() == EstadoRobo.DESLIGADO) {
            throw new RoboDesligadoException(getId() + " está desligado, não pode executar tarefa.");
        }
        setEstado(EstadoRobo.EXECUTANDO_TAREFA); // Define o estado do robô como executando tarefa
        System.out.println("Robô Terrestre " + getId() + " está patrulhando a área em (" + getX() + "," + getY() + "," + getZ() + ").");
        
        int dx_patrulha = 0, dy_patrulha = 0;
        // Determina o deslocamento (um passo) com base na direção atual
        switch (getDirecao().toUpperCase()) {
            case "NORTE": dy_patrulha = 1; break;
            case "SUL": dy_patrulha = -1; break;
            case "LESTE": dx_patrulha = 1; break;
            case "OESTE": dx_patrulha = -1; break;
        }
        try {
            // Tenta mover um passo se houver um deslocamento definido
            if (dx_patrulha != 0 || dy_patrulha != 0) {
                 System.out.println(getId() + " tentando mover 1 passo para " + getDirecao());
                 moverRelativamente(ambiente, dx_patrulha, dy_patrulha, 0); // dz é 0 para terrestre
            }
        } catch (ColisaoException | ForaDosLimitesException | RoboDesligadoException | AcaoNaoPermitidaException e) {
            // Captura exceções de moverRelativamente se quisermos tratá-las aqui
            // e não necessariamente propagar todas elas se esta tarefa tiver ações alternativas.
            // Por enquanto, apenas imprime, mas a cláusula throws deste método permite que sejam propagadas.
            System.out.println(getId() + " falhou ao tentar patrulhar (mover): " + e.getMessage());
        }
        setEstado(EstadoRobo.OCIOSO); // Define o estado do robô como ocioso após a tarefa
    }

    /**
     * Aciona todos os sensores acoplados a este robô terrestre.
     * Para cada sensor, chama seu método `monitorar`.
     *
     * @param ambiente O ambiente que os sensores irão monitorar.
     * @throws RoboDesligadoException Se o robô estiver desligado.
     */
    @Override
    public void acionarSensores(Ambiente ambiente) throws RoboDesligadoException {
        if (getEstado() == EstadoRobo.DESLIGADO) {
            throw new RoboDesligadoException(getId() + " está desligado, não pode acionar sensores.");
        }
        System.out.println("\n--- Sensores do Robô Terrestre " + getId() + " ---");
        if (getSensores().isEmpty()) {
            System.out.println(getId() + " não possui sensores acoplados.");
            return;
        }
        // Itera sobre a lista de sensores e chama o método monitorar de cada um
        for (Sensor s : getSensores()) {
            System.out.println(s.monitorar(ambiente, this));
        }
    }

    /**
     * Retorna o caractere de representação visual para robôs terrestres.
     * @return 'T'.
     */
    @Override
    public char getRepresentacao() {
        return 'T'; // 'T' para Terrestre
    }
    
    /**
     * Define o estado interno do robô (para fins de tarefa, não o estado LIGADO/DESLIGADO principal).
     * Este é um estado conceitual para a tarefa. O estado principal LIGADO/DESLIGADO
     * é controlado por Robo.ligar()/desligar().
     * Para uma máquina de estados finitos (FSM) completa, 'estado' em Robo seria protected
     * e este método o alteraria.
     * @param novoEstado O novo {@link EstadoRobo} para a tarefa.
     */
    protected void setEstado(EstadoRobo novoEstado) {
        System.out.println(getId() + " (interno): mudou estado para: " + novoEstado);
        // Nota: Esta implementação de setEstado não altera o this.estado principal do robô.
        // Se a intenção fosse mudar o estado principal (LIGADO, OCIOSO, etc.),
        // o atributo 'estado' na classe Robo deveria ser 'protected' e acessado aqui.
        // No contexto atual, é mais uma indicação de sub-estado da tarefa.
    }
}