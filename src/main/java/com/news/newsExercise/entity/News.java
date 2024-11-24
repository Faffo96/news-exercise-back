package com.news.newsExercise.entity;

import com.news.newsExercise.Enum.CategoryEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "news")
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String author;
    private String title;
    @Lob
    private String body;
    private LocalDate releaseDate = LocalDate.now();
    private String archiveDate;
    @Enumerated(EnumType.STRING)
    private CategoryEnum mainCategory;
    @Enumerated(EnumType.STRING)
    private List<CategoryEnum> otherCategoriesList;
    @ManyToMany
    @JoinTable(
            name = "news_subcategories",
            joinColumns = @JoinColumn(name = "news_id"),
            inverseJoinColumns = @JoinColumn(name = "subcategory_id"))
    private List<Subcategory> subcategoriesList;
}
