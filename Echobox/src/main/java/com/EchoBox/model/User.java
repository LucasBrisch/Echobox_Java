package com.EchoBox.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iduser")
    private Integer idUser;

    @NotNull
    @NotBlank
    @Column(name = "emailuser", nullable = false)
    private String emailUser;

    @NotNull
    @NotBlank
    @Column(name = "passworduser", nullable = false)
    private String passwordUser;

    @Column(name = "pictureuser")
    private String pictureUser;

    @Column(name = "fk_user_idcompany")
    private Integer fkUserIdCompany;
}
