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
    @Column(name = "idreply")
    private Integer id;

    @NotBlank
    @Column(name = "titlereply", nullable = false)
    private String title;

    @NotBlank
    @Column(name = "reviewreply", nullable = false, columnDefinition = "TEXT")
    private String review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_reply_idfeedback", nullable = false)
    private Feedback feedback;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_reply_iduser", nullable = false)
    private User user;

    @CreationTimestamp // You don't need to set this, but it's good for hibernate and I want to try it
    @Column(name = "createddate")
    private LocalDateTime createdDate;

}
