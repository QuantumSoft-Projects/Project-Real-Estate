package com.quantumsoft.realestate.serviceimpl;

import com.quantumsoft.realestate.entity.Property;
import com.quantumsoft.realestate.enums.PropertyStatus;
import com.quantumsoft.realestate.exception.ResourceNotFoundException;
import com.quantumsoft.realestate.repository.AgentRepository;
import com.quantumsoft.realestate.repository.PropertyRepository;
import com.quantumsoft.realestate.repository.UserRepository;
import com.quantumsoft.realestate.servicei.PropertyService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class PropertyServiceImpl implements PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Property createProperty(Property property) {
        property.setStatus(PropertyStatus.INACTIVE);
        property.setCreatedAt(LocalDateTime.now());
        agentRepository.findById(property.getAgent().getAgentId()).ifPresent(property::setAgent);
        userRepository.findById(property.getOwner().getId()).ifPresent(property::setOwner);
        return propertyRepository.save(property);
    }
    @Override
    public Property updatedProperty(Long propertyId, Property updatedProperty) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));

        property.setTitle(updatedProperty.getTitle());
        property.setDescription(updatedProperty.getDescription());
        property.setLocation(updatedProperty.getLocation());
        property.setPrice(updatedProperty.getPrice());
        property.setType(updatedProperty.getType());
        property.setAmenities(updatedProperty.getAmenities());
        property.setImages(updatedProperty.getImages());
        property.setStatus(updatedProperty.getStatus());
        property.setUpdatedAt(LocalDateTime.now());

        return propertyRepository.save(property);
    }

    @Override
    public void deleteProperty(Long propertyId) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));

        property.setDeleted(true);
        property.setStatus(PropertyStatus.INACTIVE);

        propertyRepository.save(property);
    }

//    @Override
//    public Property getPropertyById(Long propertyId) {
//        return propertyRepository.findById(propertyId);
//    }

    // API to fetch all Available properties

    @Override
    public List<Property> getAllProperties() {
        //List<Property> properties = propertyRepository.findByDeletedFalse();
        List<Property> listOfAvailableProperties = propertyRepository.findAll().stream().filter(p -> p.getStatus().equals(PropertyStatus.AVAILABLE)).toList();
        if(listOfAvailableProperties.isEmpty()){
            throw new ResourceNotFoundException("Currently there are no properties available");
        }else
            return listOfAvailableProperties;
    }

    // API to fetch all Inactive properties

    @Override
    public List<Property> getAllInactiveProperties() {
        //List<Property> properties = propertyRepository.findByDeletedFalse();
        List<Property> listOfInactiveProperties = propertyRepository.findAll().stream().filter(p -> p.getStatus().equals(PropertyStatus.INACTIVE)).toList();
        if(listOfInactiveProperties.isEmpty()){
            throw new ResourceNotFoundException("Currently there are no Inactive properties");
        }else
            return listOfInactiveProperties;
    }


    @Override
    public Property getPropertyById(Long id) {
        return propertyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Property not found with ID: " + id));
    }
}

