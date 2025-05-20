/* RoboAereo.java */

public class RoboAereo extends Robo {
    protected int altitudeAtual; // Redundante com posicaoZ, mas mantendo para compatibilidade de lógica
    protected int altitudeMaximaVoo;

    public RoboAereo(String id, String nome, int posicaoX, int posicaoY, int altitudeInicial, String direcao, int altitudeMaximaVoo) {
        super(id, nome, posicaoX, posicaoY, altitudeInicial, direcao);
        this.altitudeMaximaVoo = altitudeMaximaVoo;
        this.altitudeAtual = altitudeInicial; // Garante que posicaoZ e altitudeAtual estejam sincronizados
        if (altitudeInicial > altitudeMaximaVoo) {
            System.out.println("AVISO: Altitude inicial ("+altitudeInicial+") do " + nome + " excede a máxima ("+altitudeMaximaVoo+"). Ajustando para máxima.");
            this.posicaoZ = altitudeMaximaVoo;
            this.altitudeAtual = altitudeMaximaVoo;
        }
        if (altitudeInicial < 0) {
             System.out.println("AVISO: Altitude inicial ("+altitudeInicial+") do " + nome + " é negativa. Ajustando para 0.");
            this.posicaoZ = 0;
            this.altitudeAtual = 0;
        }
    }
    
    public int getAltitudeAtual() { return this.posicaoZ; } // Usa posicaoZ diretamente
    public int getAltitudeMaximaVoo() { return altitudeMaximaVoo; }

    public void subir(int metros, Ambiente ambiente) throws RoboDesligadoException, ForaDosLimitesException, ColisaoException, AcaoNaoPermitidaException {
        if (this.estado == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(nome + " está desligado.");
        if (this.estado == EstadoRobo.AVARIADO) throw new AcaoNaoPermitidaException(nome + " está avariado.");
        
        int novaAltitude = this.posicaoZ + metros;
        if (novaAltitude > this.altitudeMaximaVoo) {
            novaAltitude = this.altitudeMaximaVoo;
            System.out.println(nome + " atingiu a altitude máxima de voo (" + this.altitudeMaximaVoo + "m).");
        }
        if (novaAltitude >= ambiente.getAlturaMaxAmbiente()) {
             novaAltitude = ambiente.getAlturaMaxAmbiente() - 1;
             System.out.println(nome + " atingiu a altitude máxima do ambiente (" + (ambiente.getAlturaMaxAmbiente()-1) + "m).");
        }
        if (novaAltitude < 0) novaAltitude = 0; // Não pode ir abaixo do solo
        
        super.moverPara(this.posicaoX, this.posicaoY, novaAltitude, ambiente);
        this.altitudeAtual = this.posicaoZ; // Sincroniza após mover
    }

    public void descer(int metros, Ambiente ambiente) throws RoboDesligadoException, ForaDosLimitesException, ColisaoException, AcaoNaoPermitidaException {
        if (this.estado == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(nome + " está desligado.");
        if (this.estado == EstadoRobo.AVARIADO) throw new AcaoNaoPermitidaException(nome + " está avariado.");

        int novaAltitude = this.posicaoZ - metros;
        if (novaAltitude < 0) {
            novaAltitude = 0;
            System.out.println(nome + " atingiu o solo (0m).");
        }
        super.moverPara(this.posicaoX, this.posicaoY, novaAltitude, ambiente);
        this.altitudeAtual = this.posicaoZ; // Sincroniza
    }
    
    @Override
    public void moverPara(int novoX, int novoY, int novoZ, Ambiente ambiente) throws RoboDesligadoException, ColisaoException, ForaDosLimitesException, AcaoNaoPermitidaException {
        if (this.estado == EstadoRobo.DESLIGADO) {
            throw new RoboDesligadoException("Robô " + nome + " está desligado.");
        }
        if (this.estado == EstadoRobo.AVARIADO) {
            throw new AcaoNaoPermitidaException("Robô " + nome + " está avariado.");
        }
        if (novoZ > this.altitudeMaximaVoo) {
            throw new AcaoNaoPermitidaException("Movimento para altitude " + novoZ + " excede a altitude máxima de voo ("+ this.altitudeMaximaVoo +") do robô " + nome);
        }
         if (novoZ >= ambiente.getAlturaMaxAmbiente()) {
            throw new ForaDosLimitesException("Movimento para altitude " + novoZ + " excede a altura máxima do ambiente ("+ (ambiente.getAlturaMaxAmbiente()-1) +").");
        }
        if (novoZ < 0) {
            throw new AcaoNaoPermitidaException("Robô aéreo " + nome + " não pode ter altitude negativa.");
        }
        
        super.moverPara(novoX, novoY, novoZ, ambiente);
        this.altitudeAtual = this.posicaoZ; // Sincroniza
    }


    @Override
    public String executarTarefa(Ambiente ambiente, CentralComunicacao central, String[] args) throws RoboDesligadoException, AcaoNaoPermitidaException {
        if (this.estado == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(nome + " está desligado.");
         if (this.estado == EstadoRobo.AVARIADO) throw new AcaoNaoPermitidaException(nome + " está avariado.");
        this.estado = EstadoRobo.EXECUTANDO_TAREFA;
        String resultado = nome + " (Aéreo) está sobrevoando a área.";
        // Lógica de sobrevoo aqui...
        this.estado = EstadoRobo.EM_ESPERA;
        return resultado;
    }
}