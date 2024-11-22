package com.news.newsExercise.service;

import com.news.newsExercise.dto.NewsDTO;
import com.news.newsExercise.entity.News;
import com.news.newsExercise.entity.Subcategory;
import com.news.newsExercise.exception.CategoryAndSubcategoryMismatchException;
import com.news.newsExercise.exception.CategoryNotFoundException;
import com.news.newsExercise.exception.NewsDateTooEarlyException;
import com.news.newsExercise.exception.NewsNotFoundException;
import com.news.newsExercise.repository.NewsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
public class NewsService {

@Autowired
private NewsRepository newsRepository;

@Autowired
private SubcategoryService subcategoryService;

    private static final Logger loggerInfo = LoggerFactory.getLogger("loggerInfo");

    public News createNews(NewsDTO newsDTO) throws NewsDateTooEarlyException, CategoryAndSubcategoryMismatchException, CategoryNotFoundException {
        News news = new News();
        news.setAuthor(newsDTO.getAuthor());
        news.setTitle(newsDTO.getTitle());
        news.setBody(newsDTO.getBody());
        news.setReleaseDate(LocalDate.now());
        news.setMainCategory(newsDTO.getMainCategory());
        news.setOtherCategoriesList(newsDTO.getOtherCategories());
        List<Subcategory> subcategories = newsDTO.getSubcategories();
        List<Subcategory> validSubcategories = new ArrayList<>();

        for (Subcategory subcategoryDTO : subcategories) {
            Subcategory subcategory = subcategoryService.getCategoryById(subcategoryDTO.getId());
            if (subcategory.getMainCategory() != newsDTO.getMainCategory()) {
                throw new CategoryAndSubcategoryMismatchException("You can't enter subcategories that don't belong to the main category of the article.");
            }
            validSubcategories.add(subcategory);
        }

        news.setSubcategoriesList(validSubcategories);

        String archiveDateString = newsDTO.getArchiveDate();
        LocalDate archiveDate = null;

        try {
            archiveDate = LocalDate.parse(archiveDateString, DateTimeFormatter.ISO_LOCAL_DATE); // Parsing della stringa in LocalDate
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato della data di archiviazione non valido. Utilizza il formato 'yyyy-MM-dd'.");
        }

        news.setArchiveDate(archiveDate.toString());

        if (LocalDate.now().plusDays(30).isBefore(archiveDate)) {
            newsRepository.save(news);
        } else {
            throw new NewsDateTooEarlyException("La data di archiviazione deve essere almeno 30 giorni dopo la data di pubblicazione.");
        }

        loggerInfo.info("News con ID " + news.getId() + " creata.");
        return news;
    }

    public News getNewsById(UUID id) throws NewsNotFoundException {
        return newsRepository.findById(id)
                .orElseThrow(() -> new NewsNotFoundException("News not found with id: " + id));
    }

    public List<News> getAllNewss(String sortBy) {
        List<News> newss = newsRepository.findAll(Sort.by(sortBy));
        loggerInfo.info("Retrieved all newss sorted by " + sortBy);
        return newss;
    }

    public Page<News> getNewss(int page, String sortBy) {
        int fixedSize = 15;
        Pageable pageable = PageRequest.of(page, fixedSize, Sort.by(sortBy));
        Page<News> newss = newsRepository.findAll(pageable);
        loggerInfo.info("Retrieved newss page " + page + " with fixed size " + fixedSize + " sorted by " + sortBy);
        return newss;
    }

    public News updateNews(UUID id, NewsDTO newsDTO) throws NewsNotFoundException, NewsDateTooEarlyException, CategoryAndSubcategoryMismatchException, CategoryNotFoundException {
        News news = getNewsById(id);

        news.setAuthor(newsDTO.getAuthor());
        news.setTitle(newsDTO.getTitle());
        news.setBody(newsDTO.getBody());
        List<Subcategory> subcategories = newsDTO.getSubcategories();
        for (int i = 0; i < subcategories.size(); i++) {
            Subcategory subcategory = subcategoryService.getCategoryById(subcategories.get(i).getId());
            if (subcategory.getMainCategory() != newsDTO.getMainCategory()) {
                throw new CategoryAndSubcategoryMismatchException("You can't enter subcategories that don't belong to the main category of the article.");
            }
            news.setSubcategoriesList(subcategories);
        }


        String archiveDateString = newsDTO.getArchiveDate();
        LocalDate archiveDate = null;

        try {
            archiveDate = LocalDate.parse(archiveDateString, DateTimeFormatter.ISO_LOCAL_DATE); // Parsing della stringa in LocalDate
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid archive date format. Use the format 'yyyy-MM-dd'.");
        }

        news.setArchiveDate(archiveDate.toString());

        if (LocalDate.now().plusDays(30).isBefore(archiveDate)) {
            newsRepository.save(news);
        } else {
            throw new NewsDateTooEarlyException("The archive date must be at least 30 days after the publication date.");
        }
        loggerInfo.info("News with id " + id + " updated.");

        return news;
    }

    public String deleteNews(UUID id) throws NewsNotFoundException {
        News news = getNewsById(id);
        newsRepository.delete(news);
        loggerInfo.info("News with id " + id + " deleted successfully.");
        return "News with id " + id + " deleted successfully.";
    }
}
