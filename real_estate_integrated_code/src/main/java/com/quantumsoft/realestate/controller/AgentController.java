package com.quantumsoft.realestate.controller;

import com.quantumsoft.realestate.dto.AdminLoginDto;
import com.quantumsoft.realestate.dto.AgentLoginDto;
import com.quantumsoft.realestate.entity.Admin;
import com.quantumsoft.realestate.entity.Agent;
import com.quantumsoft.realestate.enums.AgentStatus;
import com.quantumsoft.realestate.securityconfig.CustomUserDetailsService;
import com.quantumsoft.realestate.securityconfig.JwtService;
import com.quantumsoft.realestate.servicei.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/agents")
@CrossOrigin("*")
public class AgentController {
    @Autowired
    private AgentService agentService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<Agent> register(@RequestBody Agent agent) {
        return ResponseEntity.ok(agentService.register(agent));
    }
//    @PostMapping("/login")
//    public ResponseEntity<Agent> login(@RequestBody Map<String, String> payload) {
//        String email = payload.get("email");
//        String password = payload.get("password");
//        Agent agent = agentService.login(email, password);
//        return ResponseEntity.ok(agent);
//    }

    @PostMapping(value = "/login", consumes = "application/json", produces = "text/plain")
    public ResponseEntity<String> login(@RequestBody AgentLoginDto agentLoginDto) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(agentLoginDto.getEmail(), agentLoginDto.getPassword());
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        if (authenticate.isAuthenticated()) {
            Agent agent = userDetailsService.getAgent(agentLoginDto.getEmail());
            String jwtToken = jwtService.generateToken(agent);
            agent.setStatus(AgentStatus.ACTIVE);
            agentService.savedAgentStatus(agent);
            return new ResponseEntity<String>(jwtToken, HttpStatus.OK);
        } else
            return new ResponseEntity<String>("Invalid Credentials", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/{email}")
    public ResponseEntity<Agent> getByEmail(@PathVariable String email) {
        return ResponseEntity.ok(agentService.getByEmail(email));
    }
    @PutMapping("/{email}")
    public ResponseEntity<Agent> update(@PathVariable String email, @RequestBody Agent agent) {
        return ResponseEntity.ok(agentService.update(email, agent));
    }
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> delete(@PathVariable String email) {
        agentService.delete(email);
        return ResponseEntity.noContent().build();
    }

}
