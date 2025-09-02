package com.EchoBox.model;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

@Data
public class Feedback {
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
    private Integer userId;

    @NotNull
    private Integer companyId;

    @NotNull
    private Integer categoryId;

    @NotNull
    private Integer statusId;
}
