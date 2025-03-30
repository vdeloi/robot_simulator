/* Classe Main.java */

// Esta classe contem o método main e testa as demais classes

public class Main
{
    public static void main(String[] args)
    {
        
        // Cria um ambiente 100 x 100
        Ambiente amb = new Ambiente(100, 100);
        
        // Define a elevação máxima do ambiente
        // elevação máxima é 100 metros, ou seja, a região é [0, 100] x [0, 100] x [0, 100]
        amb.definirElevacao(100); 


        // Adicionar os diferentes tipos de robôs no ambiente

        // Cria os robôs
        Robo r1 = new Robo();
        RoboTerrestre r2 = new RoboTerrestre();
        RoboAereo r3 = new RoboAereo();
        RoboAspirador r4 = new RoboAspirador();
        RoboCachorro r5 = new RoboCachorro();
        RoboDroneCarga r6 = new RoboDroneCarga();
        RoboPassaro r7 = new RoboPassaro();

        // Adiciona no ambiente
        amb.adicionarRobo(r1);
        amb.adicionarRobo(r2);
        amb.adicionarRobo(r3);
        amb.adicionarRobo(r4);
        amb.adicionarRobo(r5);
        amb.adicionarRobo(r6);
        amb.adicionarRobo(r7);


        // Testar movimentação terrestre

        // Testar movimentacao aérea

        // Testar os limites de velocidade

        // Testar altura máxima

        // Testar método específicos

        // Exibir a posição final de cada robo no console
        

    }   
}