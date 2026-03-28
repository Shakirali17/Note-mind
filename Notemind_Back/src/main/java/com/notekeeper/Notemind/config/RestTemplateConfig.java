package com.notekeeper.Notemind.config;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public org.springframework.web.client.RestTemplate restTemplate() {
        // Create default CloseableHttpClient — it already supports TLS 1.2/1.3
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // Use it in RestTemplate
        return new org.springframework.web.client.RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
    }
}
