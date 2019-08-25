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
import pl.bartlomiejpietrzyk.model.Hint;
import pl.bartlomiejpietrzyk.repository.HintRepository;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/hint")
@Api(value = "Hint management system",
        description = "Operations pertaining to hint in Hint management system.")
public class HintRestController {
    private static final Logger logger = LoggerFactory.getLogger(HintRestController.class);
    private HintRepository hintRepository;

    @Autowired
    public HintRestController(HintRepository hintRepository) {
        this.hintRepository = hintRepository;
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 201, message = "Successfully created resource"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 409, message = "The resource you were trying to create, already exist!"),
            @ApiResponse(code = 410, message = "The resource you were trying to reach is gone")
    })

    @ApiOperation(value = "Show list of all hints")
    @RequestMapping(method = RequestMethod.GET)
    public List<Hint> showAllHints() {
        return hintRepository.findAll();
    }

    @ApiOperation(value = "Find Hint by ID")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Hint findHintById(@PathVariable("id") Long id) {
        return hintRepository.getOne(id);
    }

    @ApiOperation(value = "Create a hint")
    @RequestMapping(value = "/add/{title}/{description}",
            method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> createHint(@PathVariable("title") String title,
                                             @PathVariable("description") String description) {
        Hint hint = new Hint();
        if (hintRepository.findByTitle(title) != null) {
            logger.error(LocalDateTime.now() + " :: Hint: " + title + " already exist!");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        hint.setTitle(title);
        hint.setDescription(description);
        hintRepository.save(hint);
        logger.info(LocalDateTime.now() + " :: Hint: " + title + " created!");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update a hint")
    @RequestMapping(value = "/update/{id}/{title}/{description}",
            method = {RequestMethod.GET, RequestMethod.PUT})
    public ResponseEntity<String> updateHint(@PathVariable("id") Long id,
                                             @PathVariable("title") String title,
                                             @PathVariable("description") String description) {
        Hint hint = hintRepository.getOne(id);
        if (hint == null) {
            logger.error(LocalDateTime.now() + " :: Hint: " + title + " doesn't exist!");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        hint.setTitle(title);
        hint.setDescription(description);
        hintRepository.save(hint);
        logger.info(LocalDateTime.now() + " :: Hint: " + title + " updated!");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Delete a Hint")
    @RequestMapping(value = "/delete/{id}",
            method = {RequestMethod.GET, RequestMethod.DELETE})
    public ResponseEntity<String> deleteHint(@PathVariable("id") Long id) {
        Hint hint = hintRepository.getOne(id);
        if (hint == null) {
            logger.error(LocalDateTime.now() + " :: Hint: " + hint.getTitle() + " doesn't exist!");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        hintRepository.deleteById(id);
        logger.info(LocalDateTime.now() + " :: Hint: " + hint.getTitle() + " deleted!");
        return new ResponseEntity<>(HttpStatus.GONE);
    }
}
