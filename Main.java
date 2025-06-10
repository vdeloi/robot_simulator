// Main.java
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

// Importando pacotes inteiros com o curinga *
import ambiente.*;
import comunicacao.*;
import missao.Missao;
import missao.MissaoExplorar;
import robo.*;
import sensores.*;
import util.Log; // Importar a classe de Log

/**
 * Classe principal que inicia e gerencia a simulação de robôs.
 * Contém o método `main` para executar o programa, inicializa o ambiente,
 * os robôs, os obstáculos e fornece um menu interativo para o usuário.
 */
public class Main {
    private static Ambiente ambiente;
    private static CentralComunicacao centralComunicacao;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            ambiente = new Ambiente(20, 15, 5);
            centralComunicacao = new CentralComunicacao();
            inicializarEntidades();
            menuInterativo();
        } catch (Exception e) {
            System.err.println("Erro fatal na inicialização ou execução: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    private static void inicializarEntidades() {
        System.out.println("Inicializando entidades...");
        Log.registrar("SIMULADOR INICIADO: Ambiente e entidades sendo criados.");
        try {
            RoboTerrestre rterrestre1 = new RoboTerrestre("T-800", 2, 2, "NORTE", 10);
            rterrestre1.adicionarSensor(new SensorProximidade("Prox-T800", 5.0));
            rterrestre1.ligar();

            RoboAereo rAereoBasico1 = new RoboAereo("DroneV1", 5, 5, 2, "LESTE", 4, 2);
            rAereoBasico1.adicionarSensor(new SensorAltitude("Alt-DroneV1", 0));
            rAereoBasico1.adicionarSensor(new SensorProximidade("Prox-DroneV1", 8.0));
            rAereoBasico1.ligar();

            // Wall-E agora é um AgenteInteligente
            RoboDroneDeCarga rDroneCarga1 = new RoboDroneDeCarga("Wall-E", 1, 1, 1, "NORTE", 3, 4, 0, 10);
            rDroneCarga1.adicionarSensor(new SensorAltitude("Alt-WallE", 0));
            rDroneCarga1.ligar();
            
            RoboComunicador roboTagarela = new RoboComunicador("Tagarela1", 3, 3, "SUL", 5);
            roboTagarela.ligar();

            ambiente.adicionarEntidade(rterrestre1);
            ambiente.adicionarEntidade(rAereoBasico1);
            ambiente.adicionarEntidade(rDroneCarga1);
            ambiente.adicionarEntidade(roboTagarela);

            Obstaculo parede1 = new Obstaculo(0, 7, 5, 7, TipoObstaculo.PAREDE, 0, 2);
            Obstaculo arvore1 = new Obstaculo(8, 8, 8, 8, TipoObstaculo.ARVORE, 0, 3);
            Obstaculo predio1 = new Obstaculo(10,0, 12,3, TipoObstaculo.PREDIO,0,4);

            ambiente.adicionarEntidade(parede1);
            ambiente.adicionarEntidade(arvore1);
            ambiente.adicionarEntidade(predio1);

            System.out.println("Entidades inicializadas.");
            Log.registrar("Entidades inicializadas com sucesso.");
            ambiente.visualizarAmbiente();

        } catch (ColisaoException | ForaDosLimitesException | IllegalArgumentException e) {
            System.err.println("Erro ao inicializar entidades: " + e.getMessage());
            Log.registrar("ERRO INICIALIZACAO: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void menuInterativo() {
        int opcao = -1;
        do {
            System.out.println("\n--- Menu Principal Simulador de Robôs ---");
            System.out.println("1. Listar robôs (por tipo ou estado)");
            System.out.println("2. Escolher robô para interagir");
            System.out.println("3. Visualizar status do ambiente e robôs");
            System.out.println("4. Visualizar mapa do ambiente");
            System.out.println("5. Listar mensagens trocadas");
            System.out.println("6. Acionar todos os sensores (teste global)");
            System.out.println("7. Gerenciar Missões"); // NOVA OPÇÃO
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            try {
                if (scanner.hasNextInt()) {
                    opcao = scanner.nextInt();
                } else {
                    System.out.println("Entrada inválida. Por favor, digite um número.");
                    scanner.next();
                    opcao = -1;
                    continue;
                }
                scanner.nextLine();

                switch (opcao) {
                    case 1: listarRobos(); break;
                    case 2: escolherRoboParaInteragir(); break;
                    case 3: visualizarStatusGeral(); break;
                    case 4: ambiente.visualizarAmbiente(); break;
                    case 5: centralComunicacao.exibirMensagens(); break;
                    case 6: ambiente.executarSensoresGlobais(); break;
                    case 7: gerenciarMissoes(); break; // NOVA CHAMADA
                    case 0: System.out.println("Saindo do simulador..."); Log.registrar("SIMULADOR FINALIZADO."); break;
                    default: System.out.println("Opção inválida.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
                if (scanner.hasNextLine()) scanner.nextLine();
                opcao = -1;
            } catch (Exception e) {
                 System.err.println("Ocorreu um erro no menu: " + e.getMessage());
                 e.printStackTrace();
                 opcao = -1;
            }
        } while (opcao != 0);
    }

    /**
     * Lista os robôs presentes no ambiente, permitindo filtrar por tipo ou estado.
     */
    private static void listarRobos() {
        System.out.println("\n--- Listar Robôs ---");
        System.out.println("Listar por: 1. Tipo | 2. Estado (Ligado/Desligado)");
        System.out.print("Sua escolha: ");
        int criterio = -1;
        // Lê o critério de listagem
        if (scanner.hasNextInt()){
            criterio = scanner.nextInt();
        } else {
            System.out.println("Entrada inválida para critério.");
            if(scanner.hasNext()) scanner.next(); // Limpa entrada inválida
            return; // Retorna ao menu anterior
        }
        scanner.nextLine(); // Consome a nova linha

        // Obtém todos os robôs do ambiente
        List<Robo> robos = ambiente.getEntidades().stream()
                                .filter(e -> e instanceof Robo) // Filtra apenas entidades que são Robôs
                                .map(e -> (Robo) e)           // Converte Entidade para Robo
                                .collect(Collectors.toList()); // Coleta em uma lista
        if (robos.isEmpty()) {
            System.out.println("Nenhum robô no ambiente.");
            return;
        }

        if (criterio == 1) { // Listar por tipo
            robos.stream()
                 .collect(Collectors.groupingBy(r -> r.getClass().getSimpleName())) // Agrupa robôs pelo nome da classe (tipo)
                 .forEach((tipo, lista) -> {
                     System.out.println("\nTipo: " + tipo);
                     lista.forEach(r -> System.out.println("  - " + r.getDescricao())); // Imprime descrição de cada robô do tipo
                 });
        } else if (criterio == 2) { // Listar por estado
            robos.stream()
                 .collect(Collectors.groupingBy(Robo::getEstado)) // Agrupa robôs pelo estado (LIGADO, DESLIGADO, etc.)
                 .forEach((estado, lista) -> {
                     System.out.println("\nEstado: " + estado);
                     lista.forEach(r -> System.out.println("  - " + r.getDescricao())); // Imprime descrição de cada robô no estado
                 });
        } else {
            System.out.println("Critério inválido.");
        }
    }

    /**
     * Exibe o status geral do ambiente, incluindo dimensões, número de entidades,
     * e uma lista dos robôs presentes. Também visualiza o mapa do ambiente.
     * @throws ForaDosLimitesException se `visualizarAmbiente` lançar (improvável aqui).
     */
    private static void visualizarStatusGeral() throws ForaDosLimitesException { // Adicionado throws para cobrir visualizarAmbiente
        System.out.println("\n--- Status do Ambiente ---");
        System.out.println("Dimensões: " + ambiente.getLargura() + "x" + ambiente.getProfundidade() + "x" + ambiente.getAltura());
        System.out.println("Total de Entidades: " + ambiente.getEntidades().size());
        // Conta o número de robôs e obstáculos
        long numRobos = ambiente.getEntidades().stream().filter(e -> e instanceof Robo).count();
        long numObstaculos = ambiente.getEntidades().stream().filter(e -> e instanceof Obstaculo).count();
        System.out.println("Robôs: " + numRobos + " | Obstáculos: " + numObstaculos);

        System.out.println("\n--- Status dos Robôs ---");
        if (numRobos == 0) {
            System.out.println("Nenhum robô no ambiente.");
        } else {
            // Imprime a descrição de cada robô
            ambiente.getEntidades().stream()
                .filter(e -> e instanceof Robo)
                .map(e -> (Robo) e)
                .forEach(r -> System.out.println(r.getDescricao()));
        }
        ambiente.visualizarAmbiente(); // Mostra o mapa atual do ambiente
    }

    /**
     * Permite ao usuário selecionar um robô da lista de robôs disponíveis no ambiente.
     * @return O objeto {@link Robo} selecionado, ou `null` se nenhum robô for selecionado ou se a seleção for inválida.
     */
    private static Robo selecionarRobo() {
        // Obtém a lista de robôs do ambiente
        List<Robo> robos = ambiente.getEntidades().stream()
                                .filter(e -> e instanceof Robo)
                                .map(e -> (Robo) e)
                                .collect(Collectors.toList());
        if (robos.isEmpty()) {
            System.out.println("Nenhum robô disponível.");
            return null;
        }
        System.out.println("\nSelecione o Robô:");
        // Lista os robôs disponíveis para seleção
        for (int i = 0; i < robos.size(); i++) {
            System.out.println((i + 1) + ". " + robos.get(i).getId() + " (" + robos.get(i).getClass().getSimpleName() + ")");
        }
        System.out.print("Número do robô: ");
        int escolha = -1;
        // Lê a escolha do usuário
        if (scanner.hasNextInt()){
            escolha = scanner.nextInt() - 1; // Ajusta para índice baseado em zero
        } else {
            System.out.println("Entrada inválida para seleção.");
            if(scanner.hasNext()) scanner.next(); // Limpa entrada inválida
            return null; // Retorna null se a entrada não for um número
        }
        scanner.nextLine(); // Consome a nova linha
        // Verifica se a escolha é válida
        if (escolha >= 0 && escolha < robos.size()) {
            return robos.get(escolha); // Retorna o robô selecionado
        }
        System.out.println("Seleção inválida.");
        return null; // Retorna null se a seleção for inválida
    }

    /**
     * Permite ao usuário escolher um robô e, em seguida, interagir com ele
     * através de um submenu de ações específicas para o robô selecionado.
     */
    private static void escolherRoboParaInteragir() { 
        Robo roboSelecionado = selecionarRobo(); // Pede ao usuário para selecionar um robô
        if (roboSelecionado == null) return; // Se nenhum robô foi selecionado, retorna

        System.out.println("Interagindo com: " + roboSelecionado.getDescricao());
        int subOpcao = -1;
        do {
            System.out.println("\n--- Ações para " + roboSelecionado.getId() + " (" + roboSelecionado.getEstado() + ") ---");
            System.out.println("1. Mover (Relativo: dx, dy, dz)");
            System.out.println("2. Ligar/Desligar Robô");
            System.out.println("3. Executar Tarefa Específica");
            // Opções condicionais baseadas nas capacidades do robô (interfaces implementadas)
            if (roboSelecionado instanceof Sensoreavel) System.out.println("4. Acionar Sensores");
            if (roboSelecionado instanceof Comunicavel) System.out.println("5. Enviar Mensagem");
            if (roboSelecionado instanceof Autonomo) System.out.println("6. Executar Ação Autônoma");
            System.out.println("0. Voltar ao menu principal");
            System.out.print("Escolha uma ação: ");

            try {
                // Lê a sub-opção do usuário
                if (scanner.hasNextInt()){
                    subOpcao = scanner.nextInt();
                } else {
                    System.out.println("Entrada inválida para ação.");
                    if(scanner.hasNext()) scanner.next(); // Limpa entrada inválida
                    subOpcao = -1; // Reseta a sub-opção
                    continue; // Volta ao início do loop de sub-menu
                }
                scanner.nextLine(); // Consome a nova linha

                // Executa a ação do robô com base na sub-opção
                switch (subOpcao) {
                    case 1: controlarMovimentacaoRelativa(roboSelecionado); break;
                    case 2: ligarDesligarRobo(roboSelecionado); break;
                    case 3: roboSelecionado.executarTarefa(ambiente); break; // Chamada polimórfica da tarefa específica do robô
                    case 4: 
                        if (roboSelecionado instanceof Sensoreavel) {
                            ((Sensoreavel)roboSelecionado).acionarSensores(ambiente);
                        } else {
                            System.out.println("Robô não possui sensores.");
                        }
                        break;
                    case 5: 
                        if (roboSelecionado instanceof Comunicavel) {
                            enviarMensagemComRobo((Comunicavel)roboSelecionado);
                        } else {
                            System.out.println("Robô não é comunicável.");
                        }
                        break;
                    case 6: 
                        if (roboSelecionado instanceof Autonomo) {
                            ((Autonomo)roboSelecionado).executarProximaAcaoAutonoma(ambiente);
                        } else {
                            System.out.println("Robô não é Autônomo");
                        }
                        break;
                    case 0: break; // Volta ao menu principal
                    default: System.out.println("Opção inválida.");
                }
            // Captura exceções que podem ser lançadas pelas ações do robô
            } catch (RoboDesligadoException | ColisaoException | ForaDosLimitesException | AcaoNaoPermitidaException | ErroComunicacaoException | RecursoInsuficienteException e) {
                System.err.println("Erro na ação do robô: " + e.getMessage());
            } catch (InputMismatchException e) { // Se o usuário não digitar um número para a ação
                System.out.println("Entrada inválida. Por favor, digite um número para a ação.");
                if (scanner.hasNextLine()) scanner.nextLine(); // Limpa entrada inválida
                subOpcao = -1; // Reseta a sub-opção
            } catch (Exception e) { // Captura qualquer outra exceção inesperada
                System.err.println("Erro inesperado na interação com robô: " + e.getMessage());
                e.printStackTrace();
                subOpcao = -1; // Reseta a sub-opção
            }
        } while (subOpcao != 0); // Continua no sub-menu até o usuário escolher voltar
    }

    /**
     * Controla a movimentação relativa de um robô.
     * Pede ao usuário os deslocamentos deltaX, deltaY e deltaZ.
     * @param robo O robô a ser movido.
     * @throws ColisaoException Se o movimento causar uma colisão.
     * @throws ForaDosLimitesException Se o movimento levar o robô para fora dos limites.
     * @throws RoboDesligadoException Se o robô estiver desligado.
     * @throws AcaoNaoPermitidaException Se o tipo de movimento não for permitido para o robô.
     */
    private static void controlarMovimentacaoRelativa(Robo robo) throws ColisaoException, ForaDosLimitesException, RoboDesligadoException, AcaoNaoPermitidaException {
        System.out.print("Digite o deslocamento em X (deltaX): "); int deltaX = scanner.nextInt();
        System.out.print("Digite o deslocamento em Y (deltaY): "); int deltaY = scanner.nextInt();
        System.out.print("Digite o deslocamento em Z (deltaZ): "); int deltaZ = scanner.nextInt();
        scanner.nextLine(); // Consome a nova linha

        // Chama o método de mover relativamente do robô
        robo.moverRelativamente(ambiente, deltaX, deltaY, deltaZ);
        ambiente.visualizarAmbiente(); // Mostra o ambiente após o movimento
    }

    /**
     * Liga ou desliga um robô, alternando seu estado atual.
     * @param robo O robô a ter seu estado alterado.
     */
    private static void ligarDesligarRobo(Robo robo) {
        if (robo.getEstado() == EstadoRobo.LIGADO) {
            robo.desligar(); // Se estiver ligado, desliga
        } else {
            robo.ligar(); // Se estiver desligado, liga
        }
        System.out.println("Novo estado de " + robo.getId() + ": " + robo.getEstado());
    }

    /**
     * Permite que um robô (remetente) envie uma mensagem para outro robô (destinatário).
     * @param remetente O robô comunicável que enviará a mensagem.
     * @throws RoboDesligadoException Se o remetente estiver desligado.
     * @throws ErroComunicacaoException Se houver um erro na comunicação (ex: destinatário inválido ou desligado).
     */
    private static void enviarMensagemComRobo(Comunicavel remetente) throws RoboDesligadoException, ErroComunicacaoException { 
        // Verifica se o remetente é de fato um Robô (já que Comunicavel é uma interface)
        if (!(remetente instanceof Robo)) {
             System.out.println("Remetente selecionado não é um robô.");
             return;
        }
        System.out.println("Selecione o destinatário para " + ((Robo)remetente).getId() + ":");
        // Lista todos os robôs que também são Comunicavel e não são o próprio remetente
        List<Robo> potenciaisDestinatarios = ambiente.getEntidades().stream()
                                .filter(e -> e instanceof Robo && e instanceof Comunicavel && e != remetente)
                                .map(e -> (Robo) e)
                                .collect(Collectors.toList());
        if (potenciaisDestinatarios.isEmpty()) {
            System.out.println("Nenhum outro robô comunicável disponível.");
            return;
        }
        // Mostra os destinatários disponíveis
        for (int i = 0; i < potenciaisDestinatarios.size(); i++) {
            System.out.println((i + 1) + ". " + potenciaisDestinatarios.get(i).getId());
        }
        System.out.print("Número do destinatário: ");
        int escolha = scanner.nextInt() - 1; // Ajusta para índice baseado em zero
        scanner.nextLine(); // Consome a nova linha

        if (escolha >= 0 && escolha < potenciaisDestinatarios.size()) {
            Comunicavel destinatario = (Comunicavel) potenciaisDestinatarios.get(escolha);
            System.out.print("Digite a mensagem: ");
            String msg = scanner.nextLine();
            // Envia a mensagem através da interface Comunicavel do remetente
            remetente.enviarMensagem(centralComunicacao, destinatario, msg);
        } else {
            System.out.println("Seleção de destinatário inválida.");
        }
    }

   private static void gerenciarMissoes() {
        System.out.println("\n--- Gerenciamento de Missões ---");
        System.out.println("1. Atribuir missão a um agente");
        System.out.println("2. Executar missão de um agente");
        System.out.println("0. Voltar");
        System.out.print("Escolha uma opção: ");
        int opcao = -1;
        try {
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1: atribuirMissao(); break;
                case 2: executarMissaoDeAgente(); break;
                case 0: break;
                default: System.out.println("Opção inválida.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida.");
            scanner.nextLine();
        }
    }

    /**
     * Permite ao usuário escolher um AgenteInteligente e atribuir uma missão a ele.
     */
    private static void atribuirMissao() {
        System.out.println("\nSelecione o agente para receber a missão:");
        List<AgenteInteligente> agentes = ambiente.getEntidades().stream()
                .filter(e -> e instanceof AgenteInteligente)
                .map(e -> (AgenteInteligente) e)
                .collect(Collectors.toList());

        if (agentes.isEmpty()) {
            System.out.println("Nenhum Agente Inteligente disponível.");
            return;
        }

        for (int i = 0; i < agentes.size(); i++) {
            System.out.println((i + 1) + ". " + agentes.get(i).getId());
        }
        System.out.print("Número do agente: ");
        int escolha = scanner.nextInt() - 1;
        scanner.nextLine();

        if (escolha >= 0 && escolha < agentes.size()) {
            AgenteInteligente agenteSelecionado = agentes.get(escolha);
            
            // Por enquanto, só temos a MissaoExplorar como opção
            System.out.println("Atribuindo MissaoExplorar para " + agenteSelecionado.getId());
            Missao missao = new MissaoExplorar();
            agenteSelecionado.definirMissao(missao);
            Log.registrar("Missão " + missao.getClass().getSimpleName() + " atribuída a " + agenteSelecionado.getId());
        } else {
            System.out.println("Seleção inválida.");
        }
    }

    /**
     *  Permite ao usuário escolher um AgenteInteligente para executar sua missão.
     */
    private static void executarMissaoDeAgente() {
        System.out.println("\nSelecione o agente para executar a missão:");
        List<AgenteInteligente> agentes = ambiente.getEntidades().stream()
                .filter(e -> e instanceof AgenteInteligente && ((AgenteInteligente) e).temMissao())
                .map(e -> (AgenteInteligente) e)
                .collect(Collectors.toList());

        if (agentes.isEmpty()) {
            System.out.println("Nenhum Agente Inteligente com missão atribuída.");
            return;
        }

        for (int i = 0; i < agentes.size(); i++) {
            System.out.println((i + 1) + ". " + agentes.get(i).getId());
        }
        System.out.print("Número do agente: ");
        int escolha = scanner.nextInt() - 1;
        scanner.nextLine();

        if (escolha >= 0 && escolha < agentes.size()) {
            AgenteInteligente agenteSelecionado = agentes.get(escolha);
            agenteSelecionado.executarMissao(ambiente);
        } else {
            System.out.println("Seleção inválida.");
        }
    } 
}