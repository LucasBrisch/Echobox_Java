package com.EchoBox.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
@Table(name = "\"user\"")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @Column(name = "idUser")
    private Integer id;

    @NotBlank
    @Column(name = "emailUser", nullable = false)
    private String email;

    @NotBlank
    @Column(name = "passwordUser", nullable = false, length = 64)
    private String password;

    @Column(name = "pictureUser")
    private String picture;

    @ManyToOne
    @JoinColumn(name = "fk_user_idCompany")
    private Company company;
}
