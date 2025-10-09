package com.EchoBox.controller;

import com.EchoBox.exception.ErrorCode;
import com.EchoBox.exception.ResourceNotFoundException;
import com.EchoBox.model.AuthenticationRequest;
import com.EchoBox.model.AuthenticationResponse;
import com.EchoBox.model.User;
import com.EchoBox.repository.UserRepository;
import com.EchoBox.service.JwtUtil;
import com.EchoBox.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// ############### CRUD OPERATIONS ###############

@RestController
@RequestMapping("/users")
@Tag(name = "User", description = "API endpoints for user management")
public class UserController {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserController(UserRepository userRepository, AuthenticationManager authenticationManager, UserDetailsService userDetailsService, UserService userService, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    @Operation(summary = "Registers a new user")
    public ResponseEntity<User> register(@Valid @RequestBody User user) {
        User registeredUser = userService.register(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticates a user and returns a JWT")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
        );

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        final User user = userRepository.findByEmail(authenticationRequest.getEmail()).orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND));
        final String jwt = jwtUtil.generateToken(userDetails, user.getIsAdmin());

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    // ############### POST OPERATION ###############

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Creates a new user", description = "Creates a new user with auto-incremented ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<User> save(@Valid @RequestBody User user) {
        User savedUser = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    // ############### GET ALL OPERATION ###############

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Gets a list of users", description = "Retrieves all users from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User list retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Failed to retrieve user list")
    })
    public List<User> findAll() {
        return userRepository.findAll();
    }

    // ############### DELETE OPERATION ###############

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deletes a user", description = "Deletes the user with the specified ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND);
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ############### PUT OPERATION ###############

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Updates a user", description = "Updates the user with the specified ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<User> update(@PathVariable("id") Integer id, @RequestBody User user) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND);
        }
        user.setId(id);
        User updatedUser = userRepository.save(user);
        return ResponseEntity.ok(updatedUser);
    }

    // ############### GET BY ID OPERATION ###############

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Gets a user by ID", description = "Retrieves the user with the specified ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<User> findById(@PathVariable("id") Integer id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND));
    }
}
