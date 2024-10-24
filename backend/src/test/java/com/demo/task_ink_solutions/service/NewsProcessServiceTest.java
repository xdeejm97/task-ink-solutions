package com.demo.task_ink_solutions.service;

import com.demo.task_ink_solutions.repository.NewsArticleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NewsProcessServiceTest {

    @Mock
    private NewsServiceImpl newsService;

    @Mock
    private NewsArticleRepository newsArticleRepository;

    @InjectMocks
    private NewsProcessService newsProcessService;

    @Test
    void testScrapeNewsForCity_GeneralException() {
        doThrow(new RuntimeException("Unexpected error")).when(newsService).scrapeNewsByCity("LosAngeles");

        newsProcessService.scrapeNewsForCity("LosAngeles");

        verify(newsService, times(1)).scrapeNewsByCity("LosAngeles");
    }

}