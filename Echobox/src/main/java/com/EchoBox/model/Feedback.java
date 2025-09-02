package com.EchoBox.model;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.*;

@Entity
@Data
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idfeedback")
    private Integer idFeedback;

    @NotNull
    @NotBlank
    @Column(name = "titlefeedback", nullable = false)
    private String titleFeedback;

    @NotNull
    @NotBlank
    @Column(name = "reviewfeedback", nullable = false)
    private String reviewFeedback;

    @NotNull
    @Column(name = "fk_feedback_iduser", nullable = false)
    private Integer fkFeedbackIdUser;

    @NotNull
    @Column(name = "fk_feedback_idcompany", nullable = false)
    private Integer fkFeedbackIdCompany;

    @NotNull
    @Column(name = "fk_feedback_idcategory", nullable = false)
    private Integer fkFeedbackIdCategory;

    @NotNull
    @Column(name = "fk_feedback_idstatus", nullable = false)
    private Integer fkFeedbackIdStatus;
}
