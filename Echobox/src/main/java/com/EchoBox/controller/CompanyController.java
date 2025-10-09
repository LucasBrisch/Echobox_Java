package com.EchoBox.controller;

import com.EchoBox.exception.ErrorCode;
import com.EchoBox.exception.ResourceNotFoundException;
import com.EchoBox.model.Company;
import com.EchoBox.repository.CompanyRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // ############### POST OPERATION ###############

    @PostMapping
    @Operation(summary = "Creates a new company", description = "Creates a new company with auto-incremented ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<Company> save(@Valid @RequestBody Company company) {
        Company savedCompany = companyRepository.save(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCompany);
    }

    // ############### GET ALL OPERATION ###############

    @GetMapping
    @Operation(summary = "Gets a list of companies", description = "Retrieves all companies from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Company list retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Failed to retrieve company list")
    })
    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    // ############### DELETE OPERATION ###############

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes a company", description = "Deletes the company with the specified ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Company not found")
    })
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        if (!companyRepository.existsById(id)) {
            throw new ResourceNotFoundException(ErrorCode.COMPANY_NOT_FOUND);
        }
        companyRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ############### PUT OPERATION ###############

    @PutMapping("/{id}")
    @Operation(summary = "Updates a company", description = "Updates the company with the specified ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated successfully"),
            @ApiResponse(responseCode = "404", description = "Company not found")
    })
    public ResponseEntity<Company> update(@PathVariable("id") Integer id, @RequestBody Company company) {
        if (!companyRepository.existsById(id)) {
            throw new ResourceNotFoundException(ErrorCode.COMPANY_NOT_FOUND);
        }
        company.setId(id);
        Company updatedCompany = companyRepository.save(company);
        return ResponseEntity.ok(updatedCompany);
    }

    // ############### GET BY ID OPERATION ###############

    @GetMapping("/{id}")
    @Operation(summary = "Gets a company by ID", description = "Retrieves the company with the specified ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Company retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Company not found")
    })
    public ResponseEntity<Company> findById(@PathVariable("id") Integer id) {
        return companyRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.COMPANY_NOT_FOUND));
    }
}
