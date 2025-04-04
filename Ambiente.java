/* Ambiente.java */


// Faz a importação da classe ArrayList no pacote java.util
// Essa clase é utilizada para armazenar objetos do tipo Robo me uma lista
import java.util.ArrayList;

// Classe que guarda o ambiente da simulação dos robôs
public class Ambiente
{
    // Assumindo que a região 3D do ambiente é (x, y, z) pertencente à [0, xmax] x [0, ymax] x [0, elevacao]
    private int xmax;
    private int ymax;
    private int elevacao;

    // Este atributo armazena os robôs ativos no ambiente
    private ArrayList<Robo> listaDeRobos = new ArrayList<>();

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


    



}
