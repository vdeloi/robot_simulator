/* Ambiente.java */

// Este arquivo contém a classe Ambiente, que representa o ambiente onde os robôs operam.
// A classe é responsável por armazenar informações sobre os robôs e obstáculos presentes no ambiente.


// Faz a importação da classe ArrayList no pacote java.util
// Essa clase é utilizada para armazenar objetos do tipo Robo me uma lista
import java.util.ArrayList;

// Classe que guarda o ambiente da simulação dos robôs
public class Ambiente
{
    // Assumindo que a região 3D do ambiente é (x, y, z) pertencente à [0, xmax] x [0, ymax] x [0, elevacao]
    private int xmax; // largura
    private int ymax; // altura
    private int elevacao;

    // Este atributo armazena os robôs ativos no ambiente
    private ArrayList<Robo> listaDeRobos = new ArrayList<>();
    private ArrayList<Obstaculo> listaDeObstaculos = new ArrayList<>(); //talvez não...

    // Este é o método construtor da classe Ambiente
    // Ele recebe apenas dois argumentos (int xmax, int ymax) para que 
    // Possa ser utilizado tanto como ambiente 2D quanto ambiente 3D (atráves do método void definirElevacao(int elevacao))
    Ambiente(int xmax, int ymax)
    {
        this.xmax = xmax;
        this.ymax = ymax;
    }
    
    // Adiciona um objeto r da classe Robo à lista de robôs ativos no ambiente
    public void adicionarRobo(Robo r)
    {
        listaDeRobos.add(r);
    }
    // Remove um objeto r da classe Robo da lista de robôs ativos no ambiente
    public void removeRobo(Robo r)
    {
        listaDeRobos.remove(r);
    }

    // Retorna o número de robôs ativos no ambiente 
    public int getNumDeRobos()
    {
        return listaDeRobos.size();
    }

    // Este método pode ser utilizado para tornar a classe um Ambiente tridimensional
    public void definirElevacao(int elevacao)
    {
        this.elevacao = elevacao;
    }

    // Verifica se um ponto (x, y) está dentro dos limites do ambiente 
    // retorna true se o ponto (x, y) está dentro da região [0, xmax] x [0, ymax]
    public boolean dentroDosLimites(int x, int y)
    {
        if (x < 0 || y < 0)
        {
            return false;
        }

        return (x <= this.xmax && y <= this.ymax);

    }

    // Este método é um overload para pontos no espaço, será útil para a subclasse RoboAereo e seus filhos
    public boolean dentroDosLimites(int x, int y, int elevacao)
    {
        if (x < 0 || y < 0 || elevacao < 0)
        {
            return false;
        }

        return ( x <= this.xmax && y <= this.ymax && elevacao <= this.elevacao);
    }

    // Retorna a lista de robôs no ambiente
    public ArrayList<Robo> getListaDeRobos()
    {
        return listaDeRobos;

    }

    public void adicionarObstaculo (Obstaculo o)
    {
        listaDeObstaculos.add(o);
    {
        
    
     // Usa a informação dos obstáculos na lista
    public boolean temObstaculoBloqueante(int x, int y) {
        for (Obstaculo obs : listaDeObstaculos) { // Para cada obstáculo específico na lista...
            // Verifica se ele bloqueia E se a posição (x,y) está na área dele
            if (obs.isBloqueiaPassagem()) { // <-- Pergunta ao obstáculo se ele bloqueia
                // ... (lógica para verificar se x,y está na área de obs) ...
                if (/* x,y está na área de obs */) {
                    return true;
                }
            }
        }
        return false;
    }

     public void detectarColisoes() {
         for (Robo robo : robos) {
             // Verifica se a posição do robô coincide com um obstáculo bloqueante
             if (temObstaculoBloqueante(robo.getPosicaoX(), robo.getPosicaoY())) { // <-- Usa o método auxiliar
                  System.out.println("ALERTA: Robô '" + robo.getNome() + "' colidiu com um obstáculo!");
             }
         }
     }  


}
