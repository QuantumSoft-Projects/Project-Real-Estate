package com.quantumsoft.realestate.serviceimpl;

import com.quantumsoft.realestate.entity.Property;
import com.quantumsoft.realestate.entity.ScheduleVisit;
import com.quantumsoft.realestate.entity.Users;
import com.quantumsoft.realestate.enums.VisitStatus;
import com.quantumsoft.realestate.exception.ResourceNotFoundException;
import com.quantumsoft.realestate.repository.PropertyRepository;
import com.quantumsoft.realestate.repository.ScheduleVisitRepository;
import com.quantumsoft.realestate.repository.UserRepository;
import com.quantumsoft.realestate.servicei.ScheduleVisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ScheduleVisitServiceImpl implements ScheduleVisitService {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScheduleVisitRepository scheduleVisitRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Value(value = "${spring.mail.username}")
    private String from;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public String scheduleVisit(Long id, ScheduleVisit scheduleVisit, String email) {
        Optional<Property> propertyOptional = propertyRepository.findById(id);
        Optional<Users> userOptional = userRepository.findByEmail(email);
        if(propertyOptional.isPresent() && userOptional.isPresent()) {
            Property property = propertyOptional.get();
            Users user = userOptional.get();
            scheduleVisit.setPropertyToVisit(property);
            scheduleVisit.setUser(user);
            ScheduleVisit savedScheduleVisit = scheduleVisitRepository.save(scheduleVisit);
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(from);
            simpleMailMessage.setTo(property.getAgent().getEmail()); // AGENT
            simpleMailMessage.setSubject("RealEstate - Property Visit");
            simpleMailMessage.setText("Dear " + property.getAgent().getName() + "\n \n" + "Buyer " + user.getName() + " is trying to schedule the visit with id " + savedScheduleVisit.getId() + ". \n" + "Confirm/Reject the meeting as per your work schedule");
            javaMailSender.send(simpleMailMessage);
            return "Request of visit is send to Agent";
        }else
            throw new ResourceNotFoundException("Property record or User record not found in database");
    }

    @Override
    public String managePropertyVisit(Long id, String visitStatus) {
        Optional<ScheduleVisit> scheduleVisitOptional = scheduleVisitRepository.findById(id);
        if(scheduleVisitOptional.isPresent()) {
            ScheduleVisit scheduleVisit = scheduleVisitOptional.get();
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            if(visitStatus.equals("CONFIRMED")){
            scheduleVisit.setVisitStatus(VisitStatus.CONFIRMED);
            scheduleVisitRepository.save(scheduleVisit);
            Map<String, String> notification = new HashMap<>();
            notification.put("message", "Hi " + scheduleVisit.getUser().getName() + "\n \n" + "Your meeting is confirmed on " + scheduleVisit.getDate() + " at " + scheduleVisit.getTime() + " with " + scheduleVisit.getVisitingMode().name() + " mode."+ "\n\n Please be ready on scheduled Date and Time.");
            messagingTemplate.convertAndSend("/topic/notifications", notification);
            simpleMailMessage.setFrom(from);
            simpleMailMessage.setTo(scheduleVisit.getUser().getEmail()); // BUYER
            simpleMailMessage.setSubject("RealEstate - Property Visit");
            simpleMailMessage.setText("Dear " + scheduleVisit.getUser().getName() + "\n \n" + "Your meeting is confirmed on " + scheduleVisit.getDate() + " at " + scheduleVisit.getTime() + " with " + scheduleVisit.getVisitingMode().name() + " mode."+ "\n\n Please be ready on scheduled Date and Time.");
            javaMailSender.send(simpleMailMessage);
            return "Visit confirmed";
            }else{
                scheduleVisit.setVisitStatus(VisitStatus.CANCELLED);
                scheduleVisit.setMessage("heavy rain");
                scheduleVisitRepository.save(scheduleVisit);
                Map<String, String> notification = new HashMap<>();
                notification.put("message", "Hi " + scheduleVisit.getUser().getName() + "\n \n" + "Your meeting is cancelled due to " + scheduleVisit.getMessage());
                messagingTemplate.convertAndSend("/topic/notifications", notification);
                simpleMailMessage.setFrom(scheduleVisit.getPropertyToVisit().getAgent().getEmail());
                simpleMailMessage.setTo(scheduleVisit.getUser().getEmail());
                simpleMailMessage.setSubject("RealEstate - Property Visit");
                simpleMailMessage.setText("Dear " + scheduleVisit.getUser().getName() + "\n \n" + "Your meeting is cancelled due to " + scheduleVisit.getMessage());
                javaMailSender.send(simpleMailMessage);
                return "Visit cancelled";
            }
        }else
            throw new ResourceNotFoundException("ScheduleVisit record not found in the database");
    }
}
