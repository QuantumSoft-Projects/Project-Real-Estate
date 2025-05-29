package com.quantumsoft.realestate.repository;


import com.quantumsoft.realestate.entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AgentRepository extends JpaRepository<Agent, Long> {
    Optional<Agent> findByEmail(String email);
    boolean existsByEmail(String email);
}

