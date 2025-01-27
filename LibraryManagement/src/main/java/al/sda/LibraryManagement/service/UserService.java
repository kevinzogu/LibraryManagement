package al.sda.LibraryManagement.service;

import al.sda.LibraryManagement.controller.StudentController;
import al.sda.LibraryManagement.dto.ResponseDto;
import al.sda.LibraryManagement.entity.User;
import al.sda.LibraryManagement.repository.StudentRepository;
import al.sda.LibraryManagement.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    private void dbg(String s) {
        logger.info("UserService =>  {}", s);
    }
    
    public UserService(UserRepository userRepository, StudentRepository studentRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(new User());
    }
    
    public User findUserByUsername(String username) {
        Optional<User> existingUserOpt = userRepository.findByUsername(username);
        dbg("Existing user: " + existingUserOpt);
        if (existingUserOpt.isEmpty()) {
            dbg("User not found");
            return new User();
        }
        return existingUserOpt.get();
    }
    
    public ResponseDto updateUser(String username, User updatedUser) {
        dbg("Inside updateUser method with username: " + username);
        Optional<User> existingUserOpt = userRepository.findByUsername(username);
        dbg("Existing user: " + existingUserOpt);
        if (existingUserOpt.isEmpty()) {
            dbg("User not found");
            return new ResponseDto(false, "Përdoruesi nuk u gjet.");
        }
        
        User existingUser = existingUserOpt.get();
        
        dbg("updatedUser: " + updatedUser);
        
        if (!existingUser.getUsername().equals(updatedUser.getUsername()) &&
                userRepository.existsByUsername(updatedUser.getUsername())) {
            return new ResponseDto(false, "Ky username tashmë është i zënë.");
        }
        
        if (!existingUser.getEmail().equals(updatedUser.getEmail()) &&
                userRepository.existsByEmail(updatedUser.getEmail())) {
            return new ResponseDto(false, "Ky email tashmë është i përdorur.");
        }
        
        if (!existingUser.getPhone().equals(updatedUser.getPhone()) &&
                userRepository.existsByPhone(updatedUser.getPhone())) {
            return new ResponseDto(false, "Ky numër telefoni tashmë është i përdorur.");
        }
        
        if (!passwordEncoder.matches(updatedUser.getPassword(), existingUser.getPassword())) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        
        existingUser.setName(updatedUser.getName());
        existingUser.setSurname(updatedUser.getSurname());
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPhone(updatedUser.getPhone());
        existingUser.setAddress(updatedUser.getAddress());
        existingUser.setBirthDate(updatedUser.getBirthDate());
        
        userRepository.save(existingUser);
        
        return new ResponseDto(true, "Përdoruesi u përditësua me sukses.");
    }
    
}
