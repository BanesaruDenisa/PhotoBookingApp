package com.example.photobooking.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@Controller
//@RestControllerAdvice
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<String> handleError() {

        return ResponseEntity.status(404).body("The requested resource was not found.");
    }

//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<?> handleAccessDenied(AccessDeniedException ex) {
//        return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                .body("Access denied: " + ex.getMessage());
//    }


}