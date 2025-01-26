package al.sda.LibraryManagement.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    
    private void dbg(String s) {
        logger.info("LoginController =>  {}", s);
    }
    
    @GetMapping("/login")
    public String showLoginPage() {
        dbg("Inside showLoginPage");
        return "login";
    }
}
