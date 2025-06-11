package robo.modulos;

import ambiente.*;
import robo.EstadoRobo;
import robo.RoboAereo;

/**
 * Implementação do ControleMovimento para robôs aéreos.
 * Restrições: respeita a altitude máxima e não pode ir abaixo do solo (Z=0).
 */
public class ControleMovimentoAereo extends ControleMovimento {

    public ControleMovimentoAereo(RoboAereo robo) {
        super(robo);
    }

    @Override
    public void moverRelativamente(Ambiente ambiente, int dx, int dy, int dz) 
            throws ColisaoException, ForaDosLimitesException, RoboDesligadoException, AcaoNaoPermitidaException {
        
        if (robo.getEstado() == EstadoRobo.DESLIGADO) {
            throw new RoboDesligadoException(robo.getId() + " está desligado.");
        }

        // Calcula a posição futura para validar as regras.
        int futuroZ = robo.getZ() + dz;
        RoboAereo roboAereo = (RoboAereo) this.robo;

        // Regra específica: verifica a altitude máxima.
        if (futuroZ > roboAereo.getAltitudeMaxima()) {
            throw new AcaoNaoPermitidaException(robo.getId() + " não pode se mover para Z=" + futuroZ + " (acima da alt max: "+ roboAereo.getAltitudeMaxima() +").");
        }

        // Regra específica: não pode ir para altitude negativa.
        if (futuroZ < 0) {
            throw new AcaoNaoPermitidaException(robo.getId() + " não pode se mover para Z=" + futuroZ + " (abaixo de 0).");
        }

        // Se todas as regras passaram, calcula a nova posição e delega para o ambiente.
        int novoX = robo.getX() + dx;
        int novoY = robo.getY() + dy;

        ambiente.moverEntidade(robo, novoX, novoY, futuroZ);
    }
}