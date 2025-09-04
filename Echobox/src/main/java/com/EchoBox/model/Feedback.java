package com.EchoBox.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
@Table(name = "feedback")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @Column(name = "idFeedback")
    private Integer id;

    @NotBlank
    @Column(name = "titleFeedback", nullable = false)
    private String title;

    @NotBlank
    @Column(name = "reviewFeedback", nullable = false, columnDefinition = "TEXT")
    private String review;

    @ManyToOne
    @JoinColumn(name = "fk_feedback_idUser", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "fk_feedback_idCompany", nullable = false)
    private Company company;

    @ManyToOne
    @JoinColumn(name = "fk_feedback_idCategory", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "fk_feedback_idStatus", nullable = false)
    private Status status;
}
