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

    @Override
    public void onMessageReceived(MessageReceivedEvent ereignis) {

        try {
            if (ereignis.isFromGuild()) {
                // Wenn es ein Bot ist, dann wird die Nachricht nicht weiter verarbeitet
                if (ereignis.getAuthor().isBot()) {
                    return;
                } else { // wenn der Chat ein Admin ist
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
