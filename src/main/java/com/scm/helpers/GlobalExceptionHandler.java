package com.scm.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFoundException(ResourceNotFoundException ex, Model model) {
        logger.error("Resource not found: {}", ex.getMessage());
        model.addAttribute("message", Message.builder()
                .content(ex.getMessage())
                .type(MessageType.red).build());
        return "error_page";
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public String handleUserAlreadyExistsException(UserAlreadyExistsException ex, Model model) {
        logger.error("User already exists: {}", ex.getMessage());
        model.addAttribute("message", Message.builder()
                .content(ex.getMessage())
                .type(MessageType.red).build());
        return "error_page";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, Model model) {
        logger.error("Internal Server Error: ", ex);
        model.addAttribute("message", Message.builder()
                .content("Something went wrong! Please try again later.")
                .type(MessageType.red).build());
        return "error_page";
    }
}
