package al.sda.LibraryManagement.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {
    
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    
    private void dbg(String s) {
        logger.info("HomeController =>  {}", s);
    }
    
    @GetMapping("/")
    public String home(Model model) {
        dbg("Inside home");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        dbg("Authentication => " + authentication);
        if (authentication != null && authentication.isAuthenticated()) {
            dbg("Authentication is authenticated");
            User user = (User) authentication.getPrincipal();
            String username = user.getUsername();
            dbg("Username => " + username);
            model.addAttribute("username", username);
        } else {
            dbg("Authentication is not authenticated");
            model.addAttribute("username", "Vizitor");
        }
        return "home";
    }
}
