package robo.modulos;

import ambiente.*;
import robo.Robo;

/**
 * Classe abstrata que define o contrato para um módulo de controle de movimento.
 * Cada tipo de robô (terrestre, aéreo) terá sua própria implementação.
 */
public abstract class ControleMovimento {
    
    protected Robo robo;

    public ControleMovimento(Robo robo) {
        this.robo = robo;
    }

    /**
     * Método abstrato para mover o robô relativamente.
     * As subclasses implementarão as regras de movimento específicas.
     * @param ambiente O ambiente onde o robô se move.
     * @param dx O deslocamento em X.
     * @param dy O deslocamento em Y.
     * @param dz O deslocamento em Z.
     * @throws ColisaoException Se houver colisão.
     * @throws ForaDosLimitesException Se o movimento for para fora do ambiente.
     * @throws RoboDesligadoException Se o robô estiver desligado.
     * @throws AcaoNaoPermitidaException Se o movimento violar as regras do robô.
     */
    public abstract void moverRelativamente(Ambiente ambiente, int dx, int dy, int dz) 
            throws ColisaoException, ForaDosLimitesException, RoboDesligadoException, AcaoNaoPermitidaException;
}