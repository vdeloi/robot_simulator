package robo;

import java.util.ArrayList;
import java.util.List;

import ambiente.AcaoNaoPermitidaException;
import ambiente.ColisaoException;
import ambiente.ErroComunicacaoException;
import ambiente.ForaDosLimitesException;
import ambiente.RecursoInsuficienteException;
import ambiente.RoboDesligadoException;
import ambiente.Ambiente;
import ambiente.Entidade;
import ambiente.TipoEntidade;
import robo.modulos.ControleMovimento;
import robo.modulos.GerenciadorSensores;
import sensores.Sensor;

/**
 * Classe abstrata que representa um robô genérico no ambiente.
 * Utiliza composição para delegar responsabilidades de movimento e sensores
 * para módulos especializados. 
 */
public abstract class Robo implements Entidade {
    private final String id;
    protected int x, y, z;
    private TipoEntidade tipoEntidade;
    private EstadoRobo estado;
    private String direcao;
    private List<Sensor> sensores;

    // --- MÓDULOS DE COMPOSIÇÃO ---
    // Cada robô TERÁ um módulo de controle de movimento e um de sensores.
    protected ControleMovimento controleMovimento;
    protected GerenciadorSensores gerenciadorSensores;

    /**
     * Construtor para um robô.
     * As subclasses serão responsáveis por instanciar os módulos corretos.
     */
    public Robo(String id, int x, int y, int z, String direcao) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.tipoEntidade = TipoEntidade.ROBO;
        this.estado = EstadoRobo.DESLIGADO;
        this.direcao = direcao;
        this.sensores = new ArrayList<>();
        
        // Inicializa o gerenciador de sensores. O módulo de movimento
        // será inicializado nas subclasses (RoboTerrestre, RoboAereo).
        this.gerenciadorSensores = new GerenciadorSensores(this, this.sensores);
    }

    // --- MÉTODOS DELEGADOS AOS MÓDULOS ---

    /**
     * Solicita que o robô se mova relativamente à sua posição atual.
     * A chamada é delegada para o módulo de controle de movimento,
     * que contém a lógica específica para o tipo de robô.
     */
    public void moverRelativamente(Ambiente ambiente, int dx, int dy, int dz) throws ColisaoException, ForaDosLimitesException, RoboDesligadoException, AcaoNaoPermitidaException {
        // Delega a responsabilidade para o módulo de movimento.
        this.controleMovimento.moverRelativamente(ambiente, dx, dy, dz);
    }
    
    /**
     * Solicita que o robô se mova para uma nova posição absoluta.
     * Este método calcula o delta e reutiliza o moverRelativamente.
     */
    public void moverPara(Ambiente ambiente, int novoX, int novoY, int novoZ) throws ColisaoException, ForaDosLimitesException, RoboDesligadoException, AcaoNaoPermitidaException {
        if (this.estado == EstadoRobo.DESLIGADO) {
            throw new RoboDesligadoException("Robô " + id + " está desligado. Não pode mover.");
        }
        int dx = novoX - this.x;
        int dy = novoY - this.y;
        int dz = novoZ - this.z;
        this.moverRelativamente(ambiente, dx, dy, dz);
    }

    // --- MÉTODOS E GETTERS/SETTERS PADRÃO ---

    @Override
    public int getX() { return x; }
    @Override
    public int getY() { return y; }
    @Override
    public int getZ() { return z; }
    @Override
    public TipoEntidade getTipo() { return tipoEntidade; }

    @Override
    public String getDescricao() {
        return "Robo ID: " + id + ", Tipo: " + getClass().getSimpleName() + 
               ", Pos: (" + x + "," + y + "," + z + "), Estado: " + estado + ", Dir: " + direcao;
    }

    @Override
    public char getRepresentacao() {
        String className = getClass().getSimpleName();
        if (className.isEmpty() || className.equals("Robo")) {
            return 'R';
        }
        return Character.toUpperCase(className.charAt(0));
    }

    public void ligar() {
        this.estado = EstadoRobo.LIGADO;
        System.out.println("Robô " + id + " ligado.");
    }

    public void desligar() {
        this.estado = EstadoRobo.DESLIGADO;
        System.out.println("Robô " + id + " desligado.");
    }

    public void atualizarPosicao(int novoX, int novoY, int novoZ) {
        this.x = novoX;
        this.y = novoY;
        this.z = novoZ;
    }

    public abstract void executarTarefa(Ambiente ambiente) throws RoboDesligadoException, AcaoNaoPermitidaException, ColisaoException, ForaDosLimitesException, RecursoInsuficienteException, ErroComunicacaoException;

    public String getId() { return id; }
    public EstadoRobo getEstado() { return estado; }
    public String getDirecao() { return direcao; }
    public void setDirecao(String direcao) { this.direcao = direcao; }

    public List<Sensor> getSensores() { return sensores; }
    public void adicionarSensor(Sensor s) { this.sensores.add(s); }
    public void removerSensor(Sensor s) { this.sensores.remove(s); }
}