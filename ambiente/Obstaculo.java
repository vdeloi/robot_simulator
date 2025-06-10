package ambiente; 


// Obstaculo.java

/**
 * Representa um obstáculo fixo no ambiente.
 * Obstáculos são definidos por um par de coordenadas (x1,y1,z1) e (x2,y2,z2)
 * que formam uma caixa delimitadora (bounding box) tridimensional, ou por uma base 2D,
 * um nível Z base e uma altura.
 * Implementa a interface {@link Entidade}.
 */
public class Obstaculo implements Entidade {
    // Coordenadas que definem o volume do obstáculo (mínimas e máximas)
    private final int x1, y1, z1; // Canto inferior-esquerdo-frontal
    private final int x2, y2, z2; // Canto superior-direito-traseiro
    private final TipoObstaculo tipoObstaculo; // O tipo do obstáculo (ex: PAREDE, ARVORE)
    private final char representacao; // Caractere para visualização no mapa

    /**
     * Construtor para definir um obstáculo usando uma caixa delimitadora 3D (bounding box).
     * As coordenadas são normalizadas para que (x1,y1,z1) sejam sempre menores ou iguais a (x2,y2,z2).
     *
     * @param x1 Coordenada X do primeiro canto.
     * @param y1 Coordenada Y do primeiro canto.
     * @param z1 Coordenada Z do primeiro canto.
     * @param x2 Coordenada X do segundo canto.
     * @param y2 Coordenada Y do segundo canto.
     * @param z2 Coordenada Z do segundo canto.
     * @param tipo O {@link TipoObstaculo} deste obstáculo.
     */
    public Obstaculo(int x1, int y1, int z1, int x2, int y2, int z2, TipoObstaculo tipo) {
        // Garante que x1 <= x2, y1 <= y2, z1 <= z2
        this.x1 = Math.min(x1, x2);
        this.y1 = Math.min(y1, y2);
        this.z1 = Math.min(z1, z2);
        this.x2 = Math.max(x1, x2);
        this.y2 = Math.max(y1, y2);
        this.z2 = Math.max(z1, z2);
        this.tipoObstaculo = tipo;
        this.representacao = assignRepresentation(tipo); // Define o caractere de representação
    }

    /**
     * Construtor para definir um obstáculo a partir de uma base 2D, um nível Z base e uma altura.
     * Útil para criar obstáculos como paredes ou prédios com uma altura definida a partir do chão.
     *
     * @param baseX1 Coordenada X do primeiro canto da base.
     * @param baseY1 Coordenada Y do primeiro canto da base.
     * @param baseX2 Coordenada X do segundo canto da base.
     * @param baseY2 Coordenada Y do segundo canto da base.
     * @param tipo O {@link TipoObstaculo} deste obstáculo.
     * @param baseZ O nível Z onde a base do obstáculo começa.
     * @param altura A altura do obstáculo a partir da baseZ. Uma altura de 1 significa que z1=z2=baseZ.
     */
    public Obstaculo(int baseX1, int baseY1, int baseX2, int baseY2, TipoObstaculo tipo, int baseZ, int altura) {
        // Garante que baseX1 <= baseX2, baseY1 <= baseY2
        this.x1 = Math.min(baseX1, baseX2);
        this.y1 = Math.min(baseY1, baseY2);
        this.z1 = baseZ; // Z inicial é o nível da base
        this.x2 = Math.max(baseX1, baseX2);
        this.y2 = Math.max(baseY1, baseY2);
        // Calcula z2: se altura é 1, z2 = baseZ. Se altura > 1, z2 = baseZ + altura - 1.
        // Math.max(0, altura - 1) garante que, se altura for 0 ou 1, o resultado para o delta seja 0.
        this.z2 = baseZ + Math.max(0, altura - 1);
        this.tipoObstaculo = tipo;
        this.representacao = assignRepresentation(tipo); // Define o caractere de representação
    }

    /**
     * Atribui um caractere de representação visual com base no tipo do obstáculo.
     * @param tipo O {@link TipoObstaculo}.
     * @return O caractere correspondente.
     */
    private char assignRepresentation(TipoObstaculo tipo) {
        switch (tipo) {
            case PAREDE: return '#';
            case ARVORE: return 'A';
            case PREDIO: return 'P';
            case BURACO: return 'B';
            default: return 'O'; // 'O' para Outro tipo de obstáculo
        }
    }

    // Getters para as coordenadas delimitadoras do obstáculo
    public int getX1() { return x1; }
    public int getY1() { return y1; }
    public int getZ1() { return z1; }
    public int getX2() { return x2; }
    public int getY2() { return y2; }
    public int getZ2() { return z2; }
    public TipoObstaculo getTipoObstaculo() { return tipoObstaculo; }

    /**
     * Retorna a coordenada X do ponto de referência primário do obstáculo (x1).
     * @return A coordenada X1.
     */
    @Override
    public int getX() { return x1; } // Ponto de referência primário

    /**
     * Retorna a coordenada Y do ponto de referência primário do obstáculo (y1).
     * @return A coordenada Y1.
     */
    @Override
    public int getY() { return y1; } // Ponto de referência primário

    /**
     * Retorna a coordenada Z do ponto de referência primário do obstáculo (z1).
     * @return A coordenada Z1.
     */
    @Override
    public int getZ() { return z1; } // Ponto de referência primário

    /**
     * Retorna o tipo da entidade, que para Obstaculo é sempre {@link TipoEntidade#OBSTACULO}.
     * @return {@link TipoEntidade#OBSTACULO}.
     */
    @Override
    public TipoEntidade getTipo() { return TipoEntidade.OBSTACULO; }

    /**
     * Retorna uma descrição textual do obstáculo, incluindo seu tipo e coordenadas.
     * @return Uma string descritiva.
     */
    @Override
    public String getDescricao() {
        return "Obstaculo: " + tipoObstaculo + " de (" + x1 + "," + y1 + "," + z1 + ") a (" + x2 + "," + y2 + "," + z2 + ")";
    }

    /**
     * Retorna o caractere de representação visual do obstáculo.
     * @return O caractere definido no construtor.
     */
    @Override
    public char getRepresentacao() {
        return this.representacao;
    }

    /**
     * Sobrescreve o método toString para retornar a descrição do obstáculo.
     * @return A descrição do obstáculo.
     */
    @Override
    public String toString() {
        return getDescricao();
    }
}