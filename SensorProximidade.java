// SensorProximidade.java
import java.util.stream.Collectors; // Garante que esta importação esteja presente

/**
 * Um tipo específico de {@link Sensor} que detecta outras entidades (robôs e obstáculos)
 * dentro de um determinado raio de alcance ao redor do robô.
 * Calcula distâncias tridimensionais para robôs e distâncias à superfície
 * de obstáculos (bounding box).
 */
public class SensorProximidade extends Sensor {

    /**
     * Construtor para o SensorProximidade.
     *
     * @param nome O nome do sensor (ex: "Detector Frontal").
     * @param raio O raio de detecção do sensor. Entidades dentro desta distância
     * serão detectadas.
     */
    public SensorProximidade(String nome, double raio) {
        super(nome, raio);
    }

    /**
     * Monitora o ambiente em busca de outros robôs e obstáculos dentro do raio de alcance.
     * Para robôs, calcula a distância entre os centros.
     * Para obstáculos, calcula a distância do centro do robô até o ponto mais próximo
     * na superfície da caixa delimitadora (bounding box) do obstáculo.
     *
     * @param ambiente O {@link Ambiente} contendo as entidades a serem verificadas.
     * @param robo     O {@link Robo} que está realizando a varredura com este sensor.
     * @return Uma string formatada listando todas as entidades detectadas e suas distâncias,
     * ou uma mensagem indicando que nada foi detectado.
     */
    @Override
    public String monitorar(Ambiente ambiente, Robo robo) {
        StringBuilder detections = new StringBuilder();
        detections.append("Sensor de Proximidade '").append(getNome()).append("' (Raio: ").append(getRaio()).append(") no Robô ").append(robo.getId()).append(" detectou:\n");
        boolean detected = false; // Flag para saber se algo foi detectado

        // Verifica outros robôs
        // Itera sobre todas as entidades no ambiente
        for (Robo otherRobo : ambiente.getEntidades().stream()
                .filter(e -> e instanceof Robo && e != robo) // Filtra para pegar apenas Robos que não sejam o próprio robô sensor
                .map(e -> (Robo) e)                         // Converte Entidade para Robo
                .collect(Collectors.toList())) {            // Coleta em uma lista

            // Calcula a distância 3D entre o robô sensor e o outro robô
            double distance = Math.sqrt(Math.pow(robo.getX() - otherRobo.getX(), 2) +
                    Math.pow(robo.getY() - otherRobo.getY(), 2) +
                    Math.pow(robo.getZ() - otherRobo.getZ(), 2));
            
            // Se a distância for menor ou igual ao raio do sensor, detectou
            if (distance <= getRaio()) {
                detections.append("  - Robô ").append(otherRobo.getId()).append(" @(").append(otherRobo.getX()).append(",").append(otherRobo.getY()).append(",").append(otherRobo.getZ()).append(") a ").append(String.format("%.2f", distance)).append(" unidades.\n");
                detected = true;
            }
        }

        // Verifica obstáculos
        // Itera sobre todas as entidades no ambiente
        for (Obstaculo obstaculo : ambiente.getEntidades().stream()
                .filter(e -> e instanceof Obstaculo)        // Filtra para pegar apenas Obstaculos
                .map(e -> (Obstaculo) e)                    // Converte Entidade para Obstaculo
                .collect(Collectors.toList())) {           // Coleta em uma lista

            // Calcula o ponto mais próximo na superfície do obstáculo (bounding box) ao robô
            double closestX = Math.max(obstaculo.getX1(), Math.min(robo.getX(), obstaculo.getX2()));
            double closestY = Math.max(obstaculo.getY1(), Math.min(robo.getY(), obstaculo.getY2()));
            double closestZ = Math.max(obstaculo.getZ1(), Math.min(robo.getZ(), obstaculo.getZ2()));

            // Calcula a distância 3D do robô a este ponto mais próximo na superfície do obstáculo
            double distanceToObstacleSurface = Math.sqrt(
                Math.pow(robo.getX() - closestX, 2) +
                Math.pow(robo.getY() - closestY, 2) +
                Math.pow(robo.getZ() - closestZ, 2)
            );

            // Se a distância à superfície for menor ou igual ao raio do sensor, detectou
            if (distanceToObstacleSurface <= getRaio()) {
                detections.append("  - Obstáculo ").append(obstaculo.getTipoObstaculo())
                          .append(" (").append(obstaculo.getRepresentacao()).append(") from (").append(obstaculo.getX1()).append(",").append(obstaculo.getY1()).append(",").append(obstaculo.getZ1())
                          .append(") to (").append(obstaculo.getX2()).append(",").append(obstaculo.getY2()).append(",").append(obstaculo.getZ2()).append(")")
                          .append(" a aprox. ").append(String.format("%.2f", distanceToObstacleSurface)).append(" unidades.\n");
                detected = true;
            }
        }

        // Se nada foi detectado, adiciona uma mensagem informando
        if (!detected) {
            detections.append("  Nenhum objeto detectado dentro do raio.\n");
        }
        return detections.toString();
    }
}