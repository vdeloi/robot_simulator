// Interface Sensoreavel

public interface Sensoreavel {
    // O método acionarSensores na classe Robo já retorna String, então a interface também pode.
    String acionarSensores(Ambiente ambiente) throws RoboDesligadoException; 
}