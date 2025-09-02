package com.EchoBox.model;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.*;

@Entity
@Data
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCompany;

    @NotNull
    @NotBlank
    @Column(nullable = false)
    private String nameCompany;

    @NotNull
    @NotBlank
    @Column(nullable = false)
    private String emailCompany;

    @NotNull
    @NotBlank
    @Column(nullable = false)
    private String cnpjCompany;
}
