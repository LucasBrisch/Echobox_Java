package com.EchoBox.controller;

import com.EchoBox.model.Status;
import com.EchoBox.repository.StatusRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/status")
@Tag(name = "Status", description = "APIs for managing statuses")
public class StatusController {

    @Autowired
    private StatusRepository statusRepository;

    @PostMapping
    public ResponseEntity<Status> save(@Valid @RequestBody Status status) {
        Status savedStatus = statusRepository.save(status);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStatus);
    }

    @GetMapping
    @Operation(summary = "Get the list of statuses", description = "Returns the list of statuses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
    })
    public List<Status> findAll() {
        return statusRepository.findAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Status> update(@PathVariable("id") Integer id, @RequestBody Status status) {
        if (!statusRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        status.setIdStatus(id);
        Status updatedStatus = statusRepository.save(status);
        return ResponseEntity.ok(updatedStatus);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
    }
}
