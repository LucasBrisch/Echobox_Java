package com.EchoBox.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @Column(name = "isAdminUser", nullable = false)
    private Boolean isAdmin = false;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "fk_user_idCompany")
    private Company company;

    // ############### GETTERS AND SETTERS FOR FOREIGN KEYS ###############

    @JsonProperty("company")
    public Integer getCompanyId() {
        return company != null ? company.getId() : null;
    }

    @JsonProperty("company")
    public void setCompanyId(Integer companyId) {
        this.company = new Company();
        this.company.setId(companyId);
    }
}
