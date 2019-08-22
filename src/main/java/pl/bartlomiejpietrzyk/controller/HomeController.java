package pl.bartlomiejpietrzyk.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.bartlomiejpietrzyk.service.UserService;

@Controller
public class HomeController {

    @RequestMapping("/")
    public String index() {
        return "index";
    }

}
