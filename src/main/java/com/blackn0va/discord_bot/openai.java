package com.blackn0va.discord_bot;

import java.util.HashMap;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.service.OpenAiService;

public class openai {

    // funktion return string antwort
    public static String getAnswer(String question) {
        try {
            OpenAiService service = new OpenAiService(Main.openaitoken);

            ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                    .builder()
                    .model("gpt-3.5-turbo")
                    .temperature(0.5)
                    .presencePenalty(0.0)
                    .frequencyPenalty(0.5)
                    .messages(NachrichtenReaction.messages)
                    .n(1)
                    .maxTokens(200)
                    .logitBias(new HashMap<>())
                    .build();

            // service.createCompletion(completionRequest).getChoices().forEach(System.out::print);
            Main.answer = service.createChatCompletion(chatCompletionRequest).getChoices().toString()
                    .replace("[ChatCompletionChoice(index=0, message=ChatMessage(role=assistant, content=", "");

            // if answer contains "), finishReason=length)]" remove it
            if (Main.answer.contains("), finishReason=length)]")) {
                Main.answer = Main.answer.replace("), finishReason=length)]", "");
            }
            // if answer contains "), finishReason=stop)]" remove it
            if (Main.answer.contains("), finishReason=stop)]")) {
                Main.answer = Main.answer.replace("), finishReason=stop)]", "");
            }

            System.out.println("Antwort:\n" + Main.answer);
            WriteLogs.writeLog("Antwort:\n" + Main.answer);

            // NachrichtenReaction.answer =
            // NachrichtenReaction.answer.replace("ReceivedMessage(*)", "");

            return Main.answer;

        } catch (Exception e) {
            System.out.println("Fehler beim Erstellen der Antwort");
            //e.printStackTrace();
            WriteLogs.writeLog("Fehler beim Erstellen der Antwort");
            Main.answer = e.toString();

        }
        return Main.answer;
    }

}
