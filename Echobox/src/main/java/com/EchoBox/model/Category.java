package com.EchoBox.model;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;


@Data
public class Category {
    @NotNull
    @NotBlank
    private Integer id;
    @NotNull
    @NotBlank
    private String type;
    @NotNull
    @NotBlank
    private String color;
}
