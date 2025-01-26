package al.sda.LibraryManagement.service;

import al.sda.LibraryManagement.dto.ResponseDto;
import al.sda.LibraryManagement.entity.Author;
import al.sda.LibraryManagement.repository.AuthorRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    
    private static final Logger logger = LoggerFactory.getLogger(AuthorService.class);
    
    private void dbg(String s) {
        logger.info("AuthorService =>  {}", s);
    }
    
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }
    
    public List<Author> getAllStudents() {
        dbg("Inside getAllStudents");
        return authorRepository.findAll();
    }
    
    public List<Author> searchAuthorsByEmail(String searchText) {
        dbg("Inside searchAuthorsByEmail with searchText: " + searchText);
        return authorRepository.findByEmailContainingIgnoreCase(searchText);
    }
    
    public List<Author> searchAuthorsByName(String searchText) {
        dbg("Inside searchAuthorsByName with searchText: " + searchText);
        return authorRepository.findByNameContainingIgnoreCase(searchText);
    }
    
    public ResponseDto addAuthor(Author author) {
        dbg("Inside addAuthor with author: " + author);
        try {
            Optional<Author> existingAuthor = authorRepository.findByEmail(author.getEmail());
            if (existingAuthor.isPresent()) {
                return new ResponseDto(Boolean.FALSE, "Një autor me këtë email tashmë ekziston.");
            }
            authorRepository.save(author);
            return new ResponseDto(Boolean.TRUE, "Autori u shtua me sukses.");
        } catch (Exception e) {
            dbg("Gabim gjatë shtimit të autorit: " + e.getMessage());
            return new ResponseDto(Boolean.FALSE, "Ka ndodhur një gabim gjatë shtimit të autorit: " + e.getMessage());
        }
    }
    
    
    public List<Author> getAllAuthors() {
        dbg("Inside getAllAuthors");
        return authorRepository.findAll();
    }
    
    public ResponseDto deleteAuthor(Long id) {
        dbg("Inside deleteAuthor with id: " + id);
        try {
            authorRepository.deleteById(id);
            return new ResponseDto(Boolean.TRUE, "Autori u fshi me sukses");
        } catch (DataIntegrityViolationException e) {
            dbg("Gabim gjatë fshirjes së autorit (shkelje e integritetit të të dhënave): " + e.getMessage());
            return new ResponseDto(Boolean.FALSE, "Nuk mund të fshihet autori. Ai është i lidhur me regjistrime të tjera (p.sh. libra).");
        } catch (EmptyResultDataAccessException e) {
            dbg("Gabim gjatë fshirjes së autorit (nuk janë gjetur të dhëna): " + e.getMessage());
            return new ResponseDto(Boolean.FALSE, "Autori me id " + id + " nuk ekziston në bazën e të dhënave.");
        } catch (Exception e) {
            dbg("Gabim gjatë fshirjes së autorit: " + e.getMessage());
            return new ResponseDto(Boolean.FALSE, "Ka ndodhur një gabim gjatë përpjekjes për të fshirë autorin: " + e.getMessage());
        }
    }
    
    public Author getAuthorById(Long id) {
        dbg("Inside getAuthorById with id: " + id);
        return authorRepository.findById(id).orElse(new Author());
    }
}
