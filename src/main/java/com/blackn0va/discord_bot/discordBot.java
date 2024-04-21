package com.blackn0va.discord_bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class DiscordBot {

    // Methode zum Starten des Discord Bots
    public static void start() {
        try {
            // disable logging complete
            // Erstellen eines JDABuilder mit dem Token aus der Main-Klasse
            JDABuilder builder = JDABuilder.createDefault(Main.token)
                    .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT,
                            GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_MEMBERS,
                            GatewayIntent.DIRECT_MESSAGE_REACTIONS,
                            GatewayIntent.GUILD_EMOJIS_AND_STICKERS, GatewayIntent.GUILD_MESSAGE_REACTIONS,
                            GatewayIntent.DIRECT_MESSAGE_TYPING, GatewayIntent.GUILD_WEBHOOKS,
                            GatewayIntent.GUILD_VOICE_STATES)
                    .setAutoReconnect(true)
                    .addEventListeners(new DiscordMessageReaction(), new DiscordGiveRole());
            // Konfigurieren der Speichernutzung des Bots
            configureMemoryUsage(builder);
            // Erstellen des Bots und setzen des Status auf ONLINE
            Main.bauplan = builder.build();
            Main.bauplan.getPresence().setStatus(OnlineStatus.ONLINE);
            // Warten, bis der Bot mit Discord verbunden ist
            Main.bauplan.awaitStatus(JDA.Status.CONNECTED);

            System.out.println("Discord Bot gestartet");
            WriteLogs.writeLog("Discord Bot gestartet");

            DiscordMessageReaction.startMessageProcessing();

        } catch (Exception e) {
            WriteLogs.writeLog("Fehler beim Starten von DC: ");
            System.out.println("Fehler beim Starten von DC: ");
            // Bei einem Fehler beim Starten des Bots, versuchen, den Bot neu zu starten
            restartDiscordBot();
        }

        try {
            // Speichern der IDs der Textkanäle "chatgpt" und "📣rsi-news" in der
            // Main-Klasse
            Main.GPTChannelID = Main.bauplan.getTextChannelsByName("chatgpt", true).get(0).getId();
            Main.StarCitizenPatchChannelID = Main.bauplan.getTextChannelsByName("📣rsi-news", true).get(0).getId();
        } catch (Exception e) {
        }
    }

    // Methode zum Konfigurieren der Speichernutzung des Discord Bots
    public static void configureMemoryUsage(JDABuilder bauplan) {

        // Deaktivieren des Caches für verschiedene Funktionen
        bauplan.disableCache(CacheFlag.ACTIVITY);
        bauplan.disableCache(CacheFlag.CLIENT_STATUS);
        bauplan.disableCache(CacheFlag.EMOJI);
        bauplan.disableCache(CacheFlag.FORUM_TAGS);
        bauplan.disableCache(CacheFlag.MEMBER_OVERRIDES);
        bauplan.disableCache(CacheFlag.ONLINE_STATUS);
        bauplan.disableCache(CacheFlag.ROLE_TAGS);
        bauplan.disableCache(CacheFlag.VOICE_STATE);
        bauplan.disableCache(CacheFlag.SCHEDULED_EVENTS);
        bauplan.disableCache(CacheFlag.STICKER);

        // Cachen aller Mitglieder
        bauplan.setMemberCachePolicy(MemberCachePolicy.ALL);

        // Nur Mitglieder cachen, die entweder in einem Sprachkanal sind oder Besitzer
        // der Gilde sind
        bauplan.setChunkingFilter(ChunkingFilter.NONE);
        // Gilden mit mehr als 50 Mitgliedern als "groß" betrachten
        //
        bauplan.setLargeThreshold(5);
    }

    // Methode zum Stoppen des Discord Bots
    public static void stop() {
        WriteLogs.writeLog("Discord Bot wird gestoppt");
        System.out.println("Discord Bot wird gestoppt");
        if (Main.bauplan != null) { // Überprüfen, ob bauplan nicht null ist
            Main.bauplan.shutdown(); // Aufrufen der shutdown()-Methode auf bauplan
            try {
                WriteLogs.writeLog("Warte auf Disconnect von DC...");
                System.out.println("Warte auf Disconnect von DC...");
                // Erstellen eines neuen Threads, der auf das Trennen des Bots wartet
                Thread disconnectThread = new Thread(() -> {
                    try {
                        Main.bauplan.awaitStatus(JDA.Status.SHUTDOWN);
                    } catch (InterruptedException e) {
                        WriteLogs.writeLog("Fehler beim warten: ");
                        System.out.println("Fehler beim warten: ");
                    }
                });
                disconnectThread.start();
                // Warten auf das Beenden des Trennungs-Threads, mit einem Timeout
                disconnectThread.join(5000); // Warten für 5 Sekunden
                if (disconnectThread.isAlive()) {
                    WriteLogs.writeLog("DC hat nicht reagiert. Beende den Thread...");
                    System.out.println("DC hat nicht reagiert. Beende den Thread...");
                    disconnectThread.interrupt(); // Unterbrechen des Threads, wenn er noch läuft
                }
            } catch (InterruptedException e) {
                WriteLogs.writeLog("Fehler beim warten: ");
                System.out.println("Fehler beim warten: ");
            } finally {
                // Trennen die Verbindung und beende alle Hintergrundthreads
                WriteLogs.writeLog("Trenne die Verbindung und beende alle Hintergrundthreads");
                System.out.println("Trenne die Verbindung und beende alle Hintergrundthreads");
                Main.bauplan.shutdownNow();
                Main.bauplan = null; // Setzen von bauplan auf null
                // Garbage Collect
                System.gc();
            }

            WriteLogs.writeLog("Discord Bot gestoppt");
            System.out.println("Discord Bot gestoppt");

        } else {
            // Behandeln des Falls, in dem bauplan null ist
            WriteLogs.writeLog("Discord Bot ist nicht gestartet");
            System.out.println("Discord Bot ist nicht gestartet");
        }
    }

    // Methode zum Neustarten des Discord Bots
    public static void restartDiscordBot() {
        System.out.println("Neustart des Discord Bots");
        stop();
        try {
            Thread.sleep(5000); // Warten für 5 Sekunden
        } catch (InterruptedException e) {
            System.out.println("Fehler beim Warten: ");
        }
        start();
    }
}