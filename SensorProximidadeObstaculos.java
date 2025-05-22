// Subclasse Sensor de Proximidade de Obstáculos

public class SensorProximidadeObstaculos extends Sensor {

    public SensorProximidadeObstaculos(double raio) {
        super(raio); // Chama o construtor da classe Sensor
    }
    /* ################################################################################################################################### */

    /**
     * Monitora o ambiente em busca de obstáculos próximos ao robô, dentro do raio de alcance do sensor.
     * @param amb O ambiente onde o robô e os obstáculos existem.
     * @param robo O robô que está utilizando o sensor.
     * @return Uma string formatada indicando se obstáculos foram detectados e o raio de alcance.
     */

     /* ################################################################################################################################### */

    @Override
    public String monitorar(Ambiente amb, Robo robo) {
        if (amb == null || robo == null) {
            return "Sensor Proximidade: Erro - Ambiente ou Robô não fornecido.";
        }

        StringBuilder detectados = new StringBuilder();
        int contadorObstaculosProximos = 0;

        for (Entidade entidade : amb.getEntidades()) {
            if (entidade instanceof Obstaculo && entidade != robo) { // Verifica se é um obstáculo e não o próprio robô
                Obstaculo obstaculo = (Obstaculo) entidade;
                
                // Calcula a distância do robô ao ponto de referência do obstáculo (x1, y1, z_base)
                // Para uma detecção mais precisa com obstáculos volumétricos, seria necessário
                // verificar a distância ao ponto mais próximo do obstáculo.
                // Para simplificar, usamos o ponto de referência (getX, getY, getZ).
                double distancia = Math.sqrt(
                    Math.pow(obstaculo.getX() - robo.getX(), 2) + 
                    Math.pow(obstaculo.getY() - robo.getY(), 2) +
                    Math.pow(obstaculo.getZ() - robo.getZ(), 2) // Considera a base do obstáculo
                );

                if (distancia <= this.raio) {
                    if (contadorObstaculosProximos == 0) {
                        detectados.append("Obstáculos detectados próximos (até ").append(String.format("%.1f", this.raio)).append("m): ");
                    } else {
                        detectados.append(", ");
                    }
                    detectados.append(obstaculo.getNome()).append(" (").append(obstaculo.getTipo().name()).append(" a ~").append(String.format("%.1f", distancia)).append("m)");
                    contadorObstaculosProximos++;
                }
            }
        }

        if (contadorObstaculosProximos == 0) {
            return String.format("Sensor Proximidade (Raio %.1fm): Nenhum obstáculo detectado.", this.raio);
        } else {
            return "Sensor Proximidade: " + detectados.toString();
        }
    }
    /* ################################################################################################################################### */

    @Override
    public String toString() {
        return "SensorProximidadeObstaculos [raio=" + raio + "]";
    }
}