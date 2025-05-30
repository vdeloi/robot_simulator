// Main.java
//import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Classe principal que inicia e gerencia a simulação de robôs.
 * Contém o método `main` para executar o programa, inicializa o ambiente,
 * os robôs, os obstáculos e fornece um menu interativo para o usuário.
 */
public class Main {
    private static Ambiente ambiente; // O ambiente de simulação compartilhado
    private static CentralComunicacao centralComunicacao; // A central para registrar mensagens
    private static Scanner scanner = new Scanner(System.in); // Scanner para entrada do usuário

    /**
     * Ponto de entrada do programa.
     * Inicializa o ambiente, as entidades e o menu interativo.
     * @param args Argumentos da linha de comando (não utilizados).
     */
    public static void main(String[] args) {
        try {
            // Cria um novo ambiente com dimensões 20 (largura), 15 (profundidade), 5 (altura)
            ambiente = new Ambiente(20, 15, 5);
            centralComunicacao = new CentralComunicacao(); // Inicializa a central de comunicação
            inicializarEntidades(); // Adiciona robôs e obstáculos ao ambiente
            menuInterativo(); // Inicia o loop do menu para interação com o usuário
        } catch (Exception e) {
            // Captura qualquer erro fatal durante a inicialização ou execução
            System.err.println("Erro fatal na inicialização ou execução: " + e.getMessage());
            e.printStackTrace(); // Imprime o stack trace para depuração
        } finally {
            // Garante que o scanner seja fechado ao final da execução
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    /**
     * Inicializa as entidades (robôs e obstáculos) no ambiente.
     * Cria instâncias de diferentes tipos de robôs e obstáculos e os adiciona ao ambiente.
     */
    private static void inicializarEntidades() {
        System.out.println("Inicializando entidades...");
        try {
            // Criação e configuração do RoboTerrestre T-800
            RoboTerrestre rterrestre1 = new RoboTerrestre("T-800", 2, 2, "NORTE", 10);
            rterrestre1.adicionarSensor(new SensorProximidade("Prox-T800", 5.0));
            rterrestre1.ligar();

            // Criação e configuração do RoboAereo DroneV1
            RoboAereo rAereoBasico1 = new RoboAereo("DroneV1", 5, 5, 2, "LESTE", 4, 2);
            rAereoBasico1.adicionarSensor(new SensorAltitude("Alt-DroneV1", 0));
            rAereoBasico1.adicionarSensor(new SensorProximidade("Prox-DroneV1", 8.0));
            rAereoBasico1.ligar();

            // Criação e configuração do RoboDroneDeCarga Wall-E
            RoboDroneDeCarga rDroneCarga1 = new RoboDroneDeCarga("Wall-E", 1, 1, 1, "NORTE", 3, 4, 0, 10);
            rDroneCarga1.adicionarSensor(new SensorAltitude("Alt-WallE", 0));
            rDroneCarga1.ligar();
            
            // Criação e configuração do RoboComunicador Tagarela1
            RoboComunicador roboTagarela = new RoboComunicador("Tagarela1", 3, 3, "SUL", 5);
            roboTagarela.ligar();

            // Adicionando os robôs ao ambiente
            ambiente.adicionarEntidade(rterrestre1);
            ambiente.adicionarEntidade(rAereoBasico1);
            ambiente.adicionarEntidade(rDroneCarga1);
            ambiente.adicionarEntidade(roboTagarela);

            // Criação de obstáculos
            Obstaculo parede1 = new Obstaculo(0, 7, 5, 7, TipoObstaculo.PAREDE, 0, 2); // Uma parede no nível Z=0 a Z=1
            Obstaculo arvore1 = new Obstaculo(8, 8, 8, 8, TipoObstaculo.ARVORE, 0, 3); // Uma árvore do nível Z=0 a Z=2
            Obstaculo predio1 = new Obstaculo(10,0, 12,3, TipoObstaculo.PREDIO,0,4);  // Um prédio do nível Z=0 a Z=3

            // Adicionando os obstáculos ao ambiente
            ambiente.adicionarEntidade(parede1);
            ambiente.adicionarEntidade(arvore1);
            ambiente.adicionarEntidade(predio1);

            System.out.println("Entidades inicializadas.");
            ambiente.visualizarAmbiente(); // Mostra o estado inicial do ambiente

        } catch (ColisaoException | ForaDosLimitesException | IllegalArgumentException e) { // Captura exceções específicas da inicialização
            System.err.println("Erro ao inicializar entidades: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Apresenta o menu principal interativo para o usuário controlar a simulação.
     * Permite listar robôs, interagir com eles, visualizar o ambiente, etc.
     */
    private static void menuInterativo() {
        int opcao = -1; // Variável para armazenar a escolha do usuário
        do {
            System.out.println("\n--- Menu Principal Simulador de Robôs ---");
            System.out.println("1. Listar robôs (por tipo ou estado)");
            System.out.println("2. Escolher robô para interagir");
            System.out.println("3. Visualizar status do ambiente e robôs");
            System.out.println("4. Visualizar mapa do ambiente");
            System.out.println("5. Listar mensagens trocadas");
            System.out.println("6. Acionar todos os sensores (teste global)");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");


            try {
                // Lê a entrada do usuário
                if (scanner.hasNextInt()) {
                    opcao = scanner.nextInt();
                } else {
                    System.out.println("Entrada inválida. Por favor, digite um número.");
                    scanner.next(); // Limpa a entrada inválida
                    opcao = -1; // Reseta a opção para continuar no loop
                    continue; // Volta ao início do loop
                }
                scanner.nextLine(); // Consome a nova linha restante após nextInt()

                // Executa a ação com base na opção escolhida
                switch (opcao) {
                    case 1: listarRobos(); break;
                    case 2: escolherRoboParaInteragir(); break;
                    case 3: visualizarStatusGeral(); break;
                    case 4: ambiente.visualizarAmbiente(); break;
                    case 5: centralComunicacao.exibirMensagens(); break;
                    case 6: ambiente.executarSensoresGlobais(); break;
                    case 0: System.out.println("Saindo do simulador..."); break;
                    default: System.out.println("Opção inválida.");
                }
            } catch (InputMismatchException e) { // Se o usuário não digitar um número
                System.out.println("Entrada inválida. Por favor, digite um número.");
                if (scanner.hasNextLine()) scanner.nextLine(); // Limpa a entrada inválida
                opcao = -1; // Reseta a opção
            } catch (Exception e) { // Captura qualquer outra exceção que possa ocorrer
                 System.err.println("Ocorreu um erro no menu: " + e.getMessage());
                 e.printStackTrace();
                 opcao = -1; // Reseta a opção para evitar loop infinito em caso de erro
            }
        } while (opcao != 0); // Continua o loop até o usuário escolher sair (opção 0)
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

    // --- Implementações de Exemplo de Robôs (Classes Internas Estáticas) ---

    /**
     * Representa um robô aéreo genérico.
     * Pode se mover no espaço 3D até uma altitude máxima.
     * Implementa {@link Sensoreavel} para interagir com sensores.
     */
    public static class RoboAereo extends Robo implements Sensoreavel {
        protected final int altitudeMaxima; // Altitude máxima que o robô aéreo pode atingir
        protected int numHelices;           // Número de hélices do robô aéreo

        /**
         * Construtor para RoboAereo.
         * @param id Identificador do robô.
         * @param x Posição inicial X.
         * @param y Posição inicial Y.
         * @param z Posição inicial Z (altitude).
         * @param direcao Direção inicial.
         * @param altitudeMaxima Altitude máxima de voo.
         * @param numHelices Número de hélices.
         */
        public RoboAereo(String id, int x, int y, int z, String direcao, int altitudeMaxima, int numHelices) {
            super(id, x, y, z, direcao);
            this.altitudeMaxima = Math.max(0, altitudeMaxima); // Garante que a altitude máxima não seja negativa
            this.numHelices = numHelices;
            // Ajusta a altitude inicial se estiver fora dos limites permitidos
            if (z > this.altitudeMaxima) {
                 System.out.println("Aviso: Alt inicial ("+z+") do RoboAereo "+id+" excede max ("+this.altitudeMaxima+"). Ajustando.");
                 this.z = this.altitudeMaxima; // Define para altitude máxima se exceder
            }
            if (z < 0) {
                 System.out.println("Aviso: Alt inicial ("+z+") do RoboAereo "+id+" negativa. Ajustando para 0.");
                 this.z = 0; // Define para 0 se for negativa
            }
        }
        
        /**
         * Faz o robô aéreo subir uma determinada quantidade de metros.
         * @param metros Quantidade de metros para subir (deve ser positivo).
         * @throws RoboDesligadoException Se o robô estiver desligado.
         * @throws AcaoNaoPermitidaException Se `metros` não for positivo ou se exceder a altitude máxima.
         * @throws ColisaoException Se houver colisão ao subir.
         * @throws ForaDosLimitesException Se o movimento levar para fora dos limites do ambiente.
         */
        public void subir(int metros) throws RoboDesligadoException, AcaoNaoPermitidaException, ColisaoException, ForaDosLimitesException {
            if (getEstado() == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(getId() + " desligado.");
            if (metros <=0) throw new AcaoNaoPermitidaException("Metros para subir deve ser positivo.");
            // Chama moverRelativamente da superclasse (Robo) para efetuar o movimento vertical
            super.moverRelativamente(ambiente, 0,0, metros); 
        }

        /**
         * Faz o robô aéreo descer uma determinada quantidade de metros.
         * @param metros Quantidade de metros para descer (deve ser positivo).
         * @throws RoboDesligadoException Se o robô estiver desligado.
         * @throws AcaoNaoPermitidaException Se `metros` não for positivo ou se tentar descer abaixo de 0.
         * @throws ColisaoException Se houver colisão ao descer.
         * @throws ForaDosLimitesException Se o movimento levar para fora dos limites do ambiente.
         */
        public void descer(int metros) throws RoboDesligadoException, AcaoNaoPermitidaException, ColisaoException, ForaDosLimitesException {
            if (getEstado() == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(getId() + " desligado.");
            if (metros <=0) throw new AcaoNaoPermitidaException("Metros para descer deve ser positivo.");
            // Chama moverRelativamente da superclasse (Robo) para efetuar o movimento vertical negativo
            super.moverRelativamente(ambiente, 0,0, -metros);
        }

        /**
         * Sobrescreve o método para mover o robô aéreo relativamente.
         * Adiciona verificações específicas para altitude máxima e mínima (0).
         * @param ambiente O ambiente de simulação.
         * @param dx Deslocamento em X.
         * @param dy Deslocamento em Y.
         * @param dz Deslocamento em Z.
         * @throws ColisaoException Se o movimento causar colisão.
         * @throws ForaDosLimitesException Se o movimento for para fora dos limites do ambiente.
         * @throws RoboDesligadoException Se o robô estiver desligado.
         * @throws AcaoNaoPermitidaException Se o movimento violar restrições de altitude.
         */
        @Override
        public void moverRelativamente(Ambiente ambiente, int dx, int dy, int dz) throws ColisaoException, ForaDosLimitesException, RoboDesligadoException, AcaoNaoPermitidaException {
            if (this.getEstado() == EstadoRobo.DESLIGADO) {
                throw new RoboDesligadoException("Robô " + getId() + " está desligado.");
            }
            int futuroZ = this.getZ() + dz; // Calcula a altitude futura
            // Verifica se a altitude futura excede a máxima permitida
            if (futuroZ > altitudeMaxima) {
                throw new AcaoNaoPermitidaException(getId() + " não pode se mover para Z=" + futuroZ + " (acima da alt max: "+altitudeMaxima+").");
            }
            // Verifica se a altitude futura é menor que zero
            if (futuroZ < 0) {
                throw new AcaoNaoPermitidaException(getId() + " não pode se mover para Z=" + futuroZ + " (abaixo de 0).");
            }
            // Chama o método da superclasse para realizar o movimento se as verificações passarem
            super.moverRelativamente(ambiente, dx, dy, dz); 
        }

        /**
         * Executa a tarefa específica de um robô aéreo: patrulha aérea.
         * O robô tenta mover-se um passo na direção atual e depois muda sua direção para SUL.
         * @param ambiente O ambiente de simulação.
         * @throws RoboDesligadoException Se o robô estiver desligado.
         * @throws AcaoNaoPermitidaException Se a ação não for permitida.
         * @throws ColisaoException Se houver colisão.
         * @throws ForaDosLimitesException Se sair dos limites.
         * @throws RecursoInsuficienteException (Declarada para compatibilidade com a superclasse, não usada diretamente aqui).
         * @throws ErroComunicacaoException (Declarada para compatibilidade com a superclasse, não usada diretamente aqui).
         */
        @Override
        // CORRIGIDO: Adicionadas todas as exceções de Robo.executarTarefa para segurança ao sobrescrever
        public void executarTarefa(Ambiente ambiente) throws RoboDesligadoException, AcaoNaoPermitidaException, ColisaoException, ForaDosLimitesException, RecursoInsuficienteException, ErroComunicacaoException {
            if (getEstado() == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(getId() + " desligado.");
            System.out.println("Robô Aéreo " + getId() + " está realizando patrulha aérea em Z=" + getZ() + ".");
            
            int dx_patrulha = 0, dy_patrulha = 0;
            // Determina o deslocamento com base na direção atual
            switch (getDirecao().toUpperCase()) {
                case "NORTE": dy_patrulha = 1; break;
                case "SUL": dy_patrulha = -1; break;
                case "LESTE": dx_patrulha = 1; break;
                case "OESTE": dx_patrulha = -1; break;
            }
            // Tenta mover um passo na direção calculada
            if (dx_patrulha != 0 || dy_patrulha != 0) {
                 System.out.println(getId() + " (Aereo) tentando mover 1 passo para " + getDirecao());
                 // Esta chamada deve ser compatível com sua própria cláusula throws
                 moverRelativamente(ambiente, dx_patrulha, dy_patrulha, 0); // Movimento apenas no plano XY
            }
            setDirecao("SUL"); // Muda a direção para SUL após a patrulha
        }

        /**
         * Aciona todos os sensores acoplados a este robô aéreo.
         * @param ambiente O ambiente para os sensores monitorarem.
         * @throws RoboDesligadoException Se o robô estiver desligado.
         */
        @Override
        public void acionarSensores(Ambiente ambiente) throws RoboDesligadoException {
            if (getEstado() == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(getId() + " desligado.");
            System.out.println("\n--- Sensores do Robô Aéreo " + getId() + " ---");
            if (getSensores().isEmpty()) {
                System.out.println(getId() + " não possui sensores."); return;
            }
            // Itera sobre os sensores e chama o método monitorar de cada um
            for (Sensor s : getSensores()) {
                System.out.println(s.monitorar(ambiente, this));
            }
        }
        /**
         * Retorna a representação visual do robô aéreo.
         * @return 'V' para Robô Aéreo (Voador).
         */
        @Override
        public char getRepresentacao() { return 'V';} 
    }

    /**
     * Representa um drone de carga, um tipo especializado de {@link RoboAereo}.
     * Possui capacidade de carga e pode executar ações autônomas.
     * Implementa {@link Autonomo}.
     */
    public static class RoboDroneDeCarga extends RoboAereo implements Autonomo {
        private int carga; // Carga atual do drone
        private final int cargaMaxima; // Capacidade máxima de carga

        /**
         * Construtor para RoboDroneDeCarga.
         * @param id Identificador do robô.
         * @param x Posição inicial X.
         * @param y Posição inicial Y.
         * @param z Posição inicial Z (altitude).
         * @param direcao Direção inicial.
         * @param altitudeMaxima Altitude máxima de voo.
         * @param numHelices Número de hélices.
         * @param cargaInicial Carga inicial do drone.
         * @param cargaMaxima Capacidade máxima de carga.
         */
        public RoboDroneDeCarga(String id, int x, int y, int z, String direcao, int altitudeMaxima,
                                int numHelices, int cargaInicial, int cargaMaxima) {
            super(id, x, y, z, direcao, altitudeMaxima, numHelices);
            this.cargaMaxima = Math.max(0, cargaMaxima); // Garante que a carga máxima não seja negativa
            // Garante que a carga inicial esteja entre 0 e a carga máxima
            this.carga = Math.max(0, Math.min(cargaInicial, this.cargaMaxima));
        }
        
        // Getters para carga
        public int getCarga() { return carga; }
        public int getCargaMaxima() { return cargaMaxima; }

        /**
         * Carrega o drone com uma determinada quantidade.
         * @param quantidade A quantidade a ser carregada (deve ser positiva).
         * @throws AcaoNaoPermitidaException Se a quantidade for inválida ou exceder a capacidade máxima.
         * @throws RoboDesligadoException Se o robô estiver desligado.
         */
        public void carregar(int quantidade) throws AcaoNaoPermitidaException, RoboDesligadoException {
            if (getEstado() == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(getId() + " desligado.");
            if (quantidade <= 0) throw new AcaoNaoPermitidaException("Quantidade para carregar deve ser positiva.");
            // Verifica se carregar a quantidade excederá a carga máxima
            if (this.carga + quantidade > this.cargaMaxima) {
                throw new AcaoNaoPermitidaException("Excederia carga máxima. Carga atual: " + this.carga + ", tentando: " + quantidade + ", Max: " + this.cargaMaxima);
            }
            this.carga += quantidade; // Aumenta a carga
            System.out.println(getId() + " carregou " + quantidade + ". Carga atual: " + this.carga + "/" + this.cargaMaxima);
        }
        
        /**
         * Descarrega uma determinada quantidade do drone.
         * @param quantidade A quantidade a ser descarregada (deve ser positiva).
         * @throws AcaoNaoPermitidaException Se a quantidade for inválida ou não houver carga suficiente.
         * @throws RoboDesligadoException Se o robô estiver desligado.
         */
        public void descarregar(int quantidade) throws AcaoNaoPermitidaException, RoboDesligadoException {
            if (getEstado() == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(getId() + " desligado.");
            if (quantidade <= 0) throw new AcaoNaoPermitidaException("Quantidade para descarregar deve ser positiva.");
            // Verifica se há carga suficiente para descarregar
            if (quantidade > this.carga) {
                throw new AcaoNaoPermitidaException("Não pode descarregar " + quantidade + ". Carga atual: " + this.carga);
            }
            this.carga -= quantidade; // Diminui a carga
            System.out.println(getId() + " descarregou " + quantidade + ". Carga atual: " + this.carga + "/" + this.cargaMaxima);
        }

        /**
         * Executa a tarefa específica do drone de carga.
         * Lógica simples: se vazio na base (0,0), tenta carregar. Se com carga e fora da base, move para a base. Se com carga na base, descarrega.
         * @param ambiente O ambiente de simulação.
         * @throws RoboDesligadoException Se o robô estiver desligado.
         * @throws AcaoNaoPermitidaException Se a ação de carregar/descarregar/mover não for permitida.
         * @throws ColisaoException Se houver colisão ao mover.
         * @throws ForaDosLimitesException Se mover para fora dos limites.
         * @throws RecursoInsuficienteException (Pode ser lançada por `carregar` se fosse implementado para pegar de um local, mas aqui `carregar` apenas aumenta o contador).
         * @throws ErroComunicacaoException (Declarada para compatibilidade, não usada aqui).
         */
        @Override
        // A assinatura corresponde a RoboAereo.executarTarefa (que agora inclui RecursoInsuficienteException implicitamente de Robo.java)
        public void executarTarefa(Ambiente ambiente) throws RoboDesligadoException, AcaoNaoPermitidaException, ColisaoException, ForaDosLimitesException, RecursoInsuficienteException, ErroComunicacaoException {
            if (getEstado() == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(getId() + " desligado.");
            System.out.println(getId() + " (Drone Carga) status: Carga " + carga + "/" + cargaMaxima);
            
            // Lógica de decisão para a tarefa do drone
            if (carga == 0 && getX() == 0 && getY() == 0) { // Se está na base (0,0) e vazio
                System.out.println(getId() + " na base e vazio, tentando carregar...");
                if (cargaMaxima > 0) { // Só tenta carregar se tiver capacidade
                    carregar(1); // Tenta carregar 1 unidade (exemplo)
                } else {
                    System.out.println(getId() + " não pode carregar (capacidade máxima é 0).");
                }
            } else if (carga > 0 && (getX() != 0 || getY() != 0)) { // Se tem carga e não está na base
                System.out.println(getId() + " tem carga, tentando mover para base (0,0) para descarregar.");
                int targetX = 0; int targetY = 0; // Coordenadas da base
                // Calcula o deslocamento para chegar à base
                int dx = Integer.compare(targetX, getX()); // Retorna -1, 0, ou 1
                int dy = Integer.compare(targetY, getY());
                if (dx !=0 || dy !=0) moverRelativamente(ambiente, dx, dy, 0); // Move um passo em direção à base
            } else if (carga > 0 && getX() == 0 && getY() == 0) { // Se tem carga e está na base
                 System.out.println(getId() + " na base com carga, descarregando...");
                 descarregar(carga); // Descarrega toda a carga
            } else {
                 System.out.println(getId() + " aguardando instruções ou em condição não prevista pela tarefa simples.");
            }
        }
        
        /**
         * Executa a próxima ação autônoma do drone de carga.
         * Lógica: Se tem carga e não está na base, move para a base. Se descarregado, move para um ponto de coleta e carrega.
         * @param ambiente O ambiente de simulação.
         * @throws RoboDesligadoException Se o robô estiver desligado.
         * @throws AcaoNaoPermitidaException Se a ação não for permitida.
         * @throws ColisaoException Se houver colisão.
         * @throws ForaDosLimitesException Se sair dos limites.
         */
        @Override
        public void executarProximaAcaoAutonoma(Ambiente ambiente) throws RoboDesligadoException, AcaoNaoPermitidaException, ColisaoException, ForaDosLimitesException {
            if (getEstado() == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(getId() + " desligado.");
            System.out.println(getId() + " (Drone Carga Autônomo) executando...");

            if (carga > 0 && (getX() != 0 || getY() != 0)) { // Se tem carga e não está na base (0,0)
                int targetX = 0; int targetY = 0; // Ponto de descarga (base)
                // Calcula o movimento para a base
                int dx = Integer.compare(targetX, getX());
                int dy = Integer.compare(targetY, getY());
                if (dx !=0 || dy !=0) { // Se não está na base
                    System.out.println("Autônomo: Movendo para base (" + dx + "," + dy + ")");
                    moverRelativamente(ambiente, dx, dy, 0); // Move um passo em direção à base
                } else { // Se chegou na base e ainda tem carga (caso raro, pois deveria descarregar)
                     try { if (carga > 0) descarregar(carga); }
                     catch (RoboDesligadoException | AcaoNaoPermitidaException e) { System.err.println("Autônomo: Falha ao descarregar - " + e.getMessage());}
                }
            } else if (carga < cargaMaxima) { // Se não está com carga máxima (prioriza carregar)
                // Define um ponto de coleta (ex: canto oposto do ambiente)
                int targetX = ambiente.getLargura() -1; 
                int targetY = ambiente.getProfundidade() -1; 
                // Calcula o movimento para o ponto de coleta
                int dx = Integer.compare(targetX, getX());
                int dy = Integer.compare(targetY, getY());
                if (dx !=0 || dy !=0) { // Se não está no ponto de coleta
                    System.out.println("Autônomo: Movendo para coleta (" + dx + "," + dy + ")");
                    moverRelativamente(ambiente, dx, dy, 0); // Move um passo em direção ao ponto de coleta
                } else { // Se chegou no ponto de coleta
                    try { if (cargaMaxima - carga > 0) carregar(cargaMaxima - carga); } // Carrega até a capacidade máxima
                    catch (RoboDesligadoException | AcaoNaoPermitidaException e) { System.err.println("Autônomo: Falha ao carregar - " + e.getMessage());}
                }
            } else { // Se está com carga máxima e na base (ou outra condição não tratada)
                 System.out.println(getId() + " (Autônomo) - Carga máxima e na base, ou outra condição.");
            }
        }
        /**
         * Retorna a representação visual do drone de carga.
         * @return 'D' para Drone de Carga.
         */
         @Override
        public char getRepresentacao() { return 'D';} 
    }
    
    /**
     * Representa um robô terrestre com capacidade de comunicação.
     * Estende {@link RoboTerrestre} e implementa {@link Comunicavel}.
     */
    public static class RoboComunicador extends RoboTerrestre implements Comunicavel {
        /**
         * Construtor para RoboComunicador.
         * @param id Identificador do robô.
         * @param x Posição inicial X.
         * @param y Posição inicial Y.
         * @param direcao Direção inicial.
         * @param velocidadeMaxima Velocidade máxima do robô terrestre.
         */
        public RoboComunicador(String id, int x, int y, String direcao, int velocidadeMaxima) {
            super(id, x, y, direcao, velocidadeMaxima);
        }

        /**
         * Envia uma mensagem para outro robô comunicável.
         * @param central A {@link CentralComunicacao} para registrar a mensagem.
         * @param destinatario O {@link Comunicavel} que receberá a mensagem.
         * @param mensagem O conteúdo da mensagem.
         * @throws RoboDesligadoException Se o remetente ou o destinatário estiverem desligados.
         * @throws ErroComunicacaoException Se o destinatário não for um robô válido ou houver outro erro.
         */
        @Override
        public void enviarMensagem(CentralComunicacao central, Comunicavel destinatario, String mensagem) throws RoboDesligadoException, ErroComunicacaoException {
            if (getEstado() == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(getId() + " desligado.");
            // Verifica se o destinatário é um Robô (necessário para checar estado)
            if (!(destinatario instanceof Robo)) throw new ErroComunicacaoException("Destinatário não é um robô válido.");
            // Verifica se o robô destinatário está desligado
            if (((Robo)destinatario).getEstado() == EstadoRobo.DESLIGADO) throw new ErroComunicacaoException("Destinatário " + ((Robo)destinatario).getId() + " está desligado.");
            
            System.out.println(getId() + " (Comunicador) enviando para " + ((Robo)destinatario).getId() + ": " + mensagem);
            central.registrarMensagem(this.getId(), ((Robo)destinatario).getId(), mensagem); // Registra na central
            destinatario.receberMensagem(this.getId(), mensagem); // Entrega a mensagem ao destinatário
        }

        /**
         * Recebe uma mensagem de outro robô.
         * @param remetenteId O ID do robô que enviou a mensagem.
         * @param mensagem O conteúdo da mensagem recebida.
         * @throws RoboDesligadoException Se este robô (receptor) estiver desligado.
         */
        @Override
        public void receberMensagem(String remetenteId, String mensagem) throws RoboDesligadoException {
            if (getEstado() == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(getId() + " desligado.");
            System.out.println(getId() + " (Comunicador) recebeu de " + remetenteId + ": " + mensagem);
        }
        
        /**
         * Executa a tarefa específica do robô comunicador: procurar outro robô comunicável e enviar uma saudação.
         * @param ambiente O ambiente de simulação.
         * @throws RoboDesligadoException Se o robô estiver desligado.
         * @throws AcaoNaoPermitidaException (Declarada para compatibilidade).
         * @throws ColisaoException (Declarada para compatibilidade).
         * @throws ForaDosLimitesException (Declarada para compatibilidade).
         * @throws RecursoInsuficienteException (Declarada para compatibilidade).
         * @throws ErroComunicacaoException Se falhar ao enviar a mensagem.
         */
        @Override 
        // A assinatura corresponde a RoboTerrestre.executarTarefa (que agora inclui ErroComunicacaoException de Robo.java)
        public void executarTarefa(Ambiente ambiente) throws RoboDesligadoException, AcaoNaoPermitidaException, ColisaoException, ForaDosLimitesException, RecursoInsuficienteException, ErroComunicacaoException {
            if (getEstado() == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(getId() + " desligado.");
            System.out.println(getId() + " (Comunicador) está ocioso, procurando alguém para conversar...");
            
            // Procura por outros robôs que são Comunicavel, estão ligados e não são ele mesmo
            List<Robo> outrosComunicaveis = ambiente.getEntidades().stream()
                .filter(e -> e instanceof Robo && e instanceof Comunicavel && e != this && ((Robo)e).getEstado() == EstadoRobo.LIGADO)
                .map(e -> (Robo)e)
                .collect(Collectors.toList());

            if (!outrosComunicaveis.isEmpty()) { // Se encontrou alguém
                Comunicavel destinatario = (Comunicavel) outrosComunicaveis.get(0); // Pega o primeiro da lista
                enviarMensagem(centralComunicacao, destinatario, "Olá de " + getId() + "!"); // Envia uma saudação
            } else {
                System.out.println(getId() + " não encontrou ninguém para conversar agora.");
            }
        }
        /**
         * Retorna a representação visual do robô comunicador.
         * @return 'C' para Comunicador.
         */
        @Override
        public char getRepresentacao() { return 'C';} 
    }
}