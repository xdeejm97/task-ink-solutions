package com.demo.task_ink_solutions.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "us_cities")
@Getter
@Setter
@RequiredArgsConstructor
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String state;

}