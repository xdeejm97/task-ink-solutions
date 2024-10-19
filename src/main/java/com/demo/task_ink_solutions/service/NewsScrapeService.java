package com.demo.task_ink_solutions.service;

import com.demo.task_ink_solutions.model.NewsArticle;
import com.demo.task_ink_solutions.repository.NewsArticleRepository;
import org.springframework.stereotype.Service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class NewsScrapeService {

    private final NewsArticleRepository newsArticleRepository;

    public NewsScrapeService(NewsArticleRepository newsArticleRepository) {
        this.newsArticleRepository = newsArticleRepository;
    }

    private static final List<String> NEWS_SOURCES = List.of(
            "https://www.localnewssource1.com",
            "https://www.localnewssource2.com",
            "https://www.globalnewssource1.com",
            "https://www.globalnewssource2.com"
    );

    public void scrapeNews() {
        List<NewsArticle> allArticles = new ArrayList<>();

        for (String source : NEWS_SOURCES) {
            try {
                Document doc = Jsoup.connect(source).get();
                Elements articles = doc.select("article");

                for (var article : articles) {
                    String title = article.select("h2").text();
                    String content = article.select("p").text();

                    NewsArticle newsArticle = new NewsArticle();
                    newsArticle.setTitle(title);
                    newsArticle.setContent(content);

                    allArticles.add(newsArticle);

                    if (allArticles.size() >= 20) break;
                }
            } catch (IOException e) {
                System.err.println("Error scraping " + source + ": " + e.getMessage());
            }
        }

        Collections.shuffle(allArticles);
        newsArticleRepository.saveAll(allArticles.subList(0, Math.min(allArticles.size(), 100)));
    }

}
