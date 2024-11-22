package com.news.newsExercise.controller;

import com.news.newsExercise.dto.NewsDTO;
import com.news.newsExercise.entity.News;
import com.news.newsExercise.exception.CategoryAndSubcategoryMismatchException;
import com.news.newsExercise.exception.CategoryNotFoundException;
import com.news.newsExercise.exception.NewsDateTooEarlyException;
import com.news.newsExercise.exception.NewsNotFoundException;
import com.news.newsExercise.service.NewsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @PostMapping
    public ResponseEntity<Object> createNews(@Valid @RequestBody NewsDTO newsDTO, BindingResult result) throws NewsDateTooEarlyException, CategoryAndSubcategoryMismatchException, CategoryNotFoundException {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errorMessages);
        }

        News createdNews = newsService.createNews(newsDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNews);
    }


    @GetMapping("/{id}")
    public ResponseEntity<News> getNewsById(@PathVariable UUID id) throws NewsNotFoundException {
        News news = newsService.getNewsById(id);
        return ResponseEntity.ok(news);
    }

    @GetMapping
    public ResponseEntity<List<News>> getAllNews(@RequestParam(defaultValue = "id") String sortBy) {
        List<News> news = newsService.getAllNewss(sortBy);
        return ResponseEntity.ok(news);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<News>> getNews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "id") String sortBy) {
        Page<News> newsPage = newsService.getNewss(page, sortBy);
        return ResponseEntity.ok(newsPage);
    }

   /* @GetMapping("/active")
    public ResponseEntity<List<News>> getAllActiveNews(@RequestParam(defaultValue = "id") String sortBy) {
        List<News> news = newsService.getAllNewss(sortBy);

        List<News> filteredNews = news.stream()
                .filter(n -> {
                    LocalDate releaseDate = n.getReleaseDate();
                    LocalDate archiveDate = LocalDate.parse(n.getArchiveDate());

                    long daysDifference = java.time.temporal.ChronoUnit.DAYS.between(releaseDate, archiveDate);

                    return daysDifference > 30;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(filteredNews);
    }


    @GetMapping("/active/page")
    public ResponseEntity<Page<News>> getActiveNews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "id") String sortBy) {

        Page<News> newsPage = newsService.getNewss(page, sortBy);

        List<News> filteredNews = newsPage.stream()
                .filter(n -> {
                    LocalDate releaseDate = n.getReleaseDate();
                    LocalDate archiveDate = LocalDate.parse(n.getArchiveDate()); // Assumendo che archiveDate sia una stringa in formato YYYY-MM-DD

                    long daysDifference = java.time.temporal.ChronoUnit.DAYS.between(releaseDate, archiveDate);

                    return daysDifference > 30;
                })
                .collect(Collectors.toList());

        // Crea una nuova Page con il risultato filtrato
        Page<News> filteredPage = new PageImpl<>(filteredNews, newsPage.getPageable(), filteredNews.size());

        return ResponseEntity.ok(filteredPage);
    }*/


    @PutMapping("/{id}")
    public ResponseEntity<Object> updateNews(@PathVariable UUID id, @Valid @RequestBody NewsDTO newsDTO, BindingResult result) throws NewsNotFoundException, NewsDateTooEarlyException, CategoryAndSubcategoryMismatchException, CategoryNotFoundException {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errorMessages);
        }

            News updatedNews = newsService.updateNews(id, newsDTO);
            return ResponseEntity.ok(updatedNews);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNews(@PathVariable UUID id) throws NewsNotFoundException {
            String result = newsService.deleteNews(id);
            return ResponseEntity.ok(result);
    }
}
