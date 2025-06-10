package robo;
import ambiente.Ambiente;
import missao.Missao;


/**
 * Representa um tipo especial de robô com comportamento autônomo,
 * capaz de receber e executar missões. 
 * Herda de Robo e adiciona a lógica de gerenciamento de missão. 
 */
public abstract class AgenteInteligente extends Robo {
    protected Missao missao;

    public AgenteInteligente(String id, int x, int y, int z, String direcao) {
        super(id, x, y, z, direcao);
    }

    /**
     * Define a missão que este agente deve executar.
     * @param m A missão a ser atribuída.
     */
    public void definirMissao(Missao m) {
        this.missao = m;
        System.out.println("Missão " + m.getClass().getSimpleName() + " atribuída ao robô " + getId());
    }

    /**
     * Verifica se o agente possui uma missão atribuída.
     * @return true se houver uma missão, false caso contrário.
     */
    public boolean temMissao() {
        return missao != null;
    }

    /**
     * Método abstrato que as subclasses devem implementar para
     * invocar a execução da missão. 
     * @param a O ambiente de execução.
     */
    public abstract void executarMissao(Ambiente a);
}
