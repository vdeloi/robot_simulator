package src.br.unicamp.ic.mc322.lab05.comunicacao;
// CentralComunicacao.java
import java.util.ArrayList;
import java.util.List;

/**
 * Representa uma central de comunicação que registra todas as mensagens trocadas
 * entre entidades comunicáveis (geralmente robôs).
 * Funciona como um hub para o registro e visualização de comunicações.
 */
public class CentralComunicacao {
    private List<String> mensagens; // Lista para armazenar o log das mensagens

    /**
     * Construtor da CentralComunicacao.
     * Inicializa a lista de mensagens.
     */
    public CentralComunicacao() {
        this.mensagens = new ArrayList<>();
    }

    /**
     * Registra uma mensagem enviada entre duas entidades ou para todos.
     * A mensagem é formatada e adicionada ao histórico.
     *
     * @param remetenteId   O ID da entidade que enviou a mensagem.
     * @param destinatarioId O ID da entidade destinatária. Pode ser null se a mensagem for para "TODOS".
     * @param msg           O conteúdo da mensagem.
     */
    public void registrarMensagem(String remetenteId, String destinatarioId, String msg) {
        // Formata a mensagem para o log
        String log = "De: " + remetenteId + " | Para: " + (destinatarioId != null ? destinatarioId : "TODOS") + " | Msg: " + msg;
        this.mensagens.add(log); // Adiciona a mensagem formatada à lista
        System.out.println("Central: Mensagem registrada - " + log); // Imprime uma confirmação no console
    }

    /**
     * Exibe todas as mensagens registradas na central de comunicação.
     * Se não houver mensagens, informa ao usuário.
     */
    public void exibirMensagens() {
        System.out.println("\n--- Histórico de Mensagens da Central ---");
        if (mensagens.isEmpty()) {
            System.out.println("Nenhuma mensagem registrada.");
            return;
        }
        // Itera sobre a lista de mensagens e as exibe numeradas
        for (int i = 0; i < mensagens.size(); i++) {
            System.out.println((i + 1) + ". " + mensagens.get(i));
        }
        System.out.println("--------------------------------------");
    }
}