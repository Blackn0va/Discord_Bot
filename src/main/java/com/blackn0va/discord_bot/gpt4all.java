package com.blackn0va.discord_bot;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class gpt4all {

    public static String getAnswer(String question) throws Exception {
        String text = "";
        try {
            String shortenedPrompt = shortenText(question, 100);
            String apiBase = "http://192.168.178.117:4891/v1";
            String apiKey = "";
            String model = "mistral-7b-instruct-v0.1.Q4_0.gguf";

            Map<String, Object> params = new HashMap<>();
            params.put("model", model);
            params.put("prompt", shortenedPrompt);
            params.put("max_tokens", 500);
            params.put("temperature", 0.28);
            params.put("top_p", 0.95);
            params.put("n", 1);
            params.put("echo", true);
            params.put("stream", false);

            String requestBody = new ObjectMapper().writeValueAsString(params);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiBase + "/completions"))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Umwandeln der Antwort in ein JsonNode
            ObjectMapper mapper = new ObjectMapper();
            JsonNode responseJson = mapper.readTree(response.body());

            // Extrahieren des Texts aus dem ersten Element des "choices"-Arrays
            text = responseJson.get("choices").get(0).get("text").asText();

        } catch (Exception e) {
            text = "API Server nicht erreichbar";
        }

        return text;
    }

    public static String shortenText(String text, int maxWords) {
        String[] words = text.split(" ");
        if (words.length <= maxWords) {
            return text;
        }
        return String.join(" ", Arrays.copyOfRange(words, 0, maxWords));
    }

}
