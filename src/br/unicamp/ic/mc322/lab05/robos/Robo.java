package br.unicamp.ic.mc322.lab05.robos;

import java.util.ArrayList;
import java.util.List;

import br.unicamp.ic.mc322.lab05.ambiente.Ambiente;
import br.unicamp.ic.mc322.lab05.ambiente.Entidade;
import br.unicamp.ic.mc322.lab05.ambiente.TipoEntidade;
import br.unicamp.ic.mc322.lab05.excecoes.AcaoNaoPermitidaException;
import br.unicamp.ic.mc322.lab05.excecoes.ColisaoException;
import br.unicamp.ic.mc322.lab05.excecoes.ErroComunicacaoException;
import br.unicamp.ic.mc322.lab05.excecoes.ForaDosLimitesException;
import br.unicamp.ic.mc322.lab05.excecoes.RecursoInsuficienteException;
import br.unicamp.ic.mc322.lab05.excecoes.RoboDesligadoException;
import br.unicamp.ic.mc322.lab05.sensores.Sensor;

/**
 * Classe abstrata que representa um robô genérico no ambiente.
 * Todos os tipos específicos de robôs (terrestre, aéreo, etc.) devem herdar desta classe.
 * Implementa a interface {@link Entidade}.
 * Um robô possui um ID, posição (x,y,z), tipo, estado, direção e uma lista de sensores.
 */
public abstract class Robo implements Entidade {
    private final String id; // Identificador único do robô
    protected int x, y, z;   // Posição atual do robô. Protegido para acesso por subclasses diretas, embora getters sejam preferidos.
    private TipoEntidade tipoEntidade; // Tipo da entidade, sempre ROBO para instâncias de Robo
    private EstadoRobo estado;         // Estado atual do robô (LIGADO, DESLIGADO, etc.)
    private String direcao;            // Direção para a qual o robô está virado (ex: NORTE, SUL, LESTE, OESTE)
    private List<Sensor> sensores;     // Lista de sensores acoplados ao robô

    /**
     * Construtor para um robô.
     *
     * @param id      O identificador único do robô.
     * @param x       A coordenada X inicial.
     * @param y       A coordenada Y inicial.
     * @param z       A coordenada Z inicial.
     * @param direcao A direção inicial do robô.
     */
    public Robo(String id, int x, int y, int z, String direcao) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.tipoEntidade = TipoEntidade.ROBO; // Todo robô é do tipo ROBO
        this.estado = EstadoRobo.DESLIGADO;   // Robôs começam desligados por padrão
        this.direcao = direcao;
        this.sensores = new ArrayList<>();    // Inicializa a lista de sensores vazia
    }

    // Getters da interface Entidade
    @Override
    public int getX() { return x; }
    @Override
    public int getY() { return y; }
    @Override
    public int getZ() { return z; }
    @Override
    public TipoEntidade getTipo() { return tipoEntidade; }

    /**
     * Retorna uma descrição detalhada do robô, incluindo ID, tipo específico (nome da subclasse),
     * posição, estado e direção.
     * @return Uma string descritiva do robô.
     */
    @Override
    public String getDescricao() {
        return "Robo ID: " + id + ", Tipo: " + getClass().getSimpleName() + // getClass().getSimpleName() pega o nome da classe concreta (ex: RoboTerrestre)
               ", Pos: (" + x + "," + y + "," + z + "), Estado: " + estado + ", Dir: " + direcao;
    }

    /**
     * Retorna um caractere para representar o robô visualmente.
     * Padrão: Primeira letra do nome da classe (subclasse), ou 'R' se o nome da classe for apenas "Robo".
     * As subclasses devem sobrescrever este método para fornecer representações mais específicas (ex: 'T' para Terrestre).
     * @return Um caractere de representação.
     */
    @Override
    public char getRepresentacao() {
        String className = getClass().getSimpleName(); // Obtém o nome simples da classe (ex: "RoboAereo")
        if (className.isEmpty() || className.equals("Robo")) {
            return 'R'; // Retorna 'R' para a classe base Robo ou se o nome for vazio
        }
        return Character.toUpperCase(className.charAt(0)); // Retorna a primeira letra maiúscula do nome da classe
    }

    // Métodos de controle de estado
    /**
     * Liga o robô, mudando seu estado para {@link EstadoRobo#LIGADO}.
     */
    public void ligar() {
        this.estado = EstadoRobo.LIGADO;
        System.out.println("Robô " + id + " ligado.");
    }

    /**
     * Desliga o robô, mudando seu estado para {@link EstadoRobo#DESLIGADO}.
     */
    public void desligar() {
        this.estado = EstadoRobo.DESLIGADO;
        System.out.println("Robô " + id + " desligado.");
    }

    /**
     * Atualiza a posição (x, y, z) do robô.
     * Este método é tipicamente chamado pela classe {@link Ambiente} após um movimento bem-sucedido.
     * @param novoX A nova coordenada X.
     * @param novoY A nova coordenada Y.
     * @param novoZ A nova coordenada Z.
     */
    public void atualizarPosicao(int novoX, int novoY, int novoZ) {
        this.x = novoX;
        this.y = novoY;
        this.z = novoZ;
    }

    /**
     * Método abstrato para que cada tipo de robô execute sua tarefa específica.
     * As subclasses DEVEM implementar este método.
     * Declara todas as exceções que qualquer tarefa específica PODE lançar.
     *
     * @param ambiente O ambiente no qual a tarefa será executada.
     * @throws RoboDesligadoException Se o robô estiver desligado.
     * @throws AcaoNaoPermitidaException Se a tarefa não for permitida.
     * @throws ColisaoException Se a tarefa resultar em colisão.
     * @throws ForaDosLimitesException Se a tarefa tentar mover para fora dos limites.
     * @throws RecursoInsuficienteException Se a tarefa requerer recursos indisponíveis.
     * @throws ErroComunicacaoException Se houver erro de comunicação durante a tarefa.
     */
    public abstract void executarTarefa(Ambiente ambiente) throws RoboDesligadoException, AcaoNaoPermitidaException, ColisaoException, ForaDosLimitesException, RecursoInsuficienteException, ErroComunicacaoException;

    // Getters para atributos específicos do Robô
    public String getId() { return id; }
    public EstadoRobo getEstado() { return estado; }
    public String getDirecao() { return direcao; }
    public void setDirecao(String direcao) { this.direcao = direcao; } // Permite mudar a direção do robô

    // Gerenciamento de sensores
    public List<Sensor> getSensores() { return sensores; }
    public void adicionarSensor(Sensor s) { this.sensores.add(s); }
    public void removerSensor(Sensor s) { this.sensores.remove(s); }

    // Métodos de solicitação de movimento para o robô.
    // Estes métodos declaram todas as exceções que o processo de movimento (incluindo verificações do Ambiente) pode encontrar.

    /**
     * Solicita que o robô se mova para uma nova posição absoluta (coordenadas explícitas).
     * O movimento real é validado e realizado pelo {@link Ambiente}.
     * As subclasses podem adicionar verificações ANTES de chamar `ambiente.moverEntidade`.
     *
     * @param ambiente O ambiente onde o robô se moverá.
     * @param novoX   A nova coordenada X de destino.
     * @param novoY   A nova coordenada Y de destino.
     * @param novoZ   A nova coordenada Z de destino.
     * @throws ColisaoException Se houver colisão na nova posição.
     * @throws ForaDosLimitesException Se a nova posição estiver fora dos limites.
     * @throws RoboDesligadoException Se o robô estiver desligado.
     * @throws AcaoNaoPermitidaException Se o movimento não for permitido para este tipo de robô ou estado.
     */
    public void moverPara(Ambiente ambiente, int novoX, int novoY, int novoZ) throws ColisaoException, ForaDosLimitesException, RoboDesligadoException, AcaoNaoPermitidaException {
        if (this.estado == EstadoRobo.DESLIGADO) {
            throw new RoboDesligadoException("Robô " + id + " está desligado. Não pode mover.");
        }
        // Subclasses podem adicionar checagens aqui ANTES de chamar ambiente.moverEntidade.
        // Por exemplo, verificar velocidade ou se o movimento é válido para seu tipo.
        // Se essas checagens falharem, elas lançariam AcaoNaoPermitidaException.
        ambiente.moverEntidade(this, novoX, novoY, novoZ); // Delega o movimento para o ambiente
    }

    /**
     * Solicita que o robô se mova relativamente à sua posição atual (usando deslocamentos dx, dy, dz).
     * O movimento real é validado e realizado pelo {@link Ambiente}.
     * As subclasses podem adicionar verificações ANTES de chamar `ambiente.moverEntidade`.
     *
     * @param ambiente O ambiente onde o robô se moverá.
     * @param dx      O deslocamento na direção X.
     * @param dy      O deslocamento na direção Y.
     * @param dz      O deslocamento na direção Z.
     * @throws ColisaoException Se houver colisão na nova posição.
     * @throws ForaDosLimitesException Se a nova posição estiver fora dos limites.
     * @throws RoboDesligadoException Se o robô estiver desligado.
     * @throws AcaoNaoPermitidaException Se o movimento não for permitido para este tipo de robô ou estado.
     */
    public void moverRelativamente(Ambiente ambiente, int dx, int dy, int dz) throws ColisaoException, ForaDosLimitesException, RoboDesligadoException, AcaoNaoPermitidaException {
        if (this.estado == EstadoRobo.DESLIGADO) {
            throw new RoboDesligadoException("Robô " + id + " está desligado.");
        }
        // Calcula a nova posição com base nos deslocamentos
        int novoX = this.x + dx;
        int novoY = this.y + dy;
        int novoZ = this.z + dz;
        // Subclasses podem adicionar checagens aqui ANTES de chamar ambiente.moverEntidade.
        // Por exemplo, verificar velocidade ou se o movimento é válido para seu tipo.
        // Se essas checagens falharem, elas lançariam AcaoNaoPermitidaException.
        ambiente.moverEntidade(this, novoX, novoY, novoZ); // Delega o movimento para o ambiente
    }
}