package br.unicamp.ic.mc322.lab05.robos;
// EstadoRobo.java

/**
 * Enumeração que define os possíveis estados de um {@link Robo}.
 * Um robô pode estar LIGADO, DESLIGADO, MOVENDO, EXECUTANDO_TAREFA ou OCIOSO.
 * Estes estados ajudam a controlar o comportamento e as ações permitidas para o robô.
 */
public enum EstadoRobo {
    /** O robô está ativo e pronto para receber comandos ou executar tarefas. */
    LIGADO,
    /** O robô está inativo e não pode executar a maioria das ações. */
    DESLIGADO,
    /** O robô está atualmente em processo de movimentação. */
    MOVENDO,
    /** O robô está engajado na execução de uma tarefa específica. */
    EXECUTANDO_TAREFA,
    /** O robô está ligado, mas não está se movendo nem executando uma tarefa específica no momento. */
    OCIOSO
}