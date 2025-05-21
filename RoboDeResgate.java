/* RoboDeResgate.java */
import java.util.ArrayList;
import java.util.List;

class RoboDeResgate extends RoboTerrestre implements InterCarregador {
    private final int capacidadeDeCargaKg; // Carga em Kg que pode transportar (simulando resgatados)
    private int cargaAtualKg;
    private List<String> itensResgatados; // Descrição dos "itens" ou "vítimas" resgatadas
    private final boolean possuiSensorTermico;
    private final boolean possuiSensorRadar;

    public RoboDeResgate(String id, String nome, int posicaoX, int posicaoY, String direcao, 
                         int velocidadeMaxima, int capacidadeDeCargaKg, boolean sensorTermico, boolean sensorRadar) {
        super(id, nome, posicaoX, posicaoY, direcao, velocidadeMaxima);
        this.capacidadeDeCargaKg = capacidadeDeCargaKg;
        this.cargaAtualKg = 0;
        this.itensResgatados = new ArrayList<>();
        this.possuiSensorTermico = sensorTermico;
        this.possuiSensorRadar = sensorRadar;
        this.tipoEntidadeRobo = TipoEntidade.ROBO;
        if(sensorTermico) adicionarSensor(new SensorTemperatura(5.0)); // Raio de exemplo
        if(sensorRadar) adicionarSensor(new SensorProximidadeObstaculos(10.0)); // Exemplo de outro sensor
    }

    public int getCapacidadeDeCargaKg() { return capacidadeDeCargaKg; }
    public int getCargaAtualKg() { return cargaAtualKg; }
    public boolean hasSensorTermico() { return possuiSensorTermico; }
    public boolean hasSensorRadar() { return possuiSensorRadar; }

    // Implementação de InterCarregador (adaptada para "resgatados")
    @Override
    public void carregarItem(String descricaoItemResgatado) throws RoboDesligadoException, AcaoNaoPermitidaException { // Simula resgatar algo/alguém
        if (this.estado == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(nome + " está desligado.");
        if (this.estado == EstadoRobo.AVARIADO) throw new AcaoNaoPermitidaException(nome + " está avariado.");
        
        // Simula um "peso" para o item resgatado. Para simplificar, cada "item" tem um peso fixo de 10kg.
        int pesoItemEstimado = 10; 
        if (cargaAtualKg + pesoItemEstimado > capacidadeDeCargaKg) {
            throw new AcaoNaoPermitidaException(nome + " não pode resgatar '" + descricaoItemResgatado + "'. Excederia a capacidade de carga. ("+cargaAtualKg+" + "+pesoItemEstimado+" > "+capacidadeDeCargaKg+")");
        }
        itensResgatados.add(descricaoItemResgatado);
        cargaAtualKg += pesoItemEstimado;
        System.out.println(nome + " resgatou: " + descricaoItemResgatado + ". Carga atual: " + cargaAtualKg + "/" + capacidadeDeCargaKg + "kg.");
    }

    @Override
    public void descarregarItem() throws RoboDesligadoException, AcaoNaoPermitidaException { // Descarrega o último "resgatado"
        if (this.estado == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(nome + " está desligado.");
        if (this.estado == EstadoRobo.AVARIADO) throw new AcaoNaoPermitidaException(nome + " está avariado.");
        if (itensResgatados.isEmpty()) {
            System.out.println(nome + " não possui resgatados a bordo para descarregar.");
            return;
        }
        String itemDescarregado = itensResgatados.remove(itensResgatados.size() - 1);
        int pesoItemEstimado = 10; // Deve ser consistente com o carregamento
        cargaAtualKg -= pesoItemEstimado;
        if (cargaAtualKg < 0) cargaAtualKg = 0;
        System.out.println(nome + " entregou/descarregou o resgatado: " + itemDescarregado + ". Carga restante: " + cargaAtualKg + "kg.");
    }

    @Override
    public String verItensCarregados() throws RoboDesligadoException {
        if (this.estado == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(nome + " está desligado.");
        if (itensResgatados.isEmpty()) {
            return nome + " não possui resgatados a bordo.";
        }
        return nome + " (Resgate) transportando (" + cargaAtualKg + "/" + capacidadeDeCargaKg + "kg): " + String.join(", ", itensResgatados);
    }

    // Método específico de erguer carga (diferente de transportar)
    public String erguerCargaLocal(int cargaExternaKg) throws RoboDesligadoException, AcaoNaoPermitidaException {
        if (this.estado == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(nome + " está desligado.");
        if (this.estado == EstadoRobo.AVARIADO) throw new AcaoNaoPermitidaException(nome + " está avariado.");
        if (cargaExternaKg <= 0) return "Carga a ser erguida deve ser positiva.";
        if (cargaExternaKg > capacidadeDeCargaKg * 0.5) { // Ex: só pode erguer no local até 50% da sua capacidade de transporte
            return "ERRO: Carga externa ("+cargaExternaKg+"kg) excede a capacidade de içamento local do robô " + nome;
        }
        return nome + " está erguendo uma carga externa de " + cargaExternaKg + " kg no local " + exibirPosicao();
    }

    @Override
    public String executarTarefa(Ambiente ambiente, CentralComunicacao central, String[] args) throws RoboDesligadoException, AcaoNaoPermitidaException, ForaDosLimitesException, ColisaoException {
        if (this.estado == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(nome + " está desligado.");
        if (this.estado == EstadoRobo.AVARIADO) throw new AcaoNaoPermitidaException(nome + " está avariado.");

        this.estado = EstadoRobo.EXECUTANDO_TAREFA;
        StringBuilder log = new StringBuilder(nome + " (Resgate) executando tarefa: ");

        if (args != null && args.length > 0) {
            String comando = args[0].toLowerCase();
            if ("buscar_vitima".equals(comando) && args.length > 3) {
                try {
                    int vitimaX = Integer.parseInt(args[1]);
                    int vitimaY = Integer.parseInt(args[2]);
                    String descVitima = args[3];
                    log.append("Indo para (").append(vitimaX).append(",").append(vitimaY).append(",0) para resgatar '").append(descVitima).append("'.");
                    moverPara(vitimaX, vitimaY, 0, ambiente);
                    log.append("\nChegou ao local. Tentando resgatar...");
                    carregarItem(descVitima); // Tenta carregar (pode falhar se cheio)
                    log.append("\n'").append(descVitima).append("' resgatado(a). Retornando à base (simulado).");
                    // Mover de volta para um ponto de "base", ex: (0,0,0)
                    // moverPara(0,0,0, ambiente); 
                } catch (NumberFormatException e) {
                    log.append("\nArgumentos inválidos para buscar_vitima. Use: <x> <y> <descricao_vitima>");
                } catch (AcaoNaoPermitidaException | RoboDesligadoException | ColisaoException | ForaDosLimitesException e){
                    log.append("\nFalha na busca e resgate: ").append(e.getMessage());
                    this.estado = EstadoRobo.EM_ESPERA;
                    return log.toString();
                }
            } else if ("erguer_obstaculo".equals(comando) && args.length > 1) {
                 try {
                    int pesoObstaculo = Integer.parseInt(args[1]);
                    log.append(erguerCargaLocal(pesoObstaculo));
                } catch (NumberFormatException e) {
                    log.append("\nPeso inválido para erguer obstáculo. Use um número.");
                }
            }
            else {
                log.append("Comando de tarefa desconhecido: '").append(comando).append("'. Use 'buscar_vitima <x> <y> <desc>' ou 'erguer_obstaculo <pesoKg>'.");
            }
        } else {
            log.append("Nenhuma tarefa específica. Status dos sensores:\n");
            log.append(" - Sensor Térmico: ").append(possuiSensorTermico ? "Ativo" : "Inativo").append("\n");
            log.append(" - Sensor Radar: ").append(possuiSensorRadar ? "Ativo" : "Inativo").append("\n");
            log.append(" - ").append(verItensCarregados());
        }
        
        this.estado = EstadoRobo.EM_ESPERA;
        return log.toString();
    }
}


    @Override
    public String monitorar(Ambiente amb, Robo robo) {
        // Lógica para detectar obstáculos próximos dentro do raio
        // Aqui, uma simulação simples:
        boolean obstaculoProximo = false;
        for (Entidade e : amb.getEntidades()) {
            if (e instanceof Obstaculo && e != robo) {
                double distancia = Math.sqrt(
                    Math.pow(e.getX() - robo.getX(), 2) + 
                    Math.pow(e.getY() - robo.getY(), 2) +
                    Math.pow(e.getZ() - robo.getZ(), 2)
                );
                if (distancia <= raio) {
                    obstaculoProximo = true;
                    break;
                }
            }
        }
        return String.format("Sensor Proximidade: Raio %.1fm. Obstáculo próximo: %s", raio, obstaculoProximo ? "SIM" : "NÃO");
    }
}