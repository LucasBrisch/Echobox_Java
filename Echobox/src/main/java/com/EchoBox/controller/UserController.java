package com.EchoBox.controller;

import com.EchoBox.model.User;
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
@RequestMapping("/user")
@Tag(name = "User", description = "APIs for managing users")
public class UserController {

    private List<User> users = new ArrayList<>();

    @PostMapping
    public ResponseEntity<User> save(@Valid @RequestBody User user) {
        user.setId(1);
        users.add(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping
    @Operation(summary = "Get the list of users", description = "Returns the list of users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
    })
    public List<User> findAll() {
        return users;
    }

    @PutMapping("/{id}")
    public User update(@PathVariable("id") Integer id, @RequestBody User user) {
        user.setId(id);
        return user;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
    }
}
