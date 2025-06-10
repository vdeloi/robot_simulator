package util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Classe utilitária para registrar logs de eventos da simulação em um arquivo.
 * As mensagens são gravadas com um timestamp.
 */
public class Log {
    private static final String NOME_ARQUIVO = "missao_log.txt"; // Nome do arquivo de log
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    /**
     * Registra uma mensagem no arquivo de log.
     * A mensagem é prefixada com a data e hora atuais.
     * O arquivo é aberto em modo 'append', então novas mensagens são adicionadas ao final.
     *
     * @param mensagem A mensagem a ser registrada.
     */
    public static void registrar(String mensagem) {
        // Usa try-with-resources para garantir que o PrintWriter e FileWriter sejam fechados
        try (FileWriter fw = new FileWriter(NOME_ARQUIVO, true); // true para modo append
             PrintWriter pw = new PrintWriter(fw)) {
            
            // Monta a linha de log com data e hora
            String linhaLog = dtf.format(LocalDateTime.now()) + " - " + mensagem;
            pw.println(linhaLog); // Escreve a linha no arquivo
            
        } catch (IOException e) {
            // Se houver um erro de I/O, imprime uma mensagem de erro no console
            System.err.println("Erro ao escrever no arquivo de log: " + e.getMessage());
        }
    }
}