package com.quantumsoft.realestate.servicei;

import com.quantumsoft.realestate.entity.Users;
import org.springframework.http.ResponseEntity;

public interface UserService {
    String registerUser(Users user);
    ResponseEntity<?> verifyEmail(String token);
    String loginUserWithEmailAndPassword(String email, String password);
    ResponseEntity<?> forgotPassword(String email);
    ResponseEntity<?> resetPassword(String token, String newPassword);

    void savedUserStatus(Users user);
}
