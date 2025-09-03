package com.EchoBox.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
@Table(name = "category")
public class Category {

    @Id
    // In theory GenerationType.AUTO should work, but it doesn't, so we use IDENTITY
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY) // We need to make the ids read-only for auto-increment
    @Column(name = "idcategory")
    private Integer id;

    @NotBlank
    @Column(name = "typecategory", nullable = false)
    private String type;

    @NotBlank
    @Column(name = "colorcategory")
    private String color;
}
