// Enum Tipo Obstaculo

// Enumeração que representa os diferentes tipos de obstáculos que podem ser encontrados em um ambiente


/* TipoObstaculo.java */
public enum TipoObstaculo {
    PAREDE(3, true),      
    ARVORE(5, true),      
    ARBUSTO(1, true),
    CADEIRA(1, true),
    PREDIO(10, true),     
    BURACO(0, true),      // Profundidade, bloqueia passagem "por cima"
    ROCHA(1, true),       
    CARRO(1, true),
    PEDESTRE(1, true),    // Considerado um obstáculo dinâmico, mas aqui é estático
    AGUA(-1, false);      // Não bloqueia passagem aérea, altura variável indica superfície

    private final int alturaPadrao;
    private final boolean bloqueiaPassagem; // [cite: 114] Nome corrigido

    TipoObstaculo(int alturaPadrao, boolean bloqueiaPassagem) {
        this.alturaPadrao = alturaPadrao;
        this.bloqueiaPassagem = bloqueiaPassagem;
    }

    public int getAlturaPadrao() { 
        return alturaPadrao;
    }

    public boolean bloqueiaPassagem() { 
        return bloqueiaPassagem;
    }
}











