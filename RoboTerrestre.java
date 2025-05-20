/* RoboTerrestre.java */
public class RoboTerrestre extends Robo {
    private int velocidadeMaxima;

    public RoboTerrestre(String id, String nome, int posicaoX, int posicaoY, String direcao, int velocidadeMaxima) {
        super(id, nome, posicaoX, posicaoY, 0, direcao); // Robo terrestre sempre em Z=0 inicialmente
        this.velocidadeMaxima = velocidadeMaxima;
    }

    public int getVelocidadeMaxima() {
        return velocidadeMaxima;
    }

    // Sobrescrita do moverPara para incluir verificação de velocidade
    @Override
    public void moverPara(int novoX, int novoY, int novoZ, Ambiente ambiente) throws RoboDesligadoException, ColisaoException, ForaDosLimitesException, AcaoNaoPermitidaException {
        if (this.estado == EstadoRobo.DESLIGADO) {
            throw new RoboDesligadoException("Robô " + nome + " está desligado.");
        }
        if (this.estado == EstadoRobo.AVARIADO) {
            throw new AcaoNaoPermitidaException("Robô " + nome + " está avariado.");
        }
        if (novoZ != 0) {
            throw new AcaoNaoPermitidaException("Robô terrestre " + nome + " não pode alterar altitude Z.");
        }

        double distancia = Math.sqrt(Math.pow(novoX - this.posicaoX, 2) + Math.pow(novoY - this.posicaoY, 2));
        // Assumindo que o movimento é em 1 unidade de tempo.
        // Se a distância for maior que a velocidade máxima, não permite.
        // Para movimentos incrementais (passo a passo), essa lógica seria diferente.
        // No contexto do lab, onde se move para uma coordenada, o "passo" é o delta.
        // Se a intenção do lab é mover um 'deltaX' e 'deltaY' por vez:
        int deltaX = novoX - this.posicaoX;
        int deltaY = novoY - this.posicaoY;
        double velocidadeDoMovimento = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        if (velocidadeDoMovimento > this.velocidadeMaxima) {
            throw new AcaoNaoPermitidaException("Movimento excede a velocidade máxima do robô " + nome + 
                                                ". Vel Mov: " + String.format("%.2f", velocidadeDoMovimento) + ", Vel Max: " + this.velocidadeMaxima);
        }
        
        super.moverPara(novoX, novoY, novoZ, ambiente);
    }

    @Override
    public String executarTarefa(Ambiente ambiente, CentralComunicacao central, String[] args) throws RoboDesligadoException, AcaoNaoPermitidaException {
        if (this.estado == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(nome + " está desligado.");
        if (this.estado == EstadoRobo.AVARIADO) throw new AcaoNaoPermitidaException(nome + " está avariado.");
        // Exemplo de tarefa: patrulhar uma pequena área
        this.estado = EstadoRobo.EXECUTANDO_TAREFA;
        String resultado = nome + " (Terrestre) está patrulhando a área designada.";
        // Lógica de patrulha aqui...
        this.estado = EstadoRobo.EM_ESPERA;
        return resultado;
    }
}