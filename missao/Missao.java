package missao;

import ambiente.Ambiente;
import robo.Robo;

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