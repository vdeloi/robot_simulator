/* TipoEntidade.java */

public enum TipoEntidade {


    // valores para os tipos de entidades

    VAZIO('.', "Espaço Vazio"), 
    ROBO('R', "Robô"),
    OBSTACULO('X', "Obstáculo"),
    DESCONHECIDO('?', "Desconhecido");

    private final String descricao;
    private final char representacao;

    TipoEntidade(char representacao, String descricao) {
        this.representacao = representacao;
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public char getRepresentacao() {
        return representacao;
    }

}