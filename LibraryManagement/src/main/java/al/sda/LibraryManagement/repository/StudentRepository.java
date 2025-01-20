package al.sda.LibraryManagement.repository;

import al.sda.LibraryManagement.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {
    List<Student> findByEmailContainingIgnoreCase(String email);
    List<Student> findByNameContainingIgnoreCase(String name);
}
