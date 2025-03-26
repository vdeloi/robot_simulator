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
    
    @Override // Vamos usar para garantir que o método está de fato sobrescrevendo um método da superclasse
    public void mover(int deltaX, int deltaY) 
    {
        // Calcula a velocidade do robo como a soma dos deslocamentos absolutos
        int velocidade_movimento = Math.abs(deltaX) + Math.abs(deltaY);

        // Verifica se a velocidade de movimento é permitida / não ultrapassa a velocidade máxima
        if (velocidade_movimento <= velocidade_maxima)  // Ocorre movimento
        {
            super.mover(deltaX, deltaY);
            
        } else // Não há movimento, já que ultrapassa velocidade_maxima
        {
            System.out.println("Movimento excede a velocidade máxima permitida!");
        }
    }
}

}
