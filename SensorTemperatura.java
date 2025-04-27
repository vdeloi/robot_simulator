// Subclasse Sensor de Temperatura 

public class SensorTemperatura extends Sensor {

    public SensorTemperatura(double raio) {
        super(raio); // Chama o construtor do sensor
    }


     @Override
    public String monitorar(Ambiente amb, Robo robo) { // Monitora a temperatura presente em um certo raio do robo
      
        double temperaturaSimulada = 10.0 + Math.random() * 30.0; // Gera entre 10.0 e < 40.0
        return String.format("Sensor Temperatura: %.1f °C", temperaturaSimulada);
    }

    @Override
    public String toString() {
        return "SensorTemperatura [raio=" + raio + "]";
    }
}
