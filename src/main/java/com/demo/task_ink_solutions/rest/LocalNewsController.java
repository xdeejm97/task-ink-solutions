package com.demo.task_ink_solutions.rest;

import com.demo.task_ink_solutions.model.NewsArticle;
import com.demo.task_ink_solutions.service.NewsProcessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LocalNewsController {

    private final NewsProcessService newsService;

    public LocalNewsController(NewsProcessService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/news/city")
    public ResponseEntity<List<NewsArticle>> getNews() {
        return ResponseEntity.ok(newsService.findAll());
    }

    @PostMapping("/scrape-news/{city}")
    public String scrapeNews(@PathVariable("city") String city) {
        newsService.scrapeNewsForCity(city);
        return "News scraped and processed successfully";
    }
}