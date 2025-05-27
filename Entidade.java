// Entidade.java

/**
 * Interface que define as características básicas de qualquer objeto
 * que pode existir dentro do {@link Ambiente}.
 * Toda entidade deve ter uma posição (x, y, z), um tipo, uma descrição e uma representação visual.
 */
public interface Entidade {
    /**
     * Retorna a coordenada X da entidade no ambiente.
     * @return A coordenada X.
     */
    int getX();

    /**
     * Retorna a coordenada Y da entidade no ambiente.
     * @return A coordenada Y.
     */
    int getY();

    /**
     * Retorna a coordenada Z da entidade no ambiente.
     * @return A coordenada Z.
     */
    int getZ();

    /**
     * Retorna o tipo da entidade (ex: ROBO, OBSTACULO).
     * @return O {@link TipoEntidade}.
     */
    TipoEntidade getTipo();

    /**
     * Retorna uma descrição textual da entidade.
     * Útil para logs e visualização de status.
     * @return Uma string descrevendo a entidade.
     */
    String getDescricao();

    /**
     * Retorna um caractere que representa visualmente a entidade no mapa do ambiente.
     * @return Um char para representação no console.
     */
    char getRepresentacao();
}