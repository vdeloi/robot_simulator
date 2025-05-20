// Interface funcional Carregador

public interface InterCarregador {

    void carregarItem(String item) throws RoboDesligadoException, AcaoNaoPermitidaException;

    void descarregarItem() throws RoboDesligadoException, AcaoNaoPermitidaException;

    String verItensCarregados();
}
