package com.demo.task_ink_solutions.service;

import com.demo.task_ink_solutions.dto.ApiResponse;
import com.demo.task_ink_solutions.model.City;
import com.demo.task_ink_solutions.model.NewsArticle;
import com.demo.task_ink_solutions.repository.CityRepository;
import com.demo.task_ink_solutions.repository.NewsArticleRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class NewsServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(NewsProcessService.class);

    private final NewsArticleRepository newsArticleRepository;
    private final CityRepository cityRepository;
    private final RestTemplate restTemplate;
    private final ProcessOpenAI processWithOpenAI;

    public NewsServiceImpl(NewsArticleRepository newsArticleRepository, CityRepository cityRepository, RestTemplate restTemplate, ProcessOpenAI processWithOpenAI) {
        this.newsArticleRepository = newsArticleRepository;
        this.cityRepository = cityRepository;
        this.restTemplate = restTemplate;
        this.processWithOpenAI = processWithOpenAI;
    }

    private final String URL_NEWS = "https://api.mediastack.com/v1/";

    @Value("${news.api.key}")
    private String NEWS_API_KEY;


    public void scrapeNewsByCity(String cityName) {
        ResponseEntity<ApiResponse> response = restTemplate.getForEntity(URL_NEWS + "news?keywords={cityName}&limit=100&languages=en&countries=us,+ca&access_key={apiKey}", ApiResponse.class, cityName, NEWS_API_KEY);

        try {
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {

                List<ApiResponse.ArticleDTO> articles = response.getBody().data();
                ObjectMapper mapper = new ObjectMapper();

                for (ApiResponse.ArticleDTO articleDTO : articles) {
                    NewsArticle article = new NewsArticle();
                    article.setTitle(articleDTO.title());
                    article.setDescription(articleDTO.description());
                    article.setUrl(articleDTO.url());

                    // Process with OpenAI
                    String openAiResponse = processWithOpenAI.processPrompt(article.getTitle() + " " + article.getDescription());
                    JsonNode openAiJson = mapper.readTree(openAiResponse);
                    String content = openAiJson.path("choices").get(0).path("message").path("content").asText();

                    // Parse the OpenAI response
                    String[] parts = content.split("\n");

                    String localityType = parts[0].trim();
                    String name = parts[1];

                    if ("local".equalsIgnoreCase(localityType)) {
                        List<City> cities = cityRepository.findByNameIgnoreCase(name);
                        if (cities.size() == 1) {
                            City city = cities.get(0);
                            if (cityName.equalsIgnoreCase(city.getName())) {
                                article.setCityUs(city.getName());
                                article.setIsLocalOrGlobal(localityType);
                            } else {
                                article.setCityUs(name);
                                article.setIsLocalOrGlobal("global");
                            }
                        }
                    } else {
                        article.setCityUs("none");
                        article.setIsLocalOrGlobal("global");
                    }

                    newsArticleRepository.save(article);
                }
            }
        } catch (DataAccessException e) {
            logger.error("Database access error while saving the article: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error occurred while processing the article: {}", e.getMessage(), e);
        }
    }

    public void scrapeNewsBySports(String cityName) {
        ResponseEntity<ApiResponse> response = restTemplate.getForEntity(URL_NEWS + "news?keywords={cityName}%sport&limit=100&languages=en&countries=us,+ca&access_key={apiKey}", ApiResponse.class, cityName, NEWS_API_KEY);

        try {
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {

                List<ApiResponse.ArticleDTO> articles = response.getBody().data();
                ObjectMapper mapper = new ObjectMapper();

                for (ApiResponse.ArticleDTO articleDTO : articles) {
                    NewsArticle article = new NewsArticle();
                    article.setTitle(articleDTO.title());
                    article.setDescription(articleDTO.description());
                    article.setUrl(articleDTO.url());

                    // Process with OpenAI
                    String openAiResponse = processWithOpenAI.processPrompt(article.getTitle() + " " + article.getDescription());
                    JsonNode openAiJson = mapper.readTree(openAiResponse);
                    String content = openAiJson.path("choices").get(0).path("message").path("content").asText();

                    // Parse the OpenAI response
                    String[] parts = content.split("\n");

                    String localityType = parts[0].trim();
                    String name = parts[1];

                    if ("local".equalsIgnoreCase(localityType)) {
                        List<City> cities = cityRepository.findByNameIgnoreCase(name);
                        if (cities.size() == 1) {
                            City city = cities.get(0);
                            if (cityName.equalsIgnoreCase(city.getName())) {
                                article.setCityUs(city.getName());
                                article.setIsLocalOrGlobal(localityType);
                            } else {
                                article.setCityUs(name);
                                article.setIsLocalOrGlobal("global");
                            }
                        }
                    } else {
                        article.setCityUs("none");
                        article.setIsLocalOrGlobal("global");
                    }

                    newsArticleRepository.save(article);
                }
            }
        } catch (DataAccessException e) {
            logger.error("Database access error while saving the article: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error occurred while processing the article: {}", e.getMessage(), e);
        }
    }

}
