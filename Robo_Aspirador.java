// Robô Aspirador


public class RoboAspirador extends RoboTerrestre
{
    
    private int capacidade_sujeira;
    private int sujeira_atual;

    public RoboAspirador(String nome, String direcao, int posicaoX, int posicaoY, int velocidade_maxima, int capacidade_sujeira)
    {
        super(nome, direcao, posicaoX, posicaoY, velocidade_maxima);
        this.capacidade_sujeira = capacidade_sujeira;
        this.sujeira_atual = 0;
    }

    public void aspirar(int sujeira)
    {
        if (sujeira_atual + sujeira <= capacidade_sujeira)
        {
            sujeira_atual += sujeira;
        }
        else
        {
            System.out.println("A sujeira excede a capacidade do robô aspirador! É necessário limpar o reservatório para continuar.");
        }
    }

    public void exibirSujeira()
    {
        System.out.println("Sujeira atual: " + this.sujeira_atual + " de " + this.capacidade_sujeira);
    }

}
