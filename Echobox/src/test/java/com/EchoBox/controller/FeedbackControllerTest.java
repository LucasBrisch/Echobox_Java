package com.EchoBox.controller;

import com.EchoBox.model.Feedback;
import com.EchoBox.repository.FeedbackRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@SpringBootTest
@AutoConfigureMockMvc
public class FeedbackControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FeedbackRepository feedbackRepository;

    @Test
    public void testGetAllFeedbacks() throws Exception {
        // Given
        Feedback feedback = new Feedback();
        feedback.setId(1);
        feedback.setTitle("Test Feedback");
        feedback.setReview("This is a test review");

        List<Feedback> feedbacks = new ArrayList<>();
        feedbacks.add(feedback);

        when(feedbackRepository.findAll()).thenReturn(feedbacks);

        // When/Then
        mockMvc.perform(get("/feedbacks")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].title").value("Test Feedback"))
                .andExpect(jsonPath("$.[0].review").value("This is a test review"));
    }

    @Test
    public void testGetFeedbackById() throws Exception {
        // Given
        Feedback feedback = new Feedback();
        feedback.setId(1);
        feedback.setTitle("Test Feedback");
        feedback.setReview("This is a test review");

        when(feedbackRepository.findById(1)).thenReturn(Optional.of(feedback));

        // When/Then
        mockMvc.perform(get("/feedbacks/1")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Feedback"))
                .andExpect(jsonPath("$.review").value("This is a test review"));
    }

    @Test
    public void testGetFeedbackByIdNotFound() throws Exception {
        // Given
        when(feedbackRepository.findById(999)).thenReturn(Optional.empty());

        // When/Then
        mockMvc.perform(get("/feedbacks/999")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateFeedback() throws Exception {
        // Given
        Feedback feedback = new Feedback();
        feedback.setId(1);
        feedback.setTitle("New Feedback");
        feedback.setReview("New review");

        when(feedbackRepository.save(any(Feedback.class))).thenReturn(feedback);

        // When/Then
        mockMvc.perform(post("/feedbacks")
                        .with(user("admin").roles("ADMIN"))
                        .contentType("application/json")
                        .content("{\"title\":\"New Feedback\",\"review\":\"New review\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Feedback"))
                .andExpect(jsonPath("$.review").value("New review"));
    }

    @Test
    public void testUpdateFeedback() throws Exception {
        // Given
        Feedback updatedFeedback = new Feedback();
        updatedFeedback.setId(1);
        updatedFeedback.setTitle("Updated Feedback");
        updatedFeedback.setReview("Updated review");

        when(feedbackRepository.existsById(1)).thenReturn(true);
        when(feedbackRepository.save(any(Feedback.class))).thenReturn(updatedFeedback);

        // When/Then
        mockMvc.perform(put("/feedbacks/1")
                        .with(user("admin").roles("ADMIN"))
                        .contentType("application/json")
                        .content("{\"title\":\"Updated Feedback\",\"review\":\"Updated review\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Feedback"))
                .andExpect(jsonPath("$.review").value("Updated review"));
    }

    @Test
    public void testUpdateFeedbackNotFound() throws Exception {
        // Given
        when(feedbackRepository.existsById(999)).thenReturn(false);

        // When/Then
        mockMvc.perform(put("/feedbacks/999")
                        .with(user("admin").roles("ADMIN"))
                        .contentType("application/json")
                        .content("{\"title\":\"Updated Feedback\",\"review\":\"Updated review\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteFeedback() throws Exception {
        // Given
        when(feedbackRepository.existsById(1)).thenReturn(true);
        doNothing().when(feedbackRepository).deleteById(1);

        // When/Then
        mockMvc.perform(delete("/feedbacks/1")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteFeedbackNotFound() throws Exception {
        // Given
        when(feedbackRepository.existsById(999)).thenReturn(false);

        // When/Then
        mockMvc.perform(delete("/feedbacks/999")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isNotFound());
    }
}
