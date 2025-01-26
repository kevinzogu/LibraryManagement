package al.sda.LibraryManagement.service;

import al.sda.LibraryManagement.entity.User;
import al.sda.LibraryManagement.repository.UserRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class SignupService {

    private final UserRepository userRepository;
    
    private static final Logger logger = LoggerFactory.getLogger(SignupService.class);
    
    private void dbg(String s) {
        logger.info("SignupService =>  {}", s);
    }
    
    public SignupService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public SignupResponse saveUser(User user) {
        dbg("Inside saveUser with user: " + user);
        try {
            userRepository.save(user);
            return new SignupResponse(Boolean.TRUE,"Perdoruesi u ruajt me sukses");
        } catch (DataIntegrityViolationException e) {
        return new SignupResponse(Boolean.FALSE,"Ky email ose username është tashmë i regjistruar. Ju lutem përdorni një tjetër.");
        } catch (Exception e) {
            return new SignupResponse(Boolean.FALSE, "Ka ndodhur një gabim i panjohur. Ju lutemi provoni më vonë.");
        }
    }
    
    public class SignupResponse {
        private String message;
        private boolean success;
        
        public SignupResponse(boolean success,String message) {
            this.message = message;
            this.success = success;
        }
        public String getMessage() {
            return message;
        }
        
        public boolean isSuccess() {
            return success;
        }
        
        @Override
        public String toString() {
            return "signupResponse{" +
                    "message='" + message + '\'' +
                    ", success=" + success +
                    '}';
        }
    }
}
