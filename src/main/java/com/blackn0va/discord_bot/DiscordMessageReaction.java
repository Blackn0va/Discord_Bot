package com.blackn0va.discord_bot;

import java.awt.Color;
import java.util.concurrent.Future;
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
            // Ausgabe der Fehlermeldung in der Konsole
            System.out.println("Fehler in onMessageReceived: " + e.getMessage());
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

                    System.out.println(
                            "Nachricht von: " + user + " auf: " + guild + " in: " + channel + " mit: " + message);

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

                         
                            // add 1 button

                            // Schreiben eines Log-Eintrags, dass die Regeln angezeigt wurden
                            WriteLogs.permissions("Regeln wurden angezeigt " + user);

                            // Wenn die Nachricht "!gpt" enthält
                        } else if (message.contains("!gpt")) {
                            // Anfordern einer Antwort von GPT-3 in einem separaten Thread
                            Future<String> futureAnswer = gpt4all.getAnswerInThread(message.substring(4));
                            new Thread(() -> {
                                try {
                                    // Erhalten der Antwort von GPT-3
                                    String answer = futureAnswer.get();
                                    // Senden einer Schreibaktion
                                    queuedEvent.getChannel().sendTyping().queue();
                                    // Senden der Antwort von GPT-3 an den Kanal
                                    DiscordSendMessage.toChannel(queuedEvent.getChannel().getId(), "GPT-3 Antwort",
                                            answer,
                                            Color.GREEN);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }).start();
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