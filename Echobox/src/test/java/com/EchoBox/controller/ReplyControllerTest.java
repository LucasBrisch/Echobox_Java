package com.EchoBox.controller;

import com.EchoBox.model.Reply;
import com.EchoBox.repository.ReplyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
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
public class ReplyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReplyRepository replyRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllReplies() throws Exception {
        // Given
        Reply reply = new Reply();
        reply.setId(1);
        reply.setTitle("Resposta ao Feedback");
        reply.setReview("Obrigado pelo seu feedback, estamos trabalhando nisso.");
        reply.setFeedbackId(1);
        reply.setUserId(1);
        reply.setCreatedDate(LocalDateTime.now());

        List<Reply> replies = new ArrayList<>();
        replies.add(reply);

        when(replyRepository.findAll()).thenReturn(replies);

        // When/Then
        mockMvc.perform(get("/replies")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].title").value("Resposta ao Feedback"))
                .andExpect(jsonPath("$.[0].review").value("Obrigado pelo seu feedback, estamos trabalhando nisso."))
                .andExpect(jsonPath("$.[0].feedback").value(1))
                .andExpect(jsonPath("$.[0].user").value(1))
                .andExpect(jsonPath("$.[0].id").value(1));
    }

    @Test
    public void testGetReplyById() throws Exception {
        // Given
        Reply reply = new Reply();
        reply.setId(1);
        reply.setTitle("Resposta Importante");
        reply.setReview("Esta é uma resposta detalhada ao seu feedback.");
        reply.setFeedbackId(2);
        reply.setUserId(3);
        reply.setCreatedDate(LocalDateTime.now());

        when(replyRepository.findById(1)).thenReturn(Optional.of(reply));

        // When/Then
        mockMvc.perform(get("/replies/1")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Resposta Importante"))
                .andExpect(jsonPath("$.review").value("Esta é uma resposta detalhada ao seu feedback."))
                .andExpect(jsonPath("$.feedback").value(2))
                .andExpect(jsonPath("$.user").value(3))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testCreateReply() throws Exception {
        // Given
        Reply reply = new Reply();
        reply.setTitle("Nova Resposta");
        reply.setReview("Agradecemos sua contribuição!");
        reply.setFeedbackId(1);
        reply.setUserId(2);

        Reply savedReply = new Reply();
        savedReply.setId(1);
        savedReply.setTitle("Nova Resposta");
        savedReply.setReview("Agradecemos sua contribuição!");
        savedReply.setFeedbackId(1);
        savedReply.setUserId(2);
        savedReply.setCreatedDate(LocalDateTime.now());

        when(replyRepository.save(any(Reply.class))).thenReturn(savedReply);

        // When/Then
        mockMvc.perform(post("/replies")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reply)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Nova Resposta"))
                .andExpect(jsonPath("$.review").value("Agradecemos sua contribuição!"))
                .andExpect(jsonPath("$.feedback").value(1))
                .andExpect(jsonPath("$.user").value(2))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testUpdateReply() throws Exception {
        // Given
        Reply reply = new Reply();
        reply.setId(1);
        reply.setTitle("Resposta Atualizada");
        reply.setReview("Conteúdo atualizado da resposta.");
        reply.setFeedbackId(1);
        reply.setUserId(1);

        when(replyRepository.existsById(1)).thenReturn(true);
        when(replyRepository.save(any(Reply.class))).thenReturn(reply);

        // When/Then
        mockMvc.perform(put("/replies/1")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reply)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Resposta Atualizada"))
                .andExpect(jsonPath("$.review").value("Conteúdo atualizado da resposta."))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testDeleteReply() throws Exception {
        // Given
        when(replyRepository.existsById(1)).thenReturn(true);

        // When/Then
        mockMvc.perform(delete("/replies/1")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetReplyByIdNotFound() throws Exception {
        // Given
        when(replyRepository.findById(999)).thenReturn(Optional.empty());

        // When/Then
        mockMvc.perform(get("/replies/999")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateReplyWithBlankTitle() throws Exception {
        // Given
        Reply reply = new Reply();
        reply.setTitle("");
        reply.setReview("Review sem título");
        reply.setFeedbackId(1);
        reply.setUserId(1);

        // When/Then
        mockMvc.perform(post("/replies")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reply)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateReplyWithBlankReview() throws Exception {
        // Given
        Reply reply = new Reply();
        reply.setTitle("Título válido");
        reply.setReview("");
        reply.setFeedbackId(1);
        reply.setUserId(1);

        // When/Then
        mockMvc.perform(post("/replies")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reply)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateReplyNotFound() throws Exception {
        // Given
        Reply reply = new Reply();
        reply.setId(999);
        reply.setTitle("Teste");
        reply.setReview("Teste de atualização");
        reply.setFeedbackId(1);
        reply.setUserId(1);

        when(replyRepository.existsById(999)).thenReturn(false);

        // When/Then
        mockMvc.perform(put("/replies/999")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reply)))
                .andExpect(status().isNotFound());
    }
}
