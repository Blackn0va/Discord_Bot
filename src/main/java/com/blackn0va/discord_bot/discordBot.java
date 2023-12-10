package com.blackn0va.discord_bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class discordBot {
    public static JDA bauplan;

    public static void start() {
        try {

            // JDA bauplan = JDABuilder.createDefault(token).build();
            JDABuilder builder = JDABuilder.createDefault(Main.token)
                    .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT,
                            GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_MEMBERS,
                            GatewayIntent.DIRECT_MESSAGE_REACTIONS,
                            GatewayIntent.GUILD_EMOJIS_AND_STICKERS, GatewayIntent.GUILD_MESSAGE_REACTIONS,
                            GatewayIntent.DIRECT_MESSAGE_TYPING, GatewayIntent.GUILD_WEBHOOKS,
                            GatewayIntent.GUILD_VOICE_STATES)
                    .setAutoReconnect(true)
                    .addEventListeners(new NachrichtenReaction(), new GiveRole());
            configureMemoryUsage(builder);
            bauplan = builder.build();
            bauplan.getPresence().setStatus(OnlineStatus.ONLINE);
            bauplan.awaitStatus(JDA.Status.CONNECTED);

            // bauplan.addEventListener(new NachrichtenReaction());
            WriteLogs.writeLog("Discord Bot gestartet");
        } catch (Exception e) {
            WriteLogs.writeLog("Fehler beim Starten von DC: " + e.getMessage());
            restartDiscordBot();

        }

        try {
            Main.GPTChannelID = bauplan.getTextChannelsByName("chatgpt", true).get(0).getId();
            Main.SCNewsChannelID = bauplan.getTextChannelsByName("📣rsi-news", true).get(0).getId();

            // System.out.println(RegelnAkzeptiert);

        } catch (Exception e) {
        }
    }

    public static void configureMemoryUsage(JDABuilder bauplan) {
        // Disable cache for member activities (streaming/games/spotify)
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
 
        bauplan.setMemberCachePolicy(MemberCachePolicy.ALL);


        // Only cache members who are either in a voice channel or owner of the guild
        // Disable member chunking on startup
        bauplan.setChunkingFilter(ChunkingFilter.NONE);
        // Consider guilds with more than 50 members as "large".
        // Large guilds will only provide online members in their setup and thus reduce
        // bandwidth if chunking is disabled.
        bauplan.setLargeThreshold(5);
    }

    public static void stop() {
        WriteLogs.writeLog("Discord Bot wird gestoppt");
        System.out.println("Discord Bot wird gestoppt");
        if (bauplan != null) { // Check if bauplan is not null
            bauplan.shutdown(); // Call the shutdown() method on bauplan
            try {
                WriteLogs.writeLog("Warte auf Disconnect von DC...");
                System.out.println("Warte auf Disconnect von DC...");
                // Create a new thread that will wait for the bot to disconnect
                Thread disconnectThread = new Thread(() -> {
                    try {
                        bauplan.awaitStatus(JDA.Status.SHUTDOWN);
                    } catch (InterruptedException e) {
                        WriteLogs.writeLog("Fehler beim warten: " + e);
                        System.out.println("Fehler beim warten: " + e);
                    }
                });
                disconnectThread.start();
                // Wait for the disconnect thread to finish, with a timeout
                disconnectThread.join(5000); // Wait for 5 seconds
                if (disconnectThread.isAlive()) {
                    WriteLogs.writeLog("DC hat nicht reagiert. Beende den Thread...");
                    System.out.println("DC hat nicht reagiert. Beende den Thread...");
                    disconnectThread.interrupt(); // Interrupt the thread if it's still running
                }
            } catch (InterruptedException e) {
                WriteLogs.writeLog("Fehler beim warten: " + e);
            } finally {
                // Trennen die Verbindung und beende alle Hintergrundthreads
                WriteLogs.writeLog("Trenne die Verbindung und beende alle Hintergrundthreads");
                System.out.println("Trenne die Verbindung und beende alle Hintergrundthreads");
                bauplan.shutdownNow();
                bauplan = null; // Set bauplan to null
                // Garbage Collect
                System.gc();
            }

            WriteLogs.writeLog("Discord Bot gestoppt");
            System.out.println("Discord Bot gestoppt");

        } else {
            // Handle the case where bauplan is null
            WriteLogs.writeLog("Discord Bot ist nicht gestartet");
            System.out.println("Discord Bot ist nicht gestartet");
        }
    }

    public static void restartDiscordBot() {
        stop();
        try {
            Thread.sleep(5000); // Wait for 5 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        start();
    }
}