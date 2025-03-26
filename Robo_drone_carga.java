// Robô Aeréo - Drone de Carga


public class Drone_Carga extends Robo_Aereo{
    
    private int capacidade_carga;
    private int carga_atual;

    public Drone_Carga(String nome, String direcao, int posicaoX, int posicaoY, int altitude_maxima, int capacidade_carga)
    {
        super(nome, direcao, posicaoX, posicaoY, altitude_maxima);
        this.capacidade_carga = capacidade_carga;
        this.carga_atual = 0;
    }

    public void carregar(int carga)
    {
        if (carga_atual + carga <= capacidade_carga)
        {
            carga_atual += carga;
        }
        else
        {
            System.out.println("A carga excede a capacidade do drone!");
        }
    }

    public void descarregar(int carga)
    {
        if (carga_atual - carga >= 0)
        {
            carga_atual -= carga;
        }
        else
        {
            System.out.println("A carga a ser descarregada é maior que a carga atual!");
        }
    }

    public void exibirCarga()
    {
        System.out.println("Carga atual: " + this.carga_atual + " de " + this.capacidade_carga);
    }

}
