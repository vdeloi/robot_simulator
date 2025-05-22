/* Robo.java */

// Robo é a classe base, pai das classes RoboAereo e RoboTerrestre

/* ################################################################################################################################### */

import java.util.ArrayList;
import java.util.List;

/* ################################################################################################################################### */


public abstract class Robo implements Entidade { 

    private final String id; // Identificador único
    protected String nome; // Nome amigável
    protected int posicaoX; 
    protected int posicaoY; 
    protected int posicaoZ; 
    protected String direcao; // Norte, Sul, Leste, Oeste (ou graus)
    protected EstadoRobo estado; 
    protected TipoEntidade tipoEntidadeRobo; 
    private ArrayList<Sensor> listaDeSensores;

    /* ################################################################################################################################### */

    public Robo(String id, String nome, int posicaoX, int posicaoY, int posicaoZ, String direcao) {

        this.id = id;
        this.nome = nome;
        this.posicaoX = posicaoX;
        this.posicaoY = posicaoY;
        this.posicaoZ = posicaoZ;
        this.direcao = direcao;
        this.estado = EstadoRobo.DESLIGADO; // Começa desliga
        this.tipoEntidadeRobo = TipoEntidade.ROBO;
        this.listaDeSensores = new ArrayList<>();
    }

    /* ################################################################################################################################### */

    // Métodos da interface Entidade
    @Override
    public String getNome() { return this.nome; }
    
    public String getId() { return this.id; }

    @Override
    public int getX() { return this.posicaoX; } 

    @Override
    public int getY() { return this.posicaoY; }

    @Override
    public int getZ() { return this.posicaoZ; }

    @Override
    public TipoEntidade getTipoEntidade() { return this.tipoEntidadeRobo; }

    @Override
    public String getDescricao() {
        return "Robô ID: " + id + ", Nome: " + nome + ", Estado: " + estado.getDescricao() +", Posição: (" + posicaoX + "," + posicaoY + "," + posicaoZ + "), Direção: " + direcao;
    }

    @Override
    public char getRepresentacao() { return this.tipoEntidadeRobo.getRepresentacao(); }

    /* ################################################################################################################################### */

    // Getters e Setters
    public String getDirecao() { return direcao; }
    public void setDirecao(String direcao) { this.direcao = direcao; }
    public EstadoRobo getEstado() { return estado; }
    public void setEstado(EstadoRobo estado) { this.estado = estado; }

    // Setters de posição (usados pelo Ambiente ao mover)
    public void setPosicaoX(int posicaoX) { this.posicaoX = posicaoX; }
    public void setPosicaoY(int posicaoY) { this.posicaoY = posicaoY; }
    public void setPosicaoZ(int posicaoZ) { this.posicaoZ = posicaoZ; }
    
    public List<Sensor> getListaDeSensores() { return listaDeSensores; }

    /* ################################################################################################################################### */


    // Métodos de controle do robô
    public void ligar() throws AcaoNaoPermitidaException { 
        if(this.estado == EstadoRobo.AVARIADO) {
            throw new AcaoNaoPermitidaException("Robô " + nome + " está avariado e não pode ser ligado.");
        }
        this.estado = EstadoRobo.EM_ESPERA; // Ou LIGADO, dependendo da semântica desejada
        System.out.println("Robô " + nome + " ligado.");
    }
    /* ################################################################################################################################### */

    public void desligar() { 
        this.estado = EstadoRobo.DESLIGADO;
        System.out.println("Robô " + nome + " desligado.");
    }
    /* ################################################################################################################################### */

    // Método abstrato para ações específicas de cada robô 

    public abstract String executarTarefa(Ambiente ambiente, CentralComunicacao central, String[] args)
    throws RoboDesligadoException, AcaoNaoPermitidaException, ForaDosLimitesException, ColisaoException, ErroComunicacaoException, EntidadeNaoEncontradaException;
    

    /* ################################################################################################################################### */


    // Movimentação básica (será chamada por um método mais genérico no menu)
    // O ambiente fará a validação e moverá a entidade
    public void moverPara(int novoX, int novoY, int novoZ, Ambiente ambiente) throws RoboDesligadoException, ColisaoException, ForaDosLimitesException, AcaoNaoPermitidaException { 
        if (this.estado == EstadoRobo.DESLIGADO) {
            throw new RoboDesligadoException("Robô " + nome + " está desligado. Não pode mover.");
        }
         if (this.estado == EstadoRobo.AVARIADO) {
            throw new AcaoNaoPermitidaException("Robô " + nome + " está avariado. Não pode mover.");
        }
        // A lógica de verificação de velocidade máxima (se aplicável) deve ser aqui ou na subclasse
        ambiente.moverEntidade(this, novoX, novoY, novoZ);
    }
    /* ################################################################################################################################### */
    
    // Adicionar e ativar sensores (Lab 03)
    public void adicionarSensor(Sensor s) {
        this.listaDeSensores.add(s);
    }

    /* ################################################################################################################################### */

    public String ativarSensoresRobo(Ambiente amb) throws RoboDesligadoException { // Renomeado para não colidir com Sensoreavel
        if (this.estado == EstadoRobo.DESLIGADO) {
            throw new RoboDesligadoException("Robô " + nome + " está desligado. Não pode ativar sensores.");
        }
        if (listaDeSensores.isEmpty()) {
            return "Robô " + nome + " não possui sensores.";
        }
        StringBuilder leituras = new StringBuilder("Leituras dos sensores do Robô " + getNome() + ":\n");
        for (Sensor s : listaDeSensores) {
            leituras.append(" - ").append(s.monitorar(amb, this)).append("\n");
        }
        return leituras.toString();
    }

    public String exibirPosicao() {
        return "(" + this.posicaoX + ", " + this.posicaoY + ", " + this.posicaoZ + ")";
    }
}
