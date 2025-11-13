package com.EchoBox.controller;

import com.EchoBox.model.Status;
import com.EchoBox.repository.StatusRepository;
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
public class StatusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StatusRepository statusRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllStatuses() throws Exception {
        // Given
        Status status = new Status();
        status.setId(1);
        status.setType("Aberto");
        status.setColor("#00ff00");

        List<Status> statuses = new ArrayList<>();
        statuses.add(status);

        when(statusRepository.findAll()).thenReturn(statuses);

        // When/Then
        mockMvc.perform(get("/statuses")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].type").value("Aberto"))
                .andExpect(jsonPath("$.[0].color").value("#00ff00"))
                .andExpect(jsonPath("$.[0].id").value(1));
    }

    @Test
    public void testGetStatusById() throws Exception {
        // Given
        Status status = new Status();
        status.setId(1);
        status.setType("Em Progresso");
        status.setColor("#ffff00");

        when(statusRepository.findById(1)).thenReturn(Optional.of(status));

        // When/Then
        mockMvc.perform(get("/statuses/1")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("Em Progresso"))
                .andExpect(jsonPath("$.color").value("#ffff00"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testCreateStatus() throws Exception {
        // Given
        Status status = new Status();
        status.setType("Fechado");
        status.setColor("#ff0000");

        Status savedStatus = new Status();
        savedStatus.setId(1);
        savedStatus.setType("Fechado");
        savedStatus.setColor("#ff0000");

        when(statusRepository.save(any(Status.class))).thenReturn(savedStatus);

        // When/Then
        mockMvc.perform(post("/statuses")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(status)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.type").value("Fechado"))
                .andExpect(jsonPath("$.color").value("#ff0000"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testUpdateStatus() throws Exception {
        // Given
        Status status = new Status();
        status.setId(1);
        status.setType("Resolvido");
        status.setColor("#0000ff");

        when(statusRepository.existsById(1)).thenReturn(true);
        when(statusRepository.save(any(Status.class))).thenReturn(status);

        // When/Then
        mockMvc.perform(put("/statuses/1")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(status)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("Resolvido"))
                .andExpect(jsonPath("$.color").value("#0000ff"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testDeleteStatus() throws Exception {
        // Given
        when(statusRepository.existsById(1)).thenReturn(true);

        // When/Then
        mockMvc.perform(delete("/statuses/1")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetStatusByIdNotFound() throws Exception {
        // Given
        when(statusRepository.findById(999)).thenReturn(Optional.empty());

        // When/Then
        mockMvc.perform(get("/statuses/999")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateStatusWithBlankType() throws Exception {
        // Given
        Status status = new Status();
        status.setType("");
        status.setColor("#00ff00");

        // When/Then
        mockMvc.perform(post("/statuses")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(status)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateStatusNotFound() throws Exception {
        // Given
        Status status = new Status();
        status.setId(999);
        status.setType("Teste");
        status.setColor("#000000");

        when(statusRepository.existsById(999)).thenReturn(false);

        // When/Then
        mockMvc.perform(put("/statuses/999")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(status)))
                .andExpect(status().isNotFound());
    }
}
