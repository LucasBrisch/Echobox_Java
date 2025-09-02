package com.EchoBox.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Data
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idreply")
    private Integer idReply;

    @NotNull
    @NotBlank
    @Column(name = "titlereply", nullable = false)
    private String titleReply;

    @NotNull
    @NotBlank
    @Column(name = "reviewreply", nullable = false)
    private String reviewReply;

    @NotNull
    @Column(name = "fk_reply_idfeedback", nullable = false)
    private Integer fkReplyIdFeedback;

    @NotNull
    @Column(name = "fk_reply_iduser", nullable = false)
    private Integer fkReplyIdUser;

    @Column(name = "createddate")
    private LocalDateTime createdDate;
}
