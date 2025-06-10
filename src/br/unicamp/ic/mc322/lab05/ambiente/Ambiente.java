package br.unicamp.ic.mc322.lab05.ambiente;

import java.util.ArrayList;
import br.unicamp.ic.mc322.lab05.excecoes.AcaoNaoPermitidaException;
import br.unicamp.ic.mc322.lab05.excecoes.ColisaoException;
import br.unicamp.ic.mc322.lab05.excecoes.ForaDosLimitesException;
import br.unicamp.ic.mc322.lab05.excecoes.RoboDesligadoException;
import br.unicamp.ic.mc322.lab05.robos.EstadoRobo;
import br.unicamp.ic.mc322.lab05.robos.Robo;
import br.unicamp.ic.mc322.lab05.sensores.Sensoreavel;

/**
 * Representa o mundo tridimensional onde as entidades (robôs, obstáculos) existem e interagem.
 * O ambiente possui dimensões fixas (largura, profundidade, altura) e mantém o controle
 * da posição de todas as entidades.
 */
public class Ambiente {
    private final int largura, profundidade, altura; // Dimensões do ambiente
    private ArrayList<Entidade> entidades;         // Lista de todas as entidades presentes no ambiente
    private TipoEntidade[][][] mapa;               // Representação tridimensional do ambiente, indicando o que ocupa cada célula

    /**
     * Construtor para criar um novo ambiente com as dimensões especificadas.
     * Inicializa o mapa como vazio e a lista de entidades.
     *
     * @param largura      A dimensão X do ambiente.
     * @param profundidade A dimensão Y do ambiente.
     * @param altura       A dimensão Z do ambiente.
     * @throws IllegalArgumentException Se alguma das dimensões for menor ou igual a zero.
     */
    public Ambiente(int largura, int profundidade, int altura) {
        this.largura = largura;
        this.profundidade = profundidade;
        this.altura = altura;
        this.entidades = new ArrayList<>(); // Inicializa a lista de entidades

        // Validação das dimensões do ambiente
        if (largura <= 0 || profundidade <= 0 || altura <= 0) {
            throw new IllegalArgumentException("As dimensões do ambiente devem ser positivas.");
        }

        // Cria o mapa tridimensional
        this.mapa = new TipoEntidade[largura][profundidade][altura];
        inicializarMapa(); // Preenche o mapa com células vazias
    }

    /**
     * Preenche todas as células do mapa do ambiente com o tipo VAZIO.
     * Este método é chamado durante a construção do ambiente.
     */
    public void inicializarMapa() {
        for (int x = 0; x < largura; x++) {
            for (int y = 0; y < profundidade; y++) {
                for (int z = 0; z < altura; z++) {
                    mapa[x][y][z] = TipoEntidade.VAZIO; // Define cada célula como vazia
                }
            }
        }
    }

    /**
     * Verifica se uma determinada coordenada (x, y, z) está dentro dos limites do ambiente.
     *
     * @param x A coordenada X.
     * @param y A coordenada Y.
     * @param z A coordenada Z.
     * @return true se a posição estiver dentro dos limites, false caso contrário.
     */
    public boolean dentroDosLimites(int x, int y, int z) {
        return x >= 0 && x < largura &&
               y >= 0 && y < profundidade &&
               z >= 0 && z < altura;
    }

    /**
     * Verifica se uma determinada posição no ambiente já está ocupada por alguma entidade.
     *
     * @param x A coordenada X.
     * @param y A coordenada Y.
     * @param z A coordenada Z.
     * @return true se a posição estiver ocupada, false caso contrário.
     * @throws ForaDosLimitesException Se a posição consultada estiver fora dos limites do ambiente.
     */
    public boolean estaOcupado(int x, int y, int z) throws ForaDosLimitesException {
        if (!dentroDosLimites(x, y, z)) {
            throw new ForaDosLimitesException("Posição (" + x + "," + y + "," + z + ") está fora dos limites do ambiente.");
        }
        return mapa[x][y][z] != TipoEntidade.VAZIO; // Verifica se a célula não está marcada como VAZIO
    }

    /**
     * Retorna a entidade que está em uma posição específica (x, y, z).
     * Este método é primariamente útil para entidades pontuais como Robôs, que ocupam uma única célula.
     * Para obstáculos que ocupam múltiplas células, este método só os encontrará se (x,y,z) for o ponto de ancoragem específico do obstáculo.
     *
     * @param x A coordenada X.
     * @param y A coordenada Y.
     * @param z A coordenada Z.
     * @return A entidade na posição especificada, ou null se a célula estiver vazia ou se for parte de um obstáculo maior não ancorado aqui.
     * @throws ForaDosLimitesException Se a consulta for para uma posição fora dos limites do ambiente.
     */
    public Entidade getEntidadeEm(int x, int y, int z) throws ForaDosLimitesException {
        if (!dentroDosLimites(x, y, z)) {
            throw new ForaDosLimitesException("Consulta de entidade fora dos limites: (" + x + "," + y + "," + z + ")");
        }
        // Verifica se há uma entidade pontual (como um Robô) na célula.
        if (mapa[x][y][z] != TipoEntidade.VAZIO) {
            for (Entidade ent : entidades) {
                // Verifica apenas entidades que ocupam um único ponto, como Robôs.
                if (ent instanceof Robo) {
                    if (ent.getX() == x && ent.getY() == y && ent.getZ() == z) {
                        return ent; // Retorna o robô encontrado
                    }
                }
            }
        }
        return null; // Nenhuma entidade pontual encontrada, ou a célula é parte de um obstáculo maior.
    }

    /**
     * Adiciona uma entidade (robô ou obstáculo) ao ambiente.
     * Verifica colisões e se a entidade está dentro dos limites do ambiente antes de adicioná-la.
     *
     * @param e A entidade a ser adicionada.
     * @throws ColisaoException        Se a posição onde a entidade seria adicionada já estiver ocupada.
     * @throws ForaDosLimitesException Se a entidade (ou parte dela) estiver fora dos limites do ambiente.
     */
    public void adicionarEntidade(Entidade e) throws ColisaoException, ForaDosLimitesException {
        if (e instanceof Robo) { // Se a entidade é um Robô
            Robo r = (Robo) e;
            // Verifica se o robô está dentro dos limites
            if (!dentroDosLimites(r.getX(), r.getY(), r.getZ())) {
                throw new ForaDosLimitesException("Robô " + r.getId() + " fora dos limites ao adicionar: (" + r.getX() + "," + r.getY() + "," + r.getZ() + ")");
            }
            // Verifica se a posição do robô já está ocupada
            if (estaOcupado(r.getX(), r.getY(), r.getZ())) {
                throw new ColisaoException("Posição (" + r.getX() + "," + r.getY() + "," + r.getZ() + ") já ocupada. Não é possível adicionar robô " + r.getId());
            }
            // Marca a posição do robô no mapa
            mapa[r.getX()][r.getY()][r.getZ()] = e.getTipo();
        } else if (e instanceof Obstaculo) { // Se a entidade é um Obstáculo
            Obstaculo o = (Obstaculo) e;
            // Verifica cada célula que o obstáculo ocupará
            for (int i = o.getX1(); i <= o.getX2(); i++) {
                for (int j = o.getY1(); j <= o.getY2(); j++) {
                    for (int k = o.getZ1(); k <= o.getZ2(); k++) {
                        // Verifica se a célula está dentro dos limites
                        if (!dentroDosLimites(i, j, k)) {
                            throw new ForaDosLimitesException("Obstáculo " + o.getDescricao() + " parcialmente fora dos limites ao adicionar em (" + i + "," + j + "," + k + ")");
                        }
                        // Verifica se a célula já está ocupada
                        if (estaOcupado(i, j, k)) {
                            throw new ColisaoException("Posição (" + i + "," + j + "," + k + ") já ocupada. Não é possível adicionar obstáculo " + o.getDescricao());
                        }
                    }
                }
            }
            // Marca todas as células do obstáculo no mapa
            for (int i = o.getX1(); i <= o.getX2(); i++) {
                for (int j = o.getY1(); j <= o.getY2(); j++) {
                    for (int k = o.getZ1(); k <= o.getZ2(); k++) {
                        mapa[i][j][k] = e.getTipo();
                    }
                }
            }
        }
        entidades.add(e); // Adiciona a entidade à lista de entidades do ambiente
    }

    /**
     * Remove uma entidade do ambiente.
     * Libera as posições que a entidade ocupava no mapa.
     *
     * @param e A entidade a ser removida.
     * @throws ForaDosLimitesException Se, ao tentar limpar o mapa, uma coordenada da entidade estiver fora dos limites (embora isso seja mais uma verificação de segurança).
     */
    public void removerEntidade(Entidade e) throws ForaDosLimitesException {
        if (e instanceof Robo) { // Se a entidade é um Robô
            Robo r = (Robo) e;
            // Se o robô estiver dentro dos limites, marca sua posição como vazia no mapa
            if (dentroDosLimites(r.getX(), r.getY(), r.getZ())) {
                mapa[r.getX()][r.getY()][r.getZ()] = TipoEntidade.VAZIO;
            }
        } else if (e instanceof Obstaculo) { // Se a entidade é um Obstáculo
            Obstaculo o = (Obstaculo) e;
            // Marca todas as células que o obstáculo ocupava como vazias
            for (int i = o.getX1(); i <= o.getX2(); i++) {
                for (int j = o.getY1(); j <= o.getY2(); j++) {
                    for (int k = o.getZ1(); k <= o.getZ2(); k++) {
                        if (dentroDosLimites(i, j, k)) { // Garante que a limpeza ocorra dentro dos limites
                            mapa[i][j][k] = TipoEntidade.VAZIO;
                        }
                    }
                }
            }
        }
        entidades.remove(e); // Remove a entidade da lista de entidades do ambiente
    }

    /**
     * Move uma entidade (especificamente um robô) para uma nova posição no ambiente.
     * Verifica se o robô está ligado, se a nova posição está dentro dos limites e se não há colisões.
     *
     * @param e      A entidade a ser movida (deve ser um Robô).
     * @param novoX  A nova coordenada X.
     * @param novoY  A nova coordenada Y.
     * @param novoZ  A nova coordenada Z.
     * @throws ColisaoException         Se a nova posição já estiver ocupada.
     * @throws ForaDosLimitesException  Se a nova posição estiver fora dos limites do ambiente.
     * @throws RoboDesligadoException   Se o robô estiver desligado.
     * @throws AcaoNaoPermitidaException Se a entidade não for um robô ou outra ação não for permitida.
     */
    public void moverEntidade(Entidade e, int novoX, int novoY, int novoZ) throws ColisaoException, ForaDosLimitesException, RoboDesligadoException, AcaoNaoPermitidaException {
        // Verifica se a entidade é um robô, pois apenas robôs podem ser movidos desta forma
        if (!(e instanceof Robo)) {
            throw new AcaoNaoPermitidaException("Apenas robôs podem ser movidos com moverEntidade. Tentativa em: " + e.getDescricao());
        }
        Robo robo = (Robo) e;

        // Verifica se o robô está ligado
        if (robo.getEstado() == EstadoRobo.DESLIGADO) {
            throw new RoboDesligadoException("Robô " + robo.getId() + " está desligado.");
        }
        // Verifica se a nova posição está dentro dos limites
        if (!dentroDosLimites(novoX, novoY, novoZ)) {
            throw new ForaDosLimitesException("Movimento para (" + novoX + "," + novoY + "," + novoZ + ") está fora dos limites para o robô " + robo.getId());
        }
        // Verifica se a nova posição está ocupada
        if (estaOcupado(novoX, novoY, novoZ)) {
            throw new ColisaoException("Colisão! Posição (" + novoX + "," + novoY + "," + novoZ + ") já está ocupada. Robô " + robo.getId() + " não pode mover.");
        }

        // Guarda a posição antiga do robô
        int antigoX = robo.getX();
        int antigoY = robo.getY();
        int antigoZ = robo.getZ();

        // Limpa a posição antiga do robô no mapa, se estiver dentro dos limites
        if (dentroDosLimites(antigoX, antigoY, antigoZ)) {
             mapa[antigoX][antigoY][antigoZ] = TipoEntidade.VAZIO;
        }

        // Atualiza a posição do objeto Robô
        robo.atualizarPosicao(novoX, novoY, novoZ);
        // Marca a nova posição do robô no mapa
        mapa[novoX][novoY][novoZ] = robo.getTipo();
        System.out.println("Robô " + robo.getId() + " moveu-se de (" + antigoX + "," + antigoY + "," + antigoZ + ") para (" + novoX + "," + novoY + "," + novoZ + ")");
    }

    /**
     * Exibe uma representação visual do ambiente no console.
     * Mostra o ambiente em camadas (slices) ao longo do eixo Z.
     *
     * @throws ForaDosLimitesException Se ocorrer um acesso fora dos limites ao ler o mapa (deve ser raro se o mapa for bem gerenciado).
     */
    public void visualizarAmbiente() throws ForaDosLimitesException { // Adicionada a declaração throws
        System.out.println("\nVisualização do Ambiente (Plano X,Y - Múltiplas Camadas Z):");
        for (int z = 0; z < altura; z++) { // Itera sobre cada camada Z
            System.out.println("--- Camada Z=" + z + " ---");
            System.out.print(" Y\\X"); // Cabeçalho para coordenadas X
            for (int x = 0; x < largura; x++) {
                System.out.printf("%3d", x);
            }
            System.out.println();
            for (int y = 0; y < profundidade; y++) { // Itera sobre cada linha Y
                System.out.printf("%3d ", y); // Coordenada Y da linha
                for (int x = 0; x < largura; x++) { // Itera sobre cada coluna X
                    char symbol = '.'; // Símbolo padrão para célula vazia
                    if (mapa[x][y][z] != TipoEntidade.VAZIO) { // Se a célula não está vazia
                        Entidade entNaCelula = null;
                        // Verifica se um robô está exatamente nesta célula x,y,z
                        for (Entidade ent : entidades) {
                            if (ent instanceof Robo && ent.getX() == x && ent.getY() == y && ent.getZ() == z) {
                                entNaCelula = ent;
                                break; // Encontrou o robô
                            }
                        }

                        if (entNaCelula != null) { // Se encontrou um robô na célula
                            symbol = entNaCelula.getRepresentacao(); // Usa a representação do robô
                        } else if (mapa[x][y][z] == TipoEntidade.OBSTACULO) {
                            // Se é um obstáculo, encontra qual obstáculo ocupa esta célula para obter sua representação específica
                            char obsSymbol = 'X'; // Símbolo padrão para parte de um obstáculo
                            for (Entidade ent : entidades) {
                                if (ent instanceof Obstaculo) {
                                    Obstaculo o = (Obstaculo) ent;
                                    // Verifica se a célula (x,y,z) pertence a este obstáculo
                                    if (x >= o.getX1() && x <= o.getX2() &&
                                        y >= o.getY1() && y <= o.getY2() &&
                                        z >= o.getZ1() && z <= o.getZ2()) {
                                        obsSymbol = o.getRepresentacao(); // Usa a representação do obstáculo
                                        break;
                                    }
                                }
                            }
                            symbol = obsSymbol;
                        } else if (mapa[x][y][z] == TipoEntidade.ROBO) {
                             symbol = 'R'; // Representação genérica para Robô se não foi encontrado pela verificação específica acima
                        }
                    }
                    System.out.printf("[%c] ", symbol); // Imprime o símbolo da célula
                }
                System.out.println(); // Nova linha para a próxima linha Y
            }
        }
        System.out.println("Legenda: R=Robô Genérico, T=Terrestre, V=Aéreo, D=DroneCarga, C=Comunicador, A=Árvore, #=Parede, P=Prédio, B=Buraco, O=Outro Obst, .=Vazio");
    }


    /**
     * Aciona os sensores de todas as entidades "Sensoreáveis" (geralmente robôs) no ambiente.
     * Cada entidade sensoreável irá então executar sua lógica de sensoriamento.
     */
    public void executarSensoresGlobais() {
        System.out.println("\n--- Executando Sensores no Ambiente ---");
        for (Entidade e : entidades) {
            if (e instanceof Sensoreavel) { // Verifica se a entidade pode ter sensores
                try {
                    ((Sensoreavel) e).acionarSensores(this); // Chama o método para acionar sensores
                } catch (RoboDesligadoException rde) {
                    // Informa se um robô estava desligado e não pôde acionar seus sensores
                    System.out.println("Erro ao acionar sensores para " + ((Robo) e).getId() + ": " + rde.getMessage());
                }
            }
        }
    }

    /**
     * Retorna a lista de todas as entidades presentes no ambiente.
     * @return Uma `ArrayList` contendo todas as entidades.
     */
    public ArrayList<Entidade> getEntidades() {
        return entidades;
    }

    // Getters para as dimensões do ambiente
    public int getLargura() { return largura; }
    public int getProfundidade() { return profundidade; }
    public int getAltura() { return altura; }
}