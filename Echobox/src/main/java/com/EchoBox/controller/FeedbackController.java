package com.EchoBox.controller;

import com.EchoBox.exception.ErrorCode;
import com.EchoBox.exception.ResourceNotFoundException;
import com.EchoBox.model.Feedback;
import com.EchoBox.repository.FeedbackRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// ############### CRUD OPERATIONS ###############

@RestController
@RequestMapping("/feedbacks")
@Tag(name = "Feedback", description = "API endpoints for feedback management")
public class FeedbackController {

    // ############### FEEDBACK CONSTRUCTOR ###############

    private final FeedbackRepository feedbackRepository;

    public FeedbackController(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    // ############### POST OPERATION ###############

    @PostMapping
    @Operation(summary = "Creates a new feedback", description = "Creates a new feedback with auto-incremented ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<Feedback> save(@Valid @RequestBody Feedback feedback) {
        Feedback savedFeedback = feedbackRepository.save(feedback);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFeedback);
    }

    // ############### GET ALL OPERATION ###############

    @GetMapping
    @Operation(summary = "Gets a list of feedbacks", description = "Retrieves all feedbacks from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Feedback list retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Failed to retrieve feedback list")
    })
    public List<Feedback> findAll() {
        return feedbackRepository.findAll();
    }

    // ############### DELETE OPERATION ###############

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes a feedback", description = "Deletes the feedback with the specified ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Feedback not found")
    })
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        if (!feedbackRepository.existsById(id)) {
            throw new ResourceNotFoundException(ErrorCode.FEEDBACK_NOT_FOUND);
        }
        feedbackRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ############### PUT OPERATION ###############

    @PutMapping("/{id}")
    @Operation(summary = "Updates a feedback", description = "Updates the feedback with the specified ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated successfully"),
            @ApiResponse(responseCode = "404", description = "Feedback not found")
    })
    public ResponseEntity<Feedback> update(@PathVariable("id") Integer id, @RequestBody Feedback feedback) {
        if (!feedbackRepository.existsById(id)) {
            throw new ResourceNotFoundException(ErrorCode.FEEDBACK_NOT_FOUND);
        }
        feedback.setId(id);
        Feedback updatedFeedback = feedbackRepository.save(feedback);
        return ResponseEntity.ok(updatedFeedback);
    }

    // ############### GET BY ID OPERATION ###############

    @GetMapping("/{id}")
    @Operation(summary = "Gets a feedback by ID", description = "Retrieves the feedback with the specified ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Feedback retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Feedback not found")
    })
    public ResponseEntity<Feedback> findById(@PathVariable("id") Integer id) {
        return feedbackRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.FEEDBACK_NOT_FOUND));
    }
}
