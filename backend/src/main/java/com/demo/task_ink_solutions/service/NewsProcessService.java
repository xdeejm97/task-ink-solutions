package com.demo.task_ink_solutions.service;

import com.demo.task_ink_solutions.model.NewsArticle;
import com.demo.task_ink_solutions.repository.NewsArticleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsProcessService {

    private static final Logger logger = LoggerFactory.getLogger(NewsProcessService.class);

    private final NewsServiceImpl newsService;
    private final NewsArticleRepository newsArticleRepository;

    public NewsProcessService(NewsServiceImpl newsService, NewsArticleRepository newsArticleRepository) {
        this.newsService = newsService;
        this.newsArticleRepository = newsArticleRepository;
    }

    public List<NewsArticle> findByCityOfUSAIgnoreCase(String city) {
        return newsArticleRepository.findByCityOfUSAIgnoreCase(city);
    }

    public void scrapeNewsForCity(String name) {
        try {
            newsService.scrapeNewsByCity(name);
        } catch (DataAccessException e) {
            logger.error("Database access error while saving the article: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error occurred while processing the article: {}", e.getMessage(), e);
        }
    }
}