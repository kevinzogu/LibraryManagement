package al.sda.LibraryManagement.controller;

import al.sda.LibraryManagement.dto.ResponseDto;
import al.sda.LibraryManagement.entity.Loan;
import al.sda.LibraryManagement.service.BookService;
import al.sda.LibraryManagement.service.LoanService;
import al.sda.LibraryManagement.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    
    private static final Logger logger = LoggerFactory.getLogger(LoanController.class);
    
    private void dbg(String s) {
        logger.info("LoanController =>  {}", s);
    }
    
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
        dbg("Inside getAllLoans with searchBy: " + searchBy + ", searchText: " + searchText);
        List<Loan> loans;
        
        if (searchText != null && !searchText.isEmpty()) {
            dbg("Searching loans by " + searchBy + " for: " + searchText);
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
            dbg("Getting all loans");
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
        dbg("Inside addLoan");
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("books", bookService.getAllBooks());
        model.addAttribute("loan", new Loan());
        return "loan/addLoan";
    }
    
    @PostMapping("/addLoan")
    public String saveLoan(@ModelAttribute Loan loan,Model model) {
        dbg("Inside saveLoan");
        Loan loanFromDb = new Loan();
        bookService.updateBookAvailabilityOnLoan(loan,"ADD");
        ResponseDto responseDto = loanService.saveLoan(loan);
        if (responseDto.isSuccess()) {
            return "redirect:/loans/list";
        }else {
            dbg("Loan creation failed with message: " + responseDto.getMessage());
            model.addAttribute("errorMessage", responseDto.getMessage());
            model.addAttribute("loan", loan);
            return "loan/addLoan";
        }
    }
    
    @GetMapping("/update/{id}")
    public String updateLoan(@PathVariable Long id, Model model) {
        dbg("Inside updateLoan");
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("books", bookService.getAllBooks());
        model.addAttribute("loan", loanService.getLoanById(id));
        return "loan/updateLoan";
    }
    
    @PostMapping("/update/{id}")
    public String updateLoan(@PathVariable Long id, @ModelAttribute Loan loan,Model model) {
        dbg("Inside updateLoan with id: " + id);
        Loan prevLoan = loanService.getLoanById(id);
        loan.setId(id);
        bookService.updateBookAvailabilityOnLoan(loan,"UPDATE");
        ResponseDto responseDto = loanService.saveLoan(loan);
        if (responseDto.isSuccess()) {
            return "redirect:/loans/list";
        }else {
            dbg("Loan creation failed with message: " + responseDto.getMessage());
            model.addAttribute("errorMessage", responseDto.getMessage());
            model.addAttribute("loan", loan);
            return "loan/updateLoan";
        }
    }
    
    @RequestMapping("/delete/{id}")
    public String deleteLoan(@PathVariable Long id,Model model) {
        dbg("Inside deleteLoan with id: " + id);
        Loan loan = loanService.getLoanById(id);
        bookService.updateBookAvailabilityOnLoan(loan,"DELETE");
        ResponseDto responseDto = loanService.deleteLoan(id);
        if (responseDto.isSuccess()) {
            dbg("Loan deleted successfully");
            return "redirect:/loans/list";
        }else {
            dbg("Loan deletion failed with message: " + responseDto.getMessage());
            model.addAttribute("errorMessage", responseDto.getMessage());
            return "/loan/error";
        }
    }
    
}
