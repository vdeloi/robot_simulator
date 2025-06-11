package missao;

import ambiente.Ambiente;
import robo.Robo;
import util.Log;
import java.util.ArrayList;
import java.util.List;

/**
 * Missão que faz o robô seguir uma rota de patrulha pré-definida.
 * A rota é uma lista de coordenadas (pontos de patrulha).
 */
public class MissaoPatrulhar implements Missao {
    private List<int[]> rota; // Lista de pontos [x, y, z]
    private int pontoAtualIndex = 0; // Índice do próximo ponto de patrulha na rota

    /**
     * Construtor que define a rota de patrulha.
     */
    public MissaoPatrulhar() {
        this.rota = new ArrayList<>();
        // Exemplo de rota de patrulha: um quadrado no plano Z=0
        rota.add(new int[]{5, 5, 0});
        rota.add(new int[]{5, 10, 0});
        rota.add(new int[]{10, 10, 0});
        rota.add(new int[]{10, 5, 0});
    }

    @Override
    public void executar(Robo robo, Ambiente ambiente) {
        if (rota.isEmpty()) {
            System.out.println("Missão Patrulhar: Nenhuma rota definida para " + robo.getId());
            return;
        }

        // Pega o próximo ponto da rota
        int[] proximoPonto = rota.get(pontoAtualIndex);
        int alvoX = proximoPonto[0];
        int alvoY = proximoPonto[1];
        int alvoZ = proximoPonto[2];

        System.out.println("Robô " + robo.getId() + " patrulhando em direção a (" + alvoX + ", " + alvoY + ", " + alvoZ + ")");
        Log.registrar("MISSAO PATRULHAR: " + robo.getId() + " indo para o ponto " + (pontoAtualIndex + 1) + "/" + rota.size());

        try {
            // Lógica de movimento simples: move um passo em direção ao alvo
            int dx = Integer.compare(alvoX, robo.getX()); // Retorna -1, 0 ou 1
            int dy = Integer.compare(alvoY, robo.getY());
            int dz = Integer.compare(alvoZ, robo.getZ());
            
            // Se o robô não estiver no ponto alvo, move-se
            if (dx != 0 || dy != 0 || dz != 0) {
                robo.moverRelativamente(ambiente, dx, dy, dz);
            } else {
                // Se chegou ao ponto, avança para o próximo ponto da rota
                System.out.println(robo.getId() + " chegou ao ponto de patrulha: (" + alvoX + ", " + alvoY + ", " + alvoZ + ")");
                Log.registrar("MISSAO PATRULHAR: " + robo.getId() + " chegou ao ponto de patrulha.");
                pontoAtualIndex = (pontoAtualIndex + 1) % rota.size(); // Volta ao início se chegar ao fim da rota
            }

        } catch (Exception e) {
            System.err.println("Falha na patrulha: " + e.getMessage());
            Log.registrar("MISSAO PATRULHAR: Falha ao mover " + robo.getId() + " - " + e.getMessage());
            // Em caso de falha (ex: colisão), pode ser útil avançar para o próximo ponto
            pontoAtualIndex = (pontoAtualIndex + 1) % rota.size();
        }
    }
}