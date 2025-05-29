package com.quantumsoft.realestate.controller;

import com.quantumsoft.realestate.entity.ScheduleVisit;
import com.quantumsoft.realestate.servicei.ScheduleVisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/scheduleVisit")
@CrossOrigin("*")
public class ScheduleVisitController {

    @Autowired
    private ScheduleVisitService scheduleVisitService;

    @PreAuthorize("hasRole('BUYER')")
    @PostMapping(value = "/visit/{id}")
    public ResponseEntity<String> scheduleVisit(@PathVariable Long id, @RequestBody ScheduleVisit scheduleVisit){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return new ResponseEntity<String>(scheduleVisitService.scheduleVisit(id, scheduleVisit, email), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('AGENT') or hasRole('SELLER')")
    @PatchMapping(value = "/manage/{id}")
    public ResponseEntity<String> managePropertyVisit(@PathVariable Long id, @RequestParam String visitStatus){
        return new ResponseEntity<String>(scheduleVisitService.managePropertyVisit(id, visitStatus), HttpStatus.OK);
    }
}
