package com.quantumsoft.realestate.serviceimpl;

import com.quantumsoft.realestate.entity.Users;
import com.quantumsoft.realestate.enums.Role;
import com.quantumsoft.realestate.exception.ResourceNotFoundException;
import com.quantumsoft.realestate.repository.UserRepository;
import com.quantumsoft.realestate.securityconfig.CustomUserDetailsService;
import com.quantumsoft.realestate.servicei.EmailService;
import com.quantumsoft.realestate.servicei.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public String registerUser(Users user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return "Email is already registered.";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setVerificationToken(UUID.randomUUID().toString());
        user.setEmailVerified(false);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        if (user.getRole() == null) {
            user.setRole(Role.BUYER);  // or Role.SELLER if you prefer
        }

        userRepository.save(user); // ✅ this should now work

        String verificationLink = "http://localhost:8080/api/users/verify?token=" + user.getVerificationToken();
        emailService.sendSimpleMessage(
                user.getEmail(),
                "Verify your email",
                "Click the link to verify your email: " + verificationLink
        );

        return "User registered. Please verify your email.";
    }

    @Override
    public ResponseEntity<?> verifyEmail(String token) {
        Optional<Users> optionalUser = userRepository.findByVerificationToken(token);
        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();
            user.setEmailVerified(true);
            user.setVerificationToken(null);
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
            return ResponseEntity.ok("Email verified successfully.");
        }
        return ResponseEntity.badRequest().body("Invalid verification token.");
    }

    @Override
    public String loginUserWithEmailAndPassword(String email, String password) {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        if (!user.isActive()) {
            throw new RuntimeException("User account is inactive.");
        }

        if (!user.isEmailVerified()) {
            throw new RuntimeException("Email is not verified.");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid email or password.");
        }

        return "User login Successfullly";

    }

    @Override
    public ResponseEntity<?> forgotPassword(String email) {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email not found."));

        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        String resetLink = "http://localhost:8080/api/users/reset-password?token=" + token;
        emailService.sendSimpleMessage(
                email,
                "Reset Your Password",
                "Click this link to reset your password: " + resetLink
        );

        return ResponseEntity.ok("Reset link sent to your email.");
    }

    @Override
    public ResponseEntity<?> resetPassword(String token, String newPassword) {
        Users user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid reset token."));

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        return ResponseEntity.ok("Password reset successfully.");
    }

    @Override
    public void savedUserStatus(Users user) {
        userRepository.save(user);
    }
}
