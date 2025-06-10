package br.unicamp.ic.mc322.lab05.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe utilitária para registrar logs de execução da missão. 
 * As mensagens são armazenadas em uma lista e podem ser salvas em um arquivo.
 */
public class Log {
    private static final List<String> logs = new ArrayList<>();
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    /**
     * Registra uma nova mensagem de log com timestamp.
     * @param mensagem A mensagem a ser registrada.
     */
    public static void registrar(String mensagem) {
        String logEntry = dtf.format(LocalDateTime.now()) + " - " + mensagem;
        logs.add(logEntry);
    }

    /**
     * Salva todos os logs registrados em um arquivo de texto.
     * @param nomeArquivo O nome do arquivo (ex: "log_missao.txt").
     */
    public static void salvar(String nomeArquivo) {
        try (PrintWriter out = new PrintWriter(new FileWriter(nomeArquivo))) {
            for (String log : logs) {
                out.println(log);
            }
            System.out.println("Log salvo com sucesso em " + nomeArquivo);
        } catch (IOException e) {
            System.err.println("Erro ao salvar o arquivo de log: " + e.getMessage());
        }
    }

    /**
     * Limpa todos os logs da memória.
     */
    public static void limparLogs() {
        logs.clear();
    }
}
