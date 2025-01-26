package al.sda.LibraryManagement.service;

import al.sda.LibraryManagement.dto.ResponseDto;
import al.sda.LibraryManagement.entity.Loan;
import al.sda.LibraryManagement.repository.LoanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    
    private static final Logger logger = LoggerFactory.getLogger(LoanService.class);
    
    private void dbg(String s) {
        logger.info("LoanService =>  {}", s);
    }
    
    public LoanService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }
    
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }
    
    public ResponseDto saveLoan(Loan loan) {
        dbg("Inside saveLoan: " + loan);
        try {
            loanRepository.save(loan);
            return new ResponseDto(Boolean.TRUE,"Huazimi u ruajt me sukses");
        } catch (Exception e) {
            dbg("Gabim gjatë shtimit të Huazimit : " + e.getMessage());
            return new ResponseDto(Boolean.FALSE, "Ka ndodhur një gabim gjatë shtimit të Huazimit: " + e.getMessage());
        }
    }
    
    public Loan getLoanById(Long id) {
        dbg("Inside getLoanById: " + id);
        return loanRepository.findById(id).orElse(new Loan());
    }
    
    public ResponseDto deleteLoan(Long id) {
        dbg("Inside deleteLoan: " + id);
        try {
            loanRepository.deleteById(id);
            return new ResponseDto(Boolean.TRUE,"Huazimi u fshi me sukses");
        } catch (DataIntegrityViolationException e) {
            dbg("Gabim gjatë fshirjes së huazimit (shkelje e integritetit të të dhënave): " + e.getMessage());
            return new ResponseDto(Boolean.FALSE, "Nuk mund të fshihet huazimit. Ai është i lidhur me regjistrime të tjera (p.sh. autor,liber).");
        } catch (EmptyResultDataAccessException e) {
            dbg("Gabim gjatë fshirjes së autorit (nuk janë gjetur të dhëna): " + e.getMessage());
            return new ResponseDto(Boolean.FALSE, "Huazimi me id " + id + " nuk ekziston në bazën e të dhënave.");
        } catch (Exception e) {
            dbg("Gabim gjatë fshirjes së autorit: " + e.getMessage());
            return new ResponseDto(Boolean.FALSE, "Ka ndodhur një gabim gjatë përpjekjes për të fshirë huazimin : " + e.getMessage());
        }
    }
    
    public List<Loan> searchLoansByStudentName(String name) {
        dbg("Inside searchLoansByStudentName: " + name);
        return loanRepository.findByStudentNameContainingIgnoreCase(name);
    }
    
    public List<Loan> searchLoansByBookTitle(String title) {
        dbg("Inside searchLoansByBookTitle: " + title);
        return loanRepository.findByBookTitleContainingIgnoreCase(title);
    }
    
    public List<Loan> searchLoansByBookAuthor(String author) {
        dbg("Inside searchLoansByBookAuthor: " + author);
        return loanRepository.findByBookAuthor_NameContainingIgnoreCase(author);
    }
}
