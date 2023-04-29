package com.blackn0va.discord_bot;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

public class openai {

    // funktion return string antwort
    public static String getAnswer(String question) {
        try {
            OpenAiService service = new OpenAiService(Main.openaitoken);


             System.out.println("\nCreating completion... ");
            final CompletionRequest completionRequest = CompletionRequest.builder()
                    .model("text-davinci-003")
                    .prompt(question)
                    .maxTokens(200)
                    .temperature(0.5)
                    .topP(0.3)
                    .frequencyPenalty(0.5)
                    .presencePenalty(0.0)
                    .echo(true)
                    .build();

            // service.createCompletion(completionRequest).getChoices().forEach(System.out::print);
            NachrichtenReaction.answer = service.createCompletion(completionRequest).getChoices().toString()
                    .replace("[CompletionChoice(text=", "").replace(", index=)]", "")
                    .replace(", logprobs=", "").replace(", finish_reason=", "")
                    .replace(", index=0nullstop)", "").replace("]", "")
                    .replace("index=0nulllength)", "");

                    //NachrichtenReaction.answer = NachrichtenReaction.answer.replace("ReceivedMessage(*)", "");

            return NachrichtenReaction.answer;

        } catch (Exception e) {
            System.out.println("Fehler beim Erstellen der Antwort");
            NachrichtenReaction.answer = e.toString();

        }
        return NachrichtenReaction.answer;
    }

}
