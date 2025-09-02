package com.EchoBox.controller;

import com.EchoBox.model.Status;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/status")
@Tag(name = "Status", description = "APIs for managing statuses")
public class StatusController {

    private List<Status> statuses = new ArrayList<>();

    @PostMapping
    public ResponseEntity<Status> save(@Valid @RequestBody Status status) {
        status.setId(1);
        statuses.add(status);
        return ResponseEntity.status(HttpStatus.CREATED).body(status);
    }

    @GetMapping
    @Operation(summary = "Get the list of statuses", description = "Returns the list of statuses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
    })
    public List<Status> findAll() {
        return statuses;
    }

    @PutMapping("/{id}")
    public Status update(@PathVariable("id") Integer id, @RequestBody Status status) {
        status.setId(id);
        return status;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
    }
}
