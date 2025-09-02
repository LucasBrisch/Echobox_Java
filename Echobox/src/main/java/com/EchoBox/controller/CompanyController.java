package com.EchoBox.controller;

import com.EchoBox.model.Company;
import com.EchoBox.repository.CompanyRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/company")
@Tag(name = "Company", description = "APIs for managing companies")
public class CompanyController {

    @Autowired
    private CompanyRepository companyRepository;

    @PostMapping
    public ResponseEntity<Company> save(@Valid @RequestBody Company company) {
        Company savedCompany = companyRepository.save(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCompany);
    }

    @GetMapping
    @Operation(summary = "Get the list of companies", description = "Returns the list of companies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
    })
    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Company> update(@PathVariable("id") Integer id, @RequestBody Company company) {
        if (!companyRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        company.setIdCompany(id);
        Company updatedCompany = companyRepository.save(company);
        return ResponseEntity.ok(updatedCompany);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        if (!companyRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        companyRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
