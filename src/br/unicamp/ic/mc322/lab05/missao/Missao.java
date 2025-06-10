package br.unicamp.ic.mc322.lab05.missao;
import br.unicamp.ic.mc322.lab05.ambiente.Ambiente;
import br.unicamp.ic.mc322.lab05.robos.Robo;
/**
 * Interface que define um contrato genérico para tarefas autônomas
 * que um robô pode executar no ambiente. 
 */
public interface Missao {
    /**
     * Executa a lógica da missão.
     * @param robo O robô que está executando a missão.
     * @param ambiente O ambiente onde a ação ocorre.
     */
    void executar(Robo robo, Ambiente ambiente);
}