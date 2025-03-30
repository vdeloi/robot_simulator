/* RoboCarroAutonomo.java */ 

class RoboCarroAutonomo extends RoboTerrestre
{
    private int numDePassageiros; // número de passageiros do carro
    private int numMaximoDePassageiros; // número máximo de passageiros do carro
    private double NivelDaBateria ; // nível da bateria do carro em porcentagem
    private int kilometrosRodados; // número de quilômetros rodados pelo carro

    // Método construtor
    RoboCarroAutonomo(String nome, int posicaoX, int posicaoY, String direcao, int velocidadeMaxima, int numDePassageiros, int numMaximoDePassageiros, double nivelDaBateria)
    {
        // Construtor da classe pai 
        super(nome, posicaoX, posicaoY, direcao, velocidadeMaxima);
        this.numDePassageiros = numDePassageiros;
        this.numMaximoDePassageiros = numMaximoDePassageiros;
        this.NivelDaBateria = nivelDaBateria;
        this.kilometrosRodados = 0; // inicia com zero km rodados
    }

    // Getter para numDePassageiros
    public int getNumDePassageiros() {
        return numDePassageiros;
    }

    // Setter para numDePassageiros
    public void setNumDePassageiros(int numDePassageiros) {
        if (numDePassageiros > numMaximoDePassageiros) {
            System.out.println("ERRO: Número de passageiros excede o máximo permitido!");
            this.numDePassageiros = numMaximoDePassageiros; // clipa no máximo permitido
        } else if (numDePassageiros < 0) {
            System.out.println("ERRO: Número de passageiros não pode ser negativo!");
            this.numDePassageiros = 0; // clipa no mínimo permitido
        } else {
            this.numDePassageiros = numDePassageiros;
        }
    }

    // Getter para numMaximoDePassageiros
    public int getNumMaximoDePassageiros() {
        return numMaximoDePassageiros;
    }

    // Getter para NivelDaBateria
    public double getNivelDaBateria() {
        return NivelDaBateria;
    }

    // Setter para NivelDaBateria
    public void setNivelDaBateria(double nivelDaBateria) {
        if (nivelDaBateria > 1) {
            System.out.println("ERRO: Nível da bateria não pode ser maior que 100%!");
            this.NivelDaBateria = 1; // clipa no máximo permitido
        } else if (nivelDaBateria < 0) {
            System.out.println("ERRO: Nível da bateria não pode ser negativo!");
            this.NivelDaBateria = 0; // clipa no mínimo permitido
        } else {
            this.NivelDaBateria = nivelDaBateria;
        }
    }

    // Getter para kilometrosRodados
    public int getKilometrosRodados() {
        return kilometrosRodados;
    }

    

}

