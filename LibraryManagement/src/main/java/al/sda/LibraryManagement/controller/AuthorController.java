package al.sda.LibraryManagement.controller;

import al.sda.LibraryManagement.entity.Author;
import al.sda.LibraryManagement.service.AuthorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/authors")
public class AuthorController {

    final private AuthorService authorService;
    
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }
    
    @RequestMapping("/list")
    @GetMapping("/list")
    public String getAllAuthors(
            @RequestParam(value = "searchBy", required = false, defaultValue = "name") String searchBy,
            @RequestParam(value = "searchText", required = false) String searchText,
            Model model) {
        
        List<Author> authors;
        
        if (searchText != null && !searchText.isEmpty()) {
            if ("email".equalsIgnoreCase(searchBy)) {
                authors = authorService.searchAuthorsByEmail(searchText);
            } else {
                authors = authorService.searchAuthorsByName(searchText);
            }
            model.addAttribute("searchText", searchText);
            model.addAttribute("searchBy", searchBy);
        } else {
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
        model.addAttribute("author", new Author());
        return "author/addAuthor";
    }
    
    @PostMapping("/addAuthor")
    public String addAuthor(@ModelAttribute Author author) {
        authorService.addAuthor(author);
        return "redirect:/authors/list";
    }
    
    @GetMapping("/delete/{id}")
    public String deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return "redirect:/authors/list";
    }
    
    @GetMapping("/update/{id}")
    public String updateAuthor(@PathVariable Long id, Model model) {
        Author author = authorService.getAuthorById(id);
        model.addAttribute("author", author);
        return "author/updateAuthor";
    }
    
    @PostMapping("/update/{id}")
    public String updateAuthor(@PathVariable Long id, @ModelAttribute Author author) {
        author.setId(id);
        authorService.addAuthor(author);
        return "redirect:/authors/list";
    }
}
