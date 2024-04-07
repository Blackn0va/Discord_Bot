package com.blackn0va.discord_bot;

import java.io.File;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import scala.App;

/**
 *
 * @author Black
 */
public class Main {

    // Verschiedene statische Variablen zur Speicherung von Konfigurationsdaten und
    // Zustandsinformationen
    public static String openaitoken = "";
    public static String RegelnChannelID = "";
    public static String RegelnPostID = "";
    public static String status = "";
    public static String token = "";
    public static String desktopPath = "";
    public static String GPTChannelID = "1155287768491110410";
    public static String SCNewsChannelID = "";
    public static String answer = "";
    public static String RSSNews = "";
    public static String RSIStatus = "";
    public static String workingDir = System.getProperty("user.dir");
    public static String os = System.getProperty("os.name").toLowerCase();

    // Erstellen von geplanten Ausführungsdiensten für Patch- und
    // Statusaktualisierungen
    private static final ScheduledExecutorService schedulerPatch = Executors.newScheduledThreadPool(1);
    private static final ScheduledExecutorService schedulerStatus = Executors.newScheduledThreadPool(1);
    public static String IconURL = "https://avatars.githubusercontent.com/u/12220332?v=4";
    public static String Footer = "Bot";
    public static final String jarPath = new File(
            App.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent();

    public static String TokenFile = "";

    // Öffentliche Zeichenketten für Berechtigungen
    public static String RegelnAkzeptiert = "";

    /**
     * @param args the command line arguments
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        // Erkennung des Betriebssystems für die Pfadverwaltung
        System.out.println("Erkanntes Betriebssystem: " + os);
        WriteLogs.writeLog("Erkanntes Betriebssystem: " + os);

        // Unterscheidung zwischen Windows- und Unix-basierten Betriebssystemen
        if (os.contains("win")) {
            try { // Betriebssystem ist Windows basiert

                TokenFile = workingDir + "\\" + "Token.txt";

                // Wenn die Datei nicht existiert, wird sie erstellt
                if (!new File(TokenFile).exists()) {
                    new File(TokenFile).createNewFile();
                    // Einfügen von 3 Zeilen in die Datei
                    try (BufferedWriter bw = new BufferedWriter(new FileWriter(TokenFile))) {
                        bw.write("token");
                        bw.newLine();
                        bw.write("openaitoken");
                        bw.newLine();
                        bw.write("Regeln_Akzeptiert_Gruppen_Name");
                        bw.newLine();

                    }
                } else {
                    File file = new File(TokenFile);
                    // Die erste Zeile der Datei ist der Token und die zweite Zeile ist der
                    // OpenAI-Token
                    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                        token = br.readLine();
                        openaitoken = br.readLine();
                        RegelnAkzeptiert = br.readLine();

                        System.out.println("Token: " + token);
                    }
                }

            } catch (IOException e) {
                System.out.println("Exception caught =" + e.getMessage());
            }
        } else if (os.contains("nix") || os.contains("aix") || os.contains("nux")) {
            try { // Betriebssystem ist Linux/Unix basiert

                TokenFile = workingDir + "/" + "Token.txt";

                if (!new File(TokenFile).exists()) {
                    new File(TokenFile).createNewFile();
                    // Einfügen von 3 Zeilen in die Datei
                    try (BufferedWriter bw = new BufferedWriter(new FileWriter(TokenFile))) {
                        bw.write("token");
                        bw.newLine();
                        bw.write("openaitoken");
                        bw.newLine();
                        bw.write("Regeln_Akzeptiert_Gruppen_Name");
                        bw.newLine();
                    }
                } else {
                    try (BufferedReader br = new BufferedReader(new FileReader(TokenFile))) {
                        token = br.readLine();
                        openaitoken = br.readLine();
                        RegelnAkzeptiert = br.readLine();
                    }
                }

            } catch (IOException e) {
                System.out.println("Exception caught =" + e.getMessage());
                WriteLogs.writeLog("Exception caught =" + e.getMessage());
            }

        }

        // Starten des Discord-Bots
        DiscordBot.start();

        // Starten der Timer für News und Status

        schedulerPatch.scheduleAtFixedRate(() -> {
            try {
                StarCitizenPatchnotes.GetLatestPatchLink();
                PalworldPatchnotes.GetLatestPatchLink();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 0, 60, TimeUnit.MINUTES);

        schedulerStatus.scheduleAtFixedRate(() -> {
            try {
                StarCitizenServerStatus.getStatus();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 0, 10, TimeUnit.MINUTES);

    }

}