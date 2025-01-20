package al.sda.LibraryManagement.service;

import al.sda.LibraryManagement.entity.Book;
import al.sda.LibraryManagement.entity.Loan;
import al.sda.LibraryManagement.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
    
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    
    public void addBook(Book book) {
        book.setAvailable(book.getQuantity());
        bookRepository.save(book);
    }
    
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
    
    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElse(new Book());
    }
    
    public void updateBookAvailability(Loan currLoan,Loan prevLoan) {
        Book book = bookRepository.findById(currLoan.getBook().getId()).orElse(new Book());
        if (currLoan.getStatus().equals(prevLoan.getStatus())) {
            return;
        }
        if (currLoan.getStatus().equals("Huazuar")) {
            book.setAvailable(book.getAvailable() - 1);
        } else if (currLoan.getStatus().equals("Kthyer")) {
            book.setAvailable(book.getAvailable() + 1);
        }
        
        bookRepository.save(book);
        
    }
    
    public List<Book> searchBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }
    
    public List<Book> searchBooksByAuthor(String author) {
        return bookRepository.findByAuthor_NameContainingIgnoreCase(author);
    }

}
