package al.sda.LibraryManagement.controller;

import al.sda.LibraryManagement.dto.ResponseDto;
import al.sda.LibraryManagement.entity.Student;
import al.sda.LibraryManagement.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);
    
    private void dbg(String s) {
        logger.info("StudentController =>  {}", s);
    }
    
    private final StudentService studentService;
    
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }
    
    @GetMapping("/list")
    public String getAllStudents(
            @RequestParam(value = "searchBy", required = false, defaultValue = "name") String searchBy,
            @RequestParam(value = "searchText", required = false) String searchText,
            Model model) {
        dbg("Inside getAllStudents with searchBy: " + searchBy + " and searchText: " + searchText);
        List<Student> students;
        
        if (searchText != null && !searchText.isEmpty()) {
            dbg("Searching students by " + searchBy + " containing " + searchText);
            if ("email".equalsIgnoreCase(searchBy)) {
                dbg("Searching students by email");
                students = studentService.searchStudentsByEmail(searchText);
            } else {
                dbg("Searching students by name");
                students = studentService.searchStudentsByName(searchText);
            }
            model.addAttribute("searchText", searchText);
            model.addAttribute("searchBy", searchBy);
        } else {
            dbg("Searching all students");
            students = studentService.getAllStudents();
            model.addAttribute("searchText", "");
            model.addAttribute("searchBy", "name");
        }
        
        model.addAttribute("students", students);
        model.addAttribute("isEmpty", students.isEmpty());
        return "student/students";
    }
    
    @GetMapping("/addStudent")
    public String addStudent(Model model) {
        dbg("Inside addStudent");
        model.addAttribute("student", new Student());
        return "student/addStudent";
    }
    
    @PostMapping("/addStudent")
    public String addStudent(@ModelAttribute Student student,Model model) {
        dbg("Inside addStudent with student: " + student);
        ResponseDto responseDto = studentService.addStudent(student);
        if (responseDto.isSuccess()) {
            return "redirect:/students/list";
        }else {
            model.addAttribute("errorMessage", responseDto.getMessage());
            model.addAttribute("student", student);
            return "student/addStudent";
        }
    }
    
    @RequestMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id,Model model) {
        dbg("Deleting student with id: " + id);
        ResponseDto responseDto = studentService.deleteStudent(id);
        if (responseDto.isSuccess()) {
            return "redirect:/students/list";
        }else {
            model.addAttribute("errorMessage", responseDto.getMessage());
            return "student/error";
        }
    }

    
    @GetMapping("/update/{id}")
    public String updateStudent(@PathVariable Long id, Model model) {
        dbg("Inside updateStudent with id: " + id);
        model.addAttribute("student", studentService.getStudentById(id));
        return "student/updateStudent";
    }
    
    @PostMapping("/update/{id}")
    public String updateStudent(@PathVariable Long id, @ModelAttribute Student student,Model model) {
        dbg("Inside updateStudent with id: " + id);
        student.setId(id);
        ResponseDto responseDto = studentService.addStudent(student);
        if (responseDto.isSuccess()) {
            return "redirect:/students/list";
        }else {
            model.addAttribute("errorMessage", responseDto.getMessage());
            model.addAttribute("student", student);
            return "student/updateStudent";
        }
    }
    
}
