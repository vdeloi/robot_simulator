// Classe Obstáculo

public class Obstaculo implements Entidade {

    private final String nome; // Adicionado para conformidade com Entidade e melhor identificação
    private int posicaoX1;
    private int posicaoY1;
    private int posicaoZ_base; // Coordenada Z da base do obstáculo
    private final int alturaObstaculo; // Altura própria do obstáculo a partir da base
    private int posicaoX2; // Usado para obstáculos que ocupam área
    private int posicaoY2; // Usado para obstáculos que ocupam área
    private final TipoObstaculo tipo;
    private final char representacao = 'X'; 

    /* ################################################################################################################################### */

    // Construtor para obstáculos pontuais ou com base em um ponto
    public Obstaculo(String nome, int x, int y, int z_base, TipoObstaculo tipo) {
        this.nome = nome;
        this.posicaoX1 = x;
        this.posicaoY1 = y;
        this.posicaoZ_base = z_base;
        this.posicaoX2 = x;
        this.posicaoY2 = y;
        this.tipo = tipo;
        this.alturaObstaculo = (tipo.getAlturaPadrao() >= 0) ? tipo.getAlturaPadrao() : 1; // Altura mínima 1 se padrão for < 0
    }

    /* ################################################################################################################################### */

    // Construtor para obstáculos que ocupam uma área retangular na base
    public Obstaculo(String nome, int x1, int y1, int x2, int y2, int z_base, TipoObstaculo tipo) {
        this.nome = nome;
        this.posicaoX1 = Math.min(x1, x2);
        this.posicaoY1 = Math.min(y1, y2);
        this.posicaoZ_base = z_base;
        this.posicaoX2 = Math.max(x1, x2);
        this.posicaoY2 = Math.max(y1, y2);
        this.tipo = tipo;
        this.alturaObstaculo = (tipo.getAlturaPadrao() >= 0) ? tipo.getAlturaPadrao() : 1;
    }

    /* ################################################################################################################################### */
    // getters

    @Override
    public String getNome() { return this.nome; }

    @Override
    public int getX() { return this.posicaoX1; } // Ponto de referência X

    @Override
    public int getY() { return this.posicaoY1; } // Ponto de referência Y

    @Override
    public int getZ() { return this.posicaoZ_base; } // Ponto de referência Z (base)

    public int getAlturaObstaculo() { return this.alturaObstaculo; }
    public int getPosicaoX2() { return posicaoX2; }
    public int getPosicaoY2() { return posicaoY2; }
    public TipoObstaculo getTipo() { return tipo; }


    @Override
    public TipoEntidade getTipoEntidade() { return TipoEntidade.OBSTACULO; }

    @Override
    public String getDescricao() { 
        return "Obstáculo: " + nome + " do tipo " + tipo.name() + " em (" + posicaoX1 + "," + posicaoY1 + "," + posicaoZ_base + ")";
    }

    @Override
    public char getRepresentacao() { return this.representacao; } 

    /* ################################################################################################################################### */
    
    public boolean bloqueiaPassagem() {
        return tipo.bloqueiaPassagem();
    }

    /* ################################################################################################################################### */

    // Método para verificar se uma coordenada (px, py, pz) está dentro dos limites deste obstáculo
    public boolean contemPonto(int px, int py, int pz) {
        return px >= this.posicaoX1 && px <= this.posicaoX2 &&
               py >= this.posicaoY1 && py <= this.posicaoY2 &&
               pz >= this.posicaoZ_base && pz < (this.posicaoZ_base + this.alturaObstaculo);
    }
}
