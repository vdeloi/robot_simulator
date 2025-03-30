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
        Robo r1 = new Robo("R2-D2", 10, 20, "Norte");
        RoboTerrestre r2 = new RoboTerrestre("T-800", 30, 40, "Leste", 50);
        RoboAereo r3 = new RoboAereo("T-1000", 50, 60, "Sul", 0, 80);
        RoboCarroAutonomo r4 = new RoboCarroAutonomo("T-X", 51, 60, "Oeste", 0, 2, 4, 0.5);
        RoboDeResgate r5 = new RoboDeResgate("C-3PO", 10, 21, "Norte", 50, 100, true, true);
        RoboDroneDeCarga r6 = new RoboDroneDeCarga("Wall-E", 10, 22, "Norte", 50, 100, 2, 4);
        RoboDroneDeVigilancia r7 = new RoboDroneDeVigilancia("ED209", 11, 21, "Norte", 50, 100, 2, 4, 5);

        // Adiciona no ambiente
        amb.adicionarRobo(r1);
        amb.adicionarRobo(r2);
        amb.adicionarRobo(r3);
        amb.adicionarRobo(r4);
        amb.adicionarRobo(r5);
        amb.adicionarRobo(r6);
        amb.adicionarRobo(r7);


        // Loop para exibir o nome dos robôs
        System.out.println("Os robôs no ambiente são:");
        for (Robo r : amb.getListaDeRobos()) // 
        {
            System.out.println("Nome do robô: " + r.getNome());
        }


        // Testando métodos da classe Robo (r1)

        // Exibindo informações iniciais de r1
        System.out.println("\nTestando métodos de Robo (r1):");
        System.out.println("Nome: " + r1.getNome());
        System.out.println("Direção inicial: " + r1.getDirecao());
        System.out.println("Posição inicial: " + r1.exibirPosicao());

        // Testar o método mover (move o robô para (posicaoX + 2, posicaoY + 3))
        r1.mover(2, 3);
        System.out.println("Após mover(2,3): " + r1.exibirPosicao());

        // Testar os métodos set (alterar direção e posição)
        r1.setDirecao("Sul");
        r1.setPosicaoX(15);
        r1.setPosicaoY(25);
        System.out.println("Após setDirecao e setPosicao: Direção - " + r1.getDirecao() + ", Posição - " + r1.exibirPosicao());

        // Testar identificarObstaculo (verificando obstáculos no ambiente)
        // Obs.: Certifique-se de que o ambiente (amb) possui outros robôs para que o teste seja significativo.
        boolean[] obstaculos = r1.identificarObstaculo(amb);
        System.out.println("Obstáculos detectados (Norte, Sul, Leste, Oeste): " 
                            + obstaculos[0] + ", " 
                            + obstaculos[1] + ", " 
                            + obstaculos[2] + ", " 
                            + obstaculos[3]);

        /* Testando métodos da classe RoboTerrestre (r2): */
        System.out.println("\nTestando métodos de RoboTerrestre (r2):");
        System.out.println("Nome: " + r2.getNome());
        System.out.println("Posição inicial: " + r2.exibirPosicao());

        // Tentativa de mover com deslocamento dentro do limite da velocidade máxima.
        // Exemplo: deltaX = 3, deltaY = 4 -> velocidade = 5 (dentro do limite de 50)
        System.out.println("Tentando mover(3,4):");
        r2.mover(3,4);
        System.out.println("Nova posição de T-800: " + r2.exibirPosicao());

        // Tentativa de mover com deslocamento que excede a velocidade máxima.
        // Exemplo: deltaX = 30, deltaY = 40 -> velocidade = 50 (igual ao limite, movendo é permitido)
        // Usaremos um deslocamento maior para forçar o erro, por exemplo, deltaX = 31, deltaY = 41 -> velocidade ≈ 53.53
        System.out.println("Tentando mover(31,41) - deve exceder a velocidade máxima:");
        r2.mover(31,41);
        System.out.println("Após tentativa de mover(31,41), posição de T-800 permanece: " + r2.exibirPosicao());

        /* Testando métodos da classe RoboAereo (r3): */
        // Supondo que exista um método getAltitude() para obter a altitude atual
        System.out.println("\nTestando métodos de RoboAereo (r3):");

        // Exibe a altitude inicial
        System.out.println("Altitude inicial de T-1000: " + r3.getAltitude());

        // Tentar subir 30 metros (dentro do limite, pois 0 + 30 <= 80)
        System.out.println("Tentando subir 30 metros:");
        r3.subir(30);
        System.out.println("Altitude após subir 30 metros: " + r3.getAltitude());

        // Tentar subir 60 metros (deve exceder o limite; 30 + 60 = 90 > 80)
        System.out.println("Tentando subir 60 metros (deve exceder a altitude máxima):");
        r3.subir(60);
        System.out.println("Altitude após tentar subir 60 metros: " + r3.getAltitude());

        // Tentar descer 50 metros (dentro do limite, desde que a altitude atual seja >= 50)
        System.out.println("Tentando descer 50 metros:");
        r3.descer(50);
        System.out.println("Altitude após descer 50 metros: " + r3.getAltitude());

        // Tentar descer 40 metros (se a altitude cair abaixo de 0 ocorrerá um erro)
        System.out.println("Tentando descer 40 metros (deve resultar em erro se a altitude ficar negativa):");
        r3.descer(40);
        System.out.println("Altitude após tentar descer 40 metros: " + r3.getAltitude());

        /* Testando métodos da classe RoboCarroAutonomo (r4): */
        System.out.println("\nTestando métodos de RoboCarroAutonomo (r4):");
        System.out.println("Nome: " + r4.getNome());
        System.out.println("Posição inicial: " + r4.exibirPosicao());
        System.out.println("Número de passageiros (inicial): " + r4.getNumDePassageiros());
        System.out.println("Nível da bateria (inicial): " + r4.getNivelDaBateria());
        System.out.println("Kilômetros rodados (inicial): " + r4.getKilometrosRodados());

        // Testando mover: tentar mover(2,2) (dentro do limite da velocidade)
        System.out.println("Tentando mover(2,2):");
        r4.mover(2,2);
        System.out.println("Posição após mover(2,2): " + r4.exibirPosicao());

        // Testando setter para número de passageiros
        System.out.println("Ajustando número de passageiros para 1 (dentro do limite):");
        r4.setNumDePassageiros(1);
        System.out.println("Número de passageiros após ajuste: " + r4.getNumDePassageiros());

        System.out.println("Ajustando número de passageiros para 5 (excede o máximo permitido):");
        r4.setNumDePassageiros(5);
        System.out.println("Número de passageiros após tentativa de ajuste: " + r4.getNumDePassageiros());

        // Testando setter para o nível da bateria
        System.out.println("Ajustando nível da bateria para 0.8 (80%):");
        r4.setNivelDaBateria(0.8);
        System.out.println("Nível da bateria após ajuste: " + r4.getNivelDaBateria());

        System.out.println("Ajustando nível da bateria para 1.2 (excede 100%):");
        r4.setNivelDaBateria(1.2);
        System.out.println("Nível da bateria após tentativa de ajuste: " + r4.getNivelDaBateria());

        System.out.println("Ajustando nível da bateria para -0.5 (valor negativo):");
        r4.setNivelDaBateria(-0.5);
        System.out.println("Nível da bateria após tentativa de ajuste: " + r4.getNivelDaBateria());

        /* Testando métodos da classe RoboDeResgate (r5): */
        System.out.println("\nTestando métodos de RoboDeResgate (r5):");
        System.out.println("Nome: " + r5.getNome());
        System.out.println("Posição inicial: " + r5.exibirPosicao());
        System.out.println("Carga máxima: " + r5.getCargaMaxima());
        System.out.println("Sensor Térmico: " + r5.hasSensorTermico());
        System.out.println("Sensor Radar: " + r5.hasSensorRadar());

        // Testando o método erguerCarga com valores válidos e inválidos
        System.out.println("Tentando erguer carga de 70 kg (dentro do limite):");
        r5.erguerCarga(70);

        System.out.println("Tentando erguer carga de 120 kg (excede o limite):");
        r5.erguerCarga(120);

        System.out.println("Tentando erguer carga negativa (-10 kg):");
        r5.erguerCarga(-10);

        /* Testando métodos da classe RoboDroneDeCarga (r6): */
        System.out.println("\nTestando métodos de RoboDroneDeCarga (r6):");
        System.out.println("Nome: " + r6.getNome());
        System.out.println("Carga inicial: " + r6.getCarga());
        System.out.println("Carga máxima: " + r6.getCargaMaxima());

        // Testando setCarga com um valor dentro do limite
        System.out.println("\nAjustando carga para 3 (dentro do limite):");
        r6.setCarga(3);
        System.out.println("Carga atual: " + r6.getCarga());

        // Testando setCarga com um valor que excede o limite
        System.out.println("\nAjustando carga para 10 (acima do limite):");
        r6.setCarga(10);
        System.out.println("Carga atual (deve estar clipada na carga máxima): " + r6.getCarga());

        // Testando setCarga com um valor negativo
        System.out.println("\nAjustando carga para -5 (valor negativo):");
        r6.setCarga(-5);
        System.out.println("Carga atual (deve ser 0): " + r6.getCarga());

        // Testando o método descarregar
        System.out.println("\nColocando carga para 4:");
        r6.setCarga(4);
        System.out.println("Carga antes de descarregar: " + r6.getCarga());
        System.out.println("Descarregando...");
        r6.descarregar();
        System.out.println("Carga após descarregar: " + r6.getCarga());

        /* Testando métodos da classe RoboDroneDeVigilancia (r7): */
        System.out.println("\nTestando métodos de RoboDroneDeVigilancia (r7):");
        System.out.println("Nome: " + r7.getNome());
        System.out.println("Qualidade da câmera: " + r7.getQualidadeDaCamera() + " MP");
        System.out.println("Frames por segundo: " + r7.getFramesPorSegundo() + " fps");
        System.out.println("Duração atual do vídeo: " + r7.getDuracaoAtualVideo() + " segundos");
        System.out.println("Duração máxima do vídeo: " + r7.getDuracaoMaximaVideo() + " segundos");

        // Testando o método iniciarGravacao (deve iniciar a gravação pois a duração atual é menor que a máxima)
        System.out.println("\nIniciando gravação:");
        r7.iniciarGravacao();

        // Testando o método pararGravacao (deve parar e resetar a duração do vídeo)
        System.out.println("\nParando gravação:");
        r7.pararGravacao();

        // Exibir a posição final de cada robo no console
        System.out.println("\nPosição final de cada robô:");
        for (Robo robo : amb.getListaDeRobos()) {
            System.out.println(robo.getNome() + " está na posição " + robo.exibirPosicao());
        }
    }   
}