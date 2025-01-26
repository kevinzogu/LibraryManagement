package al.sda.LibraryManagement.service;

import al.sda.LibraryManagement.dto.ResponseDto;
import al.sda.LibraryManagement.entity.Book;
import al.sda.LibraryManagement.entity.Loan;
import al.sda.LibraryManagement.repository.BookRepository;
import al.sda.LibraryManagement.repository.LoanRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;
    
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);
    
    private void dbg(String s) {
        logger.info("BookService =>  {}", s);
    }
    
    public BookService(BookRepository bookRepository, LoanRepository loanRepository) {
        this.bookRepository = bookRepository;
        this.loanRepository = loanRepository;
    }
    
    public List<Book> getAllBooks() {
        dbg("Inside getAllBooks");
        return bookRepository.findAll();
    }
    
    public ResponseDto addBook(Book book) {
        dbg("Inside addBook with book: " + book);
        try {
            book.setAvailable(book.getQuantity());
            bookRepository.save(book);
            return new ResponseDto(Boolean.TRUE, "Libri u shtua me sukses.");
            
        } catch (DataIntegrityViolationException e) {
            dbg("Gabim gjatë shtimit të librit (shkelje e integritetit të të dhënave): " + e.getMessage());
            return new ResponseDto(Boolean.FALSE, "Nuk mund të shtohet libri. Shkelje e integritetit të të dhënave.");
        } catch (ConstraintViolationException e) {
            dbg("Gabim gjatë shtimit të librit (shkelje kufizimi): " + e.getMessage());
            return new ResponseDto(Boolean.FALSE, "Nuk mund të shtohet libri. Ka një shkelje kufizimi të të dhënave.");
        } catch (Exception e) {
            dbg("Gabim gjatë shtimit të librit: " + e.getMessage());
            return new ResponseDto(Boolean.FALSE, "Ka ndodhur një gabim gjatë shtimit të librit: " + e.getMessage());
        }
    }
    
    
    public ResponseDto deleteBook(Long id) {
        dbg("Inside deleteBook with id: " + id);
        try {
            bookRepository.deleteById(id);
            return new ResponseDto(Boolean.TRUE,"Libri u fshi me sukses");
        } catch (DataIntegrityViolationException e) {
            dbg("Gabim gjatë fshirjes së librit (shkelje e integritetit të të dhënave): " + e.getMessage());
            return new ResponseDto(Boolean.FALSE, "Nuk mund të fshihet librit. Ai është i lidhur me regjistrime të tjera (p.sh. autor).");
        } catch (EmptyResultDataAccessException e) {
            dbg("Gabim gjatë fshirjes së autorit (nuk janë gjetur të dhëna): " + e.getMessage());
            return new ResponseDto(Boolean.FALSE, "Libri me id " + id + " nuk ekziston në bazën e të dhënave.");
        } catch (Exception e) {
            dbg("Gabim gjatë fshirjes së autorit: " + e.getMessage());
            return new ResponseDto(Boolean.FALSE, "Ka ndodhur një gabim gjatë përpjekjes për të fshirë librit: " + e.getMessage());
        }
    }
    
    public Book getBookById(Long id) {
        dbg("Inside getBookById with id: " + id);
        return bookRepository.findById(id).orElse(new Book());
    }
    
    public ResponseDto updateBookQuantity(Book book) {
        try {
            dbg("Brenda metodës updateBookQuantity");
            
            Book bookFromDb = bookRepository.findById(book.getId())
                    .orElseThrow(() -> new RuntimeException("Libri me id " + book.getId() + " nuk u gjet në bazën e të dhënave"));
            
            dbg("Libri nga baza e të dhënave: " + bookFromDb);
            dbg("Libri nga formulari: " + book);
            
            int quantityDifference = book.getQuantity() - bookFromDb.getQuantity();
            dbg("Diferenca e sasisë të librit: " + quantityDifference);
            
            int newAvailable = book.getAvailable() + quantityDifference;
            dbg("Numri i ri i librave të disponueshëm: " + newAvailable);
            
            if (newAvailable < 0) {
                newAvailable = 0;
            }
            
            book.setAvailable(newAvailable);
            book.setBorrowed(book.getBorrowed());
            
            dbg("Libri pas përditësimit: " + book);
            
            bookRepository.save(book);
            
            return new ResponseDto(Boolean.TRUE, "Sasia e librit u përditësua me sukses.");
            
        } catch (RuntimeException e) {
            dbg("Gabim gjatë kërkimit të librit: " + e.getMessage());
            return new ResponseDto(Boolean.FALSE, "Gabim: Libri nuk u gjet në bazën e të dhënave.");
        } catch (Exception e) {
            dbg("Gabim gjatë përditësimit të librit: " + e.getMessage());
            return new ResponseDto(Boolean.FALSE, "Ndodhi një gabim gjatë përditësimit të sasisë së librit.");
        }
    }
    
    
    public void updateBookAvailabilityOnLoan(Loan loan, String action) {
        dbg("Inside updateBookAvailabilityOnLoan with loan: " + loan + ", action: " + action);
        if (loan == null || loan.getBook() == null) {
            dbg("Loan or book is null. No action needed.");
            return;
        }
        
        Book bookFromDb = bookRepository.findById(loan.getBook().getId())
                .orElseThrow(() -> new RuntimeException("Book not found"));
        dbg("Book from DB: " + bookFromDb);
        String oldStatus;
        
        if (loan.getId() != null) {
            dbg("Loan ID is not null. Checking old status.");
            Loan loanFromDb = loanRepository.findById(loan.getId())
                    .orElseThrow(() -> new RuntimeException("Loan not found"));
            oldStatus = loanFromDb.getStatus();
        } else {
            oldStatus = "Returned";
        }
        
        String newStatus = loan.getStatus();
        dbg("Old status: " + oldStatus + ", New status: " + newStatus);
        
        if (oldStatus.equals(newStatus) && !"DELETE".equals(action)) {
            dbg("Status not changed. No action needed.");
            return;
        }
        
        if ("DELETE".equals(action)) {
            dbg("Deleting loan. Restoring book availability.");
            if ("Borrowed".equals(oldStatus)) {
                dbg("Book was borrowed. Restoring availability.");
                adjustBookCounts(bookFromDb, -1, 1);
            }
        }
        else if ("Borrowed".equals(newStatus) && "Returned".equals(oldStatus) && bookFromDb.getAvailable() > 0) {
            dbg("Book is available. Borrowing book.");
            adjustBookCounts(bookFromDb, 1, -1);
        }
        else if ("Returned".equals(newStatus) && "Borrowed".equals(oldStatus)) {
            dbg("Returning book.");
            adjustBookCounts(bookFromDb, -1, 1);
        }
        
        bookRepository.save(bookFromDb);
    }
    
    private void adjustBookCounts(Book book, int borrowedChange, int availableChange) {
        dbg("Adjusting book counts. Borrowed change: " + borrowedChange + ", Available change: " + availableChange);
        book.setBorrowed(book.getBorrowed() + borrowedChange);
        book.setAvailable(book.getAvailable() + availableChange);
        dbg("Updated book counts. Borrowed: " + book.getBorrowed() + ", Available: " + book.getAvailable());
    }
    
    public List<Book> searchBooksByTitle(String title) {
        dbg("Inside searchBooksByTitle with title: " + title);
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }
    
    public List<Book> searchBooksByAuthor(String author) {
        dbg("Inside searchBooksByAuthor with author: " + author);
        return bookRepository.findByAuthor_NameContainingIgnoreCase(author);
    }

}
