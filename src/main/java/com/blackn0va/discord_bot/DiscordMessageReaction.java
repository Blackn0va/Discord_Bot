package com.blackn0va.discord_bot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.awt.Color;
import com.theokanning.openai.completion.chat.ChatMessage;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
/**
*
* @author Black
*/
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DiscordMessageReaction extends ListenerAdapter {
    // Erstellen Sie eine Warteschlange für MessageReceivedEvent
    private static ConcurrentLinkedQueue<MessageReceivedEvent> messageQueue = new ConcurrentLinkedQueue<>();

    // Eine Liste zum Speichern von Chat-Nachrichten
    public static final List<ChatMessage> messages = new ArrayList<>();
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
            "Wenn du die Regeln gelesen und verstanden hast, dann klicke auf das ✅\n\n";

    // Diese Methode wird aufgerufen, wenn eine Nachricht empfangen wird
    @Override
    public void onMessageReceived(MessageReceivedEvent ereignis) {

        try {
            // Überprüfen, ob die Nachricht von einem Server (Gilde) kommt
            if (ereignis.isFromGuild()) {
                // Wenn die Nachricht von einem Bot kommt, wird sie nicht weiter verarbeitet
                if (!ereignis.getAuthor().isBot()) {
                    // Fügen Sie das Ereignis zur Warteschlange hinzu
                    messageQueue.add(ereignis);
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
                MessageReceivedEvent queuedEvent = messageQueue.poll();
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
                            DiscordSendMessage.toChannel(queuedEvent.getChannel().getId(), "Regeln auf " + guild, regeln,
                                    Color.GREEN);

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
                                    DiscordSendMessage.toChannel(queuedEvent.getChannel().getId(), "GPT-3 Antwort", answer,
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