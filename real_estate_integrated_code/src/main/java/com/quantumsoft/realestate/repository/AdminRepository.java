package com.quantumsoft.realestate.repository;

import com.quantumsoft.realestate.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

   public Optional<Admin> findByEmail(String username);

    Optional<Admin> findByOtp(String otp);
}
