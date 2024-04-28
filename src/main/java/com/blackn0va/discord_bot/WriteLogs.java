package com.blackn0va.discord_bot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WriteLogs {
    // Statische Variablen zum Speichern der Pfade für die Protokolle
    public static String logPath = "";
    public static String logFile = "log.txt";
    public static String permissionlog = "permissionlog.txt";
    public static String chatlog = "chatlog.txt";
    public static String workingDir = System.getProperty("user.dir");

    // Methode zum Schreiben von Protokollen
    public static void writeLog(String log) {
        // Bestimmen des Betriebssystems
        String os = System.getProperty("os.name").toLowerCase();
        // Wenn das Betriebssystem Windows ist, setzen Sie den Pfad für das Protokoll
        if (os.contains("win")) {
            logPath = workingDir + "\\" + logFile;
            // Wenn das Betriebssystem Linux oder Unix ist, setzen Sie den Pfad für das
            // Protokoll
        } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
            logPath = workingDir + "/" + logFile;
        }

        // Überprüfen Sie, ob das Betriebssystem Windows oder Linux ist und ob die Datei
        // auf dem Desktop existiert
        try {
            // Wenn die Datei nicht existiert, erstellen Sie sie
            if (!new File(logPath).exists()) {
                new File(logPath).createNewFile();
                // Fügen Sie 6 Zeilen in die Datei ein
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(logPath))) {
                    bw.write("Logfile");
                    bw.newLine();
                    bw.write("--------");
                    bw.newLine();
                    bw.write("Logfile created");
                    bw.newLine();
                    bw.write("--------");
                    bw.newLine();
                } catch (IOException e) {
                    WriteLogs.writeLog("Error: ");
                }
            }

            // Wenn die Datei existiert, schreiben Sie die Protokolle
            if (new File(logPath).exists()) {
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(logPath, true))) {
                    bw.newLine();
                    bw.write(log);
                } catch (IOException e) {
                    WriteLogs.writeLog("Error: ");
                }
            }

            System.out.println(log);

        } catch (Exception e) {
            // Ausgabe einer Fehlermeldung
            WriteLogs.writeLog("Error: ");
        }

    }

    // Methode zum Schreiben von Berechtigungsprotokollen
    public static void permissions(String log) {
        // Bestimmen des Betriebssystems

        // Wenn das Betriebssystem Windows ist, setzen Sie den Pfad für das Protokoll
        if (Main.os.contains("win")) {
            logPath = workingDir + "\\" + permissionlog;
            // Wenn das Betriebssystem Linux oder Unix ist, setzen Sie den Pfad für das
            // Protokoll
        } else if (Main.os.contains("nix") || Main.os.contains("nux") || Main.os.contains("aix")) {
            logPath = workingDir + "/" + permissionlog;
        }

        // Überprüfen Sie, ob das Betriebssystem Windows oder Linux ist und ob die Datei
        // auf dem Desktop existiert
        try {
            // Wenn die Datei nicht existiert, erstellen Sie sie
            if (!new File(logPath).exists()) {
                new File(logPath).createNewFile();
                // Fügen Sie 6 Zeilen in die Datei ein
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(logPath))) {
                    bw.write("Logfile");
                    bw.newLine();
                    bw.write("--------");
                    bw.newLine();
                    bw.write("Logfile created");
                    bw.newLine();
                    bw.write("--------");
                    bw.newLine();
                } catch (IOException e) {
                    WriteLogs.writeLog("Error: ");
                }
            }

            // Wenn die Datei existiert, schreiben Sie die Protokolle
            if (new File(logPath).exists()) {
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(logPath, true))) {
                    bw.newLine();
                    bw.write(log);
                } catch (IOException e) {
                    WriteLogs.writeLog("Error: ");
                }
            }

            System.out.println(log);

        } catch (Exception e) {
            // Ausgabe einer Fehlermeldung
            WriteLogs.writeLog("Error: ");
        }

    }

    // Methode zum Schreiben von Chatprotokollen
    public static void chat(String log) {
        // Wenn das Betriebssystem Windows ist, setzen Sie den Pfad für das Protokoll
        if (Main.os.contains("win")) {
            logPath = workingDir + "\\" + chatlog;
            // Wenn das Betriebssystem Linux oder Unix ist, setzen Sie den Pfad für das
            // Protokoll
        } else if (Main.os.contains("nix") || Main.os.contains("nux") || Main.os.contains("aix")) {
            logPath = workingDir + "/" + chatlog;
        }

        // Überprüfen Sie, ob das Betriebssystem Windows oder Linux ist und ob die Datei
        // auf dem Desktop existiert
        try {
            // Wenn die Datei nicht existiert, erstellen Sie sie
            if (!new File(logPath).exists()) {
                new File(logPath).createNewFile();
                // Fügen Sie 6 Zeilen in die Datei ein
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(logPath))) {
                    bw.write("Logfile");
                    bw.newLine();
                    bw.write("--------");
                    bw.newLine();
                    bw.write("Logfile created");
                    bw.newLine();
                    bw.write("--------");
                    bw.newLine();
                } catch (IOException e) {
                    WriteLogs.writeLog("Error: ");
                }
            }

            // Wenn die Datei existiert, schreiben Sie die Protokolle
            if (new File(logPath).exists()) {
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(logPath, true))) {
                    bw.newLine();
                    bw.write(log);
                } catch (IOException e) {
                    WriteLogs.writeLog("Error: ");
                }
            }

            System.out.println(log);

        } catch (Exception e) {
            WriteLogs.writeLog("Error: ");
        }

    }

}
