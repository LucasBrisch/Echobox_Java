package com.EchoBox.controller;

import com.EchoBox.exception.ErrorCode;
import com.EchoBox.exception.ResourceNotFoundException;
import com.EchoBox.model.Status;
import com.EchoBox.repository.StatusRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// ############### CRUD OPERATIONS ###############

@RestController
@RequestMapping("/statuses")
@Tag(name = "Status", description = "API endpoints for status management")
public class StatusController {

    // ############### STATUS CONSTRUCTOR ###############

    private final StatusRepository statusRepository;

    public StatusController(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    // ############### POST OPERATION ###############

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Creates a new status", description = "Creates a new status with auto-incremented ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<Status> save(@Valid @RequestBody Status status) {
        Status savedStatus = statusRepository.save(status);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStatus);
    }

    // ############### GET ALL OPERATION ###############

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Gets a list of statuses", description = "Retrieves all statuses from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status list retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Failed to retrieve status list")
    })
    public List<Status> findAll() {
        return statusRepository.findAll();
    }

    // ############### DELETE OPERATION ###############

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deletes a status", description = "Deletes the status with the specified ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Status not found")
    })
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        if (!statusRepository.existsById(id)) {
            throw new ResourceNotFoundException(ErrorCode.STATUS_NOT_FOUND);
        }
        statusRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ############### PUT OPERATION ###############

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Updates a status", description = "Updates the status with the specified ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated successfully"),
            @ApiResponse(responseCode = "404", description = "Status not found")
    })
    public ResponseEntity<Status> update(@PathVariable("id") Integer id, @RequestBody Status status) {
        if (!statusRepository.existsById(id)) {
            throw new ResourceNotFoundException(ErrorCode.STATUS_NOT_FOUND);
        }
        status.setId(id);
        Status updatedStatus = statusRepository.save(status);
        return ResponseEntity.ok(updatedStatus);
    }

    // ############### GET BY ID OPERATION ###############

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Gets a status by ID", description = "Retrieves the status with the specified ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Status not found")
    })
    public ResponseEntity<Status> findById(@PathVariable("id") Integer id) {
        return statusRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.STATUS_NOT_FOUND));
    }
}
