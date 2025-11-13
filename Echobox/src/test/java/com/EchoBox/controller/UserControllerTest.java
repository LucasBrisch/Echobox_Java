package com.EchoBox.controller;

import com.EchoBox.model.User;
import com.EchoBox.repository.UserRepository;
import com.EchoBox.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllUsers() throws Exception {
        // Given
        User user = new User();
        user.setId(1);
        user.setEmail("test@example.com");
        user.setIsAdmin(false);

        List<User> users = new ArrayList<>();
        users.add(user);

        when(userRepository.findAll()).thenReturn(users);

        // When/Then
        mockMvc.perform(get("/users")
                        .with(user("admin").roles("ADMIN", "USER"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].email").value("test@example.com"))
                .andExpect(jsonPath("$.[0].id").value(1));
    }

    @Test
    public void testGetUserById() throws Exception {
        // Given
        User user = new User();
        user.setId(1);
        user.setEmail("test@example.com");
        user.setIsAdmin(false);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // When/Then
        mockMvc.perform(get("/users/1")
                        .with(user("admin").roles("ADMIN", "USER"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testCreateUser() throws Exception {
        // Given
        User user = new User();
        user.setEmail("newuser@example.com");
        user.setPassword("password123");
        user.setIsAdmin(false);

        User savedUser = new User();
        savedUser.setId(1);
        savedUser.setEmail("newuser@example.com");
        savedUser.setPassword("password123");
        savedUser.setIsAdmin(false);

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // When/Then
        mockMvc.perform(post("/users")
                        .with(user("admin").roles("ADMIN", "USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("newuser@example.com"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testUpdateUser() throws Exception {
        // Given
        User user = new User();
        user.setId(1);
        user.setEmail("updated@example.com");
        user.setPassword("newpassword");
        user.setIsAdmin(false);

        when(userRepository.existsById(1)).thenReturn(true);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When/Then
        mockMvc.perform(put("/users/1")
                        .with(user("admin").roles("ADMIN", "USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("updated@example.com"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testDeleteUser() throws Exception {
        // Given
        when(userRepository.existsById(1)).thenReturn(true);

        // When/Then
        mockMvc.perform(delete("/users/1")
                        .with(user("admin").roles("ADMIN", "USER"))
                )
                .andExpect(status().isNoContent());
    }

    @Test
    public void testRegisterUser() throws Exception {
        // Given
        User user = new User();
        user.setEmail("register@example.com");
        user.setPassword("password123");
        user.setIsAdmin(false);

        User registeredUser = new User();
        registeredUser.setId(1);
        registeredUser.setEmail("register@example.com");
        registeredUser.setIsAdmin(false);

        when(userService.register(any(User.class))).thenReturn(registeredUser);

        // When/Then
        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("register@example.com"))
                .andExpect(jsonPath("$.id").value(1));
    }
}
