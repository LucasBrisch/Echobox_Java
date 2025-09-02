package com.EchoBox.model;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

@Data
public class User {
    @NotNull
    @NotBlank
    private Integer id;

    @NotNull
    @NotBlank
    private String email;

    @NotNull
    @NotBlank
    private String password;

    private String picture;

    private Integer companyId;
}
