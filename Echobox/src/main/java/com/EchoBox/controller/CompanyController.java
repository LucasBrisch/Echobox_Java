package com.EchoBox.controller;

import com.EchoBox.model.Category;
import com.EchoBox.model.Company;
import com.EchoBox.repository.CategoryRepository;
import com.EchoBox.repository.CompanyRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// ############### CRUD OPERATIONS ###############

@RestController
@RequestMapping("/companies")
@Tag(name = "Company", description = "API endpoints for company management")
public class CompanyController {

    // ############### COMPANY CONSTRUCTOR ###############

    private final CompanyRepository companyRepository;

    public CompanyController(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    // ############### GET ALL OPERATION ###############

    @GetMapping
    @Operation(summary = "Gets a list of companies", description = "Retrieves all companies from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Company list retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Failed to retrieve company list")
    })

    // This is just a built-in function that does a "SELECT * FROM category"
    public List<Company> findAll() {
        return companyRepository.findAll();
    }
}
