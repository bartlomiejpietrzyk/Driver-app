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
import pl.bartlomiejpietrzyk.model.Post;
import pl.bartlomiejpietrzyk.repository.PostRepository;
import pl.bartlomiejpietrzyk.repository.ThreadRepository;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/thread/post")
@Api(value = "Post management system",
        description = "Operations pertaining to post in Post management system.")
public class PostRestController {
    private static final Logger logger = LoggerFactory.getLogger(PostRestController.class);
    private PostRepository postRepository;
    private ThreadRepository threadRepository;

    @Autowired
    public PostRestController(PostRepository postRepository, ThreadRepository threadRepository) {
        this.postRepository = postRepository;
        this.threadRepository = threadRepository;
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 201, message = "Successfully created resource"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 410, message = "The resource you were trying to reach is gone")
    })

    @ApiOperation(value = "Show list of all posts")
    @RequestMapping(method = RequestMethod.GET)
    public List<Post> showAllCategories() {
        return postRepository.findAll();
    }


    @ApiOperation(value = "Find Post by ID")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Post findPostById(@PathVariable("id") Long id) {
        return postRepository.getOne(id);
    }

    @ApiOperation(value = "Create a Post")
    @RequestMapping(value = "/add/{threadId}/{description}",
            method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> createPost(@PathVariable("threadId") Long threadId,
                                             @PathVariable("description") String description,
                                             UriComponentsBuilder uriComponentsBuilder) {
        Post post = new Post();
        post.setThread(threadRepository.getOne(threadId));
        post.setDescription(description);
        if (!threadRepository.existsById(threadId)) {
            logger.error(LocalDateTime.now() + " :: Thread: " +
                    threadRepository.getOne(threadId).getTitle() + " doesn't exist!");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        logger.info(LocalDateTime.now() + " :: Post: " + description + ", ID: " + post.getId() + " created, :: Thread ID: " + threadId);
        return ResponseEntity.created(URI.create(uriComponentsBuilder.toUriString() +
                "/api/post/" + post.getId())).build();    }

    @ApiOperation(value = "Edit a Post")
    @RequestMapping(value = "/update/{threadId}/{postId}/{description}",
            method = {RequestMethod.GET, RequestMethod.PUT})
    public ResponseEntity<String> updatePost(@PathVariable("threadId") Long threadId,
                                             @PathVariable("postId") Long postId,
                                             @PathVariable("description") String description) {
        Post post = postRepository.getOne(postId);
        post.setThread(threadRepository.getOne(threadId));
        post.setDescription(description);
        if (!threadRepository.existsById(threadId)) {
            logger.error(LocalDateTime.now() + " :: Thread: " +
                    threadRepository.getOne(threadId).getTitle() + " doesn't exist!");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        if (!postRepository.existsById(postId)) {
            logger.error(LocalDateTime.now() + " :: Post ID: " + postId + " doesn't exist!");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        logger.info(LocalDateTime.now() + " :: Post: " + description + " updated!");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Delete a Post")
    @RequestMapping(value = "/delete/{postId}",
            method = {RequestMethod.GET, RequestMethod.DELETE})
    public ResponseEntity<String> deletePost(@PathVariable("postId") Long postId) {
        if (!postRepository.existsById(postId)) {
            logger.error(LocalDateTime.now() + " :: Post ID: " + postId + " doesn't exist!");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        logger.info(LocalDateTime.now() + " :: Post ID: " + postId + " deleted!");
        return new ResponseEntity<>(HttpStatus.GONE);
    }
}