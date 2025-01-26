package al.sda.LibraryManagement.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {
    
    private static final Logger logger = LoggerFactory.getLogger(CustomErrorController.class);
    
    private void dbg(String s) {
        logger.info("CustomErrorController =>  {}", s);
    }
    
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        dbg("Inside handleError with request: " + request);
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        dbg("Inside handleError with status: " + status);
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            dbg("Status code: " + statusCode);
            dbg("Status message: " + HttpStatus.NOT_FOUND.value());
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                dbg("Returning 404 error page");
                return "error/404";
            }
        }
        dbg("Returning generic error page");
        return "error/genericError";
    }
}


