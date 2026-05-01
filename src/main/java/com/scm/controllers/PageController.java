package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.scm.entities.User;
import com.scm.forms.UserForm;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class PageController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

    @RequestMapping("/home")
    public String home(Model model) {
        System.out.println("Home page handler");
        model.addAttribute("name", "Substring Technologies");
        model.addAttribute("youtubeChannel", "Learn Code With Durgesh");
        model.addAttribute("githubRepo", "https://github.com/learncodewithdurgesh/");
        return "home";
    }

    @RequestMapping("/about")
    public String aboutPage(Model model) {
        model.addAttribute("isLogin", true);
        System.out.println("About page loading");
        return "about";
    }

    @RequestMapping("/services")
    public String servicesPage() {
        System.out.println("services page loading");
        return "services";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Registration page - now with roles list
    @GetMapping("/register")
    public String register(Model model) {
        UserForm userForm = new UserForm();
        model.addAttribute("userForm", userForm);
        // Add roles for dropdown in frontend
        model.addAttribute("roles", List.of("MEMBER", "ADMIN"));
        return "register";
    }

    // Process registration
    @RequestMapping(value = "/do-register", method = RequestMethod.POST)
    public String processRegister(@Valid @ModelAttribute UserForm userForm,
                                  BindingResult rBindingResult,
                                  HttpSession session,
                                  Model model) {
        System.out.println("Processing registration");
        System.out.println(userForm);

        // Validation errors
        if (rBindingResult.hasErrors()) {
            // Re-add roles to model for redisplay
            model.addAttribute("roles", List.of("MEMBER", "ADMIN"));
            return "register";
        }

        // Check if user already exists
        if (userService.isUserExistByEmail(userForm.getEmail())) {
            rBindingResult.rejectValue("email", "error.userForm", "User with this email already exists");
            model.addAttribute("roles", List.of("MEMBER", "ADMIN"));
            return "register";
        }

        // Create new user
        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getAbout());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setEnabled(false);  // will be enabled after email verification
        user.setProfilePic("https://www.learncodewithdurgesh.com/_next/image?url=%2F_next%2Fstatic%2Fmedia%2Fdurgesh_sir.35c6cb78.webp&w=1920&q=75");

        // ---- ROLE ASSIGNMENT ----
        String selectedRole = userForm.getRole();
        if (selectedRole != null && selectedRole.equalsIgnoreCase("ADMIN")) {
            user.setRoleList(List.of("ROLE_ADMIN"));
        } else {
            user.setRoleList(List.of("ROLE_MEMBER"));
        }
        // -------------------------

        User savedUser = userService.saveUser(user);
        System.out.println("User saved with roles: " + savedUser.getRoleList());

        Message message = Message.builder()
                .content("Registration Successful! Please check your email to verify your account before logging in.")
                .type(MessageType.green)
                .build();
        session.setAttribute("message", message);

        return "redirect:/register";
    }
}