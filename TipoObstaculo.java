// TipoObstaculo.java

/**
 * Enumeração que define os diferentes tipos de {@link Obstaculo} que podem existir no ambiente.
 * Cada tipo de obstáculo pode ter uma altura padrão e uma propriedade que indica se bloqueia a passagem.
 */
public enum TipoObstaculo {
    /** Um obstáculo do tipo parede, geralmente vertical e sólido. */
    PAREDE(3, true), // Altura padrão 3, bloqueia passagem
    /** Um obstáculo do tipo árvore, pode ter volume e bloquear passagem. */
    ARVORE(5, true), // Altura padrão 5, bloqueia passagem
    /** Um obstáculo do tipo prédio, geralmente grande e sólido. */
    PREDIO(10, true), // Altura padrão 10, bloqueia passagem
    /** * Um obstáculo do tipo buraco, representa uma ausência de chão em um determinado nível.
     * Assumindo que buraco tem altura 0 (no nível em que está) e bloqueia passagem nesse nível.
     */
    BURACO(0, true), 
    /** Um tipo genérico para outros obstáculos não especificados. */
    OUTRO(-1, false); // Altura padrão indefinida (-1), por padrão não bloqueia passagem (pode ser customizado)

    private final int alturaPadrao;       // Altura padrão sugerida para este tipo de obstáculo.
    private final boolean bloqueiaPassagem; // Indica se este tipo de obstáculo, por padrão, bloqueia a passagem de robôs.

    /**
     * Construtor para os tipos de obstáculo.
     * @param alturaPadrao A altura padrão para este tipo de obstáculo.
     * @param bloqueiaPassagem true se o obstáculo tipicamente bloqueia a passagem, false caso contrário.
     */
    TipoObstaculo(int alturaPadrao, boolean bloqueiaPassagem) {
        this.alturaPadrao = alturaPadrao;
        this.bloqueiaPassagem = bloqueiaPassagem;
    }

    /**
     * Retorna a altura padrão definida para este tipo de obstáculo.
     * @return A altura padrão.
     */
    public int getAlturaPadrao() {
        return alturaPadrao;
    }

    /**
     * Indica se este tipo de obstáculo é considerado como bloqueador de passagem por padrão.
     * @return true se bloqueia a passagem, false caso contrário.
     */
    public boolean isBloqueiaPassagem() {
        return bloqueiaPassagem;
    }
}