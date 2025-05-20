// Clsse Ambiente

import java.util.ArrayList;
import java.util.List; // 

// Classe que guarda o ambiente da simulação dos robôs
public class Ambiente {

    private final int largura; // X
    private final int profundidade; // Y
    private final int alturaMaxAmbiente; // Z

    // Este atributo armazena os robôs ativos no ambiente
    private ArrayList<Robo> listaDeRobos = new ArrayList<>();
    private ArrayList<Obstaculo> listaDeObstaculos = new ArrayList<>();
    private ArrayList<Entidade> entidades;

    private TipoEntidade[][][] mapa;

    public Ambiente(int largura, int profundidade, int alturaMaxAmbiente) {

        // Inicializa o ambiente com as dimensões especificadas

        this.largura = largura;
        this.profundidade = profundidade;
        this.alturaMaxAmbiente = alturaMaxAmbiente;
        this.entidades = new ArrayList<>();
        this.mapa = new TipoEntidade[largura][profundidade][alturaMaxAmbiente];
        inicializarMapa();
    }

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

    public void adicionarEntidade(Entidade entidade) throws ColisaoException, ForaDosLimitesException { 

        // Adiciona uma entidade ao ambiente, verificando se a posição está dentro dos limites e se não há colisão
        
        if (!dentroDosLimites(entidade.getX(), entidade.getY(), entidade.getZ())) {

            throw new ForaDosLimitesException("Entidade '" + entidade.getNome() + "' fora dos limites do ambiente.");
        }
        if (estaOcupado(entidade.getX(), entidade.getY(), entidade.getZ())) {

            throw new ColisaoException("Posição (" + entidade.getX() + "," + entidade.getY() + "," + entidade.getZ() + ") já está ocupada.");
        }
        this.entidades.add(entidade);
        atualizarMapaPosicao(entidade.getX(), entidade.getY(), entidade.getZ(), entidade.getTipoEntidade());
        System.out.println("Entidade '" + entidade.getNome() + "' adicionada ao ambiente em (" + entidade.getX() + "," + entidade.getY() + "," + entidade.getZ() + ")");
    }

    public void removerEntidade(Entidade entidade) throws EntidadeNaoEncontradaException { 
        // Remove uma entidade do ambiente, atualizando o mapa
        if (this.entidades.remove(entidade)) {

            atualizarMapaPosicao(entidade.getX(), entidade.getY(), entidade.getZ(), TipoEntidade.VAZIO);
            System.out.println("Entidade '" + entidade.getNome() + "' removida do ambiente.");

        } else {

            throw new EntidadeNaoEncontradaException("Entidade '" + entidade.getNome() + "' não encontrada no ambiente.");
        }
    }

    private void atualizarMapaPosicao(int x, int y, int z, TipoEntidade tipo) {

        // Atualiza o mapa na posição (x, y, z) com o tipo de entidade especificado

        if (dentroDosLimitesMapa(x, y, z)) {

            mapa[x][y][z] = tipo;
        }
    }

    public boolean dentroDosLimites(int x, int y, int z) { 
        // Verifica se as coordenadas (x, y, z) estão dentro dos limites do ambiente, método publico, validação externa

        return x >= 0 && x < this.largura &&
               y >= 0 && y < this.profundidade &&
               z >= 0 && z < this.alturaMaxAmbiente;
    }
    
    private boolean dentroDosLimitesMapa(int x, int y, int z) {
        // Verifica se as coordenadas (x, y, z) estão dentro dos limites do mapa tridimensional, método privado, validação interna

        return x >= 0 && x < largura &&
         y >= 0 && y < profundidade &&
          z >= 0 && z < alturaMaxAmbiente;
    }


    public boolean estaOcupado(int x, int y, int z) throws ForaDosLimitesException {

        // Verifica se a posição (x, y, z) está ocupada por uma entidade

        if (!dentroDosLimites(x, y, z)) {
            throw new ForaDosLimitesException("Tentativa de verificar ocupação fora dos limites: (" + x + "," + y + "," + z + ")");
        }
        return mapa[x][y][z] != TipoEntidade.VAZIO;
    }

    public void moverEntidade(Entidade entidade, int novoX, int novoY, int novoZ) throws ColisaoException, ForaDosLimitesException { 


        // Move uma entidade para uma nova posição, verificando se a nova posição está dentro dos limites e se não há colisão

        if (!entidades.contains(entidade)) {

            System.out.println("ERRO: Entidade não está no ambiente.");
            return;
        }
        if (!dentroDosLimites(novoX, novoY, novoZ)) {
            // Se a nova posição está fora dos limites, lança uma exceção

            throw new ForaDosLimitesException("Movimento para (" + novoX + "," + novoY + "," + novoZ + ") está fora dos limites do ambiente.");
        }
        if (estaOcupado(novoX, novoY, novoZ)) {
            // Se a nova posição já está ocupada, verifica qual entidade está lá
             Entidade ocupante = null;

            for(Entidade e : entidades){

                if(e.getX() == novoX && e.getY() == novoY && e.getZ() == novoZ){
                    ocupante = e;
                    break;
                }
            }
            String nomeOcupante = (ocupante != null) ? ocupante.getNome() : "desconhecido";
            throw new ColisaoException("Posição (" + novoX + "," + novoY + "," + novoZ + ") já está ocupada por " + nomeOcupante + " ("+ mapa[novoX][novoY][novoZ].getDescricao() +").");
        }

        int antigoX = entidade.getX();
        int antigoY = entidade.getY();
        int antigoZ = entidade.getZ();

        // Atualiza posição da entidade (contanto que esteja settada para X, Y, Z)
        if (entidade instanceof Robo) {

            Robo robo = (Robo) entidade;
            robo.setPosicaoX(novoX);
            robo.setPosicaoY(novoY);
            robo.setPosicaoZ(novoZ);

        } else if (entidade instanceof Obstaculo) {
            // Se a entidade for um obstáculo, não atualiza a posição diretamente
            System.out.println("Obstáculos não podem ser movidos por este método atualmente.");
            return;
        }


        // Atualiza o mapa
        atualizarMapaPosicao(antigoX, antigoY, antigoZ, TipoEntidade.VAZIO);
        atualizarMapaPosicao(novoX, novoY, novoZ, entidade.getTipoEntidade());

        System.out.println("Entidade '" + entidade.getNome() + "' movida de (" + antigoX + "," + antigoY + "," + antigoZ +
                           ") para (" + novoX + "," + novoY + "," + novoZ + ")");
    }
    
    public Entidade getEntidadeNaPosicao(int x, int y, int z) {
        for (Entidade e : entidades) {
            if (e.getX() == x && e.getY() == y && e.getZ() == z) {
                return e;
            }
        }
        return null;
    }

    public void visualizarAmbiente() { 
        // Método para visualizar o ambiente, mostrando as entidades e obstáculos

        System.out.println("\n--- Visualização do Ambiente (Topo - Camada Z=0) ---");
        System.out.print("  "); // Espaço para os índices Y
        for (int y = 0; y < profundidade; y++) {
            System.out.printf("%2d ", y);
        }
        System.out.println();

        for (int x = 0; x < largura; x++) {
            System.out.printf("%2d ", x); // Índices X
            for (int y = 0; y < profundidade; y++) {
                // Mostra a camada Z=0, ou a primeira entidade encontrada de baixo para cima
                char representacao = TipoEntidade.VAZIO.getRepresentacao();
                for (int z_camada = 0; z_camada < alturaMaxAmbiente; z_camada++) {
                     Entidade e = getEntidadeNaPosicao(x,y,z_camada);
                     if (e != null) {
                        representacao = e.getRepresentacao();
                        break; // Mostra a primeira entidade encontrada na coluna (x,y)
                     } else if (mapa[x][y][z_camada] != TipoEntidade.VAZIO && z_camada == 0){
                        // Se não há entidade específica mas o mapa indica algo (ex: obstáculo sem objeto Entidade)
                        representacao = mapa[x][y][z_camada].getRepresentacao();
                     }
                }
                 System.out.printf(" %c ", representacao);
            }
            System.out.println();
        }
        System.out.println("Legenda: R=Robô, X=Obstáculo, .=Vazio");
        System.out.println("-------------------------------------------------");
    }


    public void verificarColisoes() { // [cite: 163] Adaptado do seu código original
        List<Entidade> entidadesCopia = new ArrayList<>(entidades); // Evitar ConcurrentModificationException

        for (int i = 0; i < entidadesCopia.size(); i++) {
            Entidade e1 = entidadesCopia.get(i);
            for (int j = i + 1; j < entidadesCopia.size(); j++) {
                Entidade e2 = entidadesCopia.get(j);
                if (e1.getX() == e2.getX() && e1.getY() == e2.getY() && e1.getZ() == e2.getZ()) {
                    System.out.println("ALERTA DE COLISÃO: '" + e1.getNome() + "' e '" + e2.getNome() +
                                       "' estão na mesma posição (" + e1.getX() + "," + e1.getY() + "," + e1.getZ() + ")!");
                    // Aqui você poderia adicionar lógica para tratar a colisão,
                    // como avariar os robôs ou parar seus movimentos.
                    if (e1 instanceof Robo) ((Robo)e1).setEstado(EstadoRobo.AVARIADO);
                    if (e2 instanceof Robo) ((Robo)e2).setEstado(EstadoRobo.AVARIADO);
                }
            }
        }
    }
}



    