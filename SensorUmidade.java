// Subclasse Sensor de Umidade

public class SensorUmidade extends Sensor {

    public SensorUmidade(double raio) {
        super(raio); // Chama o construtor do snesor
    }
  
    @Override
    public String monitorar(Ambiente amb, Robo robo) {
      
        int umidadeSimulada = 20 + (int)(Math.random() * 60); // Vamos fazer uma variação média entre 20% e 80%
        return String.format("Sensor Umidade: %d%%", umidadeSimulada);
    }

    @Override
    public String toString() {
        return "SensorUmidade [raio=" + raio + "]";
    }
}
