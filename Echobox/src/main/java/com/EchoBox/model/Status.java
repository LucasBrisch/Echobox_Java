package com.EchoBox.model;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

@Data
public class Status {
    @NotNull
    @NotBlank
    private Integer id;

    @NotNull
    @NotBlank
    private String type;

    private String color;
}
