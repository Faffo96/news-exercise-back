package com.news.newsExercise.service;

import com.news.newsExercise.dto.SubcategoryDTO;
import com.news.newsExercise.entity.Subcategory;

import com.news.newsExercise.exception.CategoryNotFoundException;
import com.news.newsExercise.repository.SubcategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SubcategoryService {

@Autowired
private SubcategoryRepository subcategoryRepository;

    private static final Logger loggerInfo = LoggerFactory.getLogger("loggerInfo");

    public Subcategory createCategory(SubcategoryDTO subcategoryDTO) {
        Subcategory subcategory = new Subcategory();
        subcategory.setMainCategory(subcategoryDTO.getMainCategory());
        subcategory.setSubcategory(subcategoryDTO.getSubcategory());
        subcategoryRepository.save(subcategory);

        loggerInfo.info("CategoryEnum con ID " + subcategory.getId() + ", con categoria '" + subcategory.getSubcategory() + "' e subcategoria '" + subcategory.getSubcategory() + "' creata.");
        return subcategory;
    }

    public Subcategory getCategoryById(Long id) throws CategoryNotFoundException {
        return subcategoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("CategoryEnum not found with id: " + id));
    }

    public List<Subcategory> getAllCategories(String sortBy) {
        List<Subcategory> subcategories = subcategoryRepository.findAll(Sort.by(sortBy));
        loggerInfo.info("Retrieved all subcategories sorted by " + sortBy);
        return subcategories;
    }

    public Page<Subcategory> getCategories(int page, String sortBy) {
        int fixedSize = 15;
        Pageable pageable = PageRequest.of(page, fixedSize, Sort.by(sortBy));
        Page<Subcategory> categories = subcategoryRepository.findAll(pageable);
        loggerInfo.info("Retrieved categories page " + page + " with fixed size " + fixedSize + " sorted by " + sortBy);
        return categories;
    }

    public Subcategory updateCategory(Long id, SubcategoryDTO subcategoryDTO) throws CategoryNotFoundException {
        Subcategory subcategory = getCategoryById(id);

        subcategory.setMainCategory(subcategoryDTO.getMainCategory());
        subcategory.setSubcategory(subcategoryDTO.getSubcategory());
        subcategoryRepository.save(subcategory);

        loggerInfo.info("CategoryEnum with id " + id + " updated.");

        return subcategory;
    }

    public String deleteCategory(Long id) throws CategoryNotFoundException {
        Subcategory subcategory = getCategoryById(id);
        subcategoryRepository.delete(subcategory);
        loggerInfo.info("CategoryEnum with id " + id + " deleted successfully.");
        return "CategoryEnum with id " + id + " deleted successfully.";
    }
}
