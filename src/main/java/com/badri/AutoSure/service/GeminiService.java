package com.badri.AutoSure.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URI;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class GeminiService {



    private static final String API_KEY = "YOUR API KEY"; // replace with real key
    private static final String GEMINI_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + API_KEY;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Map<String, Object> checkCompliance(Map<String, Object> carData) throws Exception {
        Map<String, Object> regulation = loadRegulationJson();

        String prompt = buildPrompt(carData, regulation);

        // Wrap prompt as per Gemini API format
        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of(
                                "role", "user",
                                "parts", List.of(
                                        Map.of("text", prompt)
                                )
                        )
                )
        );

        String requestJson = objectMapper.writeValueAsString(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(GEMINI_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestJson, StandardCharsets.UTF_8))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        return extractJsonFromGemini(response.body());
    }

    private Map<String, Object> loadRegulationJson() throws Exception {
        InputStream input = getClass().getResourceAsStream("/data/regulations.json");
        if (input == null) {
            throw new RuntimeException("regulations.json not found in resources/data/");
        }
        return objectMapper.readValue(input, new TypeReference<>() {});
    }

    private String buildPrompt(Map<String, Object> carData, Map<String, Object> regData) throws Exception {
        return """
        You are a car regulation checker AI agent.

        1. Below is the official regulation standard in JSON format:
        %s

        2. The manufacturer has provided this car specification:
        %s

        3. Compare both and return this JSON:
        {
          "status": "accepted" or "rejected",
          "regData": <regulationData>,
          "carData": <carSpecification>,
          "issues": [ { "field": ..., "expected": ..., "actual": ..., "reason": ... }],
          "reason": "brief explanation"
        }

        Return only valid JSON.
        """.formatted(
                objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(regData),
                objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(carData)
        );
    }

    private Map<String, Object> extractJsonFromGemini(String responseBody) throws Exception {
        Map<String, Object> fullResponse = objectMapper.readValue(responseBody, new TypeReference<>() {});

        List<Map<String, Object>> candidates = (List<Map<String, Object>>) fullResponse.get("candidates");
        if (candidates == null || candidates.isEmpty()) {
            throw new RuntimeException("No candidates in Gemini response");
        }

        Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
        List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");

        if (parts == null || parts.isEmpty()) {
            throw new RuntimeException("No parts in Gemini response");
        }

        String rawOutput = (String) parts.get(0).get("text");
        String cleaned = rawOutput.replaceAll("(?s)```json\\s*|```", "").trim();

        return objectMapper.readValue(cleaned, new TypeReference<>() {});
    }
}


//    private String extractJsonFromGemini(String response) throws IOException {
//        // Simplified parsing (you can extract from full response or clean markdown fence)
//        Map<String, Object> res = mapper.readValue(response, new TypeReference<>() {});
//        String content = (String) ((Map<?, ?>)((Map<?, ?>)((java.util.List<?>)res.get("candidates")).get(0)).get("content")).get("parts").get(0);
//        return content.replaceAll("(?s)```json\\s*|```", "").trim();
//    }




