package com.EchoBox.model;

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
    @JoinColumn(name = "fk_reply_idFeedback", nullable = false)
    private Feedback feedback;

    @ManyToOne
    @JoinColumn(name = "fk_reply_idUser", nullable = false)
    private User user;

    @CreationTimestamp
    @Column(name = "createdDate")
    private LocalDateTime createdDate;

}
