package com.EchoBox.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "reply")
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @Column(name = "idReply")
    private Integer id;

    @NotBlank
    @Column(name = "titleReply", nullable = false)
    private String title;

    @NotBlank
    @Column(name = "reviewReply", nullable = false, columnDefinition = "TEXT")
    private String review;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "fk_reply_idFeedback", nullable = false)
    private Feedback feedback;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "fk_reply_idUser", nullable = false)
    private User user;

    @CreationTimestamp
    @Column(name = "createdDate")
    private LocalDateTime createdDate;

    // ############### GETTERS AND SETTERS FOR FOREIGN KEYS ###############

    @JsonProperty("feedback")
    public Integer getFeedbackId() {
        return feedback != null ? feedback.getId() : null;
    }

    @JsonProperty("feedback")
    public void setFeedbackId(Integer feedbackId) {
        this.feedback = new Feedback();
        this.feedback.setId(feedbackId);
    }

    @JsonProperty("user")
    public Integer getUserId() {
        return user != null ? user.getId() : null;
    }

    @JsonProperty("user")
    public void setUserId(Integer userId) {
        this.user = new User();
        this.user.setId(userId);
    }

}
