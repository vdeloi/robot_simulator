// Interface para Entidade

public interface Entidade {

    int getX();

    int getY();

    int getZ();

    TipoEntidade getTipoEntidade();

    String getDescricao();

    char getRepresentacao();

    String getNome(); // adicionado para identificação
}