package pl.bartlomiejpietrzyk.controller.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import pl.bartlomiejpietrzyk.model.Category;
import pl.bartlomiejpietrzyk.repository.CategoryRepository;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/category")
@Api(value = "Category management system",
        description = "Operations pertaining to category in Category management system.")
public class CategoryRestController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryRestController.class);
    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryRestController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 201, message = "Successfully created resource"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 410, message = "The resource you were trying to reach is gone")
    })
    @ApiOperation(value = "Show list of all categories")
    @RequestMapping(method = RequestMethod.GET)
    public List<Category> showAllCategories() {
        return categoryRepository.findAll();
    }

    @ApiOperation(value = "Find Category by ID")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Category findCategoryById(@PathVariable("id") Long id) {
        return categoryRepository.getOne(id);
    }

    @ApiOperation(value = "Create a Category")
    @RequestMapping(value = "/add/{name}",
            method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> createCategory(@PathVariable("name") String name,
                                                 UriComponentsBuilder uriComponentsBuilder) {
        Category category = new Category();
        category.setName(name);
        if (categoryRepository.findByName(name) != null) {
            logger.error(LocalDateTime.now() + " :: Category: " + name + " already exist!");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        categoryRepository.save(category);
        logger.info(LocalDateTime.now() + " :: Category: " + name + ", ID: " + category.getId() + " created!");
        return ResponseEntity.created(URI.create(uriComponentsBuilder.toUriString() +
                "/api/category/" + category.getId())).build();
    }

    @ApiOperation(value = "Update a Category")
    @RequestMapping(value = "/update/{id}/{name}",
            method = {RequestMethod.GET, RequestMethod.PUT})
    public ResponseEntity<String> updateCategory(@PathVariable("id") Long id,
                                                 @PathVariable("name") String name) {
        Category category = categoryRepository.getOne(id);
        if (category == null) {
            logger.error(LocalDateTime.now() + " :: Category: " + name + " doesn't exist!");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        category.setName(name);
        categoryRepository.save(category);
        logger.info(LocalDateTime.now() + " :: Category: " + name + " updated!");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Delete a Category")
    @RequestMapping(value = "/delete/{id}",
            method = {RequestMethod.GET, RequestMethod.DELETE})
    public ResponseEntity<String> deleteCategory(@PathVariable("id") Long id) {
        Category category = categoryRepository.getOne(id);
        if (category == null) {
            logger.error(LocalDateTime.now() + " :: Category: " + category.getName() + " doesn't exist!");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        categoryRepository.deleteById(id);
        logger.info(LocalDateTime.now() + " :: Category: " + category.getName() + " deleted!");
        return new ResponseEntity<>(HttpStatus.GONE);
    }
}
