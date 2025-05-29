package com.quantumsoft.realestate.controller;

import com.quantumsoft.realestate.dto.AdminLoginDto;
import com.quantumsoft.realestate.dto.ResetPasswordDto;
import com.quantumsoft.realestate.entity.Admin;
import com.quantumsoft.realestate.enums.PropertyStatus;
import com.quantumsoft.realestate.servicei.AdminServiceI;
import com.quantumsoft.realestate.securityconfig.CustomUserDetailsService;
import com.quantumsoft.realestate.securityconfig.JwtService;
import com.quantumsoft.realestate.servicei.PropertyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/admin")
@CrossOrigin("*")
public class AdminController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AdminServiceI adminService;

    @Autowired
    private PropertyService propertyService;

    @PreAuthorize("hasRole('SUPERADMIN')")
    @PostMapping(value = "/register", consumes = "application/json", produces = "plain/text")
    public ResponseEntity<String> register(@Valid @RequestBody Admin admin) {
        return new ResponseEntity<String>(adminService.register(admin), HttpStatus.CREATED);
    }

    @GetMapping(value = "/forgot-password", consumes = "multipart/form-data", produces = "plain/text")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        return new ResponseEntity<String>(adminService.sendOtp(email), HttpStatus.OK);
    }

    @PatchMapping(value = "/reset-password", consumes = "application/json", produces = "plain/text")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {
        return new ResponseEntity<String>(adminService.resetPassword(resetPasswordDto), HttpStatus.OK);
    }

    @PostMapping(value = "/login", consumes = "application/json", produces = "plain/text")
    public ResponseEntity<String> login(@RequestBody AdminLoginDto adminLoginDto) {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(adminLoginDto.getEmail(), adminLoginDto.getPassword());
            Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            if (authenticate.isAuthenticated()) {
                Admin admin = userDetailsService.getAdmin(adminLoginDto.getEmail());
                String jwtToken = jwtService.generateToken(admin);
                admin.setActive(true);
                adminService.savedAdminStatus(admin);
                return new ResponseEntity<String>(jwtToken, HttpStatus.OK);
            } else
                return new ResponseEntity<String>("Invalid Credentials", HttpStatus.UNAUTHORIZED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(value = "/propertyApproval/{id}")
    public ResponseEntity<String> propertyApproval(@PathVariable Long id, @RequestParam String propertyStatus){
        return new ResponseEntity<String>(adminService.propertyApproval(id, propertyStatus), HttpStatus.OK);
    }

}

