// Autonomo.java

/**
 * Interface funcional para entidades que podem executar ações de forma autônoma.
 * Uma entidade autônoma deve ser capaz de decidir e executar sua próxima ação
 * com base no estado do ambiente.
 * `@FunctionalInterface` indica que esta interface destina-se a ser usada com expressões lambda.
 */
@FunctionalInterface
public interface Autonomo {
    /**
     * Executa a próxima ação autônoma da entidade no ambiente fornecido.
     *
     * @param ambiente O ambiente no qual a ação será executada.
     * @throws RoboDesligadoException Se o robô estiver desligado e tentar executar uma ação.
     * @throws AcaoNaoPermitidaException Se a ação autônoma não for permitida.
     * @throws ColisaoException Se a ação autônoma resultar em uma colisão.
     * @throws ForaDosLimitesException Se a ação autônoma tentar mover a entidade para fora dos limites.
     */
    void executarProximaAcaoAutonoma(Ambiente ambiente) throws RoboDesligadoException, AcaoNaoPermitidaException, ColisaoException, ForaDosLimitesException;
}