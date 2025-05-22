/* RoboDroneDeVigilancia.java */


public class RoboDroneDeVigilancia extends RoboAereo implements Sensoreavel, Comunicavel, InterExplorador {

    /* ################################################################################################################################### */

    private int qualidadeDaCamera;
    private int framesPorSegundo;
    private int duracaoAtualVideo = 0;
    private final int duracaoMaximaVideo;
    private boolean gravando = false;

    /* ################################################################################################################################### */


    public RoboDroneDeVigilancia(String id, String nome, int posicaoX, int posicaoY, int altitude, String direcao, int altitudeMaximaVoo,
                                 int qualidadeDaCamera, int framesPorSegundo, int duracaoMaximaVideo) {
        super(id, nome, posicaoX, posicaoY, altitude, direcao, altitudeMaximaVoo);
        this.qualidadeDaCamera = qualidadeDaCamera;
        this.framesPorSegundo = framesPorSegundo;
        this.duracaoMaximaVideo = duracaoMaximaVideo;
        this.tipoEntidadeRobo = TipoEntidade.ROBO; // Já definido na superclasse, mas pode ser especializado se necessário
    }

    /* ################################################################################################################################### */


    // Getters específicos
    public int getQualidadeDaCamera() { return qualidadeDaCamera; }
    public int getFramesPorSegundo() { return framesPorSegundo; }
    public int getDuracaoAtualVideo() { return duracaoAtualVideo; }
    public int getDuracaoMaximaVideo() { return duracaoMaximaVideo; }
    public boolean isGravando() { return gravando; }

    /* ################################################################################################################################### */


    public String iniciarGravacao() throws RoboDesligadoException, AcaoNaoPermitidaException {
        if (this.estado == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(nome + " está desligado e não pode iniciar gravação.");
        if (this.estado == EstadoRobo.AVARIADO) throw new AcaoNaoPermitidaException(nome + " está avariado e não pode iniciar gravação.");
        if (gravando) return nome + " já está gravando.";
        if (duracaoAtualVideo >= duracaoMaximaVideo) return "ERRO: Duração máxima do vídeo (" + duracaoMaximaVideo + "s) já atingida para " + nome + ". Não é possível iniciar nova gravação sem resetar.";
        
        gravando = true;
        this.estado = EstadoRobo.EXECUTANDO_TAREFA;
        return nome + " iniciando gravação com " + qualidadeDaCamera + "MP, " + framesPorSegundo + "fps. Tempo restante: " + (duracaoMaximaVideo - duracaoAtualVideo) + "s.";
    }

    /* ################################################################################################################################### */


    public String pararGravacao() throws RoboDesligadoException {
        // Permitir parar gravação mesmo se desligado ou avariado, caso a gravação tenha sido iniciada antes.
        if (!gravando) return nome + " não estava gravando.";

        gravando = false;
        String msg = nome + " parou a gravação. Duração total gravada nesta sessão: " + duracaoAtualVideo + "s.";
        // Não reseta duracaoAtualVideo aqui, para permitir continuar depois ou para relatório.
        // Poderia ter um método específico para resetar o vídeo.
        if(this.estado == EstadoRobo.EXECUTANDO_TAREFA && this.estado != EstadoRobo.AVARIADO && this.estado != EstadoRobo.DESLIGADO) {
            this.estado = EstadoRobo.EM_ESPERA;
        }
        return msg;
    }

    /* ################################################################################################################################### */

    
    public void simularTempoGravacao(int segundos) {
        if (gravando && this.estado != EstadoRobo.DESLIGADO && this.estado != EstadoRobo.AVARIADO) {
            if (segundos <=0) return;
            duracaoAtualVideo += segundos;
            System.out.println(nome + " gravou por mais " + segundos + "s. Total acumulado: " + duracaoAtualVideo + "s.");
            if (duracaoAtualVideo >= duracaoMaximaVideo) {
                duracaoAtualVideo = duracaoMaximaVideo;
                System.out.println("AVISO: " + nome + " atingiu o tempo máximo de gravação (" + duracaoMaximaVideo + "s).");
                try {
                    // Para a gravação automaticamente
                    String msgParada = pararGravacao();
                    System.out.println(msgParada);
                } catch (RoboDesligadoException e) { 
                    // Se o robô foi desligado enquanto gravava, a exceção de parar pode ocorrer.
                    System.err.println("Erro ao tentar parar gravação automaticamente: " + e.getMessage());
                }
            }
        }
    }

    /* ################################################################################################################################### */

    @Override
    public String executarTarefa(Ambiente ambiente, CentralComunicacao central, String[] args) throws RoboDesligadoException, AcaoNaoPermitidaException, ForaDosLimitesException, ColisaoException {
        if (this.estado == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(nome + " está desligado.");
        if (this.estado == EstadoRobo.AVARIADO) throw new AcaoNaoPermitidaException(nome + " está avariado.");

        if (args != null && args.length > 0) {
            String comandoTarefa = args[0].toLowerCase();
            switch (comandoTarefa) {
                case "gravar":
                    return iniciarGravacao();
                case "parar_gravar":
                    return pararGravacao();
                case "explorar_area":
                     return explorarArea(ambiente); 
                case "reset_video":
                    duracaoAtualVideo = 0;
                    return nome + ": Contador de vídeo resetado. Disponível: " + duracaoMaximaVideo + "s.";
                default:
                    return nome + " (Vigilância) não reconheceu o comando de tarefa: " + args[0] + ". Comandos: gravar, parar_gravar, explorar_area, reset_video.";
            }
        }
        return nome + " (Vigilância) aguardando comando específico de tarefa (ex: gravar, parar_gravar, explorar_area, reset_video).";
    }

    /* ################################################################################################################################### */

    // Implementação de Sensoreavel
    @Override
    public String acionarSensores(Ambiente ambiente) throws RoboDesligadoException {
        if (this.estado == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(nome + " (Vigilância) está desligado e não pode acionar sensores.");
        String sensoresBase = super.ativarSensoresRobo(ambiente); 
        return sensoresBase + 
               " - Sensor da Câmera: Ativo, Qualidade " + qualidadeDaCamera + "MP, " + framesPorSegundo + "fps.\n" +
               " - Status Gravação: " + (gravando ? "GRAVANDO ("+duracaoAtualVideo+"/"+duracaoMaximaVideo+"s)" : "PARADO");
    }

    /* ################################################################################################################################### */

    // Implementação de Comunicavel
    @Override
    public void enviarMensagem(Comunicavel destinatario, String mensagem, CentralComunicacao central) throws RoboDesligadoException, ErroComunicacaoException { 
        if (this.estado == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(nome + " (Vigilância) está desligado para enviar msg.");
        if (this.estado == EstadoRobo.AVARIADO) throw new ErroComunicacaoException(nome + " (Vigilância) está avariado demais para comunicar.");
        if (destinatario == null) throw new ErroComunicacaoException("Destinatário da mensagem não pode ser nulo.");
        if (central == null) throw new ErroComunicacaoException("Central de Comunicação não pode ser nula.");

        System.out.println("[" + nome + " -> " + destinatario.getIdComunicacao() + "]: '" + mensagem + "'");
        central.registrarMensagem(this.getIdComunicacao(), destinatario.getIdComunicacao(), mensagem);
        try {
            destinatario.receberMensagem(this.getIdComunicacao(), mensagem);
        } catch (RoboDesligadoException e){
            System.out.println("AVISO: Mensagem enviada por " + nome + " para " + destinatario.getIdComunicacao() + " mas o destinatário está desligado: " + e.getMessage());
            central.registrarMensagemBroadcast(this.getIdComunicacao(), "Tentativa de msg para " + destinatario.getIdComunicacao() + " (desligado): " + mensagem);
        }
    }

    /* ################################################################################################################################### */

    @Override
    public void receberMensagem(String remetenteIdComunicacao, String mensagem) throws RoboDesligadoException {
         if (this.estado == EstadoRobo.DESLIGADO && this.estado != EstadoRobo.AVARIADO) { // Avariado pode estar funcional para receber
             throw new RoboDesligadoException(nome + " (Vigilância) está desligado e não pode receber mensagens.");
         }
        System.out.println("[" + nome + " recebeu de " + remetenteIdComunicacao + "]: '" + mensagem + "'");
        // Poderia adicionar a mensagem recebida a um log interno do robô, se necessário.
    }
    

    /* ################################################################################################################################### */


    @Override
    public String getIdComunicacao() { return "DroneV-" + this.getId(); }

    /* ################################################################################################################################### */

    // Implementação de InterExplorador
    @Override
    public String explorarArea(Ambiente ambiente) throws RoboDesligadoException, AcaoNaoPermitidaException { 
        if (this.estado == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(nome + " está desligado e não pode explorar.");
        if (this.estado == EstadoRobo.AVARIADO) throw new AcaoNaoPermitidaException(nome + " está avariado e não pode explorar.");
        if (this.estado == EstadoRobo.EXECUTANDO_TAREFA && !gravando) { // Se já está executando outra tarefa não relacionada a gravação
            return nome + " já está executando outra tarefa e não pode iniciar exploração agora.";
        }
        
        StringBuilder exploracaoLog = new StringBuilder();
        exploracaoLog.append(nome).append(" iniciando exploração da área ao redor de ").append(exibirPosicao()).append("...");
        this.estado = EstadoRobo.EXECUTANDO_TAREFA;
        
        // Lógica de exploração simulada:
        // Poderia tentar mover para alguns pontos aleatórios ou fazer uma varredura.
        // Por simplicidade, apenas simula com tempo e reporta o que "vê".
        try {
            Thread.sleep(1000); 
            exploracaoLog.append("\n").append(nome).append(" verificando vizinhança imediata...");
            // Simular detecção de algo
            if (Math.random() > 0.7) {
                exploracaoLog.append("\nALERTA: ").append(nome).append(" detectou uma anomalia próxima!");
                if (this instanceof Comunicavel && ambiente.getEntidades().stream().anyMatch(e -> e instanceof Comunicavel && e != this)) {
                     // Tenta comunicar o alerta para a central ou outro robô, se possível
                }
            } else {
                exploracaoLog.append("\n").append(nome).append(": Nenhuma ocorrência significativa detectada na vizinhança imediata.");
            }
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            exploracaoLog.append("\nExploração de ").append(nome).append(" interrompida.");
        }

        if(this.estado == EstadoRobo.EXECUTANDO_TAREFA) this.estado = EstadoRobo.EM_ESPERA; // Só muda se não foi avariado/desligado no meio
        exploracaoLog.append("\n").append(nome).append(" concluiu a exploração e retornou ao estado de espera.");
        return exploracaoLog.toString();
    }
}