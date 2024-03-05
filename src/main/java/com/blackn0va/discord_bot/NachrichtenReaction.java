package com.blackn0va.discord_bot;

import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import com.theokanning.openai.completion.chat.ChatMessage;
/**
*
* @author Black
*/
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class NachrichtenReaction extends ListenerAdapter {

    public static final List<ChatMessage> messages = new ArrayList<>();
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

    @Override
    public void onMessageReceived(MessageReceivedEvent ereignis) {

        try {
            if (ereignis.isFromGuild()) {
                // Wenn es ein Bot ist, dann wird die Nachricht nicht weiter verarbeitet
                if (!ereignis.getAuthor().isBot()) {
                    System.out.println("Nachricht von: " + ereignis.getAuthor().getName() + " auf: "
                            + ereignis.getGuild().getName() + " in: " + ereignis.getChannel().getName() + " mit: "
                            + ereignis.getMessage().getContentStripped());

                    WriteLogs.chat("Nachricht von: " + ereignis.getAuthor().getName() + " auf: "
                            + ereignis.getGuild().getName() + " in: " + ereignis.getChannel().getName() + " mit: "
                            + ereignis.getMessage().getContentStripped());

                    if (ereignis.getMessage().getContentStripped().startsWith("!")) {
                        if (ereignis.getMessage().getContentStripped().equalsIgnoreCase("!regeln")) {
                            // delete message
                            ereignis.getMessage().delete().queue();

                            ereignis.getChannel().sendTyping().queue();
                            SendMessage.toChannel(ereignis.getChannel().getId(),
                                    "Regeln auf " + ereignis.getGuild().getName(), regeln, Color.GREEN);
                            WriteLogs.permissions("Regeln wurden angezeigt");

                        } else if (ereignis.getMessage().getContentStripped().contains("!gpt")) {

                            // get message without !gpt
                            String message = ereignis.getMessage().getContentStripped().substring(4);

                            String answer = gpt4all.getAnswer(message);
                            ereignis.getChannel().sendTyping().queue();
                            SendMessage.toChannel(ereignis.getChannel().getId(), "GPT-3 Antwort", answer, Color.GREEN);

                        }

                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Fehler in onMessageReceived: " + e.getMessage());
            WriteLogs.writeLog("Fehler in onMessageReceived: " + e.getMessage());
        }

    }

}
