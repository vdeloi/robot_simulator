public class RoboAereo extends Robo{

    private int altitude;
    private int altitude_maxima;

    public RoboAereo(String nome, String direcao, int posicaoX, int posicaoY, int altitude_maxima)
    {
        super(nome, direcao, posicaoX, posicaoY);
        this.altitude = 0;
        this.altitude_maxima = altitude_maxima;
    }

   public void subir(int metros) {
        if (altitude + metros <= altitudeMaxima) 
        {
            altitude += metros;
            System.out.println(nome + " subiu para " + altitude + " metros.");
        } else 
        {
            System.out.println(nome + " não pode subir além da altitude máxima de " + altitudeMaxima + " metros.");
        }
    }

    public void descer(int metros) 
    {
        if (altitude - metros >= 0) 
        {
            altitude -= metros;
            System.out.println(nome + " desceu para " + altitude + " metros.");
        } else 
        {
            System.out.println(nome + " não pode descer mais, pois já está no solo.");
        }
    }
}
