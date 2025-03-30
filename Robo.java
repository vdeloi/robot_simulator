/* Robo.java */

// Robo é a classe base, pai das classes RoboAereo e RoboTerrestre

class Robo
{
    private String nome;
    private String direcao; // norte, sul, leste, oeste
    private int posicaoX;
    private int posicaoY;


    // Método construtor

    public Robo(String nome, int posicaoX, int posicaoY, String direcao)
    {
        this.nome = nome;
        this.posicaoX = posicaoX;
        this.posicaoY = posicaoY;
        this.direcao = direcao;

    }

    // Métodos get

    // Retorna o nome do robô
    public String getNome()
    {
        return this.nome;
    }

    // retorna a direção do robô
    public String getDirecao()
    {
        return this.direcao;
    }

    // Retorna a posição x do robô
    public int getPosicaoX()
    {
        return this.posicaoX;

    }

    // Retorna a posição y do robô
    public int getPosicaoY()
    {
        return this.posicaoY;
    }

    // Métodos set

    public void setDirecao(String direcao)
    {
        this.direcao = direcao;
    }

    public void setPosicaoX(int x)
    {
        this.posicaoX = x;
    }

    public void setPosicaoY(int y)
    {
        this.posicaoY = y;
    }
  

    // Move robo para o ponto (posicaoX + deltaX, posicaoY + deltaY)

    public void mover(int deltaX, int deltaY)
    {
        this.posicaoX += deltaX;
        this.posicaoY += deltaY;

    }

    // Recebe um ambiente A, olha para o sul imediato, norte imediato, leste imediato e oeste imediato para 
    // identificar se há obstáculos, por enquanto apenas robôs são obstáculos 
    //
    // Retorna uma lista de valores booleanos "haObstaculo"
    // o primeiro item da lista de valores booleano é true se há um obstaculo no seu norte imediato
    // o segundo no seu sul, o terceiro no seu oeste e o quarto no seu leste imeadiato
    public boolean[] identificarObstaculo(Ambiente A) {
        // boolean[] haObstaculo = {false, false, false, false}; 
        // Ordem: [norte, sul, leste, oeste]
        boolean[] haObstaculo = {false, false, false, false};
    
        // Itera sobre todos os robôs do ambiente
        for (Robo r : A.getListaDeRobos()) {
            // Ignora a si mesmo
            if (r == this) continue;
    
            // Verifica o obstáculo no norte imediato (y + 1)
            if (this.posicaoY + 1 == r.getPosicaoY() && this.posicaoX == r.getPosicaoX()) {
                haObstaculo[0] = true;
            }
            // Verifica o obstáculo no sul imediato (y - 1)
            if (this.posicaoY - 1 == r.getPosicaoY() && this.posicaoX == r.getPosicaoX()) {
                haObstaculo[1] = true;
            }
            // Verifica o obstáculo no leste imediato (x + 1)
            if (this.posicaoX + 1 == r.getPosicaoX() && this.posicaoY == r.getPosicaoY()) {
                haObstaculo[2] = true;
            }
            // Verifica o obstáculo no oeste imediato (x - 1)
            if (this.posicaoX - 1 == r.getPosicaoX() && this.posicaoY == r.getPosicaoY()) {
                haObstaculo[3] = true;
            }
        }
        return haObstaculo;
    }
    

    // Retorna uma string que pode ser utilizada para exibir a posição do robo
    public String exibirPosicao()
    {
        return "( " + this.posicaoX + ", " + this.posicaoY + " )"; 
    }


    
}