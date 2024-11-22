package com.news.newsExercise.dto;

import com.news.newsExercise.Enum.CategoryEnum;
import lombok.Data;

@Data
public class SubcategoryDTO {
    private CategoryEnum mainCategory;
    private String subcategory;
}
