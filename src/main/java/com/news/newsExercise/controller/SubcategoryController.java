package com.news.newsExercise.controller;

import com.news.newsExercise.Enum.CategoryEnum;
import com.news.newsExercise.dto.SubcategoryDTO;
import com.news.newsExercise.entity.News;
import com.news.newsExercise.entity.Subcategory;
import com.news.newsExercise.exception.CategoryNotFoundException;
import com.news.newsExercise.service.SubcategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/subcategories")
public class SubcategoryController {

    @Autowired
    private SubcategoryService subcategoryService;

    @PostMapping
    public ResponseEntity<Object> createCategory(@Valid @RequestBody SubcategoryDTO subcategoryDTO) {
        Subcategory createdSubcategory = subcategoryService.createCategory(subcategoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSubcategory);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Subcategory> getCategoryById(@PathVariable Long id) throws CategoryNotFoundException {
        Subcategory subcategory = subcategoryService.getCategoryById(id);
        return ResponseEntity.ok(subcategory);
    }

    @GetMapping
    public ResponseEntity<List<Subcategory>> getAllSubcategories(@RequestParam(defaultValue = "id") String sortBy) {
        List<Subcategory> news = subcategoryService.getAllCategories(sortBy);
        return ResponseEntity.ok(news);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<Subcategory>> getCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "id") String sortBy) {
        Page<Subcategory> categoryPage = subcategoryService.getCategories(page, sortBy);
        return ResponseEntity.ok(categoryPage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Subcategory> updateCategory(@PathVariable Long id, @Valid @RequestBody SubcategoryDTO subcategoryDTO) throws CategoryNotFoundException {
            Subcategory updatedSubcategory = subcategoryService.updateCategory(id, subcategoryDTO);
            return ResponseEntity.ok(updatedSubcategory);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) throws CategoryNotFoundException {
            String result = subcategoryService.deleteCategory(id);
            return ResponseEntity.ok(result);

    }

    @GetMapping("/mainCategories")
    public ResponseEntity<List<CategoryEnum>> getAllMainCategories() {
        // Restituisce tutti i valori dell'enum come lista
        List<CategoryEnum> mainCategories = Arrays.asList(CategoryEnum.values());
        return ResponseEntity.ok(mainCategories);
    }
}
