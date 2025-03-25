public class RoboTerrestre extends Robo{

    private int velocidade_maxima;

    public RoboTerrestre(String nome, String direcao, int posicaoX, int posicaoY, int velocidade_maxima)
    {
        super(nome, direcao, posicaoX, posicaoY);
        this.velocidade_maxima = velocidade_maxima;
    }
    public void mover(int deltaX, int deltaY)
    {
        super.mover(deltaX, deltaY);
    }




}
