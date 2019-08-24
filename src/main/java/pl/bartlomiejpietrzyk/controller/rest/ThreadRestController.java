package pl.bartlomiejpietrzyk.controller.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.bartlomiejpietrzyk.model.Thread;
import pl.bartlomiejpietrzyk.repository.HintRepository;
import pl.bartlomiejpietrzyk.repository.ThreadRepository;

import java.util.List;

@RestController
@RequestMapping("/api/thread")
public class ThreadRestController {
    private static final Logger logger = LoggerFactory.getLogger(ThreadRestController.class);
    private ThreadRepository threadRepository;
    private HintRepository hintRepository;

    @Autowired
    public ThreadRestController(ThreadRepository threadRepository, HintRepository hintRepository) {
        this.threadRepository = threadRepository;
        this.hintRepository = hintRepository;
    }

    @RequestMapping(value = "/add/{title}/{description}",
            method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> createThread(@PathVariable("title") String title,
                                               @PathVariable("description") String description,
                                               @PathVariable("hintId") Long hintId) {
        Thread thread = new Thread();
        if (threadRepository.findByTitle(title) != null) {
            logger.error("Thread: " + title + " already exist!");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        thread.setTitle(title);
        thread.setDescription(description);
        thread.setHint(hintRepository.getOne(hintId));
        threadRepository.save(thread);
        logger.info("Thread: " + title + " created!");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/update/{id}/{title}/{description}",
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
        logger.error("Thread " + title + " updated!");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/{id}",
            method = {RequestMethod.GET, RequestMethod.DELETE})
    public ResponseEntity<String> deleteThread(@PathVariable("id") Long id) {
        Thread thread = threadRepository.getOne(id);
        if (thread == null) {
            logger.error("Thread: " + thread.getTitle() + " doesn't exist!");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        threadRepository.deleteById(id);
        logger.error("Thread: " + thread.getTitle() + " deleted!");
        return new ResponseEntity<>(HttpStatus.GONE);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Thread> showAllThreads() {
        return threadRepository.findAll();
    }
}
