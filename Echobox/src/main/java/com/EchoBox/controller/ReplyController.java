package com.EchoBox.controller;

import com.EchoBox.model.Reply;
import com.EchoBox.repository.ReplyRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reply")
@Tag(name = "Reply", description = "APIs for managing replies")
public class ReplyController {

    @Autowired
    private ReplyRepository replyRepository;

    @PostMapping
    public ResponseEntity<Reply> save(@Valid @RequestBody Reply reply) {
        Reply savedReply = replyRepository.save(reply);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReply);
    }

    @GetMapping
    @Operation(summary = "Get the list of replies", description = "Returns the list of replies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
    })
    public List<Reply> findAll() {
        return replyRepository.findAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reply> update(@PathVariable("id") Integer id, @RequestBody Reply reply) {
        if (!replyRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        reply.setIdReply(id);
        Reply updatedReply = replyRepository.save(reply);
        return ResponseEntity.ok(updatedReply);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        if (!replyRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        replyRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
