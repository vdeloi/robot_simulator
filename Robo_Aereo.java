// Robô Aeréo herda de Robô


public class RoboAereo extends Robo{

    private int altitude; // Altura atual que o Robô se encontra
    private int altitude_maxima; // Máxima altura que o Robô é capaz de chegar

    public RoboAereo(String nome, String direcao, int posicaoX, int posicaoY, int altitude_maxima)
    {
        super(nome, direcao, posicaoX, posicaoY); // Herdado de Robo
        this.altitude_maxima = altitude_maxima;
        this.altitude = 0; // Altitude inicial nula
    }

   public void subir(int metros) 
    {
        // Verifica se é possível aumentar a altitude ou não 
        
        if (altitude + metros <= altitudeMaxima) // No caso em que é possível, se aumenta a altitude e é impressa o nome do Robô e sua altitude atual
        {
            altitude += metros;
            System.out.println(nome + " subiu para " + altitude + " metros.");
        } else // No caso em que não é possível, se identifica o Robô e é impresso a sua altitude máxima
        {
            System.out.println(nome + " não pode subir além da altitude máxima de " + altitudeMaxima + " metros.");
        }
    }

    public void descer(int metros) 
    {
        // Verifica se é possível diminuir a altitude ou não
        
        if (altitude - metros >= 0) // Caso o Robô não se encontre já no solo, é possível diminuir sua altitude
        {
            altitude -= metros;
            System.out.println(nome + " desceu para " + altitude + " metros.");
        } else // O Robô não consegue descer mais
        {
            System.out.println(nome + " não pode descer mais, pois já está no solo.");
        }
    }
}
