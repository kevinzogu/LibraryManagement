package al.sda.LibraryManagement.repository;

import al.sda.LibraryManagement.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    
    // Kërkim për tituj libri që përmbajnë një fjalë të caktuar, pa marrë parasysh rastin
    List<Book> findByTitleContainingIgnoreCase(String title);
    
    // Kërkim për autorët që përmbajnë një fjalë të caktuar, pa marrë parasysh rastin
    List<Book> findByAuthor_NameContainingIgnoreCase(String author);
}
