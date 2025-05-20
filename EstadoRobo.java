// Enumeração EstadoRobo

public enum EstadoRobo {

    PARADO("Parado"),
    MOVIMENTANDO("Movimentando-se"),
    LIGADO("Ligado"),
    DESLIGADO("Desligado"),
    EXECUTANDO_TAREFA("Executando Tarefa"),
    EM_ESPERA("Em Espera"),
    AVARIADO("Avariado");

    private final String descricao;

    EstadoRobo(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
