package tig.server.testLogin;

import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller
public class Controller {

    @GetMapping("/")
    public String login() {
        return "login.html";
    }

}
