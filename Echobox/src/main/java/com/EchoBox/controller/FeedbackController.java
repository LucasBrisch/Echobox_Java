package com.EchoBox.controller;

import com.EchoBox.model.Feedback;
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
@RequestMapping("/feedback")
@Tag(name = "Feedback", description = "APIs for managing feedbacks")
public class FeedbackController {

    private List<Feedback> feedbacks = new ArrayList<>();

    @PostMapping
    public ResponseEntity<Feedback> save(@Valid @RequestBody Feedback feedback) {
        feedback.setId(1);
        feedbacks.add(feedback);
        return ResponseEntity.status(HttpStatus.CREATED).body(feedback);
    }

    @GetMapping
    @Operation(summary = "Get the list of feedbacks", description = "Returns the list of feedbacks")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
    })
    public List<Feedback> findAll() {
        return feedbacks;
    }

    @PutMapping("/{id}")
    public Feedback update(@PathVariable("id") Integer id, @RequestBody Feedback feedback) {
        feedback.setId(id);
        return feedback;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
    }
}
