package com.EchoBox.model;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class Reply {
    @NotNull
    @NotBlank
    private Integer id;

    @NotNull
    @NotBlank
    private String title;

    @NotNull
    @NotBlank
    private String review;
    @NotNull
    private Integer feedbackId;

    @NotNull
    private Integer userId;

    private LocalDateTime createdDate;
}
