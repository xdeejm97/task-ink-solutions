package com.demo.task_ink_solutions.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "news", indexes = {
        @Index(name = "idx_city_name", columnList = "cityOfUSA"),
        @Index(name = "idx_is_local", columnList = "localOrGlobal")
}, uniqueConstraints = {
        @UniqueConstraint(columnNames = {"title", "city_ofusa"})
})
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

    @Column(length = 2048)
    private String url;

    private String cityOfUSA;

    private String localOrGlobal;

}