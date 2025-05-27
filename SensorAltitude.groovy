// SensorAltitude.java
public class SensorAltitude extends Sensor {
    public SensorAltitude(String nome, double raio) {
        super(nome, raio);
    }

    @Override
    public String monitorar(Ambiente ambiente, Robo robo) {
        // Assuming Robo class has a getAltitude() method or similar
        // For terrestrial robots, altitude can be considered 0
        int currentAltitude = 0;
        if (robo instanceof RoboAereo) {
            currentAltitude = ((RoboAereo) robo).getAltitude();
        }
        return "Sensor de Altitude '" + getNome() + "' detectou altitude: " + currentAltitude + " metros.";
    }
}