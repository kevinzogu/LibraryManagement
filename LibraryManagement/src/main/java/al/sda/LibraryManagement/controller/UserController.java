package al.sda.LibraryManagement.controller;

import al.sda.LibraryManagement.dto.ResponseDto;
import al.sda.LibraryManagement.entity.User;
import al.sda.LibraryManagement.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {
    
    private final UserService userService;
    
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    private void dbg(String s) {
        logger.info("UserController =>  {}", s);
    }
    
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping("/updateUser/{username}")
    public String updateUser(@PathVariable String username, Model model) {
        dbg("updateUser");
        model.addAttribute("username", username);
        model.addAttribute("user", userService.findUserByUsername(username));
        return "user/updateUser";
    }
    
    @PostMapping("/updateUser/{username}")
    public String updateUser(@PathVariable String username, @ModelAttribute User user,Model model) {
        dbg("Inside updateUser method with username: " + username);
        ResponseDto responseDto = userService.updateUser(username, user);
        if (responseDto.isSuccess()) {
            dbg("User updated successfully");
            return "redirect:/";
        }else {
            dbg("Error updating user: " + responseDto.getMessage());
            model.addAttribute("errorMessage",responseDto.getMessage());
            model.addAttribute("user", user);
            return "user/updateUser";
        }
    }
}
