package com.demo.task_ink_solutions.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
public class NewsId implements Serializable {
    private String title;
    private String cityOfUSA;
}
