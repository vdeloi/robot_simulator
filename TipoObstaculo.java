// Enum Tipo Obstaculo

// Enumeração que representa os diferentes tipos de obstáculos que podem ser encontrados em um ambiente


public enum TipoObstaculo {

    // Define os tipos de obstáculos como constantes
   
    PAREDE(3, true),      
    ARVORE(5, true),      
    ARBUSTO(1, true),
    CADEIRA(1, true),
    PREDIO(10, true),     
    BURACO(0, true),      
    ROCHA(1, true),       
    CARRO(1, true),
    PEDESTRE(1, true),
    AGUA(-1, false);      // Não bloqueia, tem altura variavel

    // Atributos privados da enumeração
    private final int alturaPadrao;        // Armazena a altura do obstáculo
    private final boolean bloqueiaPassagem; // Indica se o obstáculo bloqueia a passagem ou não


    TipoObstaculo(int alturaPadrao, boolean bloqueiaPassagem) {
        this.alturaPadrao = alturaPadrao;
        this.bloqueiaPassagem = bloqueiaPassagem;
    }

    // Métodos públicos para acessar os atributos privados
    public int getAlturaPadrao() {
        return alturaPadrao;
    }

    public boolean isBloqueiaPassagem() {
        return bloqueiaPassagem;
    }
}












