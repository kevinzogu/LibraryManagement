package al.sda.LibraryManagement.controller;

import al.sda.LibraryManagement.entity.Loan;
import al.sda.LibraryManagement.service.BookService;
import al.sda.LibraryManagement.service.LoanService;
import al.sda.LibraryManagement.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/loans")
public class LoanController {
    
    private final LoanService loanService;
    private final StudentService studentService;
    private final BookService bookService;
    
    public LoanController(LoanService loanService, StudentService studentService, BookService bookService) {
        this.loanService = loanService;
        this.studentService = studentService;
        this.bookService = bookService;
    }
    
    @GetMapping("/list")
    public String getAllLoans(
            @RequestParam(value = "searchBy", required = false, defaultValue = "studentName") String searchBy,
            @RequestParam(value = "searchText", required = false) String searchText,
            Model model) {
        
        List<Loan> loans;
        
        if (searchText != null && !searchText.isEmpty()) {
            switch (searchBy) {
                case "bookTitle":
                    loans = loanService.searchLoansByBookTitle(searchText);
                    break;
                case "bookAuthor":
                    loans = loanService.searchLoansByBookAuthor(searchText);
                    break;
                case "studentName":
                default:
                    loans = loanService.searchLoansByStudentName(searchText);
            }
            model.addAttribute("searchText", searchText);
            model.addAttribute("searchBy", searchBy);
        } else {
            loans = loanService.getAllLoans();
            model.addAttribute("searchText", "");
            model.addAttribute("searchBy", "studentName");
        }
        
        model.addAttribute("loans", loans);
        model.addAttribute("isEmpty", loans.isEmpty());
        return "loan/loans";
    }
    
    @GetMapping("/addLoan")
    public String addLoan(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("books", bookService.getAllBooks());
        model.addAttribute("loan", new Loan());
        return "loan/addLoan";
    }
    
    @PostMapping("/addLoan")
    public String saveLoan(@ModelAttribute Loan loan) {
        Loan loanFromDb = new Loan();
        bookService.updateBookAvailability(loan,loanFromDb);
        loanService.saveLoan(loan);
        return "redirect:/loans/list";
    }
    
    @GetMapping("/update/{id}")
    public String updateLoan(@PathVariable Long id, Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("books", bookService.getAllBooks());
        model.addAttribute("loan", loanService.getLoanById(id));
        return "loan/updateLoan";
    }
    
    @PostMapping("/update/{id}")
    public String updateLoan(@PathVariable Long id, @ModelAttribute Loan loan) {
        Loan prevLoan = loanService.getLoanById(id);
        loan.setId(id);
        bookService.updateBookAvailability(loan,prevLoan);
        loanService.saveLoan(loan);
        return "redirect:/loans/list";
    }
    
    @RequestMapping("/delete/{id}")
    public String deleteLoan(@PathVariable Long id) {
        loanService.deleteLoan(id);
        return "redirect:/loans/list";
    }
    
}
