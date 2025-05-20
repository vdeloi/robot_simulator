/* RoboDroneDeVigilancia.java */
public class RoboDroneDeVigilancia extends RoboAereo implements Sensoreavel, Comunicavel, Explorador {
    private int qualidadeDaCamera;
    private int framesPorSegundo;
    private int duracaoAtualVideo = 0;
    private final int duracaoMaximaVideo;
    private boolean gravando = false;

    public RoboDroneDeVigilancia(String id, String nome, int posicaoX, int posicaoY, int altitude, String direcao, int altitudeMaximaVoo,
                                 int qualidadeDaCamera, int framesPorSegundo, int duracaoMaximaVideo) {
        super(id, nome, posicaoX, posicaoY, altitude, direcao, altitudeMaximaVoo);
        this.qualidadeDaCamera = qualidadeDaCamera;
        this.framesPorSegundo = framesPorSegundo;
        this.duracaoMaximaVideo = duracaoMaximaVideo;
    }

    // Getters específicos
    public int getQualidadeDaCamera() { return qualidadeDaCamera; }
    public int getFramesPorSegundo() { return framesPorSegundo; }
    public int getDuracaoAtualVideo() { return duracaoAtualVideo; }
    public int getDuracaoMaximaVideo() { return duracaoMaximaVideo; }
    public boolean isGravando() { return gravando; }

    public String iniciarGravacao() throws RoboDesligadoException, AcaoNaoPermitidaException {
        if (this.estado == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(nome + " está desligado.");
        if (this.estado == EstadoRobo.AVARIADO) throw new AcaoNaoPermitidaException(nome + " está avariado.");
        if (gravando) return nome + " já está gravando.";
        if (duracaoAtualVideo >= duracaoMaximaVideo) return "ERRO: Duração máxima do vídeo atingida para " + nome + ".";
        
        gravando = true;
        this.estado = EstadoRobo.EXECUTANDO_TAREFA;
        return nome + " iniciando gravação com " + qualidadeDaCamera + "MP, " + framesPorSegundo + "fps.";
    }

    public String pararGravacao() throws RoboDesligadoException {
        if (this.estado == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(nome + " está desligado.");
        // Não impede se avariado, pois pode ser necessário parar a gravação
        if (!gravando) return nome + " não estava gravando.";

        gravando = false;
        String msg = nome + " parando gravação. Duração total: " + duracaoAtualVideo + "s.";
        duracaoAtualVideo = 0; // Reseta para a próxima
        if(this.estado == EstadoRobo.EXECUTANDO_TAREFA) this.estado = EstadoRobo.EM_ESPERA;
        return msg;
    }
    
    // Simula a passagem do tempo de gravação
    public void simularTempoGravacao(int segundos) {
        if (gravando && this.estado != EstadoRobo.DESLIGADO && this.estado != EstadoRobo.AVARIADO) {
            duracaoAtualVideo += segundos;
            if (duracaoAtualVideo >= duracaoMaximaVideo) {
                duracaoAtualVideo = duracaoMaximaVideo;
                try {
                    System.out.println(pararGravacao() + " (Tempo máximo atingido)");
                } catch (RoboDesligadoException e) { /* já tratado */ }
            }
        }
    }


    @Override
    public String executarTarefa(Ambiente ambiente, CentralComunicacao central, String[] args) throws RoboDesligadoException, AcaoNaoPermitidaException {
        // Tarefa padrão pode ser iniciar/parar gravação ou explorar
        if (args != null && args.length > 0) {
            if (args[0].equalsIgnoreCase("gravar")) return iniciarGravacao();
            if (args[0].equalsIgnoreCase("parar_gravar")) return pararGravacao();
            if (args[0].equalsIgnoreCase("explorar_auto")) {
                 explorarArea(ambiente); // Chama o método da interface Explorador
                 return nome + " concluindo exploração automática.";
            }
        }
        return nome + " (Vigilância) aguardando comando específico de tarefa (gravar, parar_gravar, explorar_auto).";
    }

    // Implementação de Sensoreavel
    @Override
    public String acionarSensores(Ambiente ambiente) throws RoboDesligadoException { // [cite: 175]
        if (this.estado == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(nome + " (Vigilância) está desligado.");
        // Um drone de vigilância poderia ter sensores de proximidade, além dos da classe base
        String sensoresBase = super.ativarSensoresRobo(ambiente); // Chama os sensores genéricos do Robo
        return sensoresBase + "\n - Sensor da Câmera: Ativo, Qualidade " + qualidadeDaCamera + "MP.";
    }

    // Implementação de Comunicavel
    @Override
    public void enviarMensagem(Comunicavel destinatario, String mensagem, CentralComunicacao central) throws RoboDesligadoException, ErroComunicacaoException { // [cite: 175]
        if (this.estado == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(nome + " (Vigilância) está desligado para enviar msg.");
        if (this.estado == EstadoRobo.AVARIADO) throw new ErroComunicacaoException(nome + " (Vigilância) está avariado demais para comunicar.");
        if (destinatario == null) throw new ErroComunicacaoException("Destinatário nulo.");

        System.out.println(nome + " enviando para " + destinatario.getIdComunicacao() + ": '" + mensagem + "'");
        central.registrarMensagem(this.getIdComunicacao(), destinatario.getIdComunicacao(), mensagem);
        destinatario.receberMensagem(this.getIdComunicacao(), mensagem);
    }

    @Override
    public void receberMensagem(String remetente, String mensagem) throws RoboDesligadoException { // [cite: 176]
         if (this.estado == EstadoRobo.DESLIGADO && this.estado != EstadoRobo.AVARIADO) {
            // Se estiver apenas desligado, poderia armazenar a mensagem para ler depois, mas o lab pede exceção
             throw new RoboDesligadoException(nome + " (Vigilância) está desligado para receber msg.");
         }
        System.out.println(nome + " (Vigilância) recebeu de " + remetente + ": '" + mensagem + "'");
    }
    
    @Override
    public String getIdComunicacao() { return "DroneVigilancia-" + this.getId(); }


    // Implementação de Explorador
    @Override
    public void explorarArea(Ambiente ambiente) throws RoboDesligadoException, AcaoNaoPermitidaException { // [cite: 186]
        if (this.estado == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(nome + " está desligado.");
        if (this.estado == EstadoRobo.AVARIADO) throw new AcaoNaoPermitidaException(nome + " está avariado.");
        
        System.out.println(nome + " iniciando exploração da área...");
        this.estado = EstadoRobo.EXECUTANDO_TAREFA;
        // Lógica de exploração: mover-se para alguns pontos aleatórios dentro de um raio, por exemplo.
        // Por simplicidade, apenas simula.
        try {
            Thread.sleep(1000); // Simula tempo de exploração
            System.out.println(nome + " reportando: Área XYZ verificada. Nenhum incidente.");
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        this.estado = EstadoRobo.EM_ESPERA;
        System.out.println(nome + " concluiu a exploração.");
    }
}
