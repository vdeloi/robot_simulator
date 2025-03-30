/* RoboDroneDeVigilancia.java */

class RoboDroneDeVigilancia extends RoboAereo
{
    private int qualidadeDaCamera; // qualidade da camera do drone em Megapixels
    private int framesPorSegundo; // fps da câmera
    private int duracaoAtualVideo = 0; // duração do vídeo em segundos
    private int duracaoMaximaVideo; // duração máxima do vídeo em segundos

    // Método construtor
    RoboDroneDeVigilancia(String nome, int posicaoX, int posicaoY, String direcao, int altitude, int altidudeMaxima, int qualidadeDaCamera, int framesPorSegundo, int duracaoMaximaVideo)
    {
        // Construtor da classe pai 
        super(nome, posicaoX, posicaoY, direcao, altitude, altidudeMaxima);
        this.qualidadeDaCamera = qualidadeDaCamera;
        this.framesPorSegundo = framesPorSegundo;
        this.duracaoMaximaVideo = duracaoMaximaVideo;
    }

    // Getter para qualidadeDaCamera
    public int getQualidadeDaCamera() {
        return qualidadeDaCamera;
    }

    // Getter para framesPorSegundo
    public int getFramesPorSegundo() {
        return framesPorSegundo;
    }

    // Getter para duracaoAtualVideo
    public int getDuracaoAtualVideo() {
        return duracaoAtualVideo;
    }

    // Getter para duracaoMaximaVideo
    public int getDuracaoMaximaVideo() {
        return duracaoMaximaVideo;
    }

    // Método para iniciar a gravação
    public void iniciarGravacao() 
    {
        if (duracaoAtualVideo < duracaoMaximaVideo) {
            System.out.println("Iniciando gravação de vídeo com qualidade de " + qualidadeDaCamera + " MP e " + framesPorSegundo + " fps.");
        } else {
            System.out.println("ERRO: Duração máxima do vídeo atingida!");
        }
    }

    // Método para parar a gravação
    public void pararGravacao() 
    {
        System.out.println("Parando gravação. Duração total do vídeo: " + duracaoAtualVideo + " segundos.");
        duracaoAtualVideo = 0; // Reseta a duração atual do vídeo
    }

    

}
