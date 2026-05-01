package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entities.User;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthController {

    // verify email

    @Autowired
    private UserService userService;

    @GetMapping("/verify-email")
    public String verifyEmail(
            @RequestParam("token") String token, HttpSession session) {

        try {
            userService.verifyEmail(token);
            session.setAttribute("message", Message.builder()
                    .type(MessageType.green)
                    .content("Your email is verified. Now you can login.")
                    .build());
            return "success_page";
        } catch (Exception e) {
            session.setAttribute("message", Message.builder()
                    .type(MessageType.red)
                    .content("Email not verified! Token is invalid or expired.")
                    .build());
            return "error_page";
        }
    }

}
