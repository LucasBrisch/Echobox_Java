package com.EchoBox.controller;

import com.EchoBox.model.Company;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/company")
@Tag(name = "Company", description = "APIs for managing companies")
public class CompanyController {

    private List<Company> companies = new ArrayList<>();

    @PostMapping
    public ResponseEntity<Company> save(@Valid @RequestBody Company company) {
        company.setId(1);
        companies.add(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(company);
    }

    @GetMapping
    @Operation(summary = "Get the list of companies", description = "Returns the list of companies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
    })
    public List<Company> findAll() {
        return companies;
    }

    @PutMapping("/{id}")
    public Company update(@PathVariable("id") Integer id, @RequestBody Company company) {
        company.setId(id);
        return company;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
    }
}
