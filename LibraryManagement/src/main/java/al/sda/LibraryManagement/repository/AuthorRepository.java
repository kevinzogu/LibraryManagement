package al.sda.LibraryManagement.repository;

import al.sda.LibraryManagement.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author,Long> {
    List<Author> findByEmailContainingIgnoreCase(String searchText);
    List<Author> findByNameContainingIgnoreCase(String searchText);
    Optional<Author> findByEmail(String email);
}
