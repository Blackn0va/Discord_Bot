package com.blackn0va.discord_bot;

import java.awt.Color;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.*;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class DiscordSendMessage {

    // Methode zum Senden einer Nachricht an einen bestimmten Kanal
    public static void toChannel(String ChannelID, String Title, String Message, Color color) {
        try {
            Message = Message.replace("[", "").replace("]", "");
            // Abrufen des Kanals anhand der ChannelID
            TextChannel channel = Main.bauplan.getTextChannelById(ChannelID);
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

            // Add buttons weiter und zurück
            channel.sendMessageEmbeds(embed.build()).setActionRow(
                    Button.primary("RegelnAkzeptieren", "Regeln Akzeptieren"),
                    Button.primary("RegelnAblehnen", "Ablehnen")).queue(sentMessage -> {
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
            // remove [ and ] from the message
            Message = Message.replace("[", "").replace("]", "");
            // Abrufen des Kanals anhand der ChannelID
            TextChannel channel = Main.bauplan.getTextChannelById(ChannelID);
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
                // add fire
                sentMessage.addReaction(Emoji.fromUnicode("\uD83D\uDD25")).queue();
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

    // editMessageEmbeds
    public static void editMessage(String ChannelID, String Title, String Message, String messageID) {
        try {
            Message = Message.replace("[", "").replace("]", "");
            // Abrufen des Kanals anhand der ChannelID
            TextChannel channel = Main.bauplan.getTextChannelById(ChannelID);
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
                    .setColor(Color.GREEN)
                    .setTimestamp(java.time.Instant.now())
                    .setFooter(Main.Footer, Main.IconURL);

            // Add buttons weiter und zurück
            channel.editMessageEmbedsById(messageID, embed.build()).setActionRow(
                    Button.primary("RegelnAkzeptieren", "Regeln Akzeptieren"),
                    Button.primary("RegelnAblehnen", "Ablehnen")).queue(sentMessage -> {
                    });

            // Setzen Sie alle Variablen auf null und rufen Sie den Garbage Collector auf,
            // um den Speicher freizugeben
            channel = null;
            embed = null;
            Title = null;
            ChannelID = null;
            System.gc();
        } catch (Exception e) {
            // Schreiben Sie eine Fehlermeldung in das Protokoll, wenn ein Fehler auftritt
            WriteLogs.writeLog("Fehler in toChannel: " + e.getMessage());
        }

    }

}