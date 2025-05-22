/* RoboCarroAutonomo.java */

// Adicionamos InterDefensiva à lista de interfaces implementadas

class RoboCarroAutonomo extends RoboTerrestre implements InterDefensiva {

    private int numDePassageiros;
    private final int numMaximoDePassageiros;
    private double nivelDaBateria; // Percentual (0.0 a 1.0)
    private int kilometrosRodados;
    private boolean modoDefesaAtivo; // Novo atributo para InterDefensiva

    /* ################################################################################################################################### */

    // Construtor 
    public RoboCarroAutonomo(String id, String nome, int posicaoX, int posicaoY, String direcao,
                             int velocidadeMaxima, int numMaximoDePassageiros, double nivelDaBateriaInicial) {
        super(id, nome, posicaoX, posicaoY, direcao, velocidadeMaxima);
        this.numMaximoDePassageiros = numMaximoDePassageiros;
        this.setNivelDaBateria(nivelDaBateriaInicial); // Usa o setter para validação
        this.numDePassageiros = 0;
        this.kilometrosRodados = 0;
        this.tipoEntidadeRobo = TipoEntidade.ROBO;
        this.modoDefesaAtivo = false; // Inicializa o modo de defesa como desativado
    }

    /* ################################################################################################################################### */

    // Getters
    public int getNumDePassageiros() { return numDePassageiros; }
    public int getNumMaximoDePassageiros() { return numMaximoDePassageiros; }
    public double getNivelDaBateria() { return nivelDaBateria; }
    public int getKilometrosRodados() { return kilometrosRodados; }
    public boolean isModoDefesaAtivo() { return modoDefesaAtivo; } // Getter para o estado de defesa

    /* ################################################################################################################################### */

    /*define o número de passageiros no robô carro autônomo, garantindo que o valor seja não negativo e não exceda o limite máximo permitido. Caso o valor exceda o limite, ele é ajustado automaticamente para o máximo, e uma mensagem de aviso é exibida. */

    public void setNumDePassageiros(int numDePassageiros) throws AcaoNaoPermitidaException {
        if (numDePassageiros < 0) {
            throw new AcaoNaoPermitidaException("Número de passageiros não pode ser negativo.");
        }
        if (numDePassageiros > numMaximoDePassageiros) {
            this.numDePassageiros = numMaximoDePassageiros;
            System.out.println("AVISO: Número de passageiros ("+numDePassageiros+") excede o máximo permitido ("+numMaximoDePassageiros+"). Ajustado para o máximo.");
        } else {
            this.numDePassageiros = numDePassageiros;
        }
        System.out.println(nome + " agora com " + this.numDePassageiros + " passageiros.");
    }

    /* ################################################################################################################################### */

    /* define o nível da bateria do robô carro autônomo, garantindo que o valor esteja entre 0.0 e 1.0. Caso o valor exceda esses limites, ele é ajustado automaticamente para o máximo ou mínimo permitido. */

    public void setNivelDaBateria(double nivelDaBateria) { // nivel 0.0 a 1.0
        if (nivelDaBateria > 1.0) {
            this.nivelDaBateria = 1.0;
        } else if (nivelDaBateria < 0.0) {
            this.nivelDaBateria = 0.0;
        } else {
            this.nivelDaBateria = nivelDaBateria;
        }
    }


    /* ################################################################################################################################### */


    // Consome bateria ao mover e registra km

    @Override
    public void moverPara(int novoX, int novoY, int novoZ, Ambiente ambiente) throws RoboDesligadoException, ColisaoException, ForaDosLimitesException, AcaoNaoPermitidaException {
        if (nivelDaBateria <= 0.05 && (novoX != this.posicaoX || novoY != this.posicaoY) ) { // 5% de bateria
            throw new AcaoNaoPermitidaException(nome + " com bateria muito baixa (" + String.format("%.1f%%", nivelDaBateria*100) + ") para mover.");
        }
        if (modoDefesaAtivo) {
            throw new AcaoNaoPermitidaException(nome + " está com modo de defesa ativo e não pode se mover.");
        }
        int oldX = this.posicaoX;
        int oldY = this.posicaoY;

        super.moverPara(novoX, novoY, novoZ, ambiente); // Chama o mover da superclasse (RoboTerrestre) que valida velocidade

        // Se moveu, gasta bateria e conta km
        if (this.posicaoX != oldX || this.posicaoY != oldY) {
            double distancia = Math.sqrt(Math.pow(this.posicaoX - oldX, 2) + Math.pow(this.posicaoY - oldY, 2));
            this.kilometrosRodados += (int) Math.round(distancia);
            this.nivelDaBateria -= (distancia * 0.01); // Ex: gasta 1% da bateria por unidade de distância
            if (this.nivelDaBateria < 0) this.nivelDaBateria = 0;
             System.out.println(nome + " moveu " + String.format("%.1f", distancia) + " unidades. Bateria: " + String.format("%.1f%%", nivelDaBateria*100) + ". Km rodados: " + kilometrosRodados);
        }
    }

    /* ################################################################################################################################### */

    /* permite que o robô carro autônomo recarregue sua bateria em uma quantidade especificada, verificando se o robô está ligado e em bom estado. Ele ajusta o nível da bateria para não exceder 100%, tratando exceções como desligamento ou avaria. */

    public String recarregarBateria(double quantidade) throws RoboDesligadoException, AcaoNaoPermitidaException {
        if (this.estado == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(nome + " está desligado.");
        if (this.estado == EstadoRobo.AVARIADO) throw new AcaoNaoPermitidaException(nome + " está avariado.");
        if (quantidade <= 0) return "Quantidade para recarregar deve ser positiva.";

        this.nivelDaBateria += quantidade;
        if (this.nivelDaBateria > 1.0) this.nivelDaBateria = 1.0;
        return nome + " recarregado. Nível da bateria: " + String.format("%.1f%%", nivelDaBateria*100);
    }

    /* ################################################################################################################################### */

    /* permite que o robô carro autônomo execute tarefas específicas, como transportar passageiros ou recarregar a bateria, verificando se o robô está ligado e em bom estado. Ele trata exceções como desligamento, avaria, colisões e limites excedidos. */

    @Override
    public String executarTarefa(Ambiente ambiente, CentralComunicacao central, String[] args) throws RoboDesligadoException, AcaoNaoPermitidaException, ForaDosLimitesException, ColisaoException {
        if (this.estado == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(nome + " está desligado.");
        if (this.estado == EstadoRobo.AVARIADO) throw new AcaoNaoPermitidaException(nome + " está avariado.");
        if (modoDefesaAtivo) throw new AcaoNaoPermitidaException(nome + " está com modo de defesa ativo e não pode executar tarefas de transporte ou recarga no momento.");


        this.estado = EstadoRobo.EXECUTANDO_TAREFA;
        StringBuilder log = new StringBuilder(nome + " (Carro Autônomo) executando tarefa: ");

        if (args != null && args.length > 0) {
            String comando = args[0].toLowerCase();
            if ("transportar_passageiro".equals(comando) && args.length > 2) {
                try {
                    int novoNumPassageiros = Integer.parseInt(args[1]);
                    setNumDePassageiros(novoNumPassageiros);
                    int destX = Integer.parseInt(args[2]);
                    int destY = Integer.parseInt(args[3]); // Assume que Z é 0 para carro
                    log.append("Transportando ").append(numDePassageiros).append(" passageiros para (").append(destX).append(",").append(destY).append(",0).");
                    moverPara(destX, destY, 0, ambiente); // Pode lançar exceções
                    log.append("\nChegou ao destino. Passageiros desembarcados (simulado).");
                    setNumDePassageiros(0); // Simula desembarque
                } catch (NumberFormatException e) {
                    log.append("\nArgumentos inválidos para transportar_passageiro. Use: <num_passageiros> <destX> <destY>");
                } catch (AcaoNaoPermitidaException | RoboDesligadoException | ColisaoException | ForaDosLimitesException e){
                    log.append("\nFalha no transporte: ").append(e.getMessage());
                     this.estado = EstadoRobo.EM_ESPERA; // Volta para espera se falhou
                    return log.toString();
                }

            } else if ("recarregar".equals(comando) && args.length > 1) {
                try {
                    double qtd = Double.parseDouble(args[1]);
                    log.append(recarregarBateria(qtd / 100.0)); // Assume que o usuário entra com porcentagem (ex: 50 para 50%)
                } catch (NumberFormatException e) {
                    log.append("\nValor inválido para recarga. Use um número.");
                }
            }
            else {
                log.append("Comando de tarefa desconhecido: '").append(comando).append("'. Use 'transportar_passageiro <num> <x> <y>' ou 'recarregar <percentual>'.");
            }
        } else {
            log.append("Nenhuma tarefa específica fornecida. Verificando status:\n");
            log.append(" - Passageiros: ").append(numDePassageiros).append("/").append(numMaximoDePassageiros).append("\n");
            log.append(" - Bateria: ").append(String.format("%.1f%%", nivelDaBateria*100)).append("\n");
            log.append(" - Km Rodados: ").append(kilometrosRodados).append("\n");
            log.append(" - Modo Defesa: ").append(modoDefesaAtivo ? "ATIVO" : "INATIVO");
        }

        this.estado = EstadoRobo.EM_ESPERA;
        return log.toString();
    }

    /* ################################################################################################################################### */
    /* permite que o robô carro autônomo exiba sua posição atual em um formato específico, incluindo a altitude (que é 0 para carros) e o estado do robô. */

    // Implementação dos métodos da interface InterDefensiva
    @Override
    public String ativarDefesa() throws RoboDesligadoException, AcaoNaoPermitidaException {
        if (this.estado == EstadoRobo.DESLIGADO) {
            throw new RoboDesligadoException(nome + " está desligado e não pode ativar o modo de defesa.");
        }
        if (this.estado == EstadoRobo.AVARIADO) {
            throw new AcaoNaoPermitidaException(nome + " está avariado e não pode ativar o modo de defesa.");
        }
        if (this.numDePassageiros > 0) {
            throw new AcaoNaoPermitidaException(nome + " não pode ativar o modo de defesa com passageiros a bordo.");
        }
        if (modoDefesaAtivo) {
            return nome + " já está com o modo de defesa ativo.";
        }
        this.modoDefesaAtivo = true;
        // Poderia adicionar lógica aqui, como parar o robô se ele estivesse em movimento,
        // ou mudar o estado para algo como "DEFENDENDO".
        // Por simplicidade, apenas ativamos o modo.
        // Um carro autônomo poderia, por exemplo, trancar as portas, soar um alarme silencioso,
        // ou ativar escudos de energia (se fosse um carro futurista!).
        // Para este simulador, vamos assumir que ele para e fica mais resistente ou em alerta.
        return nome + " (Carro Autônomo) ativou o modo de defesa. O veículo está parado e em alerta.";
    }

    /* ################################################################################################################################### */
    
    /* permite que o robô carro autônomo desative o modo de defesa, verificando se o robô está ligado e em bom estado. Ele trata exceções como desligamento, avaria e ações não permitidas. */

    @Override
    public String desativarDefesa() throws RoboDesligadoException, AcaoNaoPermitidaException {
        if (this.estado == EstadoRobo.DESLIGADO) {
            // Permitir desativar mesmo desligado, caso tenha sido ativado e o robô desligado depois?
            // Por consistência, vamos exigir que esteja ligado para interagir com sistemas.
            throw new RoboDesligadoException(nome + " está desligado e não pode desativar o modo de defesa.");
        }
         if (this.estado == EstadoRobo.AVARIADO && modoDefesaAtivo) {
            // Se estiver avariado MAS o modo de defesa estiver ativo, pode ser importante desativá-lo
            // para permitir conserto ou reboque. Mas por segurança, talvez não?
            // Vamos manter a restrição de avariado por enquanto.
            throw new AcaoNaoPermitidaException(nome + " está avariado e não pode desativar o modo de defesa (consulte manutenção).");
        }
        if (!modoDefesaAtivo) {
            return nome + " já está com o modo de defesa desativado.";
        }
        this.modoDefesaAtivo = false;
        return nome + " (Carro Autônomo) desativou o modo de defesa. Operações normais retomadas.";
    }
}