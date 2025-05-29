package com.quantumsoft.realestate.securityconfig;

import com.quantumsoft.realestate.entity.Admin;
import com.quantumsoft.realestate.entity.Agent;
import com.quantumsoft.realestate.entity.SuperAdmin;
import com.quantumsoft.realestate.entity.Users;
import com.quantumsoft.realestate.exception.FirstTimeLoginException;
import com.quantumsoft.realestate.repository.AdminRepository;
import com.quantumsoft.realestate.repository.AgentRepository;
import com.quantumsoft.realestate.repository.SuperAdminRepository;
import com.quantumsoft.realestate.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private SuperAdminRepository superAdminRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.equals("superadmin")) {
            Optional<SuperAdmin> optional = superAdminRepository.findByUsername(username);
            if (optional.isPresent()) {
                SuperAdmin superAdmin = optional.get();
                return new User(superAdmin.getUsername(), superAdmin.getPassword(), getAuthorities(superAdmin));
            }
        }

        // ✅ Check Agent first
        Optional<Agent> agentOptional = agentRepository.findByEmail(username);
        if (agentOptional.isPresent()) {
            Agent agent = agentOptional.get();
            return new User(agent.getEmail(), agent.getPassword(), getAuthorities(agent));
        }

        // ✅ Check Admin
        Optional<Admin> optional = adminRepository.findByEmail(username);
        if (optional.isPresent()) {
            Admin admin = optional.get();
            if (admin.isFirstLoginCheck()) {
                throw new FirstTimeLoginException("You are trying to login first time, before login you must need to reset the password");
            }
            return new User(admin.getEmail(), admin.getPassword(), getAuthorities(admin));
        }

        // ✅ Check Users (buyers/sellers)
        Optional<Users> userOptional = userRepository.findByEmail(username);
        if (userOptional.isPresent()) {
            Users user = userOptional.get();

            // 🔒 Account Activation Check
            if (!user.isActive()) {
                throw new DisabledException("User account is not activated");
            }

            return new User(user.getEmail(), user.getPassword(), getAuthorities(user));
        }

        // ❌ If none matched
        throw new UsernameNotFoundException("User record is not present");
    }

    // Role assignment helpers
    private Collection<SimpleGrantedAuthority> getAuthorities(SuperAdmin superAdmin) {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + superAdmin.getRole()));
    }

    private Collection<SimpleGrantedAuthority> getAuthorities(Admin admin) {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + admin.getRole()));
    }

    private Collection<SimpleGrantedAuthority> getAuthorities(Users user) {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
    }

    private Collection<SimpleGrantedAuthority> getAuthorities(Agent agent) {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + agent.getRole()));
    }

    // Fetch methods (optional utility methods)
    public SuperAdmin getSuperAdmin(String username) {
        return superAdminRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("SuperAdmin record not found in the database"));
    }

    public Admin getAdmin(String email) {
        return adminRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Admin record not found in the database"));
    }

    public Agent getAgent(String email) {
        return agentRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Agent record not found in the database"));
    }

    public Users getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User record not found in the database"));
    }
}
