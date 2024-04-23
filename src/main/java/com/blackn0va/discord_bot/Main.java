package com.blackn0va.discord_bot;

import java.io.File;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import com.theokanning.openai.completion.chat.ChatMessage;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import scala.App;

/**
 *
 * @author Black
 */
public class Main {

    // Discord Bot Einstellungen
    public static String token = "";
    public static String openaitoken = "";
    public static JDA bauplan;
    public static String IconURL = "https://avatars.githubusercontent.com/u/12220332?v=4";
    public static String Footer = "Bot";

    // Kanal-IDs
    public static String RegelnChannelID = "";
    //public static String GPTChannelID = "1155287768491110410";
    //public static String StarCitizenPatchChannelID = "1101892014351585290";
   // public static String PalworldPatchChannelID = "1205879327200378910";
    // Tests
    public static String GPTChannelID = "1155287768491110410";
     public static String StarCitizenPatchChannelID = "1231318450606051388";
     public static String PalworldPatchChannelID = "1155287768491110410";
    // public static Queue<Message> OutgoingMessageQueue = new LinkedList<>();

    public static String answer = "1155287768491110410";

    // Nachrichten-IDs
    public static String RegelnPostID = "";

    // Star Citizen Patch-Notizen Einstellungen
    public static String StarCitizenVersionFile = "";
    public static String StarCitizenPersistence = "";
    public static String StarCitizenLive = "";
    public static String StarCitizenVersion = "";
    public static String StarCitizenLink = "";
    public static String StarCitizenBaseUrl = "https://robertsspaceindustries.com/patch-notes";
    public static String StarCitizenPatchlink = "https://robertsspaceindustries.com";
    public static List<String> StarCitizenFinalStrings = new ArrayList<>();
    public static Map<Integer, String> StarCitizenPageCache = new HashMap<>();
    public static List<String> StarCitizenPatchPages;
    public static int StarCitizencurrentPageNum = 1;
    public static String StarCitizenServerStatus = "";
    public static String StarCitizenRSSNews = "";

    // Palworld Patch-Notizen Einstellungen
    public static String PalworldVersionFile = "";
    public static String PalworldRSSNews = "";
    public static String PalworldPersistence = "";
    public static String PalworldLive = "";
    public static String PalworldVersion = "";
    public static String PalworldLink = "";
    public static String PalworldBaseUrl = "https://store.steampowered.com/feeds/news/app/1623730/?cc=DE&l=german&snr=1_2108_9__2107";
    public static String PalworldPatchlink = "";
    public static String PalworldPicture = "";
    public static String PalworldTitle = "";
    public static String PalworldDescription = "";
    public static List<String> PalworldPatchPages;
    public static int PalworldCurrentPageNum = 1;
    public static Map<Integer, String> PalworldPageCache = new HashMap<>();
    public static List<String> PalworldFinalStrings = new ArrayList<>();
    // Systemeinstellungen
    public static String workingDir = System.getProperty("user.dir");
    public static String os = System.getProperty("os.name").toLowerCase();
    public static String desktopPath = "";
    public static final String jarPath = new File(
            App.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent();

    // Nachrichtenverarbeitung
    public static ConcurrentLinkedQueue<MessageReceivedEvent> messageQueue = new ConcurrentLinkedQueue<>();
    public static final List<ChatMessage> messages = new ArrayList<>();

    // Sonstiges
    public static String RegelnAkzeptiert = "";
    public static String TokenFile = "";
    private static final ScheduledExecutorService schedulerPatchPalWorld = Executors.newScheduledThreadPool(1);
    private static final ScheduledExecutorService schedulerStarCitizenServerStatus = Executors
            .newScheduledThreadPool(1);
    private static final ScheduledExecutorService schedulerPatchStarCitizen = Executors.newScheduledThreadPool(1);

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

        // Starte den Discord-Bot
        DiscordBot.start();

        // Erstelle einen separaten Thread für den Star Citizen Server Status Job
        Thread starCitizenServerStatusThread = new Thread(() -> {
            schedulerStarCitizenServerStatus.scheduleAtFixedRate(() -> {
                // PatchnotesStarCitizen.GetStarCitizenPatchnotes();
                try {
                    StarCitizenStatus.getStatus();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, 0, 10, TimeUnit.MINUTES);
        });

        // Erstelle einen separaten Thread für den Star Citizen Patchnotizen Job
        Thread starCitizenThread = new Thread(() -> {
            schedulerPatchStarCitizen.scheduleAtFixedRate(() -> {
                PatchnotesStarCitizen.GetStarCitizenPatchnotes();
            }, 0, 60, TimeUnit.MINUTES);
        });

        // Erstelle einen separaten Thread für den Palworld Patchnotizen Job
        Thread palWorldThread = new Thread(() -> {
            schedulerPatchPalWorld.scheduleAtFixedRate(() -> {
                PatchnotesPalworld.GetPalworldPatchnotes();
            }, 0, 60, TimeUnit.MINUTES);
        });

        // Starte die Threads
        starCitizenThread.start();
        palWorldThread.start();
        starCitizenServerStatusThread.start();
    }

}