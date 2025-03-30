/* RoboDeResgate.java */

class RoboDeResgate extends RoboTerrestre
{
    private int cargaMaxima; // carga máxima que o robô pode levantar
    private boolean sensorTermico; // se o robô tem sensor térmico ou não
    private boolean sensorRadar; // se o robô tem sensor de radar ou não

    public RoboDeResgate(String nome, int posicaoX, int posicaoY, String direcao, int velocidadeMaxima, int cargaMaxima, boolean sensorTermico, boolean sensorRadar)
    {
        // Construtor da classe pai 
        super(nome, posicaoX, posicaoY, direcao, velocidadeMaxima);
        this.cargaMaxima = cargaMaxima;
        this.sensorTermico = sensorTermico;
        this.sensorRadar = sensorRadar;
    }

    // Getter para cargaMaxima
    public int getCargaMaxima() {
        return cargaMaxima;
    }

    // Getter para sensorTermico
    public boolean hasSensorTermico() {
        return sensorTermico;
    }

    // Getter para sensorRadar
    public boolean hasSensorRadar() {
        return sensorRadar;
    }

    // Método para erguer carga
    public void erguerCarga(int carga) {
        if (carga > cargaMaxima) {
            System.out.println("ERRO: Carga excede a carga máxima do robô!");
        } else if (carga < 0) {
            System.out.println("ERRO: Carga não pode ser negativa!");
        } else {
            System.out.println("Erguendo carga de " + carga + " kg.");
        }
    }
    


}


