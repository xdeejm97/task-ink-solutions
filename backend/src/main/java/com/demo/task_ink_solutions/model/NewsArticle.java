package com.demo.task_ink_solutions.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "news", indexes = {
        @Index(name = "idx_city_name", columnList = "cityOfUSA"),
        @Index(name = "idx_is_local", columnList = "localOrGlobal")
})
@Getter
@Setter
@RequiredArgsConstructor
@IdClass(NewsId.class)
public class NewsArticle {

    @Id
    @Column(length = 1024)
    private String title;

    @Column(length = 1024)
    private String description;

    @Column(length = 2048)
    private String url;

    @Id
    private String cityOfUSA;

    private String localOrGlobal;

}