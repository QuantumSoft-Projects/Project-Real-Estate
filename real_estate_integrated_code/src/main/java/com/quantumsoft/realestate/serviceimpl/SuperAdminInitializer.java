package com.quantumsoft.realestate.serviceimpl;

import com.quantumsoft.realestate.entity.SuperAdmin;
import com.quantumsoft.realestate.enums.Role;
import com.quantumsoft.realestate.repository.SuperAdminRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SuperAdminInitializer implements CommandLineRunner {

    @Value(value = "${superadmin.username}")
    private String username;

    @Value(value = "${superadmin.password}")
    private String password;

    @Autowired
    private SuperAdminRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final Logger logger = LoggerFactory.getLogger(SuperAdminInitializer.class);

    @Override
    public void run(String... args) throws Exception {
        if(repository.findByUsername(username).isEmpty()){
            SuperAdmin superAdmin = new SuperAdmin();
            superAdmin.setUsername(username);
            superAdmin.setPassword(passwordEncoder.encode(password));
            superAdmin.setRole(Role.SUPERADMIN);
            repository.save(superAdmin);
        }else
            logger.info("SuperAdmin is already registered...!");
    }
}
