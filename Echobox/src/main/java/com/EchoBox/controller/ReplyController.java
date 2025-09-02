package com.EchoBox.controller;

import com.EchoBox.model.Reply;
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
@RequestMapping("/reply")
@Tag(name = "Reply", description = "APIs for managing replies")
public class ReplyController {
    
    private List<Reply> replies = new ArrayList<>();

    @PostMapping
    public ResponseEntity<Reply> save(@Valid @RequestBody Reply reply) {
        reply.setId(1);
        replies.add(reply);
        return ResponseEntity.status(HttpStatus.CREATED).body(reply);
    }

    @GetMapping
    @Operation(summary = "Get the list of replies", description = "Returns the list of replies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
    })
    public List<Reply> findAll() {
        return replies;
    }

    @PutMapping("/{id}")
    public Reply update(@PathVariable("id") Integer id, @RequestBody Reply reply) {
        reply.setId(id);
        return reply;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
    }
}

