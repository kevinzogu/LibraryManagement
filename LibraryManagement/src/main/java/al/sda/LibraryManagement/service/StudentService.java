package al.sda.LibraryManagement.service;

import al.sda.LibraryManagement.controller.StudentController;
import al.sda.LibraryManagement.dto.ResponseDto;
import al.sda.LibraryManagement.entity.Student;
import al.sda.LibraryManagement.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    
    private void dbg(String s) {
        logger.info("StudentService =>  {}", s);
    }
    
    private final StudentRepository studentRepository;
    
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }
    
    public List<Student> getAllStudents() {
        dbg("Inside getAllStudents");
        return studentRepository.findAll();
    }
    
    public ResponseDto addStudent(Student student) {
        dbg("Inside addStudent with student = " + student);
        try {
            studentRepository.save(student);
            return new ResponseDto(Boolean.TRUE,"Huazimi u ruajt me sukses");
        } catch (Exception e) {
            dbg("Gabim gjatë shtimit të Studentit : " + e.getMessage());
            return new ResponseDto(Boolean.FALSE, "Ka ndodhur një gabim gjatë shtimit të Huazimit: " + e.getMessage());
        }
    }
    
    public ResponseDto deleteStudent(Long id) {
        dbg("Inside deleteStudent with id = " + id);
        try {
            studentRepository.deleteById(id);
            return new ResponseDto(Boolean.TRUE, "Student deleted successfully");
        } catch (DataIntegrityViolationException e) {
            dbg("Gabim gjatë fshirjes së Studentit (shkelje e integritetit të të dhënave): " + e.getMessage());
            return new ResponseDto(Boolean.FALSE, "Nuk mund të fshihet studentit. Ai është i lidhur me regjistrime të tjera (p.sh. huazimi).");
        } catch (EmptyResultDataAccessException e) {
            dbg("Gabim gjatë fshirjes së studentit (nuk janë gjetur të dhëna): " + e.getMessage());
            return new ResponseDto(Boolean.FALSE, "Studenti me id " + id + " nuk ekziston në bazën e të dhënave.");
        } catch (Exception e) {
            dbg("Gabim gjatë fshirjes së autorit: " + e.getMessage());
            return new ResponseDto(Boolean.FALSE, "Ka ndodhur një gabim gjatë përpjekjes për të fshirë studentin : " + e.getMessage());
        }
    }
    
    public Student getStudentById(Long id) {
        dbg("Inside getStudentById with id = " + id);
        return studentRepository.findById(id).orElse(new Student());
    }
    
    public List<Student> searchStudentsByName(String name) {
        dbg("Inside searchStudentsByName with name = " + name);
        return studentRepository.findByNameContainingIgnoreCase(name);
    }
    
    public List<Student> searchStudentsByEmail(String email) {
        dbg("Inside searchStudentsByEmail with email = " + email);
        return studentRepository.findByEmailContainingIgnoreCase(email);
    }
    
}