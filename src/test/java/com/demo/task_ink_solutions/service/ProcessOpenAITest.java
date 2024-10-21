package com.demo.task_ink_solutions.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProcessOpenAITest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ProcessOpenAI processOpenAI;

    private final String mockApiKey = System.getenv("OPENAI_API_KEY");

    @BeforeEach
    void setUp() {
        processOpenAI.OPENAI_API_KEY = mockApiKey;
    }

    @Test
    void testProcessPrompt_ApiCallThrowsException() {
        String input = "Sample news article";

        Mockito.when(restTemplate.postForObject(
                eq("https://api.openai.com/v1/chat/completions"),
                any(HttpEntity.class),
                eq(String.class)
        )).thenThrow(new RestClientException("API call failed"));

        RestClientException exception = assertThrows(RestClientException.class, () -> {
            processOpenAI.processPrompt(input);
        });

        assertEquals("API call failed", exception.getMessage());
    }

    @Test
    void testCreateHeaders() {
        // Given & When
        HttpHeaders headers = processOpenAI.createHeaders();

        // Then
        assertEquals("Bearer " + mockApiKey, headers.getFirst(HttpHeaders.AUTHORIZATION));
        assertEquals(MediaType.APPLICATION_JSON, headers.getContentType());
    }

    @Test
    void testCreateRequestBody() {
        // Given
        String input = "Sample news article";
        ObjectMapper mapper = new ObjectMapper();

        // When
        ObjectNode requestBody = processOpenAI.createRequestBody(input, mapper);

        // Then
        assertNotNull(requestBody);
        assertEquals("gpt-4o-mini", requestBody.get("model").asText());
        assertTrue(requestBody.has("messages"));
    }


}