package al.sda.LibraryManagement.controller;

import al.sda.LibraryManagement.entity.User;
import al.sda.LibraryManagement.service.SignupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;

@Controller
public class SignupController {
    
    private final PasswordEncoder passwordEncoder;
    private final SignupService signupService;
    
    private static final Logger logger = LoggerFactory.getLogger(SignupController.class);
    
    private void dbg(String s) {
        logger.info("SignupController =>  {}", s);
    }
    
    public SignupController(PasswordEncoder passwordEncoder, SignupService signupService) {
        this.passwordEncoder = passwordEncoder;
        this.signupService = signupService;
    }

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        dbg("Inside showSignupForm");
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String processSignup(@ModelAttribute User user, Model model) {
        dbg("Inside processSignup => " + user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singletonList("ROLE_USER"));
        dbg("User after encoding => " + user);
        SignupService.SignupResponse signupResponse = signupService.saveUser(user);
        dbg("SignupResponse => " + signupResponse);
        if (signupResponse.isSuccess()){
            dbg("Signup success");
            model.addAttribute("successMessage", "Regjistrimi u krye me sukses! Mund të logoheni.");
            return "login";
        }else {
            dbg("Signup failed with message: " + signupResponse.getMessage());
            model.addAttribute("errorMessage", signupResponse.getMessage());
            return "signup";
        }
    }
}
