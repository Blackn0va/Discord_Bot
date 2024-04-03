package com.blackn0va.discord_bot;

import java.util.HashMap;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.service.OpenAiService;

public class openai {

    // Funktion, die eine Antwort von OpenAI's GPT-3.5-Turbo-Modell erhält
    public static String getAnswer(String question) {
        try {
            // Erstellen eines neuen OpenAiService mit dem Token aus der Main-Klasse
            OpenAiService service = new OpenAiService(Main.openaitoken);

            // Erstellen einer Anfrage an das ChatCompletion-API
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

            // Senden der Anfrage und Speichern der Antwort in der Main-Klasse
            Main.answer = service.createChatCompletion(chatCompletionRequest).getChoices().toString()
                    .replace("[ChatCompletionChoice(index=0, message=ChatMessage(role=assistant, content=", "");

            // Entfernen von unnötigen Teilen der Antwort
            if (Main.answer.contains("), finishReason=length)]")) {
                Main.answer = Main.answer.replace("), finishReason=length)]", "");
            }
            if (Main.answer.contains("), finishReason=stop)]")) {
                Main.answer = Main.answer.replace("), finishReason=stop)]", "");
            }

            // Ausgabe der Antwort und Speichern in den Logs
            System.out.println("Antwort:\n" + Main.answer);
            WriteLogs.writeLog("Antwort:\n" + Main.answer);

            return Main.answer;

        } catch (Exception e) {
            // Bei einem Fehler wird eine Fehlermeldung ausgegeben und in den Logs gespeichert
            System.out.println("Fehler beim Erstellen der Antwort");
            WriteLogs.writeLog("Fehler beim Erstellen der Antwort");
            Main.answer = e.toString();

        }
        return Main.answer;
    }

}