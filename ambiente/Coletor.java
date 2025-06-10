package ambiente;


/**
 * Interface funcional para entidades capazes de coletar recursos do ambiente.
 * Define um contrato para a ação de coleta.
 * `@FunctionalInterface` indica que esta interface pode ser implementada usando expressões lambda.
 *
 * @param <T> O tipo de recurso a ser coletado (não utilizado explicitamente neste exemplo, mas útil para futuras extensões).
 */
@FunctionalInterface
public interface Coletor<T> { // O parâmetro de tipo T poderia ser usado para especificar o tipo de recurso, ex: Coletor<Minerio>
    /**
     * Tenta coletar um recurso em uma posição específica do ambiente.
     *
     * @param ambiente O ambiente de onde o recurso será coletado.
     * @param x        A coordenada X da localização do recurso.
     * @param y        A coordenada Y da localização do recurso.
     * @param z        A coordenada Z da localização do recurso.
     * @return true se o recurso foi coletado com sucesso, false caso contrário.
     * @throws RoboDesligadoException        Se o robô coletor estiver desligado.
     * @throws RecursoInsuficienteException Se não houver recurso suficiente na posição para ser coletado.
     */
    boolean coletarRecurso(Ambiente ambiente, int x, int y, int z) throws RoboDesligadoException, RecursoInsuficienteException;
}