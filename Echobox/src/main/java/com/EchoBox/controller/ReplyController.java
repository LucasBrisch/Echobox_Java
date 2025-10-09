package com.EchoBox.controller;

import com.EchoBox.exception.ErrorCode;
import com.EchoBox.exception.ResourceNotFoundException;
import com.EchoBox.model.Reply;
import com.EchoBox.repository.ReplyRepository;
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
@RequestMapping("/replies")
@Tag(name = "Reply", description = "API endpoints for reply management")
public class ReplyController {

    // ############### REPLY CONSTRUCTOR ###############

    private final ReplyRepository replyRepository;

    public ReplyController(ReplyRepository replyRepository) {
        this.replyRepository = replyRepository;
    }

    // ############### POST OPERATION ###############

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Creates a new reply", description = "Creates a new reply with auto-incremented ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<Reply> save(@Valid @RequestBody Reply reply) {
        Reply savedReply = replyRepository.save(reply);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReply);
    }

    // ############### GET ALL OPERATION ###############

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Gets a list of replies", description = "Retrieves all replies from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reply list retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Failed to retrieve reply list")
    })
    public List<Reply> findAll() {
        return replyRepository.findAll();
    }

    // ############### DELETE OPERATION ###############

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deletes a reply", description = "Deletes the reply with the specified ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Reply not found")
    })
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        if (!replyRepository.existsById(id)) {
            throw new ResourceNotFoundException(ErrorCode.REPLY_NOT_FOUND);
        }
        replyRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ############### PUT OPERATION ###############

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Updates a reply", description = "Updates the reply with the specified ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated successfully"),
            @ApiResponse(responseCode = "404", description = "Reply not found")
    })
    public ResponseEntity<Reply> update(@PathVariable("id") Integer id, @RequestBody Reply reply) {
        if (!replyRepository.existsById(id)) {
            throw new ResourceNotFoundException(ErrorCode.REPLY_NOT_FOUND);
        }
        reply.setId(id);
        Reply updatedReply = replyRepository.save(reply);
        return ResponseEntity.ok(updatedReply);
    }

    // ############### GET BY ID OPERATION ###############

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Gets a reply by ID", description = "Retrieves the reply with the specified ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reply retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Reply not found")
    })
    public ResponseEntity<Reply> findById(@PathVariable("id") Integer id) {
        return replyRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.REPLY_NOT_FOUND));
    }
}
