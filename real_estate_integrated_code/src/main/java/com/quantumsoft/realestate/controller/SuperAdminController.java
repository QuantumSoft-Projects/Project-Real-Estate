package com.quantumsoft.realestate.controller;

import com.quantumsoft.realestate.dto.SuperAdminLoginDto;
import com.quantumsoft.realestate.entity.SuperAdmin;
import com.quantumsoft.realestate.securityconfig.CustomUserDetailsService;
import com.quantumsoft.realestate.securityconfig.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/superadmin")
@CrossOrigin("*")
public class SuperAdminController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtService jwtService;

    @PostMapping(value = "/login")
    public ResponseEntity<String> login(@RequestBody SuperAdminLoginDto superAdminLoginDto) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(superAdminLoginDto.getUsername(), superAdminLoginDto.getPassword());
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        if (authenticate.isAuthenticated()) {
            SuperAdmin superAdmin = userDetailsService.getSuperAdmin(superAdminLoginDto.getUsername());
            String jwtToken = jwtService.generateToken(superAdminLoginDto.getUsername(), superAdmin);
            return new ResponseEntity<String>(jwtToken, HttpStatus.OK);
        }else
            return new ResponseEntity<String>("Invalid Credentials", HttpStatus.UNAUTHORIZED);
    }
}
