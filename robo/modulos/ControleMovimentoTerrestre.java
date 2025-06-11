package robo.modulos;

import ambiente.*;
import robo.RoboTerrestre;

/**
 * Implementação do ControleMovimento para robôs terrestres.
 * Restrições: não pode se mover verticalmente e respeita uma velocidade máxima.
 */
public class ControleMovimentoTerrestre extends ControleMovimento {

    public ControleMovimentoTerrestre(RoboTerrestre robo) {
        super(robo);
    }

    @Override
    public void moverRelativamente(Ambiente ambiente, int dx, int dy, int dz) 
            throws ColisaoException, ForaDosLimitesException, RoboDesligadoException, AcaoNaoPermitidaException {

        if (robo.getEstado() == robo.EstadoRobo.DESLIGADO) {
            throw new RoboDesligadoException(robo.getId() + " está desligado.");
        }

        // Regra específica para robôs terrestres: não podem se mover verticalmente.
        if (dz != 0) {
            throw new AcaoNaoPermitidaException("RoboTerrestre " + robo.getId() + " não pode se mover verticalmente (dz=" + dz + ").");
        }

        // Regra específica: verifica a velocidade máxima.
        RoboTerrestre roboTerrestre = (RoboTerrestre) this.robo;
        double distancia = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
        if (distancia > roboTerrestre.getVelocidadeMaxima()) {
            throw new AcaoNaoPermitidaException("Movimento relativo (dist " + String.format("%.2f", distancia) + ") excede velocidade máxima (" + roboTerrestre.getVelocidadeMaxima() + ") para " + robo.getId());
        }

        // Se todas as regras passaram, calcula a nova posição e delega para o ambiente.
        int novoX = robo.getX() + dx;
        int novoY = robo.getY() + dy;
        int novoZ = robo.getZ(); // dz é sempre 0 aqui

        ambiente.moverEntidade(robo, novoX, novoY, novoZ);
    }
}