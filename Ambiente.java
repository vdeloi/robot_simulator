// Classe Ambiente

/* ################################################################################################################################### */

import java.util.ArrayList;
import java.util.List; 

/* ################################################################################################################################### */

// Classe que guarda o ambiente da simulação dos robôs
public class Ambiente {

    private final int largura; // X
    private final int profundidade; // Y
    private final int alturaMaxAmbiente; // Z

    // Este atributo armazena os robôs ativos no ambiente
    //private ArrayList<Robo> listaDeRobos = new ArrayList<>();
    //private ArrayList<Obstaculo> listaDeObstaculos = new ArrayList<>();
    private ArrayList<Entidade> entidades;

    private TipoEntidade[][][] mapa;

    /* ################################################################################################################################### */
    // Construtor da classe Ambiente

    public Ambiente(int largura, int profundidade, int alturaMaxAmbiente) {

        // Inicializa o ambiente com as dimensões especificadas

        this.largura = largura;
        this.profundidade = profundidade;
        this.alturaMaxAmbiente = alturaMaxAmbiente;
        this.entidades = new ArrayList<>();
        this.mapa = new TipoEntidade[largura][profundidade][alturaMaxAmbiente];
        inicializarMapa();
    }
    /* ################################################################################################################################### */

    // Getters
    public int getLargura() {
        return largura;
    }

    public int getProfundidade() {
        return profundidade;
    }

    public int getAlturaMaxAmbiente() {
        return alturaMaxAmbiente;
    }

    public ArrayList<Entidade> getEntidades() {
        return entidades;
    }

    public TipoEntidade[][][] getMapa() {
        return mapa;
    }

    /* ################################################################################################################################### */

    /*percorre todas as posições do mapa tridimensional do ambiente e inicializa cada célula com o tipo de entidade VAZIO. Isso garante que o mapa esteja limpo e pronto para receber entidades antes do início da simulação. */


    public void inicializarMapa() {

        // Inicializa o mapa com o tipo de entidade VAZIO
        for (int x = 0; x < largura; x++) {

            for (int y = 0; y < profundidade; y++) {

                for (int z = 0; z < alturaMaxAmbiente; z++) {

                    mapa[x][y][z] = TipoEntidade.VAZIO;
                }
            }
        }
    }

    /* ################################################################################################################################### */

    /*adiciona uma entidade ao ambiente, verificando se está dentro dos limites e se não há colisões com outras entidades. Para obstáculos, realiza uma checagem detalhada de colisões em todas as posições ocupadas. Após a validação, atualiza o mapa tridimensional e a lista de entidades, garantindo que o ambiente reflita a nova adição. */



    public void adicionarEntidade(Entidade entidade) throws ColisaoException, ForaDosLimitesException { 
        if (!dentroDosLimites(entidade.getX(), entidade.getY(), entidade.getZ())) {
            throw new ForaDosLimitesException("Entidade '" + entidade.getNome() + "' na posição ("+entidade.getX()+","+entidade.getY()+","+entidade.getZ()+") está fora dos limites do ambiente.");
        }
        // Para obstáculos que ocupam área/volume, a checagem de colisão precisa ser mais robusta
        if (entidade instanceof Obstaculo) {
            Obstaculo obs = (Obstaculo) entidade;
            for (int x = obs.getX(); x <= obs.getPosicaoX2(); x++) {
                for (int y = obs.getY(); y <= obs.getPosicaoY2(); y++) {
                    for (int z = obs.getZ(); z < obs.getZ() + obs.getAlturaObstaculo(); z++) {
                        if (!dentroDosLimites(x,y,z)) {
                             throw new ForaDosLimitesException("Parte do obstáculo '" + obs.getNome() + "' na posição ("+x+","+y+","+z+") está fora dos limites.");
                        }
                        if (estaOcupado(x,y,z)) {
                            throw new ColisaoException("Parte do obstáculo '" + obs.getNome() + "' colide em (" + x + "," + y + "," + z + ") que já está ocupada por " + mapa[x][y][z].getDescricao());
                        }
                    }
                }
            }
        } else { // Para entidades pontuais (como Robôs)
             if (estaOcupado(entidade.getX(), entidade.getY(), entidade.getZ())) { 
                throw new ColisaoException("Posição (" + entidade.getX() + "," + entidade.getY() + "," + entidade.getZ() + ") já está ocupada por " + mapa[entidade.getX()][entidade.getY()][entidade.getZ()].getDescricao());
            }
        }

        this.entidades.add(entidade);
        // Atualiza o mapa para a entidade
        if (entidade instanceof Obstaculo) {
            Obstaculo obs = (Obstaculo) entidade;
             for (int x = obs.getX(); x <= obs.getPosicaoX2(); x++) {
                for (int y = obs.getY(); y <= obs.getPosicaoY2(); y++) {
                    for (int z_offset = 0; z_offset < obs.getAlturaObstaculo(); z_offset++) {
                        int z_atual = obs.getZ() + z_offset;
                        if(dentroDosLimitesMapa(x,y,z_atual)) { // Garante que não vai escrever fora do mapa
                           atualizarMapaPosicao(x, y, z_atual, entidade.getTipoEntidade());
                        }
                    }
                }
            }
        } else {
            atualizarMapaPosicao(entidade.getX(), entidade.getY(), entidade.getZ(), entidade.getTipoEntidade());
        }
        System.out.println("Entidade '" + entidade.getNome() + "' ("+entidade.getTipoEntidade().getDescricao()+") adicionada ao ambiente em (" + entidade.getX() + "," + entidade.getY() + "," + entidade.getZ() + ")");
    }


    /* ################################################################################################################################### */

    /* remove uma entidade do ambiente, atualizando o mapa tridimensional para marcar as posições ocupadas pela entidade como vazias. Ele verifica se a entidade existe no ambiente antes de removê-la e lança uma exceção caso não seja encontrada, garantindo a consistência do estado do ambiente.*/


    public void removerEntidade(Entidade entidade) throws EntidadeNaoEncontradaException { 
        if (this.entidades.remove(entidade)) {
            // Limpa a(s) posição(ões) da entidade no mapa
            if (entidade instanceof Obstaculo) {
                Obstaculo obs = (Obstaculo) entidade;
                 for (int x = obs.getX(); x <= obs.getPosicaoX2(); x++) {
                    for (int y = obs.getY(); y <= obs.getPosicaoY2(); y++) {
                        for (int z_offset = 0; z_offset < obs.getAlturaObstaculo(); z_offset++) {
                             int z_atual = obs.getZ() + z_offset;
                             if(dentroDosLimitesMapa(x,y,z_atual)) {
                                atualizarMapaPosicao(x,y,z_atual, TipoEntidade.VAZIO);
                             }
                        }
                    }
                }
            } else {
                 atualizarMapaPosicao(entidade.getX(), entidade.getY(), entidade.getZ(), TipoEntidade.VAZIO);
            }
            System.out.println("Entidade '" + entidade.getNome() + "' removida do ambiente.");
        } else {
            throw new EntidadeNaoEncontradaException("Entidade '" + entidade.getNome() + "' não encontrada no ambiente para remoção.");
        }
    }

    /* ################################################################################################################################### */


    private void atualizarMapaPosicao(int x, int y, int z, TipoEntidade tipo) {
        if (dentroDosLimitesMapa(x, y, z)) {
            mapa[x][y][z] = tipo;
        }
    }

    /* ################################################################################################################################### */

    /*verifica se uma posição (x, y, z) está dentro dos limites do ambiente. Isso é importante para garantir que as operações de movimentação e adição de entidades não ultrapassem os limites definidos do ambiente. A função retorna verdadeiro se a posição estiver dentro dos limites e falso caso contrário.*/

    public boolean dentroDosLimites(int x, int y, int z) { 
        return x >= 0 && x < this.largura &&
               y >= 0 && y < this.profundidade &&
               z >= 0 && z < this.alturaMaxAmbiente; // z deve ser MENOR que alturaMaxAmbiente
    }

    /* ################################################################################################################################### */

    /*verifica se uma posição (x, y, z) está dentro dos limites do mapa tridimensional. Isso é importante para garantir que as operações de movimentação e adição de entidades não ultrapassem os limites definidos do ambiente. A função retorna verdadeiro se a posição estiver dentro dos limites e falso caso contrário.*/

    
    private boolean dentroDosLimitesMapa(int x, int y, int z) {
        return x >= 0 && x < largura &&
               y >= 0 && y < profundidade &&
               z >= 0 && z < alturaMaxAmbiente;
    }

    /* ################################################################################################################################### */

    /*verifica se uma posição (x, y, z) está ocupada por outra entidade. Isso é importante para evitar colisões entre entidades no ambiente. A função retorna verdadeiro se a posição estiver ocupada e falso caso contrário.*/

    public boolean estaOcupado(int x, int y, int z) throws ForaDosLimitesException {
        if (!dentroDosLimites(x, y, z)) {
            throw new ForaDosLimitesException("Tentativa de verificar ocupação fora dos limites do ambiente: (" + x + "," + y + "," + z + ")");
        }
        return mapa[x][y][z] != TipoEntidade.VAZIO;
    }

    /* ################################################################################################################################### */

    /*verifica se uma posição (x, y, z) está ocupada por outra entidade. Isso é importante para evitar colisões entre entidades no ambiente. A função retorna verdadeiro se a posição estiver ocupada e falso caso contrário.*/

    
    public Entidade getEntidadeNaPosicao(int x, int y, int z) throws ForaDosLimitesException {
        if (!dentroDosLimites(x,y,z)){
            // Não lança exceção, apenas retorna null se for consulta, 
            // mas se for uma verificação crítica, a exceção em estaOcupado é melhor.
            // Para este getter, retornar null é mais prático.
            return null; 
        }
        for (Entidade e : entidades) {
            if (e instanceof Obstaculo) {
                if (((Obstaculo)e).contemPonto(x,y,z)) return e;
            } else { // Para entidades pontuais como robôs
                if (e.getX() == x && e.getY() == y && e.getZ() == z) {
                    return e;
                }
            }
        }
        return null; // Nenhuma entidade objeto encontrada, mas o mapa pode ainda estar ocupado (ex: por parte de obstáculo)
    }

    /* ################################################################################################################################### */

    /*moverEntidade altera a posição de uma entidade no ambiente, verificando se a nova posição está dentro dos limites e se não há colisões com outras entidades. Se a movimentação for bem-sucedida, atualiza o mapa tridimensional e a posição da entidade. Caso contrário, lança exceções apropriadas para indicar problemas como colisões ou posições fora dos limites.*/


    public void moverEntidade(Entidade entidade, int novoX, int novoY, int novoZ) throws ColisaoException, ForaDosLimitesException { 
        if (!entidades.contains(entidade)) {
            System.err.println("ERRO CRÍTICO: Tentativa de mover entidade '" + entidade.getNome() + "' que não está na lista de entidades do ambiente.");
            // Considerar lançar uma EntidadeNaoEncontradaException aqui
            return;
        }
        if (!dentroDosLimites(novoX, novoY, novoZ)) {
            throw new ForaDosLimitesException("Movimento para (" + novoX + "," + novoY + "," + novoZ + ") está fora dos limites do ambiente para '" + entidade.getNome() + "'.");
        }
        
        // Verifica se a nova posição está ocupada POR OUTRA ENTIDADE.
        // Se o próprio robô já está lá (ex: mover para mesma posição), não é colisão.
        TipoEntidade tipoNaNovaPosicao = mapa[novoX][novoY][novoZ];
        if (tipoNaNovaPosicao != TipoEntidade.VAZIO) {
            Entidade ocupante = getEntidadeNaPosicao(novoX, novoY, novoZ); // Tenta pegar o objeto Entidade
            // Se o ocupante for a própria entidade se movendo, não é colisão (ex: robô parado querendo "mover" para a mesma célula)
            // Mas se for um robô tentando entrar numa célula já ocupada, é colisão.
            // Se a célula está ocupada no mapa, mas não há um objeto Entidade (ex: parte de um obstáculo grande), é colisão.
            boolean colisaoReal = true;
            if (ocupante == entidade) { // A entidade já está lá
                colisaoReal = false; // Não é colisão consigo mesma se não mudou de célula
            }

            if (colisaoReal) {
                 String nomeOcupante = (ocupante != null) ? ocupante.getNome() : "parte de um obstáculo";
                 throw new ColisaoException("Posição (" + novoX + "," + novoY + "," + novoZ + ") já está ocupada por " + nomeOcupante + " ("+ tipoNaNovaPosicao.getDescricao() +"). Movimento de '" + entidade.getNome() + "' cancelado.");
            }
        }


        int antigoX = entidade.getX();
        int antigoY = entidade.getY();
        int antigoZ = entidade.getZ();

        // Atualiza o mapa: limpa a posição antiga
        // Para obstáculos, isso seria mais complexo se eles pudessem ser movidos. Por ora, só robôs movem.
        if (!(entidade instanceof Obstaculo)){ // Obstáculos não são movidos por este método
            atualizarMapaPosicao(antigoX, antigoY, antigoZ, TipoEntidade.VAZIO);
        }


        // Atualiza posição da entidade (Robo já tem setters, outras entidades podem não ter)
        if (entidade instanceof Robo) {
            Robo robo = (Robo) entidade;
            robo.setPosicaoX(novoX);
            robo.setPosicaoY(novoY);
            robo.setPosicaoZ(novoZ);
        } else {
            // Para outras entidades que não são Robos (se pudessem mover)
            // Seria necessário um método setPosicao(x,y,z) na interface Entidade ou typecast.
            // Por ora, apenas Robos são movidos ativamente.
            System.out.println("Aviso: Tentativa de mover entidade não-robô '" + entidade.getNome()+"'. Esta funcionalidade é primariamente para robôs.");
            // Se for um obstáculo, a posição dele é fixa e definida na criação.
             if (entidade instanceof Obstaculo) {
                 System.out.println("Obstáculos não devem ser movidos por 'moverEntidade'. Eles são fixos.");
                 // Restaurar mapa se necessário, mas idealmente não chegar aqui para obstáculo
                 atualizarMapaPosicao(antigoX, antigoY, antigoZ, entidade.getTipoEntidade());
                 return;
             }
        }

        // Atualiza o mapa: ocupa a nova posição
        atualizarMapaPosicao(novoX, novoY, novoZ, entidade.getTipoEntidade());

        System.out.println("Entidade '" + entidade.getNome() + "' movida de (" + antigoX + "," + antigoY + "," + antigoZ +
                           ") para (" + novoX + "," + novoY + "," + novoZ + ")");
    }
    
    /* ################################################################################################################################### */

    /*visualizarAmbiente exibe uma representação do ambiente tridimensional no console, mostrando a posição de cada entidade. A visualização é feita em uma grade 2D, onde cada célula representa uma posição no ambiente. A função percorre o mapa tridimensional e imprime a representação de cada entidade encontrada, permitindo uma visão clara do estado atual do ambiente.*/

    public void visualizarAmbiente() { 
        System.out.println("\n--- Visualização do Ambiente (Vista de Cima - Z=0 ou primeira entidade encontrada) ---");
        System.out.print("Y\\X>"); // Ajuste para melhor leitura dos eixos
        for (int x_idx = 0; x_idx < largura; x_idx++) {
            System.out.printf("%3d", x_idx);
        }
        System.out.println();

        for (int y = 0; y < profundidade; y++) { // Eixo Y primeiro para visualização "mapa"
            System.out.printf("%3d ", y); 
            for (int x = 0; x < largura; x++) {
                char representacao = '.'; // Default para VAZIO se nada for encontrado
                boolean entidadeEncontradaNaColuna = false;
                // Procura de baixo (z=0) para cima pela primeira entidade visível naquela coluna (x,y)
                for (int z_camada = 0; z_camada < alturaMaxAmbiente; z_camada++) {
                     if (mapa[x][y][z_camada] != TipoEntidade.VAZIO) {
                        // Tenta pegar a Entidade objeto para usar sua representação específica, se disponível
                        Entidade e = null;
                        try {
                           e = getEntidadeNaPosicao(x,y,z_camada);
                        } catch (ForaDosLimitesException ex) { /* não deve acontecer aqui */ }

                        if (e != null) {
                           representacao = e.getRepresentacao();
                        } else {
                           // Se não há um objeto Entidade específico mas o mapa diz que está ocupado
                           // (ex: parte de um obstáculo maior cujo ponto de referência (getX,Y,Z) não é este)
                           representacao = mapa[x][y][z_camada].getRepresentacao();
                        }
                        entidadeEncontradaNaColuna = true;
                        break; 
                     }
                }
                if (!entidadeEncontradaNaColuna) {
                    // Se percorreu todas as camadas Z e nada foi encontrado, usa o VAZIO do mapa[x][y][0]
                    // (ou qualquer Z, já que todos seriam VAZIO na coluna).
                     representacao = TipoEntidade.VAZIO.getRepresentacao();
                }
                 System.out.printf(" %c ", representacao);
            }
            System.out.println();
        }
        System.out.println("Legenda: R=Robô, X=Obstáculo, .=Vazio (conforme definido em TipoEntidade e Entidade.getRepresentacao())");
        System.out.println("-------------------------------------------------------------------------------");
    }

    /* ################################################################################################################################### */

    /*verificaColisoes percorre todas as entidades no ambiente e verifica se há colisões entre elas ou com obstáculos. A função analisa as posições de cada entidade e determina se estão ocupando o mesmo espaço, emitindo alertas em caso de colisões detectadas. Isso é crucial para garantir a segurança e a integridade das operações no ambiente simulado.*/


    public void verificarColisoes() { 
        // Esta verificação é mais sobre robôs colidindo entre si, ou robô com obstáculo APÓS um movimento mal sucedido
        // ou se duas entidades foram adicionadas no mesmo local por engano (o adicionarEntidade deve prevenir)
        List<Entidade> entidadesCopia = new ArrayList<>(entidades); 
        int colisoesDetectadas = 0;

        for (int i = 0; i < entidadesCopia.size(); i++) {
            Entidade e1 = entidadesCopia.get(i);
            // Robôs não ocupam volumes no mesmo nível de detalhe que obstáculos no mapa.
            // A colisão de robôs é se seus pontos centrais (X,Y,Z) coincidem.
            if (e1 instanceof Obstaculo) continue; // Foco em robôs colidindo

            for (int j = i + 1; j < entidadesCopia.size(); j++) {
                Entidade e2 = entidadesCopia.get(j);
                if (e2 instanceof Obstaculo) continue;

                if (e1.getX() == e2.getX() && e1.getY() == e2.getY() && e1.getZ() == e2.getZ()) {
                    System.out.println("ALERTA DE COLISÃO DIRETA: Robô '" + e1.getNome() + "' e Robô '" + e2.getNome() +
                                       "' estão na mesma posição exata (" + e1.getX() + "," + e1.getY() + "," + e1.getZ() + ")!");
                    if (e1 instanceof Robo) ((Robo)e1).setEstado(EstadoRobo.AVARIADO);
                    if (e2 instanceof Robo) ((Robo)e2).setEstado(EstadoRobo.AVARIADO);
                    colisoesDetectadas++;
                }
            }
            // Verificar colisão de robô com obstáculo (se o robô está "dentro" de um obstáculo)
            if (e1 instanceof Robo) {
                Robo robo = (Robo) e1;
                try {
                    if (mapa[robo.getX()][robo.getY()][robo.getZ()] == TipoEntidade.OBSTACULO) {
                        Entidade obstaculoColidido = getEntidadeNaPosicao(robo.getX(), robo.getY(), robo.getZ());
                        String nomeObstaculo = (obstaculoColidido != null) ? obstaculoColidido.getNome() : "desconhecido";
                        System.out.println("ALERTA DE COLISÃO: Robô '" + robo.getNome() + "' colidiu com Obstáculo '" + nomeObstaculo +
                                           "' na posição (" + robo.getX() + "," + robo.getY() + "," + robo.getZ() + ")!");
                        robo.setEstado(EstadoRobo.AVARIADO);
                        colisoesDetectadas++;
                    }
                } catch (ForaDosLimitesException ex) {
                    // Robô fora dos limites já seria um problema diferente, mas bom tratar.
                     System.out.println("ALERTA: Robô '" + robo.getNome() + "' está fora dos limites do mapa em (" + robo.getX() + "," + robo.getY() + "," + robo.getZ() + ")!");
                     robo.setEstado(EstadoRobo.AVARIADO); // Avariar se fora dos limites
                     colisoesDetectadas++;
                }
            }
        }
        if (colisoesDetectadas == 0) {
            System.out.println("Verificação de colisões concluída. Nenhuma colisão crítica entre robôs ou robôs dentro de obstáculos detectada.");
        } else {
            System.out.println("Verificação de colisões concluída. " + colisoesDetectadas + " situações de colisão/avaria detectadas.");
        }
    }
}