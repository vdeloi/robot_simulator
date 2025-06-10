package br.unicamp.ic.mc322.lab05.ambiente;
// TipoEntidade.java

/**
 * Enumeração que define os tipos básicos de entidades que podem ocupar
 * uma célula no mapa do {@link Ambiente}.
 * Isso ajuda o ambiente a rastrear de forma geral o que está em cada coordenada.
 */
public enum TipoEntidade {
    /** Representa uma célula vazia no ambiente, sem nenhuma entidade ocupando-a. */
    VAZIO,
    /** Representa uma célula ocupada por um {@link Robo}. */
    ROBO,
    /** Representa uma célula ocupada por um {@link Obstaculo}. */
    OBSTACULO,
    /** * Representa um tipo de entidade desconhecido ou não especificado.
     * Pode ser útil para extensões futuras ou para lidar com estados imprevistos.
     */
    DESCONHECIDO // Pode adicionar mais tipos se necessário
}