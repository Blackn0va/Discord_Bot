package com.blackn0va.discord_bot;

import java.io.File;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 *
 * @author Black
 */
public class Main {

    // Discord Bot Einstellungen
    public static String token = "";
    public static JDA bauplan;
    public static String IconURL = "https://avatars.githubusercontent.com/u/12220332?v=4";
    public static String Footer = "Bot";

    // Kanal-IDs
    public static String RegelnChannelID = "";
    // public static Queue<Message> OutgoingMessageQueue = new LinkedList<>();

    public static String answer = "1155287768491110410";

    // Nachrichten-IDs
    public static String RegelnPostID = "";

    // Systemeinstellungen
    public static String workingDir = System.getProperty("user.dir");
    public static String os = System.getProperty("os.name").toLowerCase();
    public static String desktopPath = "";
    public static final String jarPath = new File(
            Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent();

    // Nachrichtenverarbeitung
    public static ConcurrentLinkedQueue<MessageReceivedEvent> messageQueue = new ConcurrentLinkedQueue<>();

    // Sonstiges
    public static String RegelnAkzeptiert = "";
    public static String TokenFile = "";

    // Eine Zeichenkette, die die Regeln für den Discord-Server enthält
    public static String regeln = "\u00A7 1. 🤝 Sei ein Freund, kein Feind. Respekt und Höflichkeit sind hier das A und O.\n\n"
            +
            "\u00A7 2. 🚫 Kein Platz für Hass. Beleidigungen, Diskriminierung, Rassismus oder Antisemitismus haben hier keinen Platz.\n\n"
            +
            "\u00A7 3. 🚀 Eigenwerbung? Nicht hier. Das Bewerben von eigenen oder fremden Inhalten, einschließlich anderer Discord-Server, ist nicht erlaubt.\n\n"
            +
            "\u00A7 4. 📚 Bleib beim Thema. Jeder Kanal hat sein eigenes Thema, und unsere Gemeinschaftssprache ist Deutsch. Bitte bemühe dich, korrektes Deutsch zu verwenden.\n\n"
            +
            "\u00A7 5. 🚷 Kein Trolling, kein Spamming. Unnötige Diskussionen, die nichts mit dem Thema zu tun haben, sind nicht erwünscht.\n\n"
            +
            "\u00A7 6. 🎙️ Keine Soundboards, Stimmverzerrer oder Aufnahmen. Das ist nicht nur unhöflich, sondern auch gesetzlich verboten.\n\n"
            +
            "\u00A7 7. 🚪 Nicht ständig rein und raus. Das dauerhafte Verlassen und Betreten eines Sprachkanals ist zu unterlassen.\n\n"
            +
            "\u00A7 8. 📵 Keine Störgeräusche. Handy, Fernseher und andere Hintergrundgeräusche sind zu unterlassen.\n\n"
            +
            "\u00A7 9. 🎤 Sprich oder schweig. In den Sprachkanal zu kommen und dauerhaft nichts zu sagen oder sich stumm zu schalten, ist unhöflich. Nutze dafür den AFK/Pause-Kanal.\n\n"
            +
            "Wenn du die Regeln gelesen, verstanden und akzeptierst, klicke unten auf 'Regeln Akzeptieren'!\n\n";

    /**
     * @param args the command line arguments
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        // Erkennung des Betriebssystems für die Pfadverwaltung
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

                    }
                } else {
                    File file = new File(TokenFile);
                    // Die erste Zeile der Datei ist der Token und die zweite Zeile ist der
                    // OpenAI-Token
                    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                        token = br.readLine();
                        WriteLogs.writeLog("Token: " + token);

                    }
                }

            } catch (IOException e) {
                WriteLogs.writeLog("Exception caught =" + e.getMessage());
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
                    }
                } else {
                    try (BufferedReader br = new BufferedReader(new FileReader(TokenFile))) {
                        token = br.readLine();
                    }
                }

            } catch (IOException e) {
                WriteLogs.writeLog("Exception caught =" + e.getMessage());
            }

        }

        // Starte den Discord-Bot
        DiscordBot.start();

    }

}