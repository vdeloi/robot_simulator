// Classe CentralComunicacao

 /* ################################################################################################################################### */

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

 /* ################################################################################################################################### */

public class CentralComunicacao {
    private List<String> mensagens; 
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


     /* ################################################################################################################################### */
     // Construtor

    public CentralComunicacao() {
        this.mensagens = new ArrayList<>();
    }

     /* ################################################################################################################################### */
    // Métodos

    public void registrarMensagem(String remetenteId, String destinatarioId, String msg) {
        String timestamp = LocalDateTime.now().format(formatter);
        String mensagemFormatada = "[" + timestamp + "] De: " + remetenteId + " | Para: " + destinatarioId + " | Msg: " + msg;
        this.mensagens.add(mensagemFormatada);
        // System.out.println("DEBUG (Central): " + mensagemFormatada); // Para depuração
    }

     /* ################################################################################################################################### */
    
    public void registrarMensagemBroadcast(String remetenteId, String msg) {
        String timestamp = LocalDateTime.now().format(formatter);
        String mensagemFormatada = "[" + timestamp + "] Broadcast de: " + remetenteId + " | Msg: " + msg;
        this.mensagens.add(mensagemFormatada);
    }

     /* ################################################################################################################################### */


    public void exibirMensagens() {
        System.out.println("\n--- Histórico de Comunicações ---");
        if (mensagens.isEmpty()) {
            System.out.println("Nenhuma mensagem registrada.");
        } else {
            for (String msg : mensagens) {
                System.out.println(msg);
            }
        }
        System.out.println("---------------------------------");
    }
}