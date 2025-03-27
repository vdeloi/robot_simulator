// Robô Cachorro

public class RoboCachorro extends RoboTerrestre
{    
    private int capacidade_latido; // Volume máximo que o latido do cachorro consegue atingir
    private int volume_latido_atual; // O volume no momento

    public RoboCachorro(String nome, String direcao, int posicaoX, int posicaoY, int velocidade_maxima, int capacidade_latido)
    {
        super(nome, direcao, posicaoX, posicaoY, velocidade_maxima); // Herdado da classe RoboTerrestre
        this.capacidade_latido = capacidade_latido;
        this.volume_latido_atual = 0; // Volume começa nulo
    }

    public void latir(int latido) 
    {
      // Função que verifica se é possível aumentar o volume do latido do cachorro
        if (volume_latido_atual + latido <= capacidade_latido) // Se a condição é cumprida, o volume aumenta
        {
            volume_latido_atual += latido;
        }
        else // Caso se queira aumentar o latido além da capacidade do robô, é impressa a seguinte mensagem
        {
            System.out.println("O volume do latido excede a capacidade do robô cachorro!");
        }
    }

    public void exibirLatido()
    {
      // Função que imprime o volume no momento e a capacidade máxima
        System.out.println("Volume do latido atual: " + this.volume_latido_atual + " de " + this.capacidade_latido);
    }

}
