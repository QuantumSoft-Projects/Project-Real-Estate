package com.quantumsoft.realestate.repository;

import com.quantumsoft.realestate.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);
    Optional<Users> findByVerificationToken(String token);
    Optional<Users> findByResetToken(String token);
    boolean existsByEmail(String email);
}
