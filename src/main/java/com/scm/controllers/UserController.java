package com.scm.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.entities.User;
import com.scm.forms.ProfileForm;
import com.scm.forms.UserForm;
import com.scm.helpers.Helper;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.services.ContactService;
import com.scm.services.ImageService;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private ImageService imageService;

    // user dashboard page
    @RequestMapping(value = "/dashboard")
    public String userDashboard(Model model, Authentication authentication) {
        String email = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(email);

        long totalContacts = contactService.countByUser(user);
        long favoriteCount = contactService.countByUserAndFavoriteTrue(user);

        model.addAttribute("totalContacts", totalContacts);
        model.addAttribute("favoriteCount", favoriteCount);
        // Placeholder for other stats if needed
        model.addAttribute("recentActivityCount", 0);
        model.addAttribute("totalGroups", 0);

        return "user/dashboard";
    }

    // user profile page
    @RequestMapping(value = "/profile")
    public String userProfile() {
        return "user/profile";
    }

    // edit profile view
    @GetMapping("/profile/edit")
    public String editProfile(Model model, Authentication authentication) {
        String email = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(email);

        ProfileForm profileForm = new ProfileForm();
        profileForm.setName(user.getName());
        profileForm.setPhoneNumber(user.getPhoneNumber());
        profileForm.setAbout(user.getAbout());
        
        model.addAttribute("profileForm", profileForm);
        model.addAttribute("userEmail", user.getEmail());
        return "user/edit_profile";
    }

    // update profile handler
    @PostMapping("/profile/update")
    public String updateProfile(
            @Valid @ModelAttribute("profileForm") ProfileForm profileForm,
            BindingResult bindingResult,
            Authentication authentication,
            HttpSession session,
            Model model) {

        if (bindingResult.hasErrors()) {
            // Need to re-add email for the readonly field in case of errors
            String email = Helper.getEmailOfLoggedInUser(authentication);
            model.addAttribute("userEmail", email);
            return "user/edit_profile";
        }

        String email = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(email);

        user.setName(profileForm.getName());
        user.setPhoneNumber(profileForm.getPhoneNumber());
        user.setAbout(profileForm.getAbout());

        // Handle profile image upload
        if (profileForm.getProfileImage() != null && !profileForm.getProfileImage().isEmpty()) {
            // Delete old image if exists
            if (user.getCloudinaryImagePublicId() != null) {
                imageService.deleteImage(user.getCloudinaryImagePublicId());
            }

            String filename = java.util.UUID.randomUUID().toString();
            String imageUrl = imageService.uploadImage(profileForm.getProfileImage(), filename);
            user.setProfilePic(imageUrl);
            user.setCloudinaryImagePublicId(filename);
        }

        userService.updateUser(user);

        session.setAttribute("message", Message.builder()
                .content("Profile updated successfully")
                .type(MessageType.green).build());

        return "redirect:/user/profile";
    }

    // user messages page
    @RequestMapping("/messages")
    public String userMessages() {
        return "user/messages";
    }

}