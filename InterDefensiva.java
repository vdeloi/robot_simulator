/* Defensivel.java */
public interface InterDefensiva {

    // Métodos para ativar e desativar a defesa

    String ativarDefesa() throws RoboDesligadoException, AcaoNaoPermitidaException;

    String desativarDefesa() throws RoboDesligadoException, AcaoNaoPermitidaException;

}