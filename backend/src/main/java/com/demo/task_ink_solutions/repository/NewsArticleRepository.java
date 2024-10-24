package com.demo.task_ink_solutions.repository;

import com.demo.task_ink_solutions.model.NewsArticle;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsArticleRepository extends JpaRepository<NewsArticle, Long> {
    List<NewsArticle> findByCityOfUSAIgnoreCase(String city);
}