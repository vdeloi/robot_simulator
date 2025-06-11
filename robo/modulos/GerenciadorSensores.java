package robo.modulos; // Crie este novo pacote

import ambiente.Ambiente;
import ambiente.RoboDesligadoException;
import robo.EstadoRobo;
import robo.Robo;
import sensores.Sensor;
import java.util.List;

public class GerenciadorSensores {

    private Robo robo;
    private List<Sensor> sensores;

    public GerenciadorSensores(Robo robo, List<Sensor> sensores) {
        this.robo = robo;
        this.sensores = sensores;
    }

    public void acionarSensores(Ambiente ambiente) throws RoboDesligadoException {
        if (robo.getEstado() == EstadoRobo.DESLIGADO) {
            throw new RoboDesligadoException(robo.getId() + " desligado, não pode acionar sensores.");
        }
        System.out.println("\n--- Módulo de Sensores do Robô " + robo.getId() + " ---");
        if (sensores.isEmpty()) {
            System.out.println(robo.getId() + " não possui sensores.");
            return;
        }
        for (Sensor s : sensores) {
            System.out.println(s.monitorar(ambiente, robo));
        }
    }
}