class Robo
{
    // atributos da classe
    String nome;
    int posicaoX;
    int posicaoY;

    // método construtor
    Robo(String nome, int posicaoX, int posicaoY)
    {
        this.nome = nome;
        this.posicaoX = posicaoX;
        this.posicaoY = posicaoY;
    }

    // atualiza a posição do robô
    void mover(int deltaX, int deltaY)
    {
        this.posicaoX += deltaX;
        this.posicaoY += deltaY;
    }

    // retorna uma string que pode ser utilizada em outras interfaces
    // é utilizada na classe Main para exibir a posição
    String exibirPosicao()
    {
        // retorna a string "(x, y)" onde x é this.posicaoX, etc.
        return "(" + this.posicaoX + ", " + this.posicaoY + ")";
    }
}
