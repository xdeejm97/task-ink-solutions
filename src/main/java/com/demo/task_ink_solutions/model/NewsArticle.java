package com.demo.task_ink_solutions.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "news")
@Getter
@Setter
@RequiredArgsConstructor
public class NewsArticle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1024)
    private String title;

    @Column(length = 1024)
    private String description;

    @Column(length = 1024)
    private String url;

    private String cityUs;

    private String isLocalOrGlobal;

}