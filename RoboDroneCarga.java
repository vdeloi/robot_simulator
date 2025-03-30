/* RoboDroneDeCarga.java */ 

class RoboDroneDeCarga extends RoboAereo
{
    private int carga; // carga inicial do drone
    private int cargaMaxima; // carga máxima que o drone aguenta

    // Método construtor
    RoboDroneDeCarga(String nome, int posicaoX, int posicaoY, String direcao, int altitude, int altidudeMaxima, int carga, int cargaMaxima)
    {
        // Construtor da classe pai 
        super(nome, posicaoX, posicaoY, direcao, altitude, altidudeMaxima);
        this.carga = carga;
        this.cargaMaxima = cargaMaxima;
    }

    // Getter para carga
    public int getCarga() {
        return carga;
    }

    // Setter para carga
    public void setCarga(int carga) 
    {
        if (carga > cargaMaxima) {
            System.out.println("ERRO: Carga excede a carga máxima do drone!");
            this.carga = cargaMaxima; // clipa na carga máxima
        } else if (carga < 0) {
            System.out.println("ERRO: Carga não pode ser negativa!");
            this.carga = 0; // clipa na carga mínima
        } else {
            this.carga = carga;
        }
    }
    

    // Getter para cargaMaxima
    public int getCargaMaxima() {
        return cargaMaxima;
    }

    public void descarregar()
    {
        System.out.println("Entregando carga de " + this.carga + " kg");
        this.carga = 0; // entrega a carga
    }



   
}