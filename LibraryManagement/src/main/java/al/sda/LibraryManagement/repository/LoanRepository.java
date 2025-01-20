package al.sda.LibraryManagement.repository;

import al.sda.LibraryManagement.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    
    // Kërkim për studentët që përmbajnë një fjalë të caktuar në emër, pa marrë parasysh rastin
    List<Loan> findByStudentNameContainingIgnoreCase(String name);
    
    // Kërkim për librat që përmbajnë një fjalë të caktuar në titull, pa marrë parasysh rastin
    List<Loan> findByBookTitleContainingIgnoreCase(String title);
    
    // Kërkim për autorët që përmbajnë një fjalë të caktuar, pa marrë parasysh rastin
    @Query("SELECT l FROM Loan l JOIN l.book b JOIN b.author a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :author, '%'))")
    List<Loan> findByBookAuthor_NameContainingIgnoreCase(String author);
}
