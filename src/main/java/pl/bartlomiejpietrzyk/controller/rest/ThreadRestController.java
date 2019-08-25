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
import pl.bartlomiejpietrzyk.model.Thread;
import pl.bartlomiejpietrzyk.repository.HintRepository;
import pl.bartlomiejpietrzyk.repository.ThreadRepository;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/thread")
@Api(value = "Thread management system",
        description = "Operations pertaining to thread in Thread management system.")
public class ThreadRestController {
    private static final Logger logger = LoggerFactory.getLogger(ThreadRestController.class);
    private ThreadRepository threadRepository;
    private HintRepository hintRepository;

    @Autowired
    public ThreadRestController(ThreadRepository threadRepository, HintRepository hintRepository) {
        this.threadRepository = threadRepository;
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

    @ApiOperation(value = "Create a Thread")
    @RequestMapping(value = "/add/{title}/{description}/{hintId}",
            method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> createThread(@PathVariable("title") String title,
                                               @PathVariable("description") String description,
                                               @PathVariable("hintId") Long hintId,
                                               UriComponentsBuilder uriComponentsBuilder) {
        Thread thread = new Thread();
        if (threadRepository.findByTitle(title) != null) {
            logger.error(LocalDateTime.now() + " :: Thread: " + title + " already exist!");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        thread.setTitle(title);
        thread.setDescription(description);
        thread.setHint(hintRepository.getOne(hintId));
        threadRepository.save(thread);
        logger.info(LocalDateTime.now() + " :: Thread: " + title + ", ID: " + thread.getId() + " created!");
        return ResponseEntity.created(URI.create(uriComponentsBuilder.toUriString() +
                "/api/thread/" + thread.getId())).build();    }

    @ApiOperation(value = "Update a Thread")
    @RequestMapping(value = "/update/{id}/{title}/{description}/{hintId}",
            method = {RequestMethod.GET, RequestMethod.PUT})
    public ResponseEntity<String> updateThread(@PathVariable("id") Long id,
                                               @PathVariable("title") String title,
                                               @PathVariable("description") String description,
                                               @PathVariable("hintId") Long hintId) {
        Thread thread = threadRepository.getOne(id);
        if (thread == null) {
            logger.error("Thread " + title + " doesn't exist!");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        thread.setTitle(title);
        thread.setDescription(description);
        thread.setHint(hintRepository.getOne(hintId));
        threadRepository.save(thread);
        logger.info("Thread " + title + " updated!");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Delete a Thread")
    @RequestMapping(value = "/delete/{id}",
            method = {RequestMethod.GET, RequestMethod.DELETE})
    public ResponseEntity<String> deleteThread(@PathVariable("id") Long id) {
        Thread thread = threadRepository.getOne(id);
        if (thread == null) {
            logger.error(LocalDateTime.now() + " :: Thread: " + thread.getTitle() + " doesn't exist!");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        threadRepository.deleteById(id);
        logger.info(LocalDateTime.now() + " :: Thread: " + thread.getTitle() + " deleted!");
        return new ResponseEntity<>(HttpStatus.GONE);
    }
    
    @ApiOperation(value = "Show list of all threads")
    @RequestMapping(method = RequestMethod.GET)
    public List<Thread> showAllThreads() {
        return threadRepository.findAll();
    }
}
