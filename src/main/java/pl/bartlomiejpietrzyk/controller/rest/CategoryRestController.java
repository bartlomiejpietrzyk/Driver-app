package pl.bartlomiejpietrzyk.controller.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.bartlomiejpietrzyk.model.Category;
import pl.bartlomiejpietrzyk.repository.CategoryRepository;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryRestController {
    public static final Logger logger = LoggerFactory.getLogger(CategoryRestController.class);
    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryRestController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @RequestMapping(value = "/add/{name}",
            method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> createCategory(@PathVariable("name") String name) {
        Category category = new Category();
        category.setName(name);
        if (categoryRepository.findByName(name) != null) {
            logger.error("Category " + name + " already exist!");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        categoryRepository.save(category);
        logger.info("Category " + name + " created!");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/update/{id}/{name}",
            method = {RequestMethod.GET, RequestMethod.PUT})
    public ResponseEntity<String> updateCategory(@PathVariable("id") Long id,
                                                 @PathVariable("name") String name) {
        Category category = categoryRepository.getOne(id);
        if (category == null) {
            logger.error("Category " + name + " doesn't exist!");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        category.setName(name);
        categoryRepository.save(category);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/{id}",
            method = {RequestMethod.GET, RequestMethod.DELETE})
    public ResponseEntity<String> deleteCategory(@PathVariable("id") Long id) {
        Category category = categoryRepository.getOne(id);
        if (category == null) {
            logger.error("Category " + category.getName() + " doesn't exist!");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        categoryRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.GONE);
    }

    @RequestMapping(value = "/all",
    method = RequestMethod.GET)
    public List<Category> showAllCategories() {
        return categoryRepository.findAll();
    }
}
