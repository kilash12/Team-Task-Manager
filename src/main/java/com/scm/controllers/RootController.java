package com.scm.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.scm.entities.User;
import com.scm.helpers.Helper;
import com.scm.services.UserService;

@ControllerAdvice
public class RootController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @ModelAttribute
    public void addLoggedInUserInformation(Model model, Authentication authentication) {

        if (authentication == null) {
            return;
        }

        System.out.println("Adding logged in user information to the model");

        // Helper class ka use karke username (email) nikalna
        String username = Helper.getEmailOfLoggedInUser(authentication);

        logger.info("User logged in: {}", username);

        // database se data ko fetch : get user from db :
        User user = userService.getUserByEmail(username);

        if (user != null) {
            System.out.println("User found: " + user.getName());
            System.out.println("User email: " + user.getEmail());

            // Frontend standard naming convention
            model.addAttribute("loggedInUser", user);
        } else {
            System.out.println("User is null, not adding to model");
        }

    }
}