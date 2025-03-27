// Robô Aspirador


public class RoboAspirador extends RoboTerrestre
{
    
    private int capacidade_sujeira; // O máximo que o Robô consegue aspirar
    private int sujeira_atual; // O quanto se tem de sujeira armazenada pelo Robô no momento

    public RoboAspirador(String nome, String direcao, int posicaoX, int posicaoY, int velocidade_maxima, int capacidade_sujeira)
    {
        super(nome, direcao, posicaoX, posicaoY, velocidade_maxima); // Herda de RoboTerrestre
        this.capacidade_sujeira = capacidade_sujeira;
        this.sujeira_atual = 0; // Começa com o armazenamento de sujeira vazio
    }

    public void aspirar(int sujeira)
    {
        // Verifica se o Robô consegue limpar a sujeira de acordo com a sua capacidade
        if (sujeira_atual + sujeira <= capacidade_sujeira) // Aqui, ele ainda possui espaço suficiente para retirar a sujeira em seu espaço de armazenamento
        {
            sujeira_atual += sujeira;
        }
        else // Nesse caso, a sujeira é maior que a capacidade do Robô e, portanto, é necessário limpá-lo para que continue
        {
            System.out.println("A sujeira excede a capacidade do robô aspirador! É necessário limpar o reservatório para continuar.");
        }
    }

    public void exibirSujeira()
    {
        // Informa sobre o quão cheio o armazenamento se encontra em relação ao total que o reservatório é capaz de suṕortar
        System.out.println("Sujeira atual: " + this.sujeira_atual + " de " + this.capacidade_sujeira);
    }

}
