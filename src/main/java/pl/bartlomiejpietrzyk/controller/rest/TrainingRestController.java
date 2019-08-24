package pl.bartlomiejpietrzyk.controller.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.bartlomiejpietrzyk.model.Training;
import pl.bartlomiejpietrzyk.repository.TrainingRepository;

import java.util.List;

@RestController
@RequestMapping("/api/training")
public class TrainingRestController {
    private static final Logger logger = LoggerFactory.getLogger(TrainingRestController.class);
    private TrainingRepository trainingRepository;

    @Autowired
    public TrainingRestController(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    @RequestMapping(value = "/add/{title}/{description}/{answerA}" +
            "/{answerB}/{answerC}/{correctAnswer}/{points}",
            method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> createTraining(
            @PathVariable("title") String title,
            @PathVariable("description") String description,
            @PathVariable("answerA") String answerA,
            @PathVariable("answerB") String answerB,
            @PathVariable("answerC") String answerC,
            @PathVariable("correctAnswer") String correctAnswer,
            @PathVariable("points") Integer points) {
        Training training = new Training();
        if (trainingRepository.findByTitle(title) != null) {
            logger.error("Training: " + title + " already exist!");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        training.setTitle(title);
        training.setDescription(description);
        training.setAnswerA(answerA);
        training.setAnswerB(answerB);
        training.setAnswerC(answerC);
        training.setCorrectAnswer(correctAnswer);
        training.setPoints(points);
        trainingRepository.save(training);
        logger.info("Training: " + title + " created!");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/update/{id}/{title}/{description}/{answerA}" +
            "/{answerB}/{answerC}/{correctAnswer}/{points}",
            method = {RequestMethod.GET, RequestMethod.PUT})
    public ResponseEntity<String> updateTraining(
            @PathVariable("id") Long id,
            @PathVariable("title") String title,
            @PathVariable("description") String description,
            @PathVariable("answerA") String answerA,
            @PathVariable("answerB") String answerB,
            @PathVariable("answerC") String answerC,
            @PathVariable("correctAnswer") String correctAnswer,
            @PathVariable("points") Integer points) {
        Training training = trainingRepository.getOne(id);
        if (!trainingRepository.findByTitle(title)) {
            logger.error("Training: " + title + " doesn't exist!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        training.setTitle(title);
        training.setDescription(description);
        training.setAnswerA(answerA);
        training.setAnswerB(answerB);
        training.setAnswerC(answerC);
        training.setCorrectAnswer(correctAnswer);
        training.setPoints(points);
        trainingRepository.save(training);
        logger.error("Training " + title + " updated!");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/{id}",
            method = {RequestMethod.GET, RequestMethod.DELETE})
    public ResponseEntity<String> deleteTraining(@PathVariable("id") Long id) {
        Training training = trainingRepository.getOne(id);
        if (training == null) {
            logger.error("Training: " + training.getTitle() + " doesn't exist!");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        trainingRepository.deleteById(id);
        logger.error("Training: " + training.getTitle() + " deleted!");
        return new ResponseEntity<>(HttpStatus.GONE);
    }

    @RequestMapping(value = "/all",
            method = RequestMethod.GET)
    public List<Training> showAllTrainings() {
        return trainingRepository.findAll();
    }
}
