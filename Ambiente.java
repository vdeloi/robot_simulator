class Ambiente
{
    int largura;
    int altura;
    // assumindo que a região 2D do ambiente é [0, largura] x [0, altura]

    // construtor
    Ambiente(int largura, int altura)
    {
        this.largura = largura;
        this.altura = altura;
    }

    // retorna true se (x, y) está dentro dos limites [0, largura] x [0, altura]
    boolean dentroDosLimites(int x, int y)
    {
        if (x < 0 || y < 0)
        {
            return false;
        }
        
        return (x <= this.largura && y <= this.altura);
    }
}