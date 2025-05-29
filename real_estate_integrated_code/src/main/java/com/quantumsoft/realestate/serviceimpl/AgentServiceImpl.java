package com.quantumsoft.realestate.serviceimpl;

import com.quantumsoft.realestate.entity.Agent;
import com.quantumsoft.realestate.enums.AgentStatus;
import com.quantumsoft.realestate.enums.Role;
import com.quantumsoft.realestate.repository.AgentRepository;
import com.quantumsoft.realestate.servicei.AgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AgentServiceImpl implements AgentService {
    @Autowired
    private  AgentRepository agentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Agent register(Agent agent) {
        if (agentRepository.existsByEmail(agent.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        agent.setPassword(passwordEncoder.encode(agent.getPassword()));
        agent.setStatus(AgentStatus.INACTIVE);
        agent.setRole(Role.AGENT);
        return agentRepository.save(agent);
    }

    @Override
    public Agent getByEmail(String email) {
        Agent agent= agentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Agent not found"));
        if (!agent.getEmail().equals(email)) {
            throw new RuntimeException("Invalid email");
        }
        return agent;

    }

    @Override
    public Agent update(String email, Agent updatedData) {
        Agent agent = agentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        if (!agent.getEmail().equals(email)) {
            throw new RuntimeException("Invalid email");
        }

        agent.setName(updatedData.getName());
        agent.setPhoneNumber(updatedData.getPhoneNumber());
        agent.setBio(updatedData.getBio());
        agent.setProfilePicture(updatedData.getProfilePicture());
        return agentRepository.save(agent);
    }

    @Override
    public void delete(String email) {
        Agent agent = agentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        if (!agent.getEmail().equals(email)) {
            throw new RuntimeException("Invalid email");
        }
        agentRepository.delete(agent);
    }

    @Override
    public Agent login(String email, String password) {
        Agent agent = agentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!agent.getEmail().equals(email)) {
            throw new RuntimeException("Invalid email");
        }

        if (!agent.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password");
        }

        return agent;
    }

    @Override
    public void savedAgentStatus(Agent agent) {
        agentRepository.save(agent);
    }
}


