package com.quantumsoft.realestate.entity;

import com.quantumsoft.realestate.enums.VisitStatus;
import com.quantumsoft.realestate.enums.VisitingMode;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class ScheduleVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "scheduleVisitId")
    private Long id;
    @OneToOne
    private Property propertyToVisit;
    @OneToOne
    private Users user; // BUYER
    private LocalDate date;
    private LocalTime time;
    @Enumerated(EnumType.STRING)
    private VisitingMode visitingMode;
    @Enumerated(EnumType.STRING)
    private VisitStatus visitStatus;
    private String message;

    public ScheduleVisit(){}

    public ScheduleVisit(Long id, Property propertyToVisit, Users user, LocalDate date, LocalTime time, VisitingMode visitingMode, VisitStatus visitStatus, String message) {
        this.id = id;
        this.propertyToVisit = propertyToVisit;
        this.user = user;
        this.date = date;
        this.time = time;
        this.visitingMode = visitingMode;
        this.visitStatus = visitStatus;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Property getPropertyToVisit() {
        return propertyToVisit;
    }

    public void setPropertyToVisit(Property propertyToVisit) {
        this.propertyToVisit = propertyToVisit;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public VisitingMode getVisitingMode() {
        return visitingMode;
    }

    public void setVisitingMode(VisitingMode visitingMode) {
        this.visitingMode = visitingMode;
    }

    public VisitStatus getVisitStatus() {
        return visitStatus;
    }

    public void setVisitStatus(VisitStatus visitStatus) {
        this.visitStatus = visitStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
