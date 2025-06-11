package robo;

import ambiente.*;
import sensores.Sensor;
import sensores.Sensoreavel;

/**
 * Representa um robô terrestre, um tipo específico de {@link Robo}.
 * Agora herda de {@link AgenteInteligente}, sendo capaz de executar missões autônomas.
 * Robôs terrestres operam no nível Z=0 (chão) e possuem uma velocidade máxima.
 */

//    Ela ainda implementa Sensoreavel para manter a capacidade de usar sensores.
public class RoboTerrestre extends AgenteInteligente implements Sensoreavel {
    private final int velocidadeMaxima; // Velocidade máxima de deslocamento no plano XY

    /**
     * Construtor para RoboTerrestre.
     * Robôs terrestres são sempre inicializados na altitude Z=0.
     * A chamada super() agora invoca o construtor de AgenteInteligente, que por sua vez chama o de Robo.
     *
     * @param id                O identificador único do robô.
     * @param x                 A coordenada X inicial.
     * @param y                 A coordenada Y inicial.
     * @param direcao           A direção inicial do robô.
     * @param velocidadeMaxima  A velocidade máxima de deslocamento do robô (deve ser >= 1).
     */
    public RoboTerrestre(String id, int x, int y, String direcao, int velocidadeMaxima) {
        super(id, x, y, 0, direcao); // Nenhuma alteração necessária aqui.
        this.velocidadeMaxima = Math.max(1, velocidadeMaxima);

        // Inicializa o módulo de movimento específico para robôs terrestres
        this.controleMovimento = new robo.modulos.ControleMovimentoTerrestre(this);
    }

    /**
     * Retorna a velocidade máxima do robô terrestre.
     * @return A velocidade máxima.
     */
    public int getVelocidadeMaxima() {
        return velocidadeMaxima;
    }

    @Override
    public void moverPara(Ambiente ambiente, int novoX, int novoY, int novoZ) throws ColisaoException, ForaDosLimitesException, RoboDesligadoException, AcaoNaoPermitidaException {
        // (Lógica original do método - sem alterações)
        if (getEstado() == EstadoRobo.DESLIGADO) {
            throw new RoboDesligadoException(getId() + " está desligado.");
        }
        if (novoZ != this.getZ()) {
            throw new AcaoNaoPermitidaException("RoboTerrestre " + getId() + " não pode mudar de altitude. Tentou mover para Z=" + novoZ + " a partir de Z=" + this.getZ());
        }
        double distancia = Math.sqrt(Math.pow(novoX - getX(), 2) + Math.pow(novoY - getY(), 2));
        if (distancia > this.velocidadeMaxima) {
            throw new AcaoNaoPermitidaException("Movimento (dist " + String.format("%.2f", distancia) + ") excede velocidade máxima (" + this.velocidadeMaxima + ") para " + getId());
        }
        super.moverPara(ambiente, novoX, novoY, this.getZ());
    }

    @Override
    public void moverRelativamente(Ambiente ambiente, int dx, int dy, int dz) throws ColisaoException, ForaDosLimitesException, RoboDesligadoException, AcaoNaoPermitidaException {
        // (Lógica original do método - sem alterações)
         if (getEstado() == EstadoRobo.DESLIGADO) {
            throw new RoboDesligadoException(getId() + " está desligado.");
        }
        if (dz != 0) {
            throw new AcaoNaoPermitidaException("RoboTerrestre " + getId() + " não pode se mover verticalmente (dz=" + dz + ").");
        }
        double distancia = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
        if (distancia > this.velocidadeMaxima) {
            throw new AcaoNaoPermitidaException("Movimento relativo (dist " + String.format("%.2f", distancia) + ") excede velocidade máxima (" + this.velocidadeMaxima + ") para " + getId());
        }
        super.moverRelativamente(ambiente, dx, dy, 0);
    }


    /**
     * Executa a tarefa padrão de um robô terrestre: patrulhar a área.
     * Este método continua existindo como o comportamento padrão do robô.
     */
    @Override
    public void executarTarefa(Ambiente ambiente) throws RoboDesligadoException, AcaoNaoPermitidaException, ColisaoException, ForaDosLimitesException, RecursoInsuficienteException, ErroComunicacaoException {
        // (Lógica original do método - sem alterações)
        if (getEstado() == EstadoRobo.DESLIGADO) {
            throw new RoboDesligadoException(getId() + " está desligado, não pode executar tarefa.");
        }
        setEstado(EstadoRobo.EXECUTANDO_TAREFA);
        System.out.println("Robô Terrestre " + getId() + " está patrulhando a área em (" + getX() + "," + getY() + "," + getZ() + ").");

        int dx_patrulha = 0, dy_patrulha = 0;
        switch (getDirecao().toUpperCase()) {
            case "NORTE": dy_patrulha = 1; break;
            case "SUL": dy_patrulha = -1; break;
            case "LESTE": dx_patrulha = 1; break;
            case "OESTE": dx_patrulha = -1; break;
        }
        try {
            if (dx_patrulha != 0 || dy_patrulha != 0) {
                 System.out.println(getId() + " tentando mover 1 passo para " + getDirecao());
                 moverRelativamente(ambiente, dx_patrulha, dy_patrulha, 0);
            }
        } catch (ColisaoException | ForaDosLimitesException | RoboDesligadoException | AcaoNaoPermitidaException e) {
            System.out.println(getId() + " falhou ao tentar patrulhar (mover): " + e.getMessage());
        }
        setEstado(EstadoRobo.OCIOSO);
    }

    /**
     * Implementação do método abstrato de AgenteInteligente.
     * Permite que o robô terrestre execute sua missão atribuída.
     * Se não houver missão, ele executa sua tarefa padrão como alternativa.
     * @param ambiente O ambiente de execução.
     */
    @Override
    public void executarMissao(Ambiente ambiente) {
        if (temMissao()) {
            System.out.println("Robô Terrestre " + getId() + " executando missão: " + missao.getClass().getSimpleName());
            missao.executar(this, ambiente); // Executa a missão específica
        } else {
            System.out.println(getId() + " não possui missão. Executando tarefa padrão de patrulha.");
            try {
                // Se não há missão, o comportamento padrão é sua tarefa normal.
                executarTarefa(ambiente);
            } catch (Exception e) {
                System.err.println("Falha ao executar tarefa padrão para " + getId() + ": " + e.getMessage());
            }
        }
    }


    /**
     * Aciona todos os sensores acoplados a este robô terrestre.
     */
    @Override
    public void acionarSensores(Ambiente ambiente) throws RoboDesligadoException {
        // (Lógica original do método - sem alterações)
        if (getEstado() == EstadoRobo.DESLIGADO) {
            throw new RoboDesligadoException(getId() + " está desligado, não pode acionar sensores.");
        }
        System.out.println("\n--- Sensores do Robô Terrestre " + getId() + " ---");
        if (getSensores().isEmpty()) {
            System.out.println(getId() + " não possui sensores acoplados.");
            return;
        }
        for (Sensor s : getSensores()) {
            System.out.println(s.monitorar(ambiente, this));
        }
    }

    /**
     * Retorna o caractere de representação visual para robôs terrestres.
     */
    @Override
    public char getRepresentacao() {
        return 'T';
    }

    protected void setEstado(EstadoRobo novoEstado) {
        
        System.out.println(getId() + " (interno): mudou estado para: " + novoEstado);
    }
}