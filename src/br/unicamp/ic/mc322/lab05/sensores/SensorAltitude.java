package src.br.unicamp.ic.mc322.lab05.sensores;
// SensorAltitude.java

import Robo;
import src.br.unicamp.ic.mc322.lab05.ambiente.Ambiente;

/**
 * Um tipo específico de {@link Sensor} que mede a altitude (coordenada Z)
 * do robô ao qual está acoplado.
 * O "raio" herdado da classe Sensor pode não ser diretamente relevante para
 * um sensor de altitude simples, mas é mantido pela herança.
 */
public class SensorAltitude extends Sensor {

    /**
     * Construtor para o SensorAltitude.
     *
     * @param nome O nome do sensor (ex: "Altímetro Principal").
     * @param raio O raio de alcance (geralmente não usado para altitude, pode ser 0).
     */
    public SensorAltitude(String nome, double raio) {
        super(nome, raio); // O raio pode não ser muito relevante para um sensor de altitude simples
    }

    /**
     * Monitora e retorna a altitude atual do robô.
     * A altitude é obtida diretamente da coordenada Z do robô.
     *
     * @param ambiente O {@link Ambiente} (não utilizado diretamente por este sensor, mas parte da assinatura do método).
     * @param robo     O {@link Robo} cuja altitude será medida.
     * @return Uma string formatada indicando a altitude detectada pelo sensor.
     * Ex: "Sensor de Altitude 'Altímetro Principal' detectou altitude: 10 metros."
     */
    @Override
    public String monitorar(Ambiente ambiente, Robo robo) {
        // Para o Lab 04, todos os Robos são Entidade e possuem getZ()
        return "Sensor de Altitude '" + getNome() + "' detectou altitude: " + robo.getZ() + " metros.";
    }
}