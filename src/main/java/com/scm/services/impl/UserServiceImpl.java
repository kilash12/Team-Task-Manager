package com.scm.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.scm.entities.User;
import com.scm.helpers.AppConstants;
import com.scm.helpers.Helper;
import com.scm.helpers.ResourceNotFoundException;
import com.scm.repositories.UserRepo;
import com.scm.services.EmailService;
import com.scm.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Helper helper;

    @Override
    @jakarta.transaction.Transactional
    public User saveUser(User user) {
        // Generate user id if not present
        if (user.getUserId() == null) {
            user.setUserId(UUID.randomUUID().toString());
        }

        // Encode password only if it's not already encoded (simple check)
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        // Set default role if no role list is set (e.g., from OAuth2 registration)
        if (user.getRoleList() == null || user.getRoleList().isEmpty()) {
            user.setRoleList(List.of("ROLE_MEMBER"));
        }

        logger.info("Saving user: {}", user.getEmail());

        // Generate email verification token only for local users (not OAuth2)
        if (user.getProvider() == null || user.getProvider().toString().equals("SELF")) {
            String emailToken = UUID.randomUUID().toString();
            user.setEmailToken(emailToken);
            User savedUser = userRepo.save(user);
            String emailLink = helper.getLinkForEmailVerificatiton(emailToken);
            try {
                logger.info("Sending verification email to: {}", savedUser.getEmail());
                emailService.sendEmail(savedUser.getEmail(), "Verify Account : Smart Contact Manager", emailLink);
                logger.info("Verification email sent successfully.");
            } catch (Exception e) {
                logger.error("Failed to send verification email: {}", e.getMessage());
            }
            return savedUser;
        } else {
            // For OAuth2 users, they are already verified
            user.setEnabled(true);
            user.setEmailVerified(true);
            return userRepo.save(user);
        }
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepo.findById(id);
    }

    @Override
    public Optional<User> updateUser(User user) {
        User user2 = userRepo.findById(user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        // Update fields
        user2.setName(user.getName());
        user2.setEmail(user.getEmail());
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user2.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        user2.setAbout(user.getAbout());
        user2.setPhoneNumber(user.getPhoneNumber());
        user2.setProfilePic(user.getProfilePic());
        user2.setCloudinaryImagePublicId(user.getCloudinaryImagePublicId());
        user2.setEnabled(user.isEnabled());
        user2.setEmailVerified(user.isEmailVerified());
        user2.setPhoneVerified(user.isPhoneVerified());
        user2.setProvider(user.getProvider());
        user2.setProviderUserId(user.getProviderUserId());
        // Role list update (if provided)
        if (user.getRoleList() != null && !user.getRoleList().isEmpty()) {
            user2.setRoleList(user.getRoleList());
        }
        User save = userRepo.save(user2);
        return Optional.ofNullable(save);
    }

    @Override
    public void deleteUser(String id) {
        User user2 = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userRepo.delete(user2);
    }

    @Override
    public boolean isUserExist(String userId) {
        return userRepo.findById(userId).isPresent();
    }

    @Override
    public boolean isUserExistByEmail(String email) {
        return userRepo.findByEmail(email).isPresent();
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email).orElse(null);
    }

    @Override
    @jakarta.transaction.Transactional
    public void verifyEmail(String token) {
        User user = userRepo.findByEmailToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid verification token"));
        user.setEmailVerified(true);
        user.setEnabled(true);
        user.setEmailToken(null);
        userRepo.save(user);
    }

    // ***** NEW METHOD IMPLEMENTATION *****
    @Override
    public long countAllUsers() {
        return userRepo.count();
    }
}