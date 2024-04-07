package com.blackn0va.discord_bot;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.emoji.*;

public class DiscordSendMessage {

    // Methode zum Senden einer Nachricht an den News-Kanal
    public static void ToNewsChannel(String Message) {
        try {
            // Durchlaufen Sie jeden Server und senden Sie die Nachricht an den Kanal,
            // dessen Name "#📣rsi-news" ist
            for (Guild guild : DiscordBot.bauplan.getGuilds()) {
                for (TextChannel channel : guild.getTextChannels()) {
                    // Überprüfen Sie, ob der Name des Kanals "#📣rsi-news" ist
                    if (channel.getName().equals("📣rsi-news")) {
                        // Wenn ja, senden Sie die Nachricht
                        channel.sendMessage(Message).queue();
                    }
                }
            }

            // Setzen Sie den RSSNews-String zurück
            Main.RSSNews = "";
        } catch (Exception e) {
            // Ausgabe einer Fehlermeldung und Schreiben der Fehlermeldung in die Protokolle
            System.out.println("Error: " + e);
            WriteLogs.writeLog("Error: " + e);
        }

    }

    // Methode zum Senden einer Nachricht an einen bestimmten Kanal
    public static void toChannel(String ChannelID, String Title, String Message, Color color) {
        try {
            // Abrufen des Kanals anhand der ChannelID
            TextChannel channel = DiscordBot.bauplan.getTextChannelById(ChannelID);
            // Wenn der Kanal nicht gefunden wird, schreiben Sie eine Fehlermeldung in das
            // Protokoll und beenden Sie die Methode
            if (channel == null) {
                WriteLogs.writeLog("Channel nicht gefunden: " + ChannelID);
                return;
            }

            // Erstellen eines eingebetteten Nachrichtenbauers mit dem gegebenen Titel, der
            // Nachricht, der Farbe und dem Zeitstempel
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(Title)
                    .setDescription(Message)
                    .setColor(color)
                    .setTimestamp(java.time.Instant.now())
                    .setFooter(Main.Footer, Main.IconURL);

            // Senden der eingebetteten Nachricht an den Kanal und Hinzufügen einer Reaktion
            // zur gesendeten Nachricht
            channel.sendMessageEmbeds(embed.build()).queue(sentMessage -> {
                sentMessage.addReaction(Emoji.fromUnicode("\u2705")).queue();
            });

            // Setzen Sie alle Variablen auf null und rufen Sie den Garbage Collector auf,
            // um den Speicher freizugeben
            channel = null;
            embed = null;
            Message = null;
            Title = null;
            color = null;
            ChannelID = null;
            System.gc();
        } catch (Exception e) {
            // Schreiben Sie eine Fehlermeldung in das Protokoll, wenn ein Fehler auftritt
            WriteLogs.writeLog("Fehler in toChannel: " + e.getMessage());
        }
    }

    // Methode zum Senden einer Nachricht mit einem Link an einen bestimmten Kanal
    public static void toChannelWithLink(String ChannelID, String Title, String Message, Color color, String Url,
            String Picture) {
        try {
            // Abrufen des Kanals anhand der ChannelID
            TextChannel channel = DiscordBot.bauplan.getTextChannelById(ChannelID);
            // Wenn der Kanal nicht gefunden wird, schreiben Sie eine Fehlermeldung in das
            // Protokoll und beenden Sie die Methode
            if (channel == null) {
                WriteLogs.writeLog("Channel nicht gefunden: " + ChannelID);
                return;
            }

            // Erstellen eines eingebetteten Nachrichtenbauers mit dem gegebenen Titel, der
            // Nachricht, der Farbe, der URL, dem Bild und dem Zeitstempel
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(Title)
                    .setDescription(Message)
                    .setColor(color)
                    .setUrl(Url)
                    .setImage(Picture)
                    .setTimestamp(java.time.Instant.now())
                    .setFooter(Main.Footer, Main.IconURL);

            // Senden der eingebetteten Nachricht an den Kanal und Hinzufügen einer Reaktion
            // zur gesendeten Nachricht
            channel.sendMessageEmbeds(embed.build()).queue(sentMessage -> {
                sentMessage.addReaction(Emoji.fromUnicode("\u2705")).queue();
            });

            // Setzen Sie alle Variablen auf null und rufen Sie den Garbage Collector auf,
            // um den Speicher freizugeben
            channel = null;
            embed = null;
            Message = null;
            Title = null;
            color = null;
            ChannelID = null;
            System.gc();
        } catch (Exception e) {
            // Schreiben Sie eine Fehlermeldung in das Protokoll, wenn ein Fehler auftritt
            WriteLogs.writeLog("Fehler in toChannel: " + e.getMessage());
        }
    }

    // Methode zum Senden einer selbstzerstörenden Nachricht
    private static RestAction<Void> selfDestruct(MessageChannel channel, String content,
            ScheduledExecutorService scheduler) {
        // Senden Sie eine Nachricht, die besagt, dass die Nachricht in 1 Minute
        // gelöscht wird
        // Bearbeiten Sie die Nachricht nach 5 Sekunden
        // Löschen Sie die Nachricht nach 1 Minute
        return channel.sendMessage("Die Nachricht wird in 1 Minute gelöscht!")
                .delay(5, TimeUnit.SECONDS, scheduler)
                .flatMap((it) -> it.editMessage(content))
                .delay(1, TimeUnit.MINUTES, scheduler)
                .flatMap(Message::delete);
    }

    // Methode zum Senden einer selbstzerstörenden Nachricht an einen bestimmten
    // Kanal
    public static void SendSelfDestruct(String ChannelID, String Message) {
        try {
            // Erstellen Sie eine Instanz des MessageChannel-Objekts
            MessageChannel channel = DiscordBot.bauplan.getTextChannelById(ChannelID);
            // Erstellen Sie ein ScheduledExecutorService-Objekt
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            // Rufen Sie die selfDestruct()-Methode auf
            RestAction<Void> action = selfDestruct(channel, Message, scheduler);
            // Führen Sie die Aktion aus
            action.queue();
            // Setzen Sie alle Variablen auf null und rufen Sie den Garbage Collector auf,
            // um den Speicher freizugeben
            channel = null;
            Message = null;
            scheduler = null;
            action = null;
            System.gc();
        } catch (Exception e) {
            // Schreiben Sie eine Fehlermeldung in das Protokoll, wenn ein Fehler auftritt
            WriteLogs.writeLog("Fehler in SendSelfDestruct: " + e.getMessage());
        }
    }

    // Methode zum asynchronen Senden einer Datei an einen Kanal
    public static void FileToChannel(String ChannelID, String filePath) {
        try {
            // Abrufen des Kanals anhand der ChannelID
            TextChannel channel = DiscordBot.bauplan.getTextChannelById(ChannelID);

            // Lesen Sie den Inhalt der Datei in ein Byte-Array
            byte[] fileContent = Files
                    .readAllBytes(Paths.get(Main.jarPath + File.separator + filePath));
            // Erstellen Sie einen InputStream aus dem Byte-Array
            InputStream inputStream = new ByteArrayInputStream(fileContent);
            // Erstellen Sie ein FileUpload-Objekt aus dem InputStream und dem Dateinamen
            FileUpload fileUpload = FileUpload.fromData(inputStream, filePath);
            // Laden Sie das FileUpload-Objekt hoch
            channel.sendFiles(fileUpload).queue();

            // Setzen Sie alle Variablen auf null und rufen Sie den Garbage Collector auf,
            // um den Speicher freizugeben
            fileContent = null;
            inputStream = null;
            fileUpload = null;
            System.gc();
        } catch (Exception e) {
            // Schreiben Sie eine Fehlermeldung in das Protokoll, wenn ein Fehler auftritt
            WriteLogs.writeLog("Fehler in FileToChannel: " + e.getMessage());
        }

    }

    // Methode zum Senden eines Datei-Streams an einen Kanal
    public static void StreamToChannel(String ChannelID, String Message) {
        try {
            // Abrufen des Kanals anhand der ChannelID
            TextChannel channel = DiscordBot.bauplan.getTextChannelById(ChannelID);

            // Erstellen Sie einen InputStream aus dem Inhalt der Nachricht
            InputStream inputStream = new ByteArrayInputStream(Message.getBytes(StandardCharsets.UTF_8));
            // Erstellen Sie ein FileUpload-Objekt aus dem InputStream und dem Dateinamen
            FileUpload fileUpload = FileUpload.fromData(inputStream, "log.txt");
            // Laden Sie das FileUpload-Objekt hoch
            channel.sendFiles(fileUpload).queue();

            // Setzen Sie alle Variablen auf null und rufen Sie den Garbage Collector auf,
            // um den Speicher freizugeben
            Message = null;
            channel = null;
            inputStream = null;
            fileUpload = null;
            System.gc();

        } catch (Exception e) {
            // Schreiben Sie eine Fehlermeldung in das Protokoll, wenn ein Fehler auftritt
            WriteLogs.writeLog("Fehler in StreamToChannel: " + e.getMessage());
        }
    }

    // Methode zum asynchronen Senden einer Nachricht an einen Benutzer
    public static void ToUser(String userID, String Title, String message, Color Color) {
        try {
            // Initialisieren Sie den Index auf 0
            int index = 0;
            // Durchlaufen Sie die Nachricht in Blöcken von maximal 3800 Zeichen
            while (index < message.length()) {
                // Bestimmen Sie das Ende des aktuellen Blocks
                int endIndex = Math.min(index + 3800, message.length());
                // Extrahieren Sie den aktuellen Block aus der Nachricht
                String block = message.substring(index, endIndex);

                // Erstellen Sie einen eingebetteten Nachrichtenbauer mit dem gegebenen Titel,
                // Block, Farbe und Zeitstempel
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle(Title)
                        .setDescription(block)
                        .setColor(Color)
                        .setTimestamp(java.time.Instant.now())
                        .setFooter(Main.Footer, Main.IconURL);

                // Öffnen Sie einen privaten Kanal zum Benutzer und senden Sie die eingebettete
                // Nachricht
                DiscordBot.bauplan.getUserById(userID).openPrivateChannel().queue((channel) -> {
                    channel.sendMessageEmbeds(embed.build()).queue();
                });

                // Aktualisieren Sie den Index für den nächsten Durchlauf
                index = endIndex;
            }

            // Setzen Sie alle Variablen auf null und rufen Sie den Garbage Collector auf,
            // um den Speicher freizugeben
            message = null;
            Title = null;
            Color = null;
            userID = null;
            System.gc();
        } catch (Exception e) {
            // Schreiben Sie eine Fehlermeldung in das Protokoll, wenn ein Fehler auftritt
            WriteLogs.writeLog("Fehler in ToUser: " + e.getMessage());
        }
    }
}