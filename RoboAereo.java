/* RoboAereo.java */

class RoboAereo extends Robo
{
    private int altitude; // altidude inicial do robô aéreo
    private int altitudeMaxima; // altitude máxima permitida

    // método construtor
    RoboAereo(String nome, int posicaoX, int posicaoY, String direcao, int altidude, int altitudeMaxima)
    {
        super(nome, posicaoX, posicaoY, direcao);
        this.altitudeMaxima = altitudeMaxima;

        // Se a altitue inicial for maior que a máxima a altitude inicial é "clipada" na altitude máxima
        if (altitude > altitudeMaxima)
        {
            System.out.println("ERRO: altitude inicial maior que altitude máxima");
            this.altitude = altitudeMaxima;
        }
        else if (altitude < 0) // tenta uma altitude inicial negativa
        {
            System.out.println("ERRO: Robo não pode ter altitude inicial negativa");
            this.altitude = 0; // se inicia em zero então
        }
        else // tudo Ok com a altitude
        {
            this.altitude = altidude;

        }
        

    }

    public void subir(int metros)
    {
        if (this.altitude + metros > this.altitudeMaxima) // Se ele tentar subir mais que o máximo permitido
        {
            System.out.println("ERRO: Está tentando subir mais que o máximo permitido!");
            this.altitude = this.altitudeMaxima; // clipa no máximo permitido

        }

    }

    public void descer(int metros)
    {
        if (this.altitude - metros < 0) // está tentando "passar do chão"
        {
            System.out.println("ERRO: RoboAereo não pode ter altitude negativa.");
            this.altitude = 0; // clipa no zero
        }

    }
    
}