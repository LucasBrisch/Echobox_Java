package com.EchoBox.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
@Table(name = "company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @Column(name = "idcompany")
    private Integer id;

    @NotBlank
    @Column(name = "namecompany", nullable = false)
    private String name;

    @Email
    @NotBlank
    @Column(name = "emailcompany", nullable = false)
    private String email;

    @NotBlank
    @Column(name = "cnpjcompany", nullable = false, length = 14)
    private String cnpj;
}
