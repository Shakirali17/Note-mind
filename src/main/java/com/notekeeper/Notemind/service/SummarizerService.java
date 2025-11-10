package com.notekeeper.Notemind.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class SummarizerService {

    @Value("${assemblyai.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    // ✅ Correct endpoint for LeMUR (not /summarize)
    private static final String LEMUR_URL = "https://api.assemblyai.com/lemur/v3/generate/summary";

    public SummarizerService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public String summarizeTranscript(String transcriptId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            // ✅ Correct request body per AssemblyAI LeMUR API
            Map<String, Object> payload = new HashMap<>();
            payload.put("transcript_ids", Collections.singletonList(transcriptId));
            payload.put("final_model", "anthropic/claude-sonnet-4-20250514"); // latest supported model
            payload.put("prompt", "Summarize the provided transcript in better related bullets points.");
            payload.put("answer_format","TLDR");

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(LEMUR_URL, request, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Object result = response.getBody().get("response");
                return result != null ? result.toString() : "No summary returned from AssemblyAI.";
            } else {
                return "Summarization failed. HTTP Code: " + response.getStatusCode();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Error during summarization: " + e.getMessage();
        }
    }

//    public boolean isTranscriptReady(String transcriptId) {
//        try {
//            HttpHeaders headers = new HttpHeaders();
//            headers.setBearerAuth(apiKey);
//
//            HttpEntity<String> request = new HttpEntity<>(headers);
//            String url = "https://api.assemblyai.com/v2/transcript/" + transcriptId;
//
//            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, request, Map.class);
//            Map<String, Object> body = response.getBody();
//
//            return body != null && "completed".equals(body.get("status"));
//
//        } catch (Exception e) {
//            return false;
//        }
//    }

}