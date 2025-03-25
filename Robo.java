class Robo
{
    // atributos da classe
    private String nome;
    private String direcao;
    private int posicaoX;
    private int posicaoY;

    // método construtor
    Robo(String nome, String direcao, int posicaoX, int posicaoY)
    {
        this.nome = nome;
        this.direcao = direcao; //(Norte, Leste, Sul, Oeste).
        this.posicaoX = posicaoX;
        this.posicaoY = posicaoY;
    }

    // atualiza a posição do robô
    public void mover(int deltaX, int deltaY)
    {
        this.posicaoX += deltaX;
        this.posicaoY += deltaY;
    }

    // identifica obstáculos ao redor do robô
    public void identificarObstaculo()
    {
        // não implementado
    }


    // retorna uma string que pode ser utilizada em outras interfaces
    // é utilizada na classe Main para exibir a posição
    public String exibirPosicao()
    {
        // retorna a string "(x, y)" onde x é this.posicaoX, etc.
        return "(" + this.posicaoX + ", " + this.posicaoY + ")";
    }
}
