package com.demo.task_ink_solutions.dto;

import java.util.List;

public record ApiResponse(List<ArticleDTO> data) {
    public record ArticleDTO(String title, String description, String url) {
    }
}
