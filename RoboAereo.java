/* RoboAereo.java */

public class RoboAereo extends Robo {

    protected int altitudeMaximaVoo;

    /* ################################################################################################################################### */
    // Construtor

    public RoboAereo(String id, String nome, int posicaoX, int posicaoY, int altitudeInicial, String direcao, int altitudeMaximaVoo) {
        super(id, nome, posicaoX, posicaoY, altitudeInicial, direcao);
        this.altitudeMaximaVoo = altitudeMaximaVoo;
        
        if (this.posicaoZ > this.altitudeMaximaVoo) {
            System.out.println("AVISO: Altitude inicial ("+this.posicaoZ+") do " + nome + " excede a máxima de voo ("+this.altitudeMaximaVoo+"). Ajustando para máxima.");
            this.posicaoZ = this.altitudeMaximaVoo;
        }
        if (this.posicaoZ < 0) {
             System.out.println("AVISO: Altitude inicial ("+this.posicaoZ+") do " + nome + " é negativa. Ajustando para 0.");
            this.posicaoZ = 0;
        }
    }
    /* ################################################################################################################################### */
    
    public int getAltitudeAtual() { return this.posicaoZ; }
    public int getAltitudeMaximaVoo() { return altitudeMaximaVoo; }

    /* ################################################################################################################################### */

    /* permite que o robô aéreo aumente sua altitude em uma quantidade especificada, verificando se o robô está ligado e em bom estado. Ele ajusta a altitude para respeitar os limites do ambiente e a altitude máxima de voo, tratando exceções como colisões, limites excedidos ou ações não permitidas. */

    public void subir(int metros, Ambiente ambiente) throws RoboDesligadoException, ForaDosLimitesException, ColisaoException, AcaoNaoPermitidaException {
        if (this.estado == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(nome + " está desligado.");
        if (this.estado == EstadoRobo.AVARIADO) throw new AcaoNaoPermitidaException(nome + " está avariado.");
        if (metros <= 0) {
            System.out.println(nome + ": valor para subir deve ser positivo.");
            return;
        }
        
        
        int novaAltitude = this.posicaoZ + metros;
        if (novaAltitude > this.altitudeMaximaVoo) {
            System.out.println(nome + " tentou subir para " + novaAltitude + "m, mas atingiu a altitude máxima de voo (" + this.altitudeMaximaVoo + "m). Ajustando.");
            novaAltitude = this.altitudeMaximaVoo;
        }
        if (novaAltitude >= ambiente.getAlturaMaxAmbiente()) { // Verifica contra o limite do ambiente
             System.out.println(nome + " tentou subir para " + novaAltitude + "m, mas atingiu a altitude máxima do ambiente (" + (ambiente.getAlturaMaxAmbiente()-1) + "m). Ajustando.");
             novaAltitude = ambiente.getAlturaMaxAmbiente() - 1;
        }
        
        super.moverPara(this.posicaoX, this.posicaoY, novaAltitude, ambiente);
        System.out.println(nome + " subiu para a altitude " + this.posicaoZ + "m.");
    }

    /* ################################################################################################################################### */

    /* permite que o robô aéreo diminua sua altitude em uma quantidade especificada, verificando se o robô está ligado e em bom estado. Ele ajusta a altitude para não ficar abaixo de zero, tratando exceções como colisões, limites excedidos ou ações não permitidas. */

    public void descer(int metros, Ambiente ambiente) throws RoboDesligadoException, ForaDosLimitesException, ColisaoException, AcaoNaoPermitidaException {
        if (this.estado == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(nome + " está desligado.");
        if (this.estado == EstadoRobo.AVARIADO) throw new AcaoNaoPermitidaException(nome + " está avariado.");
        if (metros <= 0) {
            System.out.println(nome + ": valor para descer deve ser positivo.");
            return;
        }

        int novaAltitude = this.posicaoZ - metros;
        if (novaAltitude < 0) {
            System.out.println(nome + " tentou descer para " + novaAltitude + "m, mas atingiu o solo (0m). Ajustando.");
            novaAltitude = 0;
        }
        super.moverPara(this.posicaoX, this.posicaoY, novaAltitude, ambiente);
        System.out.println(nome + " desceu para a altitude " + this.posicaoZ + "m.");
    }

    /* ################################################################################################################################### */

    /* permite que o robô aéreo mude sua posição em um ambiente tridimensional, verificando se o robô está ligado e em bom estado. Ele ajusta a altitude para respeitar os limites do ambiente e a altitude máxima de voo, tratando exceções como colisões, limites excedidos ou ações não permitidas. */
    
    @Override
    public void moverPara(int novoX, int novoY, int novoZ, Ambiente ambiente) throws RoboDesligadoException, ColisaoException, ForaDosLimitesException, AcaoNaoPermitidaException {
        if (this.estado == EstadoRobo.DESLIGADO) {
            throw new RoboDesligadoException("Robô " + nome + " está desligado.");
        }
        if (this.estado == EstadoRobo.AVARIADO) {
            throw new AcaoNaoPermitidaException("Robô " + nome + " está avariado.");
        }
        if (novoZ > this.altitudeMaximaVoo) {
            throw new AcaoNaoPermitidaException("Movimento para altitude " + novoZ + "m excede a altitude máxima de voo ("+ this.altitudeMaximaVoo +"m) do robô " + nome + ".");
        }
        if (novoZ >= ambiente.getAlturaMaxAmbiente()) { // Deve ser menor que a altura máxima do ambiente
            throw new ForaDosLimitesException("Movimento para altitude " + novoZ + "m excede ou iguala a altura máxima do ambiente ("+ ambiente.getAlturaMaxAmbiente() +"m).");
        }
        if (novoZ < 0) {
            throw new AcaoNaoPermitidaException("Robô aéreo " + nome + " não pode ter altitude negativa. Tentativa: " + novoZ + "m.");
        }
        
        super.moverPara(novoX, novoY, novoZ, ambiente);
    }

    /* ################################################################################################################################### */

    /* permite que o robô aéreo execute uma tarefa de sobrevoo, verificando se o robô está ligado e em bom estado. Ele simula o sobrevoo com mensagens de status e atualiza o estado do robô após a conclusão. */

    @Override
    public String executarTarefa(Ambiente ambiente, CentralComunicacao central, String[] args) throws RoboDesligadoException, AcaoNaoPermitidaException {
        if (this.estado == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(nome + " está desligado.");
        if (this.estado == EstadoRobo.AVARIADO) throw new AcaoNaoPermitidaException(nome + " está avariado.");
        
        this.estado = EstadoRobo.EXECUTANDO_TAREFA;
        StringBuilder resultado = new StringBuilder();
        resultado.append(nome).append(" (Aéreo) iniciando sobrevoo...");
        try {
            Thread.sleep(500);
            resultado.append("\n").append(nome).append(" está sobrevoando a área em torno de ").append(exibirPosicao()).append(" a ").append(this.posicaoZ).append("m de altitude.");
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            resultado.append("\nSobrevoo de ").append(nome).append(" interrompido.");
        }
        this.estado = EstadoRobo.EM_ESPERA;
        resultado.append("\n").append(nome).append(" concluiu o sobrevoo e está em espera.");
        return resultado.toString();
    }
}