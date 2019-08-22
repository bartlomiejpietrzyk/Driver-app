package pl.bartlomiejpietrzyk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.bartlomiejpietrzyk.model.Category;
import pl.bartlomiejpietrzyk.repository.CategoryRepository;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryRestController {
    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryRestController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @RequestMapping("/add")
    public Category createCategory(@RequestParam String name) {
        Category category = new Category();
        category.setName(name);
        categoryRepository.save(category);
        return category;
    }

    @GetMapping("/all")
    public List<Category> allCategories() {
        return categoryRepository.findAll();
    }

    @RequestMapping("/delete")
    public String deleteCategory(@RequestParam Long id) {
        categoryRepository.deleteById(id);
        return "Usunięto obiekt o ID: " + id;
    }

}
