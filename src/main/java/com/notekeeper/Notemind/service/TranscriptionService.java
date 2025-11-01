package com.notekeeper.Notemind.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class TranscriptionService {

    @Value("${assemblyai.api.key}")
    private String apiKey;

    private static final String UPLOAD_URL = "https://api.assemblyai.com/v2/upload";
    private static final String TRANSCRIBE_URL = "https://api.assemblyai.com/v2/transcript";

    private final RestTemplate restTemplate = new RestTemplate();

    // Step 1: Upload the audio file
    public String uploadAudio(byte[] audioBytes) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("authorization", apiKey);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        HttpEntity<byte[]> request = new HttpEntity<>(audioBytes, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                UPLOAD_URL,
                HttpMethod.POST,
                request,
                String.class
        );

        JSONObject jsonResponse = new JSONObject(response.getBody());
        return jsonResponse.getString("upload_url");
    }

    // Step 2: Request transcription
    public String requestTranscription(String audioUrl) {
        String url = "https://api.assemblyai.com/v2/transcript";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("audio_url", audioUrl);
        body.put("summarization", true);
        body.put("summary_model", "informative");
        body.put("summary_type", "paragraph");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
        return response.getBody().get("id").toString();
    }


    // Step 3: Poll until transcription is complete
    public Map<String, String> getTranscriptionResult(String transcriptId) throws InterruptedException {
        String url = "https://api.assemblyai.com/v2/transcript/" + transcriptId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", apiKey);

        while (true) {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), Map.class);
            Map<String, Object> body = response.getBody();

            String status = (String) body.get("status");
            if ("completed".equals(status)) {
                String text = (String) body.get("text");
                String summary = (String) body.get("summary");
                Map<String, String> result = new HashMap<>();
                result.put("text", text);
                result.put("summary", summary);
                return result;
            } else if ("error".equals(status)) {
                throw new RuntimeException("Transcription failed: " + body.get("error"));
            }

            Thread.sleep(5000);
        }
    }

}
