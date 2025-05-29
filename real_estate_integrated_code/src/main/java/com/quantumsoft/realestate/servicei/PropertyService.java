package com.quantumsoft.realestate.servicei;

import com.quantumsoft.realestate.entity.Property;

import java.util.List;
public interface PropertyService {

    Property createProperty(Property property);

    Property updatedProperty(Long propertyId, Property updatedProperty);

    void deleteProperty(Long propertyId);

    List<Property> getAllProperties();

    public List<Property> getAllInactiveProperties();

    Property getPropertyById(Long id);
}
