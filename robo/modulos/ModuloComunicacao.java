package robo.modulos; // Crie este novo pacote

import ambiente.ErroComunicacaoException;
import ambiente.RoboDesligadoException;
import comunicacao.CentralComunicacao;
import comunicacao.Comunicavel;
import robo.Robo;
import robo.EstadoRobo;

public class ModuloComunicacao {
    
    private Robo robo;
    private CentralComunicacao central;

    public ModuloComunicacao(Robo robo, CentralComunicacao central) {
        this.robo = robo;
        this.central = central;
    }

    public void enviarMensagem(Comunicavel destinatario, String mensagem) throws RoboDesligadoException, ErroComunicacaoException {
        if (robo.getEstado() == robo.EstadoRobo.DESLIGADO) throw new RoboDesligadoException(robo.getId() + " desligado.");
        if (!(destinatario instanceof Robo)) throw new ErroComunicacaoException("Destinatário inválido.");
        
        Robo roboDestinatario = (Robo) destinatario;
        if (roboDestinatario.getEstado() == robo.EstadoRobo.DESLIGADO) throw new ErroComunicacaoException("Destinatário " + roboDestinatario.getId() + " está desligado.");

        System.out.println(robo.getId() + " (via Módulo) enviando para " + roboDestinatario.getId() + ": " + mensagem);
        central.registrarMensagem(robo.getId(), roboDestinatario.getId(), mensagem);
        destinatario.receberMensagem(robo.getId(), mensagem);
    }
}