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
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idstatus")
    private Integer idStatus;

    @NotNull
    @NotBlank
    @Column(name = "typestatus", nullable = false)
    private String typeStatus;

    @Column(name = "colorstatus")
    private String colorStatus;
}
