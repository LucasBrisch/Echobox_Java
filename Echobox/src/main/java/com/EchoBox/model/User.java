package com.EchoBox.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @Column(name = "iduser")
    private Integer id;

    @NotBlank
    @Column(name = "emailuser", nullable = false)
    private String email;

    @NotBlank
    @Column(name = "passworduser", nullable = false, length = 64)
    private String password;

    @Column(name = "pictureuser")
    private String picture;

    // dont need lazy fetch but it helps
    @ManyToOne(fetch = FetchType.LAZY) // Many to one means many users can belong to one company
    @JoinColumn(name = "fk_user_idcompany") // Need to use @JoinColumn because Company will be a Java object
    private Company company;
}
