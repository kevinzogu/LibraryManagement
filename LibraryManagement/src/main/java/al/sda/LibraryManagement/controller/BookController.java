package al.sda.LibraryManagement.controller;

import al.sda.LibraryManagement.dto.ResponseDto;
import al.sda.LibraryManagement.entity.Book;
import al.sda.LibraryManagement.service.AuthorService;
import al.sda.LibraryManagement.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {
    
    private final BookService bookService;
    private final AuthorService authorService;
    
    private static final Logger logger = LoggerFactory.getLogger(BookController.class);
    
    private void dbg(String s) {
        logger.info("BookController =>  {}", s);
    }
    
    public BookController(BookService bookService, AuthorService authorService) {
        this.bookService = bookService;
        this.authorService = authorService;
    }
    
    @GetMapping("/list")
    public String getAllBooks(
            @RequestParam(value = "searchBy", required = false, defaultValue = "title") String searchBy,
            @RequestParam(value = "searchText", required = false) String searchText,
            Model model) {
        dbg("Inside getAllBooks with searchBy: " + searchBy + ", searchText: " + searchText);
        List<Book> books;
        
        if (searchText != null && !searchText.isEmpty()) {
            dbg("Searching books by " + searchBy + " for: " + searchText);
            if ("author".equalsIgnoreCase(searchBy)) {
                dbg("Searching by author");
                books = bookService.searchBooksByAuthor(searchText);
            } else {
                dbg("Searching by title");
                books = bookService.searchBooksByTitle(searchText);
            }
            dbg("Books found: " + books.size());
            model.addAttribute("searchText", searchText);
            model.addAttribute("searchBy", searchBy);
        } else {
            dbg("Getting all books");
            books = bookService.getAllBooks();
            
            model.addAttribute("searchText", "");
            model.addAttribute("searchBy", "title");
        }
        
        model.addAttribute("books", books);
        model.addAttribute("isEmpty", books.isEmpty());
        return "book/books";
    }
    
    
    
    @GetMapping("/addBook")
    public String addBook(Model model) {
        dbg("Inside addBook");
        model.addAttribute("authors", authorService.getAllAuthors());
        model.addAttribute("book", new Book());
        return "book/addBook";
    }
    
    @PostMapping("/addBook")
    public String addBook(@ModelAttribute Book book,Model model) {
        dbg("Inside addBook");
        ResponseDto responseDto = bookService.addBook(book);
        if (responseDto.isSuccess()) {
            dbg("Book added successfully");
            return "redirect:/books/list";
        }else {
            dbg("Book not added : " + responseDto.getMessage() );
            model.addAttribute("errorMessage", responseDto.getMessage());
            model.addAttribute("book", book);
            return "book/addBook";
        }
    }
    
    @RequestMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id,Model model) {
        dbg("Inside deleteBook with id: " + id);
        ResponseDto responseDto = bookService.deleteBook(id);
        if (responseDto.isSuccess()) {
            return "redirect:/books/list";
        } else {
            model.addAttribute("errorMessage", responseDto.getMessage());
            return "book/error";
        }
        
    }
    
    @GetMapping("/update/{id}")
    public String updateBook(@PathVariable Long id, Model model) {
        dbg("Inside updateBook with id: " + id);
        model.addAttribute("authors", authorService.getAllAuthors());
        model.addAttribute("book", bookService.getBookById(id));
        return "book/updateBook";
    }
    
    @PostMapping("/update/{id}")
    public String updateBook(@PathVariable Long id, @ModelAttribute Book book,Model model) {
        dbg("Inside updateBook with id: " + id);
        book.setId(id);
        ResponseDto responseDto = bookService.updateBookQuantity(book);
        if (responseDto.isSuccess()) {
            dbg("Book added successfully");
            return "redirect:/books/list";
        }else {
            dbg("Book not added : " + responseDto.getMessage() );
            model.addAttribute("errorMessage", responseDto.getMessage());
            model.addAttribute("book", book);
            return "book/updateBook";
        }
    }
}
