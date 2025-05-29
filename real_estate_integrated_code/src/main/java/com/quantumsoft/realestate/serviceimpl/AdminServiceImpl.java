package com.quantumsoft.realestate.serviceimpl;

import com.quantumsoft.realestate.dto.ResetPasswordDto;
import com.quantumsoft.realestate.entity.Admin;
import com.quantumsoft.realestate.entity.Property;
import com.quantumsoft.realestate.enums.PropertyStatus;
import com.quantumsoft.realestate.exception.AdminRecordNotFoundException;
import com.quantumsoft.realestate.exception.ResourceNotFoundException;
import com.quantumsoft.realestate.repository.AdminRepository;
import com.quantumsoft.realestate.repository.PropertyRepository;
import com.quantumsoft.realestate.servicei.AdminServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class AdminServiceImpl implements AdminServiceI {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Value(value = "${spring.mail.username}")
    private String from;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public String register(Admin admin) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setTo(admin.getEmail());
        simpleMailMessage.setSubject("RealEstate App credentials");
        simpleMailMessage.setText("Dear " + admin.getFullName() + ", \n\n" + "Your RealEstate App credentials are: \n" + "email: " + admin.getEmail() + "\nPassword: " + admin.getPassword() + "\n\nKindly changed the password before login to this app.");

        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setActive(false);
        admin.setFirstLoginCheck(true);
        adminRepository.save(admin);

        javaMailSender.send(simpleMailMessage);
        return "Admins record has saved successfully.";
    }

    @Override
    public String sendOtp(String email) {
        Optional<Admin> adminOptional = adminRepository.findByEmail(email);
        if (adminOptional.isPresent()) {
            String otp = String.valueOf(100000 + new Random().nextInt(90000));
            Admin admin = adminOptional.get();
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(from);
            simpleMailMessage.setTo(email);
            simpleMailMessage.setSubject("RealEstate App OTP");
            simpleMailMessage.setText("Your OTP for the RealEstate is: " + otp);
            admin.setOtp(passwordEncoder.encode(otp));
            admin.setOtpGeneratedTime(LocalDateTime.now());
            adminRepository.save(admin);

            javaMailSender.send(simpleMailMessage);
            return "Otp has been sent to registered email.";
        } else
            throw new AdminRecordNotFoundException("Admin record with registered email not found. You have entered the wrong email.");
    }

    @Override
    public String resetPassword(ResetPasswordDto resetPasswordDto) {
        if(resetPasswordDto.getPassword().equals(resetPasswordDto.getConfirmPassword())){
            Optional<Admin> adminOptional = adminRepository.findByEmail(resetPasswordDto.getEmail());
            if(adminOptional.isPresent()){
                Admin admin = adminOptional.get();
                LocalDateTime otpExpiryTime = admin.getOtpGeneratedTime().plusMinutes(2);
                if(LocalDateTime.now().isAfter(otpExpiryTime)){
                    return "OTP is expired";
                }
                if(passwordEncoder.matches(resetPasswordDto.getOtp(), admin.getOtp())) {
                    admin.setOtp(null);
                    admin.setPassword(passwordEncoder.encode(resetPasswordDto.getPassword()));
                    admin.setFirstLoginCheck(false);
                    adminRepository.save(admin);
                    return "Password has reset successfully.";
                }else
                    return "OTP is incorrect";
            }else
                throw new AdminRecordNotFoundException("Admin record with given email is not found in database");
        }else
            return "Password and ConfirmPassword are not matching.";
    }

    @Override
    public void savedAdminStatus(Admin admin) {
        adminRepository.save(admin);
    }

    @Override
    public String propertyApproval(Long id, String propertyStatus) {
        Optional<Property> propertyOptional = propertyRepository.findById(id);
        if(propertyOptional.isPresent()){
            Property property = propertyOptional.get();
            if(propertyStatus.equals("APPROVED")) {
                property.setStatus(PropertyStatus.AVAILABLE);
                propertyRepository.save(property);
                Map<String, String> notification = new HashMap<>();
                notification.put("message", "Hi Seller your property is approved.");
                messagingTemplate.convertAndSend("/topic/notifications", notification);
                return "Property Approved";
            }else{
                property.setStatus(PropertyStatus.REJECT);
                propertyRepository.save(property);
                return "Property REJECTED";
            }
        }else
            throw new ResourceNotFoundException("Property record not found in the database");
    }
}
