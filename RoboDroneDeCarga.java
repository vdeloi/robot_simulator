/* RoboDroneDeCarga.java */


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/* ################################################################################################################################### */


class RoboDroneDeCarga extends RoboAereo implements InterCarregador {
    private List<String> itensCarregados;
    private int capacidadeMaximaItens; 

    /* ################################################################################################################################### */

    // Construtor ajustado
    public RoboDroneDeCarga(String id, String nome, int posicaoX, int posicaoY, int altitudeInicial, String direcao, 
                            int altitudeMaximaVoo, int capacidadeMaximaItens) {
        super(id, nome, posicaoX, posicaoY, altitudeInicial, direcao, altitudeMaximaVoo);
        this.itensCarregados = new ArrayList<>();
        this.capacidadeMaximaItens = capacidadeMaximaItens;
        this.tipoEntidadeRobo = TipoEntidade.ROBO;
    }

    /* ################################################################################################################################### */

    /*permite que o drone de carga adicione um item à sua lista de itens carregados, verificando se o robô está ligado, em bom estado e se ainda há capacidade disponível. Caso o item seja carregado com sucesso, uma mensagem é exibida indicando o progresso da carga. */

    // Implementação de InterCarregador
    @Override
    public void carregarItem(String item) throws RoboDesligadoException, AcaoNaoPermitidaException {
        if (this.estado == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(nome + " está desligado.");
        if (this.estado == EstadoRobo.AVARIADO) throw new AcaoNaoPermitidaException(nome + " está avariado.");
        if (itensCarregados.size() >= capacidadeMaximaItens) {
            throw new AcaoNaoPermitidaException(nome + " está com capacidade máxima ("+capacidadeMaximaItens+" itens) e não pode carregar mais.");
        }
        itensCarregados.add(item);
        System.out.println(nome + " carregou o item: " + item + ". Itens a bordo: " + itensCarregados.size() + "/" + capacidadeMaximaItens);
    }

    /* ################################################################################################################################### */

    /*permite que o drone de carga descarregue o último item da lista de itens carregados, verificando se o robô está ligado e em bom estado. Caso o item seja descarregado com sucesso, uma mensagem é exibida indicando o progresso da descarga. */


    @Override
    public void descarregarItem() throws RoboDesligadoException, AcaoNaoPermitidaException { // Descarrega o último item
        if (this.estado == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(nome + " está desligado.");
        if (this.estado == EstadoRobo.AVARIADO) throw new AcaoNaoPermitidaException(nome + " está avariado.");
        if (itensCarregados.isEmpty()) {
            System.out.println(nome + " não tem itens para descarregar.");
            return;
        }
        String itemDescarregado = itensCarregados.remove(itensCarregados.size() - 1);
        System.out.println(nome + " descarregou o item: " + itemDescarregado + ". Itens restantes: " + itensCarregados.size());
    }

    /* ################################################################################################################################### */

    /*permite que o drone de carga descarregue um item específico da lista de itens carregados, verificando se o robô está ligado e em bom estado. Caso o item seja encontrado e descarregado com sucesso, uma mensagem é exibida indicando o progresso da descarga. */

    
    public void descarregarItemEspecifico(String item) throws RoboDesligadoException, AcaoNaoPermitidaException {
        if (this.estado == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(nome + " está desligado.");
        if (this.estado == EstadoRobo.AVARIADO) throw new AcaoNaoPermitidaException(nome + " está avariado.");
        if (itensCarregados.isEmpty()) {
            System.out.println(nome + " não tem itens para descarregar.");
            return;
        }
        if (itensCarregados.remove(item)) {
            System.out.println(nome + " descarregou o item específico: " + item + ". Itens restantes: " + itensCarregados.size());
        } else {
            System.out.println(nome + " não encontrou o item '" + item + "' para descarregar.");
        }
    }

    /* ################################################################################################################################### */

    /*permite que o drone de carga verifique os itens atualmente carregados, verificando se o robô está ligado. Caso não haja itens carregados, uma mensagem é exibida indicando que o robô não está carregando nada. */



    @Override
    public String verItensCarregados() throws RoboDesligadoException {
        if (this.estado == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(nome + " está desligado.");
        if (itensCarregados.isEmpty()) {
            return nome + " não está carregando nenhum item.";
        }
        return nome + " está carregando ("+itensCarregados.size()+"/"+capacidadeMaximaItens+"): " + String.join(", ", itensCarregados);
    }


    /* ################################################################################################################################### */

    /*permite que o drone de carga execute uma tarefa, verificando se o robô está ligado e em bom estado. Caso a tarefa seja executada com sucesso, uma mensagem é exibida indicando o progresso da tarefa. */


    // Implementação de executarTarefa
    @Override
    public String executarTarefa(Ambiente ambiente, CentralComunicacao central, String[] args) throws RoboDesligadoException, AcaoNaoPermitidaException, ForaDosLimitesException, ColisaoException {
        if (this.estado == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(nome + " está desligado.");
        if (this.estado == EstadoRobo.AVARIADO) throw new AcaoNaoPermitidaException(nome + " está avariado.");

        this.estado = EstadoRobo.EXECUTANDO_TAREFA;
        StringBuilder logTarefa = new StringBuilder();
        logTarefa.append(nome).append(" (Drone de Carga) iniciando tarefa.");

        if (args != null && args.length > 0) {
            String acao = args[0].toLowerCase();
            if ("coletar".equalsIgnoreCase(acao) && args.length > 1) {
                String itemParaColetar = args[1];
                // Simular voo para um local, coleta e retorno.
                // Por simplicidade, apenas adiciona o item se houver espaço.
                logTarefa.append("\nTentando coletar '").append(itemParaColetar).append("'.");
                try {
                    carregarItem(itemParaColetar);
                    logTarefa.append("\n'").append(itemParaColetar).append("' coletado.");
                } catch (AcaoNaoPermitidaException e) {
                    logTarefa.append("\nFalha ao coletar: ").append(e.getMessage());
                }
            } else if ("entregar".equalsIgnoreCase(acao)) {
                // Simular voo para local de entrega e descarregar.
                if (!itensCarregados.isEmpty()) {
                    String itemEntregue = itensCarregados.get(0); // Entrega o primeiro da lista
                    descarregarItemEspecifico(itemEntregue);
                    logTarefa.append("\nItem '").append(itemEntregue).append("' entregue em ").append(exibirPosicao());
                } else {
                    logTarefa.append("\nNenhum item para entregar.");
                }
            } else {
                logTarefa.append("\nAção de tarefa '").append(acao).append("' desconhecida para Drone de Carga. Use 'coletar <item>' ou 'entregar'.");
            }
        } else {
             logTarefa.append("\n").append(nome).append(" em modo de espera de carga/descarga. Itens atuais: ").append(verItensCarregados());
        }
        
        this.estado = EstadoRobo.EM_ESPERA;
        logTarefa.append("\n").append(nome).append(" concluiu a tarefa e está em espera.");
        return logTarefa.toString();
    }
}