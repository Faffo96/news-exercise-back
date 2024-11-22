package com.news.newsExercise.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.news.newsExercise.Enum.CategoryEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "subcategory")
public class Subcategory {
    @Id
    @GeneratedValue
    private Long id;
    @Enumerated(EnumType.STRING)
    private CategoryEnum mainCategory;
    private String subcategory;
    @ManyToMany(mappedBy = "subcategoriesList")
    @JsonIgnore
    private List<News> newsList;
}
