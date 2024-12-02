package com.blackn0va.discord_bot;

import java.awt.Color;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 *
 * @author Black
 */

public class DiscordMessageReaction extends ListenerAdapter {

    // Diese Methode wird aufgerufen, wenn eine Nachricht empfangen wird
    @Override
    public void onMessageReceived(MessageReceivedEvent ereignis) {

        try {
            // Überprüfen, ob die Nachricht von einem Server (Gilde) kommt
            if (ereignis.isFromGuild()) {
                // Wenn die Nachricht von einem Bot kommt, wird sie nicht weiter verarbeitet
                if (!ereignis.getAuthor().isBot()) {
                    // Fügen Sie das Ereignis zur Warteschlange hinzu
                    Main.messageQueue.add(ereignis);
                }
            }

            // Behandlung von Ausnahmen
        } catch (Exception e) {
            // Schreiben der Fehlermeldung in die Log-Datei
            WriteLogs.writeLog("Fehler in onMessageReceived: " + e.getMessage());
        }

    }

    // Starten Sie einen neuen Thread, um Nachrichten zu verarbeiten
    public static void startMessageProcessing() {
        new Thread(() -> {
            while (true) {
                // Holen Sie das erste Ereignis aus der Warteschlange
                MessageReceivedEvent queuedEvent = Main.messageQueue.poll();
                if (queuedEvent != null) {
                    // Ihr Code zur Verarbeitung des Ereignisses geht hier
                    // Zum Beispiel:
                    String user = queuedEvent.getAuthor().getName();
                    String message = queuedEvent.getMessage().getContentDisplay();
                    String channel = queuedEvent.getChannel().getName();
                    String guild = queuedEvent.getGuild().getName();
                    WriteLogs
                            .chat("Nachricht von: " + user + " auf: " + guild + " in: " + channel + " mit: " + message);

                    // Überprüfen, ob die Nachricht mit "!" beginnt
                    if (message.startsWith("!")) {
                        // Wenn die Nachricht "!regeln" ist
                        if (message.equalsIgnoreCase("!regeln")) {
                            // Löschen der Nachricht
                            queuedEvent.getMessage().delete().queue();

                            // Senden einer Schreibaktion
                            queuedEvent.getChannel().sendTyping().queue();
                            // Senden der Regeln an den Kanal
                            DiscordSendMessage.toChannel(queuedEvent.getChannel().getId(), "Regeln auf " + guild,
                                    Main.regeln,
                                    Color.GREEN);
                            // Schreiben eines Log-Eintrags, dass die Regeln angezeigt wurden
                            WriteLogs.permissions("Regeln wurden angezeigt " + user);
                        }

                    }

                }
                // Warten Sie eine kurze Zeit, bevor Sie die nächste Nachricht verarbeiten
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

}