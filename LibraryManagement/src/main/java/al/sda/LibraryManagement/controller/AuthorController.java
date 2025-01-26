package al.sda.LibraryManagement.controller;

import al.sda.LibraryManagement.dto.ResponseDto;
import al.sda.LibraryManagement.entity.Author;
import al.sda.LibraryManagement.service.AuthorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/authors")
public class AuthorController {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthorController.class);
    
    private void dbg(String s) {
        logger.info("AuthorController =>  {}", s);
    }
    
    final private AuthorService authorService;
    
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }
    
    @GetMapping("/list")
    public String getAllAuthors(
            @RequestParam(value = "searchBy", required = false, defaultValue = "name") String searchBy,
            @RequestParam(value = "searchText", required = false) String searchText,
            Model model) {
        dbg("Inside getAllAuthors with searchBy: " + searchBy + ", searchText: " + searchText);
        List<Author> authors;
        
        if (searchText != null && !searchText.isEmpty()) {
            dbg("Searching authors by " + searchBy + " containing: " + searchText);
            if ("email".equalsIgnoreCase(searchBy)) {
                dbg("Searching by email");
                authors = authorService.searchAuthorsByEmail(searchText);
            } else {
                dbg("Searching by name");
                authors = authorService.searchAuthorsByName(searchText);
            }
            dbg("Found " + authors.size() + " authors");
            model.addAttribute("searchText", searchText);
            model.addAttribute("searchBy", searchBy);
        } else {
            dbg("Getting all authors");
            authors = authorService.getAllStudents();
            model.addAttribute("searchText", "");
            model.addAttribute("searchBy", "name");
        }
        
        model.addAttribute("authors", authors);
        model.addAttribute("isEmpty", authors.isEmpty());
        return "author/authors";
    }
    
    @GetMapping("/addAuthor")
    public String addAuthor(Model model) {
        dbg("Inside addAuthor");
        model.addAttribute("author", new Author());
        return "author/addAuthor";
    }
    
    @PostMapping("/addAuthor")
    public String addAuthor(@ModelAttribute Author author,Model model) {
        dbg("Adding author: " + author);
        ResponseDto responseDto = authorService.addAuthor(author);
        if (responseDto.isSuccess()) {
            return "redirect:/authors/list";
        } else {
            model.addAttribute("errorMessage", responseDto.getMessage());
            model.addAttribute("author", author);
            return "author/addAuthor";
        }
    }
    
    @GetMapping("/delete/{id}")
    public String deleteAuthor(@PathVariable Long id, Model model) {
        dbg("Deleting author with id: " + id);
        ResponseDto responseDto = authorService.deleteAuthor(id);
        
        if (responseDto.isSuccess()) {
            return "redirect:/authors/list";
        } else {
            model.addAttribute("errorMessage", responseDto.getMessage());
            return "author/error";
        }
    }

    
    @GetMapping("/update/{id}")
    public String updateAuthor(@PathVariable Long id, Model model) {
        dbg("Updating author with id: " + id);
        Author author = authorService.getAuthorById(id);
        model.addAttribute("author", author);
        return "author/updateAuthor";
    }
    
    @PostMapping("/update/{id}")
    public String updateAuthor(@PathVariable Long id, @ModelAttribute Author author,Model model) {
        dbg("Updating author with id: " + id + " to: " + author);
        author.setId(id);
        ResponseDto responseDto = authorService.addAuthor(author);
        if (responseDto.isSuccess()) {
            return "redirect:/authors/list";
        } else {
            model.addAttribute("errorMessage", responseDto.getMessage());
            model.addAttribute("author", author);
            return "author/updateAuthor";
        }
    }
}
