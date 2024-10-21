package com.demo.task_ink_solutions.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class ProcessOpenAI {

    private static final Logger logger = LoggerFactory.getLogger(ProcessOpenAI.class);

    private final RestTemplate restTemplate;

    private final String URL_OPENAI = "https://api.openai.com/v1/chat/completions";


    public ProcessOpenAI(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Value("${openai.api.key}")
    protected String OPENAI_API_KEY;

    public String processPrompt(String input) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode requestBody = createRequestBody(input, mapper);

        HttpHeaders headers = createHeaders();

        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

        try {
            return makeApiCall(entity);
        } catch (RestClientException e) {
            logger.error("Error calling OpenAI API: {}", e.getMessage(), e);
            throw e;
        }
    }

    protected ObjectNode createRequestBody(String input, ObjectMapper mapper) {
        ObjectNode requestBody = mapper.createObjectNode();
        requestBody.put("model", "gpt-4o-mini");

        ObjectNode message = mapper.createObjectNode();
        message.put("role", "user");
        message.put("content", String.format(
                "Analyze the following news article: %s\n" +
                        "Please provide your analysis based on these questions:\n" +
                        "- Is this news local or global?\n" +
                        "- If it's local, specify the city it belongs to.\n" +
                        "Respond with just two lines: \n" +
                        "1. The first line should be 'local' or 'global'. \n" +
                        "2. The second line should be the city name (if local) or 'none' (if global).\n\n" +
                        "Example output:\n" +
                        "local\nBuenosAires\n\n" +
                        "or\n\n" +
                        "global\nnone", input));
        requestBody.putArray("messages").add(message);

        return requestBody;
    }

    protected HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(OPENAI_API_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private String makeApiCall(HttpEntity<String> entity) {
        return restTemplate.postForObject(URL_OPENAI, entity, String.class);
    }
}
