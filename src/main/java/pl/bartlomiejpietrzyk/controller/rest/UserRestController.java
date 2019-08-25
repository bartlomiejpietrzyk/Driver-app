package pl.bartlomiejpietrzyk.controller.rest;

import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.bartlomiejpietrzyk.repository.UserRepository;

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


}


