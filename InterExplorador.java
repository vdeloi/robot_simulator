// Interface funcional para o explorador

public interface InterExplorador {
    
    String explorarArea(Ambiente ambiente) throws RoboDesligadoException, AcaoNaoPermitidaException;
}