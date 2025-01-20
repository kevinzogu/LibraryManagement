package al.sda.LibraryManagement.controller;

import al.sda.LibraryManagement.entity.Book;
import al.sda.LibraryManagement.service.AuthorService;
import al.sda.LibraryManagement.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {
    
    private final BookService bookService;
    private final AuthorService authorService;
    
    public BookController(BookService bookService, AuthorService authorService) {
        this.bookService = bookService;
        this.authorService = authorService;
    }
    
    @GetMapping("/list")
    public String getAllBooks(
            @RequestParam(value = "searchBy", required = false, defaultValue = "title") String searchBy,
            @RequestParam(value = "searchText", required = false) String searchText,
            Model model) {
        
        List<Book> books;
        
        if (searchText != null && !searchText.isEmpty()) {
            if ("author".equalsIgnoreCase(searchBy)) {
                books = bookService.searchBooksByAuthor(searchText);
            } else {
                books = bookService.searchBooksByTitle(searchText);
            }
            model.addAttribute("searchText", searchText);
            model.addAttribute("searchBy", searchBy);
        } else {
            books = bookService.getAllBooks();
            for (Book book : books) {
               book.getAuthor().getName();
            }
            model.addAttribute("searchText", "");
            model.addAttribute("searchBy", "title");
        }
        
        model.addAttribute("books", books);
        model.addAttribute("isEmpty", books.isEmpty());
        return "book/books";
    }
    
    
    
    @GetMapping("/addBook")
    public String addBook(Model model) {
        model.addAttribute("authors", authorService.getAllAuthors());
        model.addAttribute("book", new Book());
        return "book/addBook";
    }
    
    @PostMapping("/addBook")
    public String addBook(@ModelAttribute Book book) {
        bookService.addBook(book);
        return "redirect:/books/list";
    }
    
    @RequestMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "redirect:/books/list";
    }
    
    @GetMapping("/update/{id}")
    public String updateBook(@PathVariable Long id, Model model) {
        model.addAttribute("authors", authorService.getAllAuthors());
        model.addAttribute("book", bookService.getBookById(id));
        return "book/updateBook";
    }
    
    @PostMapping("/update/{id}")
    public String updateBook(@PathVariable Long id, @ModelAttribute Book book) {
        book.setId(id);
        bookService.addBook(book);
        return "redirect:/books/list";
    }
}
