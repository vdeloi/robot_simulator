// Classe Sensor


public abstract class Sensor {

    // Atributo protegido: subclasses podem acessá-lo diretamente.
    // Representa o alcance máximo do sensor.
    protected double raio;

    public Sensor(double raio) {
        this.raio = raio;
    }

    public double getRaio() {
        return raio;
    }

    public abstract String monitorar(Ambiente amb, Robo robo);

    /**
    
     */
    @Override
    public String toString() {
        return "Sensor [raio=" + raio + "]";
    }
}

