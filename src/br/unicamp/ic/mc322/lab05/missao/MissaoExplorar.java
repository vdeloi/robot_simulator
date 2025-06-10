package br.unicamp.ic.mc322.lab05.missao;

import java.util.Random;

import br.unicamp.ic.mc322.lab05.robos.Robo;
import br.unicamp.ic.mc322.lab05.ambiente.Ambiente;
import br.unicamp.ic.mc322.lab05.util.Log;

public class MissaoExplorar implements Missao {
     private Random random = new Random();

    @Override
    public void executar(Robo robo, Ambiente ambiente) {
        System.out.println("Robô " + robo.getId() + " está explorando...");
        Log.registrar("MISSAO EXPLORAR: Iniciada por " + robo.getId());

        // Lógica de movimentação aleatória simples
        int dx = random.nextInt(3) - 1; // Gera -1, 0 ou 1
        int dy = random.nextInt(3) - 1; // Gera -1, 0 ou 1

        try {
            robo.moverRelativamente(ambiente, dx, dy, 0); // Tenta mover no plano XY
            Log.registrar("MISSAO EXPLORAR: " + robo.getId() + " moveu-se para (" + robo.getX() + ", " + robo.getY() + ", " + robo.getZ() + ")");
        } catch (Exception e) {
            System.err.println("Falha na exploração: " + e.getMessage());
            Log.registrar("MISSAO EXPLORAR: Falha ao mover " + robo.getId() + " - " + e.getMessage());
        }
    }
}
