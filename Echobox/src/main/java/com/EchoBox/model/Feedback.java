package com.EchoBox.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
@Table(name = "feedback")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @Column(name = "idFeedback")
    private Integer id;

    @NotBlank
    @Column(name = "titleFeedback", nullable = false)
    private String title;

    @NotBlank
    @Column(name = "reviewFeedback", nullable = false, columnDefinition = "TEXT")
    private String review;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "fk_feedback_idUser", nullable = false)
    private User user;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "fk_feedback_idCompany", nullable = false)
    private Company company;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "fk_feedback_idCategory", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "fk_feedback_idStatus", nullable = false)
    @JsonIgnore
    private Status status;

    // ############### GETTERS AND SETTERS FOR FOREIGN KEYS ###############

    @JsonProperty("user")
    public Integer getUserId() {
        return user != null ? user.getId() : null;
    }

    @JsonProperty("user")
    public void setUserId(Integer userId) {
        this.user = new User();
        this.user.setId(userId);
    }

    @JsonProperty("company")
    public Integer getCompanyId() {
        return company != null ? company.getId() : null;
    }

    @JsonProperty("company")
    public void setCompanyId(Integer companyId) {
        this.company = new Company();
        this.company.setId(companyId);
    }

    @JsonProperty("category")
    public Integer getCategoryId() {
        return category != null ? category.getId() : null;
    }

    @JsonProperty("category")
    public void setCategoryId(Integer categoryId) {
        this.category = new Category();
        this.category.setId(categoryId);
    }

    @JsonProperty("status")
    public Integer getStatusId() {
        return status != null ? status.getId() : null;
    }

    @JsonProperty("status")
    public void setStatusId(Integer statusId) {
        this.status = new Status();
        this.status.setId(statusId);
    }
}
