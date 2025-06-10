// Sensor.java

import br.unicamp.ic.mc322.lab05.ambiente.Ambiente;

/**
 * Classe abstrata base para todos os tipos de sensores.
 * Um sensor possui um nome e um raio de alcance (que pode ou não ser
 * relevante dependendo do tipo específico do sensor).
 * Define um método abstrato `monitorar` que as subclasses devem implementar.
 */
public abstract class Sensor {
    private final double raio; // O raio de alcance ou detecção do sensor.
    private String nome;       // Nome identificador do sensor.

    /**
     * Construtor para a classe Sensor.
     *
     * @param nome O nome do sensor (ex: "SensorDeProximidade frontal").
     * @param raio O raio de alcance do sensor. A interpretação deste raio
     * depende da subclasse específica do sensor.
     */
    public Sensor(String nome, double raio) {
        this.nome = nome;
        this.raio = raio;
    }

    /**
     * Retorna o nome do sensor.
     * @return O nome do sensor.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Retorna o raio de alcance do sensor.
     * @return O raio do sensor.
     */
    public double getRaio() {
        return raio;
    }

    /**
     * Método abstrato que define a ação de monitoramento do sensor.
     * Cada subclasse de Sensor implementará este método para realizar
     * sua lógica específica de detecção ou leitura de dados do ambiente
     * em relação ao robô que o carrega.
     *
     * @param ambiente O {@link Ambiente} que o sensor irá analisar.
     * @param robo     O {@link Robo} ao qual o sensor está acoplado e a partir do qual
     * o monitoramento é realizado (referência de posição, etc.).
     * @return Uma string contendo as informações coletadas ou o status detectado pelo sensor.
     */
    // O parâmetro foi alterado de Robo para Entidade para maior generalidade se necessário,
    // mas para a lógica do sensor, pode ser necessário converter para Robo.
    // Mantendo Robo por enquanto, conforme o Lab 3, assumindo que os sensores estão em Robos.
    public abstract String monitorar(Ambiente ambiente, Robo robo);
}