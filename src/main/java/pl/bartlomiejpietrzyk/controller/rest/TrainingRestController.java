package pl.bartlomiejpietrzyk.controller.rest;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
import pl.bartlomiejpietrzyk.exception.ResourceNotFoundException;
import pl.bartlomiejpietrzyk.model.Training;
import pl.bartlomiejpietrzyk.repository.HintRepository;
import pl.bartlomiejpietrzyk.repository.TrainingRepository;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/training")
public class TrainingRestController {
    private static final Logger logger = LoggerFactory.getLogger(TrainingRestController.class);
    private TrainingRepository trainingRepository;
    private HintRepository hintRepository;

    @Autowired
    public TrainingRestController(TrainingRepository trainingRepository, HintRepository hintRepository) {
        this.trainingRepository = trainingRepository;
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

    @ApiOperation(value = "Show list of all trainings")
    @RequestMapping(method = RequestMethod.GET)
    public List<Training> showAllTrainings() {
        return trainingRepository.findAll();
    }

    @ApiOperation(value = "Find training by ID")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Training> showTrainingById(
            @ApiParam(value = "Training ID from which Object will retrieve", required = true)
            @PathVariable(value = "id") Long id) throws ResourceNotFoundException {
        Training training = trainingRepository.getOne(id);
        if (training == null) {
            logger.error(LocalDateTime.now() + " :: Training ID: " + id + " doesn't exist!");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        logger.info(LocalDateTime.now() + " :: Training ID: " + id + " found!");
        return ResponseEntity.ok().body(training);
    }

    @ApiOperation(value = "Create a Training")
    @RequestMapping(value = "/add/{title}/{description}/{answerA}" +
            "/{answerB}/{answerC}/{correctAnswer}/{points}/{hintId}",
            method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> createTraining(
            @PathVariable("title") String title,
            @PathVariable("description") String description,
            @PathVariable("answerA") String answerA,
            @PathVariable("answerB") String answerB,
            @PathVariable("answerC") String answerC,
            @PathVariable("correctAnswer") String correctAnswer,
            @PathVariable("points") Integer points,
            @PathVariable("hintId") Long hintId,
            UriComponentsBuilder uriComponentsBuilder) {
        Training training = new Training();
        if (trainingRepository.findByTitle(title) != null) {
            logger.error(LocalDateTime.now() + " :: Training: " + title + " already exist!");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        training.setTitle(title);
        training.setDescription(description);
        training.setAnswerA(answerA);
        training.setAnswerB(answerB);
        training.setAnswerC(answerC);
        training.setCorrectAnswer(correctAnswer);
        training.setPoints(points);
        training.setHint(hintRepository.getOne(hintId));
        trainingRepository.save(training);
        logger.info(LocalDateTime.now() + " :: Training: " + title + ", ID: " + training.getId() + " created!");
        return ResponseEntity.created(URI.create(uriComponentsBuilder.toUriString() +
                "/api/training/" + training.getId())).build();
    }

    @ApiOperation(value = "Update a Training")
    @RequestMapping(value = "/update/{id}/{title}/{description}/{answerA}" +
            "/{answerB}/{answerC}/{correctAnswer}/{points}/{hintId}",
            method = {RequestMethod.GET, RequestMethod.PUT})
    public ResponseEntity<String> updateTraining(
            @PathVariable("id") Long id,
            @PathVariable("title") String title,
            @PathVariable("description") String description,
            @PathVariable("answerA") String answerA,
            @PathVariable("answerB") String answerB,
            @PathVariable("answerC") String answerC,
            @PathVariable("correctAnswer") String correctAnswer,
            @PathVariable("points") Integer points,
            @PathVariable("hintId") Long hintId) {
        Training training = trainingRepository.getOne(id);
        if (!trainingRepository.existsById(training.getId())) {
            logger.error(LocalDateTime.now() + " :: Training: " + title + " doesn't exist!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        training.setTitle(title);
        training.setDescription(description);
        training.setAnswerA(answerA);
        training.setAnswerB(answerB);
        training.setAnswerC(answerC);
        training.setCorrectAnswer(correctAnswer);
        training.setPoints(points);
        training.setHint(hintRepository.getOne(hintId));
        trainingRepository.save(training);
        logger.info(LocalDateTime.now() + " :: Training: " + title + " updated!");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Delete a Training")
    @RequestMapping(value = "/delete/{id}",
            method = {RequestMethod.GET, RequestMethod.DELETE})
    public ResponseEntity<String> deleteTraining(@PathVariable("id") Long id) {
        Training training = trainingRepository.getOne(id);
        if (training == null) {
            logger.error(LocalDateTime.now() + " :: Training: " + training.getTitle() + " doesn't exist!");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        trainingRepository.deleteById(id);
        logger.info(LocalDateTime.now() + " :: Training: " + training.getTitle() + " deleted!");
        return new ResponseEntity<>(HttpStatus.GONE);
    }
}
