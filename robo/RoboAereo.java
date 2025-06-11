package robo;

import ambiente.*;
import missao.Missao; // Import necessário
import sensores.Sensor;
import sensores.Sensoreavel;

/**
 * Representa um robô aéreo genérico.
 * Pode se mover no espaço 3D até uma altitude máxima.
 * Implementa {@link Sensoreavel} para interagir com sensores.
 * CORREÇÃO: Herda de AgenteInteligente para que seus subtipos possam usar missões.
 */
public class RoboAereo extends AgenteInteligente implements Sensoreavel { // CORRIGIDO: Herda de AgenteInteligente
    protected final int altitudeMaxima;
    protected int numHelices;

    public RoboAereo(String id, int x, int y, int z, String direcao, int altitudeMaxima, int numHelices) {
        super(id, x, y, z, direcao); // Chama o construtor do AgenteInteligente (que chama o de Robo)
        this.altitudeMaxima = Math.max(0, altitudeMaxima);
        this.numHelices = numHelices;

        // Inicializa o módulo de movimento específico para robôs aéreos
        this.controleMovimento = new robo.modulos.ControleMovimentoAereo(this);

        if (z > this.altitudeMaxima) {
             System.out.println("Aviso: Alt inicial ("+z+") do RoboAereo "+id+" excede max ("+this.altitudeMaxima+"). Ajustando.");
             this.z = this.altitudeMaxima;
        }
        if (z < 0) {
             System.out.println("Aviso: Alt inicial ("+z+") do RoboAereo "+id+" negativa. Ajustando para 0.");
             this.z = 0;
        }
    }
    
    /**
     * Faz o robô aéreo subir uma determinada quantidade de metros.
     * CORREÇÃO: Adicionado o parâmetro 'ambiente'.
     */
    public void subir(Ambiente ambiente, int metros) throws RoboDesligadoException, AcaoNaoPermitidaException, ColisaoException, ForaDosLimitesException {
        if (getEstado() == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(getId() + " desligado.");
        if (metros <=0) throw new AcaoNaoPermitidaException("Metros para subir deve ser positivo.");
        super.moverRelativamente(ambiente, 0, 0, metros); 
    }

    /**
     * Faz o robô aéreo descer uma determinada quantidade de metros.
     * CORREÇÃO: Adicionado o parâmetro 'ambiente'.
     */
    public void descer(Ambiente ambiente, int metros) throws RoboDesligadoException, AcaoNaoPermitidaException, ColisaoException, ForaDosLimitesException {
        if (getEstado() == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(getId() + " desligado.");
        if (metros <=0) throw new AcaoNaoPermitidaException("Metros para descer deve ser positivo.");
        super.moverRelativamente(ambiente, 0, 0, -metros);
    }

    @Override
    public void moverRelativamente(Ambiente ambiente, int dx, int dy, int dz) throws ColisaoException, ForaDosLimitesException, RoboDesligadoException, AcaoNaoPermitidaException {
        if (this.getEstado() == EstadoRobo.DESLIGADO) {
            throw new RoboDesligadoException("Robô " + getId() + " está desligado.");
        }
        int futuroZ = this.getZ() + dz;
        if (futuroZ > altitudeMaxima) {
            throw new AcaoNaoPermitidaException(getId() + " não pode se mover para Z=" + futuroZ + " (acima da alt max: "+altitudeMaxima+").");
        }
        if (futuroZ < 0) {
            throw new AcaoNaoPermitidaException(getId() + " não pode se mover para Z=" + futuroZ + " (abaixo de 0).");
        }
        super.moverRelativamente(ambiente, dx, dy, dz); 
    }

    @Override
    public void executarTarefa(Ambiente ambiente) throws RoboDesligadoException, AcaoNaoPermitidaException, ColisaoException, ForaDosLimitesException, RecursoInsuficienteException, ErroComunicacaoException {
        if (getEstado() == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(getId() + " desligado.");
        System.out.println("Robô Aéreo " + getId() + " está realizando patrulha aérea em Z=" + getZ() + ".");
        
        int dx_patrulha = 0, dy_patrulha = 0;
        switch (getDirecao().toUpperCase()) {
            case "NORTE": dy_patrulha = 1; break;
            case "SUL": dy_patrulha = -1; break;
            case "LESTE": dx_patrulha = 1; break;
            case "OESTE": dx_patrulha = -1; break;
        }
        if (dx_patrulha != 0 || dy_patrulha != 0) {
             System.out.println(getId() + " (Aereo) tentando mover 1 passo para " + getDirecao());
             moverRelativamente(ambiente, dx_patrulha, dy_patrulha, 0);
        }
        setDirecao("SUL");
    }

    @Override
    public void acionarSensores(Ambiente ambiente) throws RoboDesligadoException {
        if (getEstado() == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(getId() + " desligado.");
        System.out.println("\n--- Sensores do Robô Aéreo " + getId() + " ---");
        if (getSensores().isEmpty()) {
            System.out.println(getId() + " não possui sensores."); return;
        }
        for (Sensor s : getSensores()) {
            System.out.println(s.monitorar(ambiente, this));
        }
    }

    /**
     * NOVO: Implementação do método abstrato de AgenteInteligente.
     * Permite que o robô execute sua missão.
     * @param ambiente O ambiente de execução.
     */
    @Override
    public void executarMissao(Ambiente ambiente) {
        if (temMissao()) {
            missao.executar(this, ambiente);
        } else {
            System.out.println(getId() + " não possui missão para executar. Executando tarefa padrão.");
            try {
                executarTarefa(ambiente); // Executa a tarefa normal se não tiver missão
            } catch (Exception e) {
                System.err.println("Falha ao executar tarefa padrão como missão para " + getId() + ": " + e.getMessage());
            }
        }
    }

    @Override
    public char getRepresentacao() { return 'V';} 
}