package com.EchoBox.model;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.*;

@Entity
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcategory")
    private Integer idCategory;

    @Column(name = "typecategory", nullable = false)
    private String typeCategory;

    @Column(name = "colorcategory")
    private String colorCategory;
}
