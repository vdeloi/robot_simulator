/* Main.java */
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


public class Main {
    private static Ambiente ambiente;
    private static CentralComunicacao centralComunicacao;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        inicializarSimulacao(); // [cite: 200, 201]
        rodarMenuInterativo(); // [cite: 201]
        scanner.close();
    }

    private static void inicializarSimulacao() {
        System.out.println("Inicializando Simulador de Robôs MC322 - Lab 04...");
        ambiente = new Ambiente(15, 15, 10); // Largura, Profundidade, Altura do Ambiente
        centralComunicacao = new CentralComunicacao();

        try {
            // Adicionar Obstáculos [cite: 201]
            ambiente.adicionarEntidade(new Obstaculo("ParedeNorte", 0, 0, 0, TipoObstaculo.PAREDE)); // Obstáculo pontual como exemplo
            ambiente.adicionarEntidade(new Obstaculo("ArvoreGrande", 5, 5, 0, TipoObstaculo.ARVORE));
            ambiente.adicionarEntidade(new Obstaculo("PredioPequeno", 8, 2, 0, TipoObstaculo.PREDIO));
            ambiente.adicionarEntidade(new Obstaculo("RochaMedia", 2, 8, 0, TipoObstaculo.ROCHA));


            // Adicionar Robôs [cite: 201]
            RoboTerrestre r terrestre1 = new RoboTerrestre("RT001", "T-800", 1, 1, "Norte", 5);
            terrestre1.adicionarSensor(new SensorTemperatura(2.0));
            ambiente.adicionarEntidade(terrestre1);

            RoboDroneDeVigilancia drone1 = new RoboDroneDeVigilancia("DV001", "OlhoNoCeu", 3, 3, 5, "Leste", 8, 12, 30, 300);
            drone1.adicionarSensor(new SensorUmidade(10.0));
            drone1.adicionarSensor(new SensorTemperatura(10.0));
            ambiente.adicionarEntidade(drone1);
            
            RoboAereo rAereoGenerico = new RoboAereo("RA001", "Aguia1", 0, 2, 3, "Sul", 7);
            rAereoGenerico.adicionarSensor(new SensorTemperatura(5.0));
            ambiente.adicionarEntidade(rAereoGenerico);


        } catch (ColisaoException | ForaDosLimitesException e) {
            System.err.println("Erro na inicialização: " + e.getMessage());
        }
         System.out.println("Simulação inicializada.");
         ambiente.visualizarAmbiente();
    }

    private static void rodarMenuInterativo() { // [cite: 192, 193, 194, 195, 196, 197]
        boolean sair = false;
        while (!sair) {
            System.out.println("\n========= Menu Principal =========");
            System.out.println("1. Listar Robôs");
            System.out.println("2. Selecionar Robô para Interagir");
            System.out.println("3. Visualizar Mapa do Ambiente");
            System.out.println("4. Visualizar Histórico de Comunicações");
            System.out.println("5. Avançar tempo (simular gravação de drones)");
            System.out.println("6. Verificar Colisões Geral");
            System.out.println("0. Sair da Simulação");
            System.out.print("Escolha uma opção: ");

            try {
                int escolha = scanner.nextInt();
                scanner.nextLine(); // Consumir nova linha

                switch (escolha) {
                    case 1:
                        listarRobos();
                        break;
                    case 2:
                        selecionarRoboParaInteragir();
                        break;
                    case 3:
                        ambiente.visualizarAmbiente(); // [cite: 194]
                        break;
                    case 4:
                        centralComunicacao.exibirMensagens(); // [cite: 197]
                        break;
                    case 5:
                        avancarTempoSimulacao();
                        break;
                    case 6:
                        ambiente.verificarColisoes();
                        break;
                    case 0:
                        sair = true;
                        System.out.println("Encerrando simulação...");
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, insira um número.");
                scanner.nextLine(); // Limpar buffer
            } catch (Exception e) { // Captura geral para exceções inesperadas no menu
                 System.err.println("Ocorreu um erro inesperado no menu: " + e.getMessage());
                 e.printStackTrace(); // Para debug
            }
        }
    }

    private static void listarRobos() { // [cite: 192]
        System.out.println("\n--- Lista de Robôs no Ambiente ---");
        List<Robo> robos = ambiente.getEntidades().stream()
                                .filter(e -> e instanceof Robo)
                                .map(e -> (Robo) e)
                                .collect(Collectors.toList());

        if (robos.isEmpty()) {
            System.out.println("Nenhum robô no ambiente.");
            return;
        }

        System.out.println("Filtrar por: (1) Todos (2) Tipo (3) Estado (Padrão: Todos)");
        String filtroTipo = "";
        EstadoRobo filtroEstado = null;
        try {
            String linha = scanner.nextLine().trim();
            if (!linha.isEmpty()) {
                int escolhaFiltro = Integer.parseInt(linha);
                if (escolhaFiltro == 2) {
                    System.out.print("Digite o tipo (ex: RoboTerrestre, RoboDroneDeVigilancia): ");
                    filtroTipo = scanner.nextLine().trim();
                } else if (escolhaFiltro == 3) {
                    System.out.println("Estados disponíveis:");
                    for (EstadoRobo es : EstadoRobo.values()) {
                        System.out.println("- " + es.name());
                    }
                    System.out.print("Digite o estado: ");
                    try {
                        filtroEstado = EstadoRobo.valueOf(scanner.nextLine().trim().toUpperCase());
                    } catch (IllegalArgumentException e) {
                        System.out.println("Estado inválido. Mostrando todos.");
                    }
                }
            }
        } catch (NumberFormatException e) {
             System.out.println("Opção de filtro inválida. Mostrando todos.");
        }


        for (int i = 0; i < robos.size(); i++) {
            Robo robo = robos.get(i);
            boolean exibir = true;
            if (!filtroTipo.isEmpty() && !robo.getClass().getSimpleName().equalsIgnoreCase(filtroTipo)) {
                exibir = false;
            }
            if (filtroEstado != null && robo.getEstado() != filtroEstado) {
                exibir = false;
            }

            if(exibir) {
                 System.out.println((i + 1) + ". ID: " + robo.getId() + " | Nome: " + robo.getNome() +
                                   " | Tipo: " + robo.getClass().getSimpleName() +
                                   " | Estado: " + robo.getEstado().getDescricao() +
                                   " | Posição: " + robo.exibirPosicao());
            }
        }
        System.out.println("---------------------------------");
    }

    private static Robo encontrarRoboPorIdOuNome(String identificador) {
         return ambiente.getEntidades().stream()
                .filter(e -> e instanceof Robo)
                .map(e -> (Robo) e)
                .filter(r -> r.getId().equalsIgnoreCase(identificador) || r.getNome().equalsIgnoreCase(identificador))
                .findFirst()
                .orElse(null);
    }

    private static void selecionarRoboParaInteragir() { // [cite: 193]
        System.out.print("Digite o ID ou Nome do robô para interagir: ");
        String idRobo = scanner.nextLine();
        Robo roboSelecionado = encontrarRoboPorIdOuNome(idRobo);

        if (roboSelecionado == null) {
            System.out.println("Robô com ID/Nome '" + idRobo + "' não encontrado.");
            return;
        }

        menuInteracaoRobo(roboSelecionado);
    }

    private static void menuInteracaoRobo(Robo robo) { // [cite: 195, 196, 197]
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n--- Interagindo com: " + robo.getNome() + " (ID: " + robo.getId() + ") ---");
            System.out.println("Estado Atual: " + robo.getEstado().getDescricao() + " | Posição: " + robo.exibirPosicao()); // [cite: 193]
            System.out.println("1. Mover Robô");
            System.out.println("2. Ligar Robô");
            System.out.println("3. Desligar Robô");
            System.out.println("4. Acionar Sensores"); // Lab 03 e 04 [cite: 124, 195]
            System.out.println("5. Executar Tarefa Específica");
            
            int optCount = 5;
            if (robo instanceof Comunicavel) {
                optCount++; System.out.println(optCount + ". Enviar Mensagem"); // [cite: 195]
            }
            if (robo instanceof Explorador) {
                 optCount++; System.out.println(optCount + ". Iniciar Exploração");
            }
             if (robo instanceof RoboDroneDeVigilancia) {
                RoboDroneDeVigilancia drone = (RoboDroneDeVigilancia) robo;
                optCount++; System.out.println(optCount + (drone.isGravando() ? ". Parar Gravação" : ". Iniciar Gravação"));
            }
            // Adicionar mais opções para outras interfaces (Carregador, Defensivel) aqui

            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma ação para " + robo.getNome() + ": ");

            try {
                int escolhaAcao = scanner.nextInt();
                scanner.nextLine(); // Consumir nova linha
                
                int currentOpt = 0;

                switch (escolhaAcao) {
                    case 1: // Mover [cite: 196]
                        moverRoboInterativo(robo);
                        break;
                    case 2: // Ligar [cite: 197]
                        try { robo.ligar(); } catch (AcaoNaoPermitidaException e) { System.err.println("Erro ao ligar: " + e.getMessage());}
                        break;
                    case 3: // Desligar [cite: 197]
                        robo.desligar();
                        break;
                    case 4: // Sensores
                        try { System.out.println(robo.ativarSensoresRobo(ambiente)); }
                        catch (RoboDesligadoException e) { System.err.println("Erro: " + e.getMessage()); }
                        break;
                    case 5: // Tarefa
                        System.out.print("Argumentos para tarefa (opcional, separado por espaço): ");
                        String[] args = scanner.nextLine().split(" ");
                        if(args.length == 1 && args[0].isEmpty()) args = null;
                        try { System.out.println(robo.executarTarefa(ambiente, centralComunicacao, args)); }
                        catch (RoboDesligadoException | AcaoNaoPermitidaException | ForaDosLimitesException | ColisaoException e) { System.err.println("Erro na tarefa: " + e.getMessage());}
                        break;
                    // Cases para funcionalidades de interface (6, 7, 8...)
                    // A ordem aqui deve ser dinâmica baseada nas interfaces que o robo implementa
                    // Este é um ponto complexo para um menu simples e pode exigir refatoração
                    // Por simplicidade, vamos tratar os casos conhecidos
                    case 0:
                        voltar = true;
                        break;
                    default:
                        // Tratamento para opções dinâmicas
                        currentOpt = 5; // Reinicia contador após opções fixas
                        boolean acaoRealizada = false;
                        if (robo instanceof Comunicavel) {
                            currentOpt++;
                            if (escolhaAcao == currentOpt) {
                                comunicarComRobo((Comunicavel) robo);
                                acaoRealizada = true;
                            }
                        }
                        if (robo instanceof Explorador && !acaoRealizada) {
                             currentOpt++;
                             if (escolhaAcao == currentOpt) {
                                try { ((Explorador) robo).explorarArea(ambiente); }
                                catch (RoboDesligadoException | AcaoNaoPermitidaException e) { System.err.println("Erro ao explorar: " + e.getMessage());}
                                acaoRealizada = true;
                             }
                        }
                        if (robo instanceof RoboDroneDeVigilancia && !acaoRealizada) {
                            currentOpt++;
                            if (escolhaAcao == currentOpt) {
                                RoboDroneDeVigilancia drone = (RoboDroneDeVigilancia) robo;
                                try {
                                    if (drone.isGravando()) System.out.println(drone.pararGravacao());
                                    else System.out.println(drone.iniciarGravacao());
                                } catch (RoboDesligadoException | AcaoNaoPermitidaException e) { System.err.println("Erro na gravação: " + e.getMessage());}
                                acaoRealizada = true;
                            }
                        }

                        if (!acaoRealizada && escolhaAcao != 0) {
                             System.out.println("Opção de ação inválida.");
                        }
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, insira um número.");
                scanner.nextLine(); // Limpar buffer
            }
        }
    }

    private static void moverRoboInterativo(Robo robo) { // [cite: 124, 196]
        System.out.println("Posição Atual: " + robo.exibirPosicao() + " | Direção Atual: " + robo.getDirecao());
        System.out.println("Mover para: (F)rente, (T)rás, (E)squerda, (D)ireita, (C)ima, (B)aixo, ou (XY)Z para coordenadas diretas.");
        System.out.print("Comando de movimento: ");
        String comando = scanner.nextLine().toUpperCase();

        int novoX = robo.getX();
        int novoY = robo.getY();
        int novoZ = robo.getZ();
        int passo = 1; // Movimento de 1 unidade por padrão

        try {
            switch (comando) {
                case "F": // Frente (Depende da direção atual)
                    // Esta lógica de direção precisa ser implementada ou simplificada
                    // Por simplicidade, vamos assumir Norte=Y+, Sul=Y-, Leste=X+, Oeste=X-
                    // E para robôs aéreos, cima/baixo afeta Z.
                    // Para um simulador mais robusto, a direção seria em graus e o cálculo seria trigonométrico.
                    // Ou, cada robô implementaria como interpreta "frente" baseado em sua `direcao`.
                    // Vamos usar uma interpretação simples:
                    // Se direção for Norte: novoY++ , Sul: novoY--, Leste: novoX++, Oeste: novoX-- (no plano XY)
                    // Para este exemplo, moveremos incrementalmente no eixo X ou Y.
                    // O lab não especifica como "frente/trás" se traduz sem uma orientação direcional complexa.
                    // Vamos assumir um movimento direto para simplificar o menu.
                    System.out.println("Movimento incremental não implementado detalhadamente. Use coordenadas XYZ.");
                    System.out.print("Mover em X (delta): "); int dx = scanner.nextInt(); scanner.nextLine();
                    System.out.print("Mover em Y (delta): "); int dy = scanner.nextInt(); scanner.nextLine();
                    System.out.print("Mover em Z (delta): "); int dz = scanner.nextInt(); scanner.nextLine();
                    novoX += dx; novoY += dy; novoZ += dz;
                    break;
                case "C": // Cima (Apenas para robôs aéreos ou com capacidade Z)
                    if (robo instanceof RoboAereo) novoZ += passo;
                    else System.out.println(robo.getNome() + " não pode mover verticalmente dessa forma."); return;
                    break;
                case "B": // Baixo
                    if (robo instanceof RoboAereo) novoZ -= passo;
                    else System.out.println(robo.getNome() + " não pode mover verticalmente dessa forma."); return;
                    break;
                // Outros movimentos direcionais (T, E, D) precisariam de lógica similar a 'F'
                default: // Coordenadas diretas X Y Z
                    try {
                        System.out.print("Nova coordenada X: "); novoX = scanner.nextInt();
                        System.out.print("Nova coordenada Y: "); novoY = scanner.nextInt();
                        System.out.print("Nova coordenada Z: "); novoZ = scanner.nextInt();
                        scanner.nextLine(); // Consumir
                    } catch (InputMismatchException e) {
                        System.out.println("Coordenadas inválidas.");
                        scanner.nextLine(); // Limpar
                        return;
                    }
            }
            
            // Validação de altitude para RoboAereo antes de chamar moverPara
            if (robo instanceof RoboAereo) {
                RoboAereo ra = (RoboAereo) robo;
                if (novoZ > ra.getAltitudeMaximaVoo()) {
                    System.out.println("Altitude excede o máximo de voo do robô (" + ra.getAltitudeMaximaVoo() + "). Movimento cancelado.");
                    return;
                }
            }


            robo.moverPara(novoX, novoY, novoZ, ambiente);
            System.out.println(robo.getNome() + " movido para " + robo.exibirPosicao());

        } catch (RoboDesligadoException | ColisaoException | ForaDosLimitesException | AcaoNaoPermitidaException e) {
            System.err.println("Erro ao mover: " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Entrada de movimento inválida.");
            scanner.nextLine(); // Limpar buffer
        }
    }
    
    private static void comunicarComRobo(Comunicavel comunicador) { // [cite: 195]
        if (!(comunicador instanceof Robo)) {
            System.out.println("A entidade selecionada não é um robô comunicável.");
            return;
        }
        Robo roboComunicador = (Robo) comunicador;

        System.out.println("\n--- Comunicando com " + roboComunicador.getNome() + " ---");
        System.out.println("Robôs comunicáveis disponíveis:");
        List<Comunicavel> comunicaveis = ambiente.getEntidades().stream()
                                          .filter(e -> e instanceof Comunicavel && e != comunicador)
                                          .map(e -> (Comunicavel) e)
                                          .collect(Collectors.toList());
        if (comunicaveis.isEmpty()) {
            System.out.println("Nenhum outro robô comunicável no ambiente.");
            return;
        }
        for (int i = 0; i < comunicaveis.size(); i++) {
             if (comunicaveis.get(i) instanceof Robo) { // Garante que é um Robo para pegar o nome
                Robo r = (Robo) comunicaveis.get(i);
                System.out.println((i+1) + ". " + r.getNome() + " (ID Com: " + comunicaveis.get(i).getIdComunicacao() + ")");
            }
        }
        System.out.print("Escolha o número do robô destinatário: ");
        try {
            int escolhaDest = scanner.nextInt();
            scanner.nextLine(); // Consumir

            if (escolhaDest < 1 || escolhaDest > comunicaveis.size()) {
                System.out.println("Seleção inválida.");
                return;
            }
            Comunicavel destinatario = comunicaveis.get(escolhaDest - 1);
            
            System.out.print("Digite a mensagem: ");
            String mensagem = scanner.nextLine();

            comunicador.enviarMensagem(destinatario, mensagem, centralComunicacao);
            System.out.println("Mensagem enviada.");

        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida.");
            scanner.nextLine();
        } catch (RoboDesligadoException | ErroComunicacaoException e) {
            System.err.println("Erro de comunicação: " + e.getMessage());
        }
    }
    
    private static void avancarTempoSimulacao() {
        System.out.print("Quantos segundos de simulação avançar (para gravação de drones, etc.)? ");
        try {
            int segundos = scanner.nextInt();
            scanner.nextLine(); // Consumir
            if (segundos <= 0) {
                System.out.println("Tempo deve ser positivo.");
                return;
            }
            
            for (Entidade e : ambiente.getEntidades()) {
                if (e instanceof RoboDroneDeVigilancia) {
                    ((RoboDroneDeVigilancia) e).simularTempoGravacao(segundos);
                }
                // Adicionar aqui outras lógicas que dependem do tempo
            }
            System.out.println(segundos + " segundos de simulação avançados.");

        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida.");
            scanner.nextLine();
        }
    }
}