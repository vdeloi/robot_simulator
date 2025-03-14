class Main
{
    public static void main(String[] args)
    {
        Ambiente amb; // cria uma variável para armazenar uma instância da classe Ambiente
        Robo r1; 
        Robo r2;

        // cria os objetos
        amb = new Ambiente(100, 100); // um ambiente 2D 100x100
        r1 = new Robo("r2-d2", 50, 50); 
        r2 = new Robo("T-800", 0, 0);

        Robo[] robos = {r1, r2}; // array para armazenar os robôs

        // move os robôs 
        r1.mover(25, 25);
        r2.mover(0, -1);

        // verifica se as novas posições estão dentro dos limites do ambiente

        for (Robo r: robos ) // iteração sobre o array de robôs
        {
            // verifica se as novas posições estão dentro dos limites do ambiente
            if (amb.dentroDosLimites(r.posicaoX, r.posicaoY))
            {
                System.out.println("O robô " + r.nome + " está dentro dos limites do ambiente.");
            }
            else
            {
                System.out.println("O robô " + r.nome + " está fora dos limites do ambiente!");
            }

            // imprime a posição  console 
            System.out.println("Posição do robô " + r.nome + ": " + r.exibirPosicao());
        }
    }    

}