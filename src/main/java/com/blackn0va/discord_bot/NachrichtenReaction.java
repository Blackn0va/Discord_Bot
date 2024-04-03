package com.blackn0va.discord_bot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.awt.Color;
import com.theokanning.openai.completion.chat.ChatMessage;
/**
*
* @author Black
*/
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class NachrichtenReaction extends ListenerAdapter {
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
                    // Ausgabe der Nachrichtendetails in der Konsole
                    System.out.println("Nachricht von: " + ereignis.getAuthor().getName() + " auf: "
                            + ereignis.getGuild().getName() + " in: " + ereignis.getChannel().getName() + " mit: "
                            + ereignis.getMessage().getContentStripped());

                    // Schreiben der Nachrichtendetails in die Log-Datei
                    WriteLogs.chat("Nachricht von: " + ereignis.getAuthor().getName() + " auf: "
                            + ereignis.getGuild().getName() + " in: " + ereignis.getChannel().getName() + " mit: "
                            + ereignis.getMessage().getContentStripped());

                    // Überprüfen, ob die Nachricht mit "!" beginnt
                    if (ereignis.getMessage().getContentStripped().startsWith("!")) {
                        // Wenn die Nachricht "!regeln" ist
                        if (ereignis.getMessage().getContentStripped().equalsIgnoreCase("!regeln")) {
                            // Löschen der Nachricht
                            ereignis.getMessage().delete().queue();

                            // Senden einer Schreibaktion
                            ereignis.getChannel().sendTyping().queue();
                            // Senden der Regeln an den Kanal
                            SendMessage.toChannel(ereignis.getChannel().getId(),
                                    "Regeln auf " + ereignis.getGuild().getName(), regeln, Color.GREEN);
                            // Schreiben eines Log-Eintrags, dass die Regeln angezeigt wurden
                            WriteLogs.permissions("Regeln wurden angezeigt");

                            // Wenn die Nachricht "!gpt" enthält
                        } else if (ereignis.getMessage().getContentStripped().contains("!gpt")) {

                            // Extrahieren der Nachricht ohne "!gpt"
                            String message = ereignis.getMessage().getContentStripped().substring(4);

                            // Anfordern einer Antwort von GPT-3 in einem separaten Thread
                            Future<String> futureAnswer = gpt4all.getAnswerInThread(message);
                            new Thread(() -> {
                                try {
                                    // Erhalten der Antwort von GPT-3
                                    String answer = futureAnswer.get();
                                    // Senden einer Schreibaktion
                                    ereignis.getChannel().sendTyping().queue();
                                    // Senden der Antwort von GPT-3 an den Kanal
                                    SendMessage.toChannel(ereignis.getChannel().getId(), "GPT-3 Antwort", answer,
                                            Color.GREEN);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }).start();
                        }

                    }
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

}