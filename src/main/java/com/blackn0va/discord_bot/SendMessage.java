package com.blackn0va.discord_bot;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
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

public class SendMessage {

        public static void ToNewsChannel(String Message) {
        try {
            // foreach server send message to channel where name is #📣rsi-news
            for (Guild guild : discordBot.bauplan.getGuilds()) {
                for (TextChannel channel : guild.getTextChannels()) {
                    if (channel.getName().equals("📣rsi-news")) {
                        channel.sendMessage( Message).queue();
                    }
                }
            }

            Main.RSSNews = "";
        } catch (Exception e) {
            System.out.println("Error: " + e);
            WriteLogs.writeLog("Error: " + e);
        }

    }

    

    public static void toChannel(String ChannelID, String Title, String Message, Color color) {
        try {
            TextChannel channel = discordBot.bauplan.getTextChannelById(ChannelID);
            if (channel == null) {
                WriteLogs.writeLog("Channel nicht gefunden: " + ChannelID);
                return;
            }

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(Title)
                    .setDescription(Message)
                    .setColor(color)
                    .setTimestamp(java.time.Instant.now())
                    .setFooter(Main.Footer, Main.IconURL);

            //channel.sendMessageEmbeds(embed.build()).queue();

            channel.sendMessageEmbeds(embed.build()).queue(sentMessage -> {
                sentMessage.addReaction(Emoji.fromUnicode("\u2705")).queue();
            });

            channel = null;
            embed = null;
            Message = null;
            Title = null;
            color = null;
            ChannelID = null;
            System.gc();
        } catch (Exception e) {
            WriteLogs.writeLog("Fehler in toChannel: " + e.getMessage());
        }
    }


    public static void toChannelWithLink(String ChannelID, String Title, String Message, Color color, String Url, String Picture) {
        try {
            TextChannel channel = discordBot.bauplan.getTextChannelById(ChannelID);
            if (channel == null) {
                WriteLogs.writeLog("Channel nicht gefunden: " + ChannelID);
                return;
            }

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(Title)
                    .setDescription(Message)
                    .setColor(color)
                    .setUrl(Url)
                    .setImage(Picture)
                    .setTimestamp(java.time.Instant.now())
                    .setFooter(Main.Footer, Main.IconURL);

            //channel.sendMessageEmbeds(embed.build()).queue();

            channel.sendMessageEmbeds(embed.build()).queue(sentMessage -> {
                sentMessage.addReaction(Emoji.fromUnicode("\u2705")).queue();
            });

            channel = null;
            embed = null;
            Message = null;
            Title = null;
            color = null;
            ChannelID = null;
            System.gc();
        } catch (Exception e) {
            WriteLogs.writeLog("Fehler in toChannel: " + e.getMessage());
        }
    }

    private static RestAction<Void> selfDestruct(MessageChannel channel, String content,
            ScheduledExecutorService scheduler) {
        return channel.sendMessage("Die Nachricht wird in 1 Minute gelöscht!")
                .delay(5, TimeUnit.SECONDS, scheduler) // edit 10 seconds later
                .flatMap((it) -> it.editMessage(content))
                .delay(1, TimeUnit.MINUTES, scheduler) // delete 1 minute later
                .flatMap(Message::delete);
    }

    public static void SendSelfDestruct(String ChannelID, String Message) {
        try {
            // Erstelle eine Instanz des MessageChannel-Objekts
            MessageChannel channel = discordBot.bauplan.getTextChannelById(ChannelID);
            // Erstelle einen ScheduledExecutorService-Objekt
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            // Rufe die selfDestruct()-Methode auf
            RestAction<Void> action = selfDestruct(channel, Message, scheduler);
            action.queue();
            channel = null;
            Message = null;
            scheduler = null;
            action = null;
            System.gc();
        } catch (Exception e) {
            WriteLogs.writeLog("Fehler in SendSelfDestruct: " + e.getMessage());
        }

    }

    // async send file to channel
    public static void FileToChannel(String ChannelID, String filePath) {
        try {
            TextChannel channel = discordBot.bauplan.getTextChannelById(ChannelID);

            // Lese den Inhalt der Datei in einen byte-Array
            byte[] fileContent = Files
                    .readAllBytes(Paths.get(Main.jarPath + File.separator + filePath));
            // Erstelle einen InputStream aus dem byte-Array
            InputStream inputStream = new ByteArrayInputStream(fileContent);
            // Erstelle ein FileUpload-Objekt aus dem InputStream und dem Dateinamen
            FileUpload fileUpload = FileUpload.fromData(inputStream, filePath);
            // Lade das FileUpload-Objekt hoch
            channel.sendFiles(fileUpload).queue();

            fileContent = null;
            inputStream = null;
            fileUpload = null;
            System.gc();
        } catch (Exception e) {
            WriteLogs.writeLog("Fehler in FileToChannel: " + e.getMessage());
        }

    }

    // Send Filestream to Channel
    public static void StreamToChannel(String ChannelID, String Message) {
        try {
            TextChannel channel = discordBot.bauplan.getTextChannelById(ChannelID);

            // Erstelle einen InputStream aus dem Inhalt der TextArea
            InputStream inputStream = new ByteArrayInputStream(Message.getBytes(StandardCharsets.UTF_8));
            // Erstelle ein FileUpload-Objekt aus dem InputStream und dem Dateinamen
            FileUpload fileUpload = FileUpload.fromData(inputStream, "log.txt");
            // Lade das FileUpload-Objekt hoch
            channel.sendFiles(fileUpload).queue();

            Message = null;
            channel = null;
            inputStream = null;
            fileUpload = null;

            System.gc();

        } catch (Exception e) {
            WriteLogs.writeLog("Fehler in StreamToChannel: " + e.getMessage());
        }
    }

    // async send message to User
    public static void ToUser(String userID, String Title, String message, Color Color) {
        try {

            int index = 0;
            while (index < message.length()) {
                int endIndex = Math.min(index + 3800, message.length());
                String block = message.substring(index, endIndex);

                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle(Title)
                        .setDescription(block)
                        .setColor(Color)
                        .setTimestamp(java.time.Instant.now())
                        .setFooter(Main.Footer, Main.IconURL);

                discordBot.bauplan.getUserById(userID).openPrivateChannel().queue((channel) -> {
                    channel.sendMessageEmbeds(embed.build()).queue();
                });
            }
            message = null;
            Title = null;
            Color = null;
            userID = null;
            System.gc();
        } catch (Exception e) {
            WriteLogs.writeLog("Fehler in ToUser: " + e.getMessage());
        }
    }

    // main
    public static void main(String[] args) throws IOException {

    }

}