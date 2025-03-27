// Robô Aeréo - drone para gravação de video


public class Drone_Video extends Robo_Aereo {
    
    private int capacidade_memoria; // Máximo de espaço de armazenamento que o Robõ possui
    private int memoria_atual; // Quanto do armazenamento já está sendo usado

    public Drone_Video(String nome, String direcao, int posicaoX, int posicaoY, int altitude_maxima, int capacidade_memoria)
    {
        super(nome, direcao, posicaoX, posicaoY, altitude_maxima); // Herda da classe RoboAereo
        this.capacidade_memoria = capacidade_memoria;
        this.memoria_atual = 0; // Começa com o armazenamento vazio
    }

    public void armazenarVideo(int memoria)
    {
        // Verifica se é possível armazenar o vídeo gravado a depender da quantidade de memória que ele consome
        if (memoria_atual + memoria <= capacidade_memoria) // Caso a memória usada não ultrapasse o máximo, o vídeo é armazenado
        {
            memoria_atual += memoria;
        }
        else // Caso o vídeo seja consuma mais espaço de armazenamento do que se tem disponível, a seguinte mensagem é impressa
        {
            System.out.println("A memória excede a capacidade do drone!");
        }
    }

    public void exibirMemoria()
    {
        // Mostra informações sobre o armazenamento em uso e o máximo de memória disponivel
        System.out.println("Memória atual: " + this.memoria_atual + " de " + this.capacidade_memoria);
    }

}
