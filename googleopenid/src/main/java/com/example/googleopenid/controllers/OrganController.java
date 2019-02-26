package com.example.googleopenid.controllers;

import com.example.googleopenid.models.Organ;
import com.example.googleopenid.repository.OrganRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/organs")
public class OrganController {

    @Autowired
    private OrganRepository organRepository;

    @GetMapping
    public ResponseEntity<Iterable<Organ>> getAll() {
        return ResponseEntity.ok(organRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Organ> add(@RequestBody Organ organ) {
        return ResponseEntity.ok(organRepository.save(organ));
    }
}
