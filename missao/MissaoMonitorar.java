package missao;

import ambiente.Ambiente;
import ambiente.RoboDesligadoException;
import robo.Robo;
import sensores.Sensoreavel;
import util.Log;

/**
 * Missão que faz o robô ficar parado e monitorar o ambiente ao seu redor
 * utilizando seus sensores.
 */
public class MissaoMonitorar implements Missao {

    @Override
    public void executar(Robo robo, Ambiente ambiente) {
        System.out.println("Robô " + robo.getId() + " está monitorando a área em (" + robo.getX() + ", " + robo.getY() + ", " + robo.getZ() + ")");
        Log.registrar("MISSAO MONITORAR: Iniciada por " + robo.getId());

        // A missão requer que o robô tenha sensores
        if (!(robo instanceof Sensoreavel)) {
            System.out.println("Missão Monitorar: Robô " + robo.getId() + " não possui sensores.");
            Log.registrar("MISSAO MONITORAR: Falha - " + robo.getId() + " não é 'Sensoreavel'.");
            return;
        }

        try {
            // Aciona os sensores do robô
            ((Sensoreavel) robo).acionarSensores(ambiente);
            Log.registrar("MISSAO MONITORAR: Sensores de " + robo.getId() + " foram acionados.");

        } catch (RoboDesligadoException e) {
            System.err.println("Falha ao monitorar: " + e.getMessage());
            Log.registrar("MISSAO MONITORAR: Falha - " + e.getMessage());
        }
    }
}