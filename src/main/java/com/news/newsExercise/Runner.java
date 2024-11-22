package com.news.newsExercise;

import com.news.newsExercise.Enum.CategoryEnum;
import com.news.newsExercise.entity.Subcategory;
import com.news.newsExercise.repository.SubcategoryRepository;
import com.news.newsExercise.service.SubcategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class Runner implements CommandLineRunner {

    @Autowired
    private SubcategoryService subcategoryService;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    public static void main(String[] args) {
        SpringApplication.run(Runner.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (subcategoryService.getAllCategories("mainCategory").isEmpty()) {
            List<Subcategory> subcategories = List.of(
                    createSubcategory(CategoryEnum.Attualità, "Politica"),
                    createSubcategory(CategoryEnum.Attualità, "Economia"),
                    createSubcategory(CategoryEnum.Attualità, "Cronaca"),
                    createSubcategory(CategoryEnum.Ambiente, "Cambiamenti Climatici"),
                    createSubcategory(CategoryEnum.Ambiente, "Biodiversità"),
                    createSubcategory(CategoryEnum.Ambiente, "Inquinamento"),
                    createSubcategory(CategoryEnum.Moda, "Haute Couture"),
                    createSubcategory(CategoryEnum.Moda, "Streetwear"),
                    createSubcategory(CategoryEnum.Moda, "Accessori"),
                    createSubcategory(CategoryEnum.Moda, "Tendenze Stagionali")
            );

            for (int i = 0; i < subcategories.size(); i++) {
                subcategoryRepository.save(subcategories.get(i));
            }

            System.out.println("Subcategories table populated with default values.");
        } else {
            System.out.println("Subcategories table already populated.");
        }
    }

    private Subcategory createSubcategory(CategoryEnum category, String subcategoryName) {
        Subcategory subcategory = new Subcategory();
        subcategory.setMainCategory(category);
        subcategory.setSubcategory(subcategoryName);
        return subcategory;
    }
}
