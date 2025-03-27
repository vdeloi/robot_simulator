// Robô Aeréo - Drone de Carga - herda de Robo_Aereo


public class Drone_Carga extends Robo_Aereo{
    
    private int capacidade_carga; // Máximo de carga que o drone é capaz de carregar
    private int carga_atual; // Quanto ele carrega no momento

    public Drone_Carga(String nome, String direcao, int posicaoX, int posicaoY, int altitude_maxima, int capacidade_carga)
    {
        super(nome, direcao, posicaoX, posicaoY, altitude_maxima); // Herda de Robo_Aereo
        this.capacidade_carga = capacidade_carga;
        this.carga_atual = 0; // Começa descarregado
    }

    public void carregar(int carga)
    {
        // Verifica se é possível carregar o drone ou não
        if (carga_atual + carga <= capacidade_carga) // Caso a carga não ultrapasse a capacidade de carga, o valor de carga atual é aumentado
        {
            carga_atual += carga;
        }
        else // Se a carga é maior que a capacidade do Robô, se imprime a seguinte mensagem
        {
            System.out.println("A carga excede a capacidade do drone!");
        }
    }

    public void descarregar(int carga)
    {
        // Verifica se é possível descarregar o drone ou não
        if (carga_atual - carga >= 0) // É posśível caso se queira descarregar uma carga dentro dos limites da carga atual do drone
        {
            carga_atual -= carga;
        }
        else // Se quiser descarregar mais do que o drone tem de carga no momento, a seguinte mensagem é impressa
        {
            System.out.println("A carga a ser descarregada é maior que a carga atual!");
        }
    }

    public void exibirCarga()
    {
        // Imprime informações sobre a carga no momento e o máximo que o drone é capaz de carregar
        System.out.println("Carga atual: " + this.carga_atual + " de " + this.capacidade_carga);
    }

}
