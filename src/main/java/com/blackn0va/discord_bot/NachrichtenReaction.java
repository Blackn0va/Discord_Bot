package com.blackn0va.discord_bot;

import java.util.ArrayList;
import java.util.List;

import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;

/**
*
* @author Black
*/
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class NachrichtenReaction extends ListenerAdapter {

    public static final List<ChatMessage> messages = new ArrayList<>();
    public static String regeln = "\u00A7 1. Sei respektvoll gegen\u00FCber anderen Nutzern hier im Discord - eine h\u00F6fliche und angenehme Atmosph\u00E4re muss gewahrt werden.\n\n\u00A7 2. Alle Formen der Beleidigungen, Diskriminierung, Rassismus oder Antisemitismus und jegliche andere Formen des deutschen Rechts, die diesem widersprechen, sind hier in Text- oder Sprachkan\u00E4len strengstens untersagt.\n\n\u00A7 3. Werbung f\u00FCr eigene oder fremde Inhalte ist hier untersagt. Damit ist auch gemeint, den eigenen oder einen fremden Discord oder \u00C4hnliches zu bewerben. Auch in Privatnachrichten ist dies untersagt. \n\n\u00A7 4. In Chats d\u00FCrfen nur Inhalte diskutiert werden, die Themengerecht sind in den jeweiligen Kan\u00E4len. Als Community - Sprache gilt ausschlie\u00DFlich deutsch. Fremde Sprachen in Wort und Schrift werden hier nicht geduldet und untersagt. Du musst Dich so gut es geht bem\u00FChen, einigerma\u00DFen die geltende deutsche Grammatik einzuhalten.\n \n\u00A7 5. Trolling und Spamming sind komplett zu unterlassen, dazu unterl\u00E4sst Du es auch unn\u00F6tige Diskussionen zu starten, die nichts mit irgendeinem Thema hier im Discord zu tun haben.\n\n\u00A7 6. Soundboards oder Stimmverzerrer oder die Aufnahme von Gespr\u00E4chen sind untersagt, letzteres ist auch gesetzlich im \u00DCbrigen verboten.\n\n\u00A7 7. Das dauerhafte unn\u00F6tige Verlassen und Betreten eines Sprachkanals ist zu unterlassen. \n\n\u00A7 8. St\u00F6rger\u00E4usche wie Handy, Fernseher und andere Dinge, die im Hintergrund zu h\u00F6ren sind, sind zu unterlassen.\n\u00A7 9. In den Sprach Channel zu kommen und dauerhaft nichts zu sagen oder sich dann stumm zu schalten ist sehr unh\u00F6flich und sollte unterlassen werden. Ansonsten gehe daf\u00FCr in den AFK/Pause Channel, dort ist es erlaubt. \n Wenn du die Regeln gelesen hast, dann klicke auf das ✅\n\n";

    @Override
    public void onMessageReceived(MessageReceivedEvent ereignis) {

        try {
            if (ereignis.isFromGuild()) {
                // Wenn es ein Bot ist, dann wird die Nachricht nicht weiter verarbeitet
                if (!ereignis.getAuthor().isBot()) {
                    System.out.println("Nachricht von: " + ereignis.getAuthor().getName() + " auf: "
                            + ereignis.getGuild().getName() + " in: " + ereignis.getChannel().getName() + " mit: "
                            + ereignis.getMessage().getContentStripped());
                    if (ereignis.getChannel().getId().equals(Main.GPTChannelID)) {

                        // if content do not start with ! then send message ignore Case sensitive
                        if (!ereignis.getMessage().getContentStripped().startsWith("!")) {

                            String frage = ereignis.getMessage().getContentStripped();
                            System.out.println("Frage:\n" + frage);

                            // Create message
                            final ChatMessage systemMessage = new ChatMessage(ChatMessageRole.USER.value(), frage);

                            // Add message to list
                            messages.add(systemMessage);

                            // ask GPT
                            openai.getAnswer(frage);

                            // openai.getAnswer(ereignis.getMessage().toString());
                            if (Main.answer.contains("You exceeded your current quota")) {
                                // answer = "Ich habe keine Antwort gefunden";
                                ereignis.getChannel().sendTyping().queue();
                                ereignis.getChannel()
                                        .sendMessage("Du hast dein aktuelles Kontingent überschritten!")
                                        .queue();
                                WriteLogs.writeLog("Du hast dein aktuelles Kontingent überschritten!");
                            } else {

                                if (Main.answer.contains("Incorrect API key")) {
                                    ereignis.getChannel().sendTyping().queue();
                                    ereignis.getChannel()
                                            .sendMessage("API Key überprüfen!")
                                            .queue();
                                    WriteLogs.writeLog("API Key überprüfen!");
                                } else {
                                    ereignis.getChannel().sendTyping().queue();
                                    ereignis.getChannel().sendMessage(Main.answer)
                                            .queue();
                                    WriteLogs.writeLog("Antwort: " + Main.answer);
                                }

                            }
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
