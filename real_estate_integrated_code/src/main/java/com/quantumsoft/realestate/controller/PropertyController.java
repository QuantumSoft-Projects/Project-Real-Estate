package com.quantumsoft.realestate.controller;

import com.quantumsoft.realestate.entity.Property;
import com.quantumsoft.realestate.servicei.PropertyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/properties")
@CrossOrigin("*")
//@RequiredArgsConstructor
public class PropertyController {
    @Autowired
    private PropertyService propertyService;

    @PreAuthorize("hasRole('SELLER')")
    @PostMapping
    public ResponseEntity<Property> create(@Valid @RequestBody Property property) {
        return ResponseEntity.ok(propertyService.createProperty(property));
    }

    @PreAuthorize("hasRole('SELLER')")
    @PutMapping("/{id}")
    public ResponseEntity<Property> update(@PathVariable Long id, @RequestBody Property property) {

        return ResponseEntity.ok(propertyService.updatedProperty(id, property));

    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SELLER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        propertyService.deleteProperty(id);

        return ResponseEntity.noContent().build();

    }

    @GetMapping("/{id}")
    public ResponseEntity<Property> getById(@PathVariable Long id) {
        return ResponseEntity.ok(propertyService.getPropertyById(id));
    }

    @GetMapping(value = "/available")
    public ResponseEntity<List<Property>> getAll() {
        return ResponseEntity.ok(propertyService.getAllProperties());
    }

    @GetMapping(value = "/inactive")
    public ResponseEntity<List<Property>> getAllInactiveProperties() {
        return ResponseEntity.ok(propertyService.getAllInactiveProperties());
    }
}
