// Sensoreavel.java

import src.br.unicamp.ic.mc322.lab05.ambiente.Ambiente;

/**
 * Interface para entidades que podem ter sensores e acioná-los.
 * Normalmente, será implementada por robôs ou outras entidades ativas
 * que precisam perceber o ambiente ao seu redor.
 */
public interface Sensoreavel {
    /**
     * Aciona os sensores da entidade.
     * A implementação deste método deve iterar sobre os sensores da entidade
     * e chamar o método `monitorar` de cada um, possivelmente imprimindo
     * ou processando os resultados.
     *
     * @param ambiente O {@link Ambiente} que os sensores irão analisar.
     * @throws RoboDesligadoException Se a entidade for um robô e estiver desligada,
     * impedindo o acionamento dos sensores.
     */
    void acionarSensores(Ambiente ambiente) throws RoboDesligadoException;
}