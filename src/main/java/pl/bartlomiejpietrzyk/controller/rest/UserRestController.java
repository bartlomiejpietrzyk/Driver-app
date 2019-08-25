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
import pl.bartlomiejpietrzyk.model.User;
import pl.bartlomiejpietrzyk.repository.UserRepository;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@Api(value = "User management system",
        description = "Operations pertaining to category in User managament system")
public class UserRestController {
    private static final Logger logger = LoggerFactory.getLogger(UserRestController.class);
    private UserRepository userRepository;

    @Autowired
    public UserRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
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

    @ApiOperation(value = "Show list of all users")
    @RequestMapping(method = RequestMethod.GET)
    public List<User> showAllUsers() {
        return userRepository.findAll();
    }

    @ApiOperation(value = "Find User by ID")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public User findUserById(@PathVariable("id") Long id) {
        return userRepository.getOne(id);
    }

    @ApiOperation(value = "Create a user")
    @RequestMapping(value = "/add/{username}/{password}/{enabled}",
            method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<User> createUser(@PathVariable("username") String username,
                                           @PathVariable("password") String password,
                                           @PathVariable("enabled") int enabled,
                                           UriComponentsBuilder uriComponentsBuilder) {
        User user = new User();
        if (userRepository.findByUsername(username) != null) {
            logger.error(LocalDateTime.now() + " :: User: " + username + " already exist!");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        user.setUsername(username);
        user.setPassword(password);
        user.setEnabled(enabled);
        userRepository.save(user);
        logger.info(LocalDateTime.now() + " :: User: " + username + ", ID: " + user.getId() + " created!");
        return ResponseEntity.created(URI.create(uriComponentsBuilder.toUriString() +
                "/api/user/" + user.getId())).build();
    }

    @ApiOperation(value = "Update a user")
    @RequestMapping(value = "/update/{id}/{username}/{password}/{enabled}",
            method = {RequestMethod.GET, RequestMethod.PUT})
    public ResponseEntity<String> updateUser(@PathVariable("id") Long id,
                                             @PathVariable("username") String username,
                                             @PathVariable("password") String password,
                                             @PathVariable("enabled") int enabled,
                                             UriComponentsBuilder uriComponentsBuilder) {
        User user = userRepository.getOne(id);
        if (user != null) {
            logger.error(LocalDateTime.now() + " :: User: " + username + " already exist!");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        user.setUsername(username);
        user.setPassword(password);
        user.setEnabled(enabled);
        userRepository.save(user);
        logger.info(LocalDateTime.now() + " :: User: " + username + ", ID: " + user.getId() + " updated!");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Delete a user")
    @RequestMapping(value = "/delete/{id}",
            method = {RequestMethod.GET, RequestMethod.DELETE})
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
        User user = userRepository.getOne(id);
        if (user == null) {
            logger.error(LocalDateTime.now() + " :: User: " + user.getUsername() + " doesn't exist!");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        userRepository.deleteById(id);
        logger.info(LocalDateTime.now() + " :: User: " + user.getUsername() + ", ID: " + user.getId() + " updated!");
        return new ResponseEntity<>(HttpStatus.GONE);
    }
}


