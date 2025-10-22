package com.notekeeper.Notemind.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AssemblyAIService {

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
        HttpHeaders headers = new HttpHeaders();
        headers.set("authorization", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject body = new JSONObject();
        body.put("audio_url", audioUrl);

        HttpEntity<String> request = new HttpEntity<>(body.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                TRANSCRIBE_URL,
                HttpMethod.POST,
                request,
                String.class
        );

        JSONObject jsonResponse = new JSONObject(response.getBody());
        return jsonResponse.getString("id"); // transcription ID
    }

    // Step 3: Poll until transcription is complete
    public String getTranscriptionResult(String transcriptId) throws InterruptedException {
        String url = TRANSCRIBE_URL + "/" + transcriptId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("authorization", apiKey);

        while (true) {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    String.class
            );

            JSONObject jsonResponse = new JSONObject(response.getBody());
            String status = jsonResponse.getString("status");

            if ("completed".equals(status)) {
                return jsonResponse.getString("text"); // return transcription text
            } else if ("error".equals(status)) {
                throw new RuntimeException("Transcription failed: " + jsonResponse.getString("error"));
            }

            Thread.sleep(3000); // wait 3 seconds before checking again
        }
    }
}
