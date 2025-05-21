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
        rodarMenuInterativo(); 
        scanner.close();
        System.out.println("Simulação encerrada. Obrigado!");
    }

    private static void inicializarSimulacao() { // [cite: 200, 201]
        System.out.println("Inicializando Simulador de Robôs MC322 - Lab 04...");
        ambiente = new Ambiente(15, 15, 5); // Largura, Profundidade, Altura do Ambiente (ex: 0 a 4)
        centralComunicacao = new CentralComunicacao();

        try {
            // Adicionar Obstáculos
            ambiente.adicionarEntidade(new Obstaculo("Muro Leste", 14, 0, 14, 14, 0, TipoObstaculo.PAREDE)); // Parede ao longo da borda X=14
            ambiente.adicionarEntidade(new Obstaculo("Plataforma Central", 5, 5, 8, 8, 0, TipoObstaculo.PREDIO)); // Obstáculo base 5,5 a 8,8 no chao, altura do predio
            ambiente.adicionarEntidade(new Obstaculo("Rocha Alta", 2, 8, 0, TipoObstaculo.ROCHA)); // Rocha pontual alta
            ambiente.adicionarEntidade(new Obstaculo("Lagoa", 1,1, 3,3,0, TipoObstaculo.AGUA)); // Tipo AGUA pode não bloquear a passagem no mapa.


            // Adicionar Robôs
            RoboTerrestre terrestre1 = new RoboTerrestre("RT001", "T-800", 1, 1, "Norte", 3);
            terrestre1.adicionarSensor(new SensorTemperatura(2.0));
            ambiente.adicionarEntidade(terrestre1);

            RoboDroneDeVigilancia drone1 = new RoboDroneDeVigilancia("DV001", "SkyEye", 3, 3, 2, "Leste", 4, 12, 30, 300);
            drone1.adicionarSensor(new SensorUmidade(10.0));
            drone1.adicionarSensor(new SensorTemperatura(10.0));
            ambiente.adicionarEntidade(drone1);
            
            RoboAereo genericoAereo = new RoboAereo("RA001", "Aguia1", 0, 2, 1, "Sul", 3);
            genericoAereo.adicionarSensor(new SensorTemperatura(5.0));
            ambiente.adicionarEntidade(genericoAereo);

            RoboDroneDeCarga droneCarga1 = new RoboDroneDeCarga("DC001", "MulaVoadoora", 7,7,1,"Norte", 4, 5);
            ambiente.adicionarEntidade(droneCarga1);

            RoboDeResgate roboResgate1 = new RoboDeResgate("RR001", "Salvador", 10,1, "Oeste", 2, 50, true, true);
            ambiente.adicionarEntidade(roboResgate1);
            
            RoboCarroAutonomo carro1 = new RoboCarroAutonomo("CA001", "UberBot", 0,0,"Leste", 5, 4, 0.9);
            ambiente.adicionarEntidade(carro1);


        } catch (ColisaoException | ForaDosLimitesException e) {
            System.err.println("Erro crítico na inicialização do ambiente: " + e.getMessage());
            System.err.println("A simulação pode não funcionar como esperado.");
        }
         System.out.println("Simulação inicializada com sucesso.");
         ambiente.visualizarAmbiente();
    }

    private static void rodarMenuInterativo() { // [cite: 192, 193, 194, 195, 196, 197]
        boolean sair = false;
        while (!sair) {
            System.out.println("\n========= Menu Principal Simulador =========");
            System.out.println("1. Listar Robôs (com filtros)");
            System.out.println("2. Selecionar Robô para Interagir");
            System.out.println("3. Visualizar Mapa do Ambiente"); // [cite: 194]
            System.out.println("4. Visualizar Histórico de Comunicações"); // [cite: 197]
            System.out.println("5. Avançar tempo (simular gravação de drones, etc.)");
            System.out.println("6. Verificar Colisões Gerais no Ambiente");
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
                        ambiente.visualizarAmbiente();
                        break;
                    case 4:
                        centralComunicacao.exibirMensagens();
                        break;
                    case 5:
                        avancarTempoSimulacao();
                        break;
                    case 6:
                        ambiente.verificarColisoes();
                        break;
                    case 0:
                        sair = true;
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, insira um número.");
                scanner.nextLine(); // Limpar buffer do scanner
            } catch (Exception e) { 
                 System.err.println("Ocorreu um erro inesperado no menu principal: " + e.getMessage());
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

        System.out.println("Filtrar por: (1) Todos (2) Tipo de Robô (ex: RoboTerrestre) (3) Estado do Robô (Padrão: Todos)");
        System.out.print("Opção de filtro (deixe em branco para todos): ");
        String filtroTipo = "";
        EstadoRobo filtroEstado = null;
        String linhaFiltro = scanner.nextLine().trim();

        if (!linhaFiltro.isEmpty()) {
            try {
                int escolhaFiltro = Integer.parseInt(linhaFiltro);
                if (escolhaFiltro == 2) {
                    System.out.print("Digite o nome da classe do tipo de robô (ex: RoboTerrestre, RoboDroneDeVigilancia): ");
                    filtroTipo = scanner.nextLine().trim();
                } else if (escolhaFiltro == 3) {
                    System.out.println("Estados disponíveis:");
                    for (EstadoRobo es : EstadoRobo.values()) {
                        System.out.println("- " + es.name() + " (" + es.getDescricao() + ")");
                    }
                    System.out.print("Digite o nome do estado (ex: EM_ESPERA): ");
                    try {
                        filtroEstado = EstadoRobo.valueOf(scanner.nextLine().trim().toUpperCase());
                    } catch (IllegalArgumentException e) {
                        System.out.println("Estado inválido. Mostrando todos os robôs.");
                    }
                } else if (escolhaFiltro != 1) {
                     System.out.println("Opção de filtro inválida. Mostrando todos os robôs.");
                }
            } catch (NumberFormatException e) {
                 System.out.println("Opção de filtro inválida. Mostrando todos os robôs.");
            }
        }

        int contadorExibidos = 0;
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
                                   " | Posição: " + robo.exibirPosicao() +
                                   (robo instanceof RoboAereo ? " | Alt Max Voo: " + ((RoboAereo)robo).getAltitudeMaximaVoo() : "") +
                                   (robo instanceof RoboTerrestre ? " | Vel Max: " + ((RoboTerrestre)robo).getVelocidadeMaxima() : "")
                                   );
                 contadorExibidos++;
            }
        }
        if (contadorExibidos == 0) {
            System.out.println("Nenhum robô corresponde aos critérios de filtro.");
        }
        System.out.println("------------------------------------");
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
        System.out.print("Digite o ID ou Nome do robô para interagir (ou 'listar' para ver os robôs): ");
        String idRobo = scanner.nextLine().trim();
        if ("listar".equalsIgnoreCase(idRobo)) {
            listarRobos();
            System.out.print("Digite o ID ou Nome do robô para interagir: ");
            idRobo = scanner.nextLine().trim();
        }
        
        Robo roboSelecionado = encontrarRoboPorIdOuNome(idRobo);

        if (roboSelecionado == null) {
            System.out.println("Robô com ID/Nome '" + idRobo + "' não encontrado.");
            return;
        }
        menuInteracaoRobo(roboSelecionado);
    }

    private static void menuInteracaoRobo(Robo robo) { 
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n--- Interagindo com: " + robo.getNome() + " (ID: " + robo.getId() + ", Tipo: " + robo.getClass().getSimpleName() + ") ---");
            System.out.println("Estado Atual: " + robo.getEstado().getDescricao() + " | Posição: " + robo.exibirPosicao() + " | Direção: " + robo.getDirecao()); // [cite: 193]
            
            List<String> opcoesMenu = new ArrayList<>();
            opcoesMenu.add("Mover Robô"); // 1 
            opcoesMenu.add("Ligar Robô");   // 2 
            opcoesMenu.add("Desligar Robô"); // 3 
            opcoesMenu.add("Acionar Sensores do Robô"); // 4 
            opcoesMenu.add("Executar Tarefa Específica do Robô"); // 5
            
            if (robo instanceof Comunicavel) opcoesMenu.add("Enviar Mensagem"); 
            if (robo instanceof InterExplorador) opcoesMenu.add("Iniciar Exploração de Área");
            if (robo instanceof RoboDroneDeVigilancia) {
                opcoesMenu.add(((RoboDroneDeVigilancia) robo).isGravando() ? "Parar Gravação de Vídeo" : "Iniciar Gravação de Vídeo");
            }
            if (robo instanceof InterCarregador) {
                opcoesMenu.add("Carregar Item/Carga");
                opcoesMenu.add("Descarregar Item/Carga");
                opcoesMenu.add("Ver Itens/Carga");
            }
            if (robo instanceof InterDefensiva) {
                opcoesMenu.add("Ativar Defesa");
                opcoesMenu.add("Desativar Defesa");
            }
            // Adicionar mais opções para outras interfaces aqui

            System.out.println("Ações disponíveis:");
            for(int i=0; i < opcoesMenu.size(); i++) {
                System.out.println((i+1) + ". " + opcoesMenu.get(i));
            }
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma ação para " + robo.getNome() + ": ");

            try {
                int escolhaAcao = scanner.nextInt();
                scanner.nextLine(); // Consumir nova linha
                
                if (escolhaAcao == 0) {
                    voltar = true;
                    continue;
                }
                if (escolhaAcao < 1 || escolhaAcao > opcoesMenu.size()) {
                    System.out.println("Opção de ação inválida.");
                    continue;
                }

                String acaoSelecionada = opcoesMenu.get(escolhaAcao - 1);

                // Mapeamento direto de ações
                if (acaoSelecionada.equals("Mover Robô")) moverRoboInterativo(robo);
                else if (acaoSelecionada.equals("Ligar Robô")) {
                    try { robo.ligar(); } catch (AcaoNaoPermitidaException e) { System.err.println("Erro ao ligar: " + e.getMessage());}
                } else if (acaoSelecionada.equals("Desligar Robô")) robo.desligar();
              
                else if (acaoSelecionada.equals("Acionar Sensores do Robô")) {
                    try {
                        if (robo instanceof Sensoreavel) {
                            // Chama o método da interface Sensoreavel se implementado
                            System.out.println("--- Leituras via Interface Sensoreavel ---");
                            System.out.println(((Sensoreavel) robo).acionarSensores(ambiente));
                        } else {
                            // Se não implementa Sensoreavel, ou como um fallback,
                            // chama o método genérico da classe Robo (se houver sensores básicos)
                            System.out.println("--- Leituras via Método Genérico do Robô ---");
                            System.out.println(robo.ativarSensoresRobo(ambiente));
                        }
                    } catch (RoboDesligadoException e) {
                        System.err.println("Erro ao acionar sensores: " + e.getMessage());
                    }
                }
                } else if (acaoSelecionada.equals("Executar Tarefa Específica do Robô")) {
                    System.out.print("Argumentos para a tarefa (opcional, separados por espaço, ex: 'arg1 arg2'): ");
                    String[] argsTarefa = scanner.nextLine().split(" ");
                    if(argsTarefa.length == 1 && argsTarefa[0].isEmpty()) argsTarefa = null; // Sem argumentos se entrada vazia
                    try { 
                        System.out.println(robo.executarTarefa(ambiente, centralComunicacao, argsTarefa)); 
                    } catch (RoboDesligadoException | AcaoNaoPermitidaException | ForaDosLimitesException | ColisaoException e) { 
                        System.err.println("Erro na execução da tarefa: " + e.getMessage());
                    }
                } 
                // Ações de Interfaces
                else if (robo instanceof Comunicavel && acaoSelecionada.equals("Enviar Mensagem")) comunicarComRobo((Comunicavel) robo);
                else if (robo instanceof InterExplorador && acaoSelecionada.equals("Iniciar Exploração de Área")) {
                    try { System.out.println(((InterExplorador) robo).explorarArea(ambiente)); }
                    catch (RoboDesligadoException | AcaoNaoPermitidaException e) { System.err.println("Erro ao explorar: " + e.getMessage());}
                } else if (robo instanceof RoboDroneDeVigilancia) {
                    RoboDroneDeVigilancia drone = (RoboDroneDeVigilancia) robo;
                    if (acaoSelecionada.equals("Iniciar Gravação de Vídeo") && !drone.isGravando()) {
                        try { System.out.println(drone.iniciarGravacao());} 
                        catch (RoboDesligadoException | AcaoNaoPermitidaException e) { System.err.println("Erro na gravação: " + e.getMessage());}
                    } else if (acaoSelecionada.equals("Parar Gravação de Vídeo") && drone.isGravando()) {
                        try { System.out.println(drone.pararGravacao());}
                        catch (RoboDesligadoException e) { System.err.println("Erro na gravação: " + e.getMessage());}
                    }
                } else if (robo instanceof InterCarregador) {
                    InterCarregador carregador = (InterCarregador) robo;
                    if (acaoSelecionada.equals("Carregar Item/Carga")) {
                        System.out.print("Digite o nome/descrição do item a carregar: ");
                        String item = scanner.nextLine();
                        try { carregador.carregarItem(item); } 
                        catch (RoboDesligadoException | AcaoNaoPermitidaException e) { System.err.println("Erro ao carregar: " + e.getMessage());}
                    } else if (acaoSelecionada.equals("Descarregar Item/Carga")) {
                         try { carregador.descarregarItem(); } // Pode adicionar opção para item específico
                        catch (RoboDesligadoException | AcaoNaoPermitidaException e) { System.err.println("Erro ao descarregar: " + e.getMessage());}
                    } else if (acaoSelecionada.equals("Ver Itens/Carga")) {
                        try { System.out.println(carregador.verItensCarregados()); }
                        catch (RoboDesligadoException e) { System.err.println("Erro: " + e.getMessage());}
                    }
                }
                // Adicionar mais 'else if' para outras interfaces e suas ações aqui

            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, insira um número correspondente à opção.");
                scanner.nextLine(); // Limpar buffer
            } catch (Exception e) {
                 System.err.println("Ocorreu um erro inesperado na interação com o robô: " + e.getMessage());
                 e.printStackTrace();
            }
        }
    }

    private static void moverRoboInterativo(Robo robo) { // [cite: 196]
        System.out.println("Posição Atual: " + robo.exibirPosicao() + " | Direção Atual: " + robo.getDirecao());
        System.out.println("Comandos de Movimento:");
        System.out.println("  (F)rente, (T)rás, (E)squerda, (D)ireita");
        if (robo instanceof RoboAereo) {
            System.out.println("  (C)ima, (B)aixo");
        }
        System.out.println("  (XYZ) para coordenadas diretas (ex: '3 4 0')");
        System.out.println("  (M)udar direção (N, S, L, O)");
        System.out.print("Comando de movimento: ");
        String comandoStr = scanner.nextLine().toUpperCase();

        try {
            int novoX = robo.getX();
            int novoY = robo.getY();
            int novoZ = robo.getZ();
            int passo = 1; // Movimento de 1 unidade por padrão para F/T/E/D/C/B

            String[] partesComando = comandoStr.split(" ");
            String comandoPrincipal = partesComando[0];

            switch (comandoPrincipal) {
                case "F": // Frente
                    switch (robo.getDirecao().toUpperCase()) {
                        case "NORTE": novoY += passo; break;
                        case "SUL":   novoY -= passo; break;
                        case "LESTE": novoX += passo; break;
                        case "OESTE": novoX -= passo; break;
                        default: System.out.println("Direção desconhecida para mover para frente: " + robo.getDirecao()); return;
                    }
                    break;
                case "T": // Trás
                     switch (robo.getDirecao().toUpperCase()) {
                        case "NORTE": novoY -= passo; break;
                        case "SUL":   novoY += passo; break;
                        case "LESTE": novoX -= passo; break;
                        case "OESTE": novoX += passo; break;
                        default: System.out.println("Direção desconhecida para mover para trás: " + robo.getDirecao()); return;
                    }
                    break;
                case "E": // Esquerda
                    switch (robo.getDirecao().toUpperCase()) {
                        case "NORTE": novoX -= passo; break;
                        case "SUL":   novoX += passo; break;
                        case "LESTE": novoY += passo; break; // Virado para Leste, esquerda é Y+
                        case "OESTE": novoY -= passo; break; // Virado para Oeste, esquerda é Y-
                        default: System.out.println("Direção desconhecida para virar à esquerda: " + robo.getDirecao()); return;
                    }
                    break;
                case "D": // Direita
                    switch (robo.getDirecao().toUpperCase()) {
                        case "NORTE": novoX += passo; break;
                        case "SUL":   novoX -= passo; break;
                        case "LESTE": novoY -= passo; break; // Virado para Leste, direita é Y-
                        case "OESTE": novoY += passo; break; // Virado para Oeste, direita é Y+
                        default: System.out.println("Direção desconhecida para virar à direita: " + robo.getDirecao()); return;
                    }
                    break;
                case "C": // Cima
                    if (robo instanceof RoboAereo) ((RoboAereo) robo).subir(passo, ambiente);
                    else System.out.println(robo.getNome() + " não é um robô aéreo e não pode mover para cima/baixo explicitamente dessa forma.");
                    return; // Subir/descer já chamam o moverPara internamente.
                case "B": // Baixo
                    if (robo instanceof RoboAereo) ((RoboAereo) robo).descer(passo, ambiente);
                    else System.out.println(robo.getNome() + " não é um robô aéreo e não pode mover para cima/baixo explicitamente dessa forma.");
                    return; // Subir/descer já chamam o moverPara internamente.
                case "M": // Mudar direção
                    System.out.print("Nova direção (Norte, Sul, Leste, Oeste): ");
                    String novaDir = scanner.nextLine().trim();
                    // Validar novaDir aqui se necessário (ex: garantir que é uma das 4 válidas)
                    if (novaDir.equalsIgnoreCase("Norte") || novaDir.equalsIgnoreCase("Sul") || 
                        novaDir.equalsIgnoreCase("Leste") || novaDir.equalsIgnoreCase("Oeste")) {
                        robo.setDirecao(novaDir.substring(0,1).toUpperCase() + novaDir.substring(1).toLowerCase()); // Padroniza para ex: "Norte"
                        System.out.println(robo.getNome() + " agora está virado para " + robo.getDirecao());
                    } else {
                        System.out.println("Direção inválida. Escolha entre Norte, Sul, Leste, Oeste.");
                    }
                    return; // Apenas mudou direção, não moveu.
                default: // Coordenadas diretas X Y Z
                    try {
                        if (partesComando.length == 3) { // Espera X Y Z
                            novoX = Integer.parseInt(partesComando[0]);
                            novoY = Integer.parseInt(partesComando[1]);
                            novoZ = Integer.parseInt(partesComando[2]);
                        } else if (partesComando.length == 2 && !(robo instanceof RoboAereo)) { // X Y para terrestre (Z=0)
                            novoX = Integer.parseInt(partesComando[0]);
                            novoY = Integer.parseInt(partesComando[1]);
                            novoZ = 0; // Robôs terrestres ficam em Z=0
                        }
                         else {
                            System.out.println("Comando de coordenadas inválido. Use 'X Y Z' (ex: '3 4 0') ou 'X Y' para terrestres.");
                            return;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Coordenadas numéricas inválidas. Tente 'X Y Z' ou 'X Y'.");
                        return;
                    }
            }
            
            // Validação de altitude para RoboAereo antes de chamar moverPara (se não foi C ou B)
            // C e B já tratam isso em seus próprios métodos subir/descer.
            if (!comandoPrincipal.equals("C") && !comandoPrincipal.equals("B")) {
                 if (robo instanceof RoboAereo) {
                    RoboAereo ra = (RoboAereo) robo;
                    if (novoZ > ra.getAltitudeMaximaVoo()) {
                        System.out.println("Altitude " + novoZ + "m excede o máximo de voo do robô (" + ra.getAltitudeMaximaVoo() + "m). Movimento cancelado.");
                        return;
                    }
                    if (novoZ < 0) {
                        System.out.println("Altitude não pode ser negativa. Movimento cancelado.");
                        return;
                    }
                } else { // Se for terrestre
                    if (novoZ != 0) {
                        System.out.println("Robôs terrestres devem permanecer na altitude Z=0. Movimento para Z="+novoZ+" cancelado.");
                        return;
                    }
                }
                robo.moverPara(novoX, novoY, novoZ, ambiente);
                System.out.println(robo.getNome() + " movido para " + robo.exibirPosicao());
            }


        } catch (RoboDesligadoException | ColisaoException | ForaDosLimitesException | AcaoNaoPermitidaException e) {
            System.err.println("Erro ao mover " + robo.getNome() + ": " + e.getMessage());
        } catch (InputMismatchException e) { // Embora o scanner principal seja tratado, um scanner local poderia dar erro
            System.out.println("Entrada de movimento inválida.");
        } catch (Exception e) { // Captura geral para erros inesperados no movimento
            System.err.println("Erro inesperado durante o comando de movimento: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void comunicarComRobo(Comunicavel comunicador) { // 
        if (!(comunicador instanceof Robo)) {
            System.out.println("A entidade selecionada não é um robô com capacidade de comunicação ativa.");
            return;
        }
        Robo roboComunicador = (Robo) comunicador;

        System.out.println("\n--- Comunicando com " + roboComunicador.getNome() + " (ID Com: " + comunicador.getIdComunicacao() + ") ---");
        System.out.println("Robôs comunicáveis disponíveis no ambiente (exceto ele mesmo):");
        List<Comunicavel> comunicaveis = ambiente.getEntidades().stream()
                                          .filter(e -> e instanceof Comunicavel && e != comunicador)
                                          .map(e -> (Comunicavel) e)
                                          .collect(Collectors.toList());
        if (comunicaveis.isEmpty()) {
            System.out.println("Nenhum outro robô comunicável no ambiente para interagir.");
            return;
        }
        for (int i = 0; i < comunicaveis.size(); i++) {
             // Assume-se que Comunicavel é implementado por Robos, então podemos pegar o nome.
            Robo rDest = (Robo) comunicaveis.get(i); // Casting seguro se Comunicavel só é implementado por Robo
            System.out.println((i+1) + ". " + rDest.getNome() + " (ID Com: " + comunicaveis.get(i).getIdComunicacao() + ")");
        }
        System.out.print("Escolha o número do robô destinatário: ");
        try {
            int escolhaDest = scanner.nextInt();
            scanner.nextLine(); // Consumir nova linha

            if (escolhaDest < 1 || escolhaDest > comunicaveis.size()) {
                System.out.println("Seleção de destinatário inválida.");
                return;
            }
            Comunicavel destinatario = comunicaveis.get(escolhaDest - 1);
            
            System.out.print("Digite a mensagem para " + ((Robo)destinatario).getNome() + ": ");
            String mensagem = scanner.nextLine();

            comunicador.enviarMensagem(destinatario, mensagem, centralComunicacao);
            // A mensagem de sucesso/status já é impressa dentro do enviarMensagem/receberMensagem

        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Por favor, insira um número.");
            scanner.nextLine(); // Limpar buffer
        } catch (RoboDesligadoException | ErroComunicacaoException e) {
            System.err.println("Erro de comunicação: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado na comunicação: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void avancarTempoSimulacao() {
        System.out.print("Quantos segundos de simulação avançar (para gravação de drones, consumo de bateria, etc.)? ");
        try {
            int segundos = scanner.nextInt();
            scanner.nextLine(); // Consumir nova linha
            if (segundos <= 0) {
                System.out.println("O tempo a avançar deve ser um valor positivo.");
                return;
            }
            
            System.out.println("\nAvançando simulação em " + segundos + " segundos...");
            for (Entidade e : ambiente.getEntidades()) {
                if (e instanceof RoboDroneDeVigilancia) {
                    ((RoboDroneDeVigilancia) e).simularTempoGravacao(segundos);
                }
                // Adicionar aqui outras lógicas que dependem do tempo:
                // Ex: consumo de bateria passivo para robôs ligados, etc.
                // if (e instanceof RoboCarroAutonomo) {
                //    ((RoboCarroAutonomo)e).consumirBateriaPassivamente(segundos);
                // }
            }
            System.out.println(segundos + " segundos de simulação avançados.");

        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Por favor, insira um número de segundos.");
            scanner.nextLine(); // Limpar buffer
        } catch (Exception e) {
            System.err.println("Erro inesperado ao avançar o tempo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}