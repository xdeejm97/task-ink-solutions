package com.demo.task_ink_solutions.service;

import com.demo.task_ink_solutions.dto.ApiResponse;
import com.demo.task_ink_solutions.model.NewsArticle;
import com.demo.task_ink_solutions.repository.NewsArticleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;


import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NewsServiceImplTest {

    @InjectMocks
    private NewsServiceImpl newsService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private NewsArticleRepository newsArticleRepository;


    @Test
    void testScrapeNewsByCity_RestClientException() {
        // Mock a failure response from RestTemplate
        when(restTemplate.getForEntity(anyString(), eq(ApiResponse.class), anyString(), anyString()))
                .thenThrow(new RuntimeException("RestClientException"));

        // Execute the method
        assertThrows(RuntimeException.class, () -> newsService.scrapeNewsByCity("InvalidCity"));

        // Verify that no articles were saved
        verify(newsArticleRepository, never()).save(any(NewsArticle.class));
    }

}