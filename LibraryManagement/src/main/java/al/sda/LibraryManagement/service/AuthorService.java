package al.sda.LibraryManagement.service;

import al.sda.LibraryManagement.entity.Author;
import al.sda.LibraryManagement.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }
    
    public List<Author> getAllStudents() {
        return authorRepository.findAll();
    }
    
    public List<Author> searchAuthorsByEmail(String searchText) {
        return authorRepository.findByEmailContainingIgnoreCase(searchText);
    }
    
    public List<Author> searchAuthorsByName(String searchText) {
        return authorRepository.findByNameContainingIgnoreCase(searchText);
    }
    
    public void addAuthor(Author author) {
        authorRepository.save(author);
    }
    
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }
    
    public void deleteAuthor(Long id) {
        authorRepository.deleteById(id);
    }
    
    public Author getAuthorById(Long id) {
        return authorRepository.findById(id).orElse(new Author());
    }
}
