package al.sda.LibraryManagement.controller;

import al.sda.LibraryManagement.entity.Student;
import al.sda.LibraryManagement.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {
    
    private final StudentService studentService;
    
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }
    
    @GetMapping("/list")
    public String getAllStudents(
            @RequestParam(value = "searchBy", required = false, defaultValue = "name") String searchBy,
            @RequestParam(value = "searchText", required = false) String searchText,
            Model model) {
        
        List<Student> students;
        
        if (searchText != null && !searchText.isEmpty()) {
            if ("email".equalsIgnoreCase(searchBy)) {
                students = studentService.searchStudentsByEmail(searchText);
            } else {
                students = studentService.searchStudentsByName(searchText);
            }
            model.addAttribute("searchText", searchText);
            model.addAttribute("searchBy", searchBy);
        } else {
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
        model.addAttribute("student", new Student());
        return "student/addStudent";
    }
    
    @PostMapping("/addStudent")
    public String addStudent(@ModelAttribute Student student) {
        studentService.addStudent(student);
        return "redirect:/students/list";
    }
    
    @RequestMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return "redirect:/students/list";
    }
    
    @GetMapping("/update/{id}")
    public String updateStudent(@PathVariable Long id, Model model) {
        model.addAttribute("student", studentService.getStudentById(id));
        return "student/updateStudent";
    }
    
    @PostMapping("/update/{id}")
    public String updateStudent(@PathVariable Long id, @ModelAttribute Student student) {
        student.setId(id);
        studentService.addStudent(student);
        return "redirect:/students/list";
    }
    
}
