package com.quantumsoft.realestate.entity;

import com.quantumsoft.realestate.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

@Entity
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "adminId")
    private Long id;
    @NotBlank(message = "Full name cannot be blank")
    private String fullName;
    @Pattern(regexp = "^(\\+91|91)?[6-9][0-9]{9}", message = "Invalid mobile number")
    @NotBlank(message = "Phone number cannot be blank")
    private String phoneNo;
    @Email(message = "Email should be in abc@example.com")
    @NotBlank(message = "email cannot be blank")
    private String email;
    @NotBlank(message = "Password cannot be blank")
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Role role;
    private boolean isActive;
    private String otp;
    private LocalDateTime otpGeneratedTime;
    private boolean isFirstLoginCheck;

    public Admin(){}

    public Admin(Long id, String fullName, String phoneNo, String email, String password, Role role, boolean isActive, String otp, LocalDateTime otpGeneratedTime, boolean isFirstLoginCheck) {
        this.id = id;
        this.fullName = fullName;
        this.phoneNo = phoneNo;
        this.email = email;
        this.password = password;
        this.role = role;
        this.isActive = isActive;
        this.otp = otp;
        this.otpGeneratedTime = otpGeneratedTime;
        this.isFirstLoginCheck = isFirstLoginCheck;
    }

    public LocalDateTime getOtpGeneratedTime() {
        return otpGeneratedTime;
    }

    public void setOtpGeneratedTime(LocalDateTime otpGeneratedTime) {
        this.otpGeneratedTime = otpGeneratedTime;
    }

    public boolean isFirstLoginCheck() {
        return isFirstLoginCheck;
    }

    public void setFirstLoginCheck(boolean firstLoginCheck) {
        isFirstLoginCheck = firstLoginCheck;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank(message = "Full name cannot be blank") String getFullName() {
        return fullName;
    }

    public void setFullName(@NotBlank(message = "Full name cannot be blank") String fullName) {
        this.fullName = fullName;
    }

    public @Pattern(regexp = "^(\\+91|91)?[6-9][0-9]{9}", message = "Invalid mobile number") @NotBlank(message = "Phone number cannot be blank") String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(@Pattern(regexp = "^(\\+91|91)?[6-9][0-9]{9}", message = "Invalid mobile number") @NotBlank(message = "Phone number cannot be blank") String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public @Email(message = "Email should be in abc@example.com") @NotBlank(message = "email cannot be blank") String getEmail() {
        return email;
    }

    public void setEmail(@Email(message = "Email should be in abc@example.com") @NotBlank(message = "email cannot be blank") String email) {
        this.email = email;
    }

    public @NotBlank(message = "Password cannot be blank") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "Password cannot be blank") String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}