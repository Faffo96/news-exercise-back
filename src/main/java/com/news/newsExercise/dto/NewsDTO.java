package com.news.newsExercise.dto;

import com.news.newsExercise.Enum.CategoryEnum;
import com.news.newsExercise.entity.Subcategory;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class NewsDTO {
    private UUID id;
    @NotNull(message = "Author cannot be null.")
    private String author;
    @NotNull(message = "Title cannot be null.")
    @Size(max = 150, message = "Title cannot exceed 150 chars.")
    @Pattern(regexp = "^[^\\d]*$", message = "Title cannot contain numbers.")
    private String title;
    @NotNull(message = "Body cannot be null.")
    @Size(min = 80, message = "Body must have at least 80 characters.")
    private String body;
    private String releaseDate;
    @NotNull(message = "Archive Date cannot be null.")
    private String archiveDate;
    private CategoryEnum mainCategory;
    private List<CategoryEnum> otherCategories;
    private List<Subcategory> subcategories;
}
