package pl.bartlomiejpietrzyk.controller.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.bartlomiejpietrzyk.model.Hint;
import pl.bartlomiejpietrzyk.repository.HintRepository;

import java.util.List;

@RestController
@RequestMapping("/api/hint")
public class HintRestController {
    private static final Logger logger = LoggerFactory.getLogger(HintRestController.class);
    private HintRepository hintRepository;

    @Autowired
    public HintRestController(HintRepository hintRepository) {
        this.hintRepository = hintRepository;
    }

    @RequestMapping(value = "/add/{title}/{description}",
            method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> createHint(@PathVariable("title") String title,
                                             @PathVariable("description") String description) {
        Hint hint = new Hint();
        if (hintRepository.findByTitle(title) != null) {
            logger.error("Hint: " + title + " already exist!");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        hint.setTitle(title);
        hint.setDescription(description);
        hintRepository.save(hint);
        logger.info("Hint: " + title + " created!");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/update/{id}/{title}/{description}",
            method = {RequestMethod.GET, RequestMethod.PUT})
    public ResponseEntity<String> updateHint(@PathVariable("id") Long id,
                                             @PathVariable("title") String title,
                                             @PathVariable("description") String description) {
        Hint hint = hintRepository.getOne(id);
        if (hint == null) {
            logger.error("Hint " + title + " doesn't exist!");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        hint.setTitle(title);
        hint.setDescription(description);
        hintRepository.save(hint);
        logger.error("Hint " + title + " updated!");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/{id}",
            method = {RequestMethod.GET, RequestMethod.DELETE})
    public ResponseEntity<String> deleteHint(@PathVariable("id") Long id) {
        Hint hint = hintRepository.getOne(id);
        if (hint == null) {
            logger.error("Hint: " + hint.getTitle() + " doesn't exist!");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        hintRepository.deleteById(id);
        logger.error("Hint: " + hint.getTitle() + " deleted!");
        return new ResponseEntity<>(HttpStatus.GONE);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Hint> showAllHints() {
        return hintRepository.findAll();
    }
}
