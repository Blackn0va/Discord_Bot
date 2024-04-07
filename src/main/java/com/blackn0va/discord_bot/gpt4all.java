package com.blackn0va.discord_bot;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class gpt4all {

    public static Future<String> getAnswerInThread(String question) {
        // Erstellen Sie einen ExecutorService mit einem einzigen Thread
        ExecutorService executor = Executors.newSingleThreadExecutor();
        // Erstellen Sie ein Future-Objekt, das das Ergebnis einer asynchronen
        // Berechnung darstellt
        Future<String> future = executor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                // Rufen Sie die getAnswer-Methode auf und geben Sie das Ergebnis zurück
                return getAnswer(question);
            }
        });
        // Schließen Sie den ExecutorService, um Systemressourcen freizugeben
        executor.shutdown();
        // Geben Sie das Future-Objekt zurück, das das Ergebnis der Berechnung enthält
        return future;
    }

    public static String getAnswer(String question) throws Exception {
        String text = "";
        try {
            // Kürzen Sie die Frage auf 100 Zeichen
            String shortenedPrompt = shortenText(question, 100);
            // Basis-URL des API-Servers
            String apiBase = "http://192.168.178.117:4891/v1";
            // Ihr API-Schlüssel
            String apiKey = "";
            // Das Modell, das Sie verwenden möchten
            String model = "mistral-7b-instruct-v0.1.Q4_0.gguf";

            // Erstellen Sie eine Map mit den Parametern für die API-Anfrage
            Map<String, Object> params = new HashMap<>();
            params.put("model", model);
            params.put("prompt", shortenedPrompt);
            params.put("max_tokens", 500);
            params.put("temperature", 0.28);
            params.put("top_p", 0.95);
            params.put("n", 1);
            params.put("echo", true);
            params.put("stream", false);

            // Konvertieren Sie die Parameter-Map in einen JSON-String
            String requestBody = new ObjectMapper().writeValueAsString(params);

            // Erstellen Sie einen neuen HTTP-Client
            HttpClient client = HttpClient.newHttpClient();
            // Erstellen Sie eine neue HTTP-Anfrage
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiBase + "/completions"))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            // Senden Sie die Anfrage und erhalten Sie die Antwort
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Konvertieren Sie die Antwort in ein JsonNode
            ObjectMapper mapper = new ObjectMapper();
            JsonNode responseJson = mapper.readTree(response.body());

            // Extrahieren Sie den Text aus dem ersten Element des "choices"-Arrays
            text = responseJson.get("choices").get(0).get("text").asText();

        } catch (Exception e) {
            // Wenn ein Fehler auftritt, geben Sie eine Fehlermeldung zurück
            text = "API Server nicht erreichbar";
        }

        // Geben Sie den Text zurück
        return text;
    }

    public static String shortenText(String text, int maxWords) {
        // Teilen Sie den Text in Wörter auf, indem Sie ihn an den Leerzeichen trennen
        String[] words = text.split(" ");
        // Wenn die Anzahl der Wörter kleiner oder gleich der maximal zulässigen Anzahl
        // ist,
        // geben Sie den ursprünglichen Text zurück
        if (words.length <= maxWords) {
            return text;
        }
        // Wenn die Anzahl der Wörter größer als die maximal zulässige Anzahl ist,
        // erstellen Sie einen neuen String, der nur die ersten maxWords Wörter enthält
        return String.join(" ", Arrays.copyOfRange(words, 0, maxWords));
    }

}
