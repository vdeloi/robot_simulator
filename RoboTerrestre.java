/* RoboTerrestre.java */

class RoboTerrestre extends Robo // herda de Robo
{
    private int velocidadeMaxima; // "velocidade máxima do robo. Assumimos que o tempo para se deslocar é uniforme nos robôs"

    // Construtor
    public RoboTerrestre(String nome, int posicaoX, int posicaoY, String direcao, int velocidadeMaxima)
    {
        // chama o construtor do pai
        super(nome, posicaoX, posicaoY, direcao);
        this.velocidadeMaxima = velocidadeMaxima;

    }

    @Override // sobrescrita do método mover da classe Robo
    // agora o robo só se move se a "velocidade" do movimento não ultrapassa a velocidade máxima do objeto 
    public void mover(int deltaX, int deltaY)
    {
        if (Math.sqrt(deltaX*deltaX + deltaY*deltaY) > (double) this.velocidadeMaxima) // casting pois Math.sqrt() retorna double
        {
            // Aqui não deveriamos usar System.out mas ainda não aprendemos sobre exceções na linguagem, então...
            // Em Python eu tentaria uma clausula try... except... aqui
            System.out.println("ERRO: Velocidade do deslocamento é maior que a velocidade máxima do objeto!");
        }
        else
        {
            // Métodos get e set pois os atributos são privados na classe pai
            // e a subclasse não tem acesso
            this.setPosicaoX(this.getPosicaoX() + deltaX);
            this.setPosicaoY(this.getPosicaoY() + deltaY);
        }

    }


}