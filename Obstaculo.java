// Classe Obstáculo

public class Obstaculo {

    // Atributos para armazenar as propriedades do obstáculo
    private int posicaoX1;
    private int posicaoY1;
    private int altura;
    private int posicaoX2;
    private int posicaoY2;
    final TipoObstaculo tipo;

    public Obstaculo(int x1, int y1, int x2, int y2, int altura, TipoObstaculo tipo) {
        this.posicaoX1 = x1;
        this.posicaoY1 = y1;
        this.posicaoX2 = x2;
        this.posicaoY2 = y2;
        this.tipo = tipo;

        if (altura >= 0) { // Usa a altura passada
            this.altura = altura;
        } else { // Usa a altura padrão
            int alturaPadraoTipo = tipo.getAlturaPadrao();
            if (alturaPadraoTipo >= 0) {
                this.altura = alturaPadraoTipo;
            } else {
                // Se a altura padrão for negativa, define como 0
                this.altura = 0;
            }
        }
    }

    public Obstaculo(int x, int y, TipoObstaculo tipo) {
        this.posicaoX1 = x;
        this.posicaoY1 = y;
        this.posicaoX2 = x;
        this.posicaoY2 = y;
        this.tipo = tipo;

        int alturaPadraoTipo = tipo.getAlturaPadrao();
        if (alturaPadraoTipo >= 0) {
            this.altura = alturaPadraoTipo;
        } else {
            this.altura = 0; // Ou alturaPadraoTipo
        }
    }

    // --- Métodos Getters ---

    public int getPosicaoX1() {
        return posicaoX1;
    }

    public int getPosicaoY1() {
        return posicaoY1;
    }

    public int getPosicaoX2() {
        return posicaoX2;
    }

    public int getPosicaoY2() {
        return posicaoY2;
    }

    public int getAltura() {
        return altura;
    }

    public TipoObstaculo getTipo() {
        return tipo;
    }

    public boolean BloqueiaPassagem() {
        return tipo.BloqueiaPassagem(); // Delega a verificação para o enum TipoObstaculo
    }

    // --- Outros Métodos (Exemplo) ---
    /**
     * Retorna uma representação textual do obstáculo. Útil para debug ou
     * visualização.
     */
    @Override
    public String toString() {
        return "Obstaculo [tipo=" + tipo +
                ", pos1=(" + posicaoX1 + "," + posicaoY1 + ")" +
                ", pos2=(" + posicaoX2 + "," + posicaoY2 + ")" +
                ", altura=" + altura +
                ", bloqueia=" + BloqueiaPassagem() + "]";
    }
}

// talvez adicionar uma saída para entender o que acontece ao debbugar
