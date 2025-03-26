// Robô Aeréo - drone para gravação de video


public class Drone_Video extends Robo_Aereo {
    
    private int capacidade_memoria;
    private int memoria_atual;

    public Drone_Video(String nome, String direcao, int posicaoX, int posicaoY, int altitude_maxima, int capacidade_memoria)
    {
        super(nome, direcao, posicaoX, posicaoY, altitude_maxima);
        this.capacidade_memoria = capacidade_memoria;
        this.memoria_atual = 0;
    }

    public void armazenarVideo(int memoria)
    {
        if (memoria_atual + memoria <= capacidade_memoria)
        {
            memoria_atual += memoria;
        }
        else
        {
            System.out.println("A memória excede a capacidade do drone!");
        }
    }

    public void exibirMemoria()
    {
        System.out.println("Memória atual: " + this.memoria_atual + " de " + this.capacidade_memoria);
    }

}
