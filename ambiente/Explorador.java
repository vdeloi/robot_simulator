package ambiente;
/**
 * Interface funcional para entidades que podem explorar o ambiente.
 * Define um contrato para a ação de exploração.
 * `@FunctionalInterface` indica que esta interface pode ser implementada usando expressões lambda.
 */
@FunctionalInterface
public interface Explorador {
    /**
     * Realiza uma ação de exploração em uma determinada área do ambiente.
     * O resultado da exploração é retornado como uma string (por exemplo, um relatório).
     *
     * @param ambiente O ambiente a ser explorado.
     * @return Uma string contendo as informações ou resultados da exploração.
     * @throws RoboDesligadoException Se o robô explorador estiver desligado.
     */
    String explorarArea(Ambiente ambiente) throws RoboDesligadoException;
}