package com.quantumsoft.realestate.servicei;


import com.quantumsoft.realestate.entity.Agent;

public interface AgentService {

    public Agent register(Agent agent);

    public Agent getByEmail(String email);

    public Agent update(String email, Agent updatedData);

    public void delete(String email);

    Agent login(String email, String password);

    void savedAgentStatus(Agent agent);
}
