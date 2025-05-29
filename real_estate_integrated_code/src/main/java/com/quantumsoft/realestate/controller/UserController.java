package com.quantumsoft.realestate.controller;

import com.quantumsoft.realestate.dto.UserLoginDto;
import com.quantumsoft.realestate.entity.Users;
import com.quantumsoft.realestate.securityconfig.CustomUserDetailsService;
import com.quantumsoft.realestate.securityconfig.JwtService;
import com.quantumsoft.realestate.servicei.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
// @RequiredArgsConstructor // Optional if you're using constructor injection
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Users user) {
        String message = userService.registerUser(user); // ✅ fixed method name
        return ResponseEntity.ok(message);
    }


    @GetMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam("token") String token) {
        return userService.verifyEmail(token);
    }

//    // ✅ Fixed login to extract email and password from User object
//    @PostMapping("/login")
//    public ResponseEntity<?> loginUser(@RequestBody User user) {
//        String message = userService.loginUserWithEmailAndPassword(user.getEmail(), user.getPassword());
//        return ResponseEntity.ok(message);
//    }

    @PostMapping(value = "/login", consumes = "application/json", produces = "plain/text")
    public ResponseEntity<String> login(@RequestBody UserLoginDto userLoginDto) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userLoginDto.getEmail(), userLoginDto.getPassword());
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        if (authenticate.isAuthenticated()) {
            Users user = userDetailsService.getUser(userLoginDto.getEmail());
            String jwtToken = jwtService.generateToken(user);
            user.setActive(true);
            userService.savedUserStatus(user);
            return new ResponseEntity<String>(jwtToken, HttpStatus.OK);
        } else
            return new ResponseEntity<String>("Invalid Credentials", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        return userService.forgotPassword(email);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token,
                                           @RequestParam String newPassword) {
        return userService.resetPassword(token, newPassword);
    }
}
