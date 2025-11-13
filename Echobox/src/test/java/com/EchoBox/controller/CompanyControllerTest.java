package com.EchoBox.controller;

import com.EchoBox.model.Company;
import com.EchoBox.repository.CompanyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CompanyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CompanyRepository companyRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllCompanies() throws Exception {
        // Given
        Company company = new Company();
        company.setId(1);
        company.setName("Tech Solutions");
        company.setEmail("contact@techsolutions.com");
        company.setCnpj("12345678000199");

        List<Company> companies = new ArrayList<>();
        companies.add(company);

        when(companyRepository.findAll()).thenReturn(companies);

        // When/Then
        mockMvc.perform(get("/companies")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value("Tech Solutions"))
                .andExpect(jsonPath("$.[0].email").value("contact@techsolutions.com"))
                .andExpect(jsonPath("$.[0].cnpj").value("12345678000199"))
                .andExpect(jsonPath("$.[0].id").value(1));
    }

    @Test
    public void testGetCompanyById() throws Exception {
        // Given
        Company company = new Company();
        company.setId(1);
        company.setName("Tech Solutions");
        company.setEmail("contact@techsolutions.com");
        company.setCnpj("12345678000199");

        when(companyRepository.findById(1)).thenReturn(Optional.of(company));

        // When/Then
        mockMvc.perform(get("/companies/1")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Tech Solutions"))
                .andExpect(jsonPath("$.email").value("contact@techsolutions.com"))
                .andExpect(jsonPath("$.cnpj").value("12345678000199"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testCreateCompany() throws Exception {
        // Given
        Company company = new Company();
        company.setName("New Company");
        company.setEmail("info@newcompany.com");
        company.setCnpj("98765432000188");

        Company savedCompany = new Company();
        savedCompany.setId(1);
        savedCompany.setName("New Company");
        savedCompany.setEmail("info@newcompany.com");
        savedCompany.setCnpj("98765432000188");

        when(companyRepository.save(any(Company.class))).thenReturn(savedCompany);

        // When/Then
        mockMvc.perform(post("/companies")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(company)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Company"))
                .andExpect(jsonPath("$.email").value("info@newcompany.com"))
                .andExpect(jsonPath("$.cnpj").value("98765432000188"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testUpdateCompany() throws Exception {
        // Given
        Company company = new Company();
        company.setId(1);
        company.setName("Updated Company");
        company.setEmail("updated@company.com");
        company.setCnpj("11122233000144");

        when(companyRepository.existsById(1)).thenReturn(true);
        when(companyRepository.save(any(Company.class))).thenReturn(company);

        // When/Then
        mockMvc.perform(put("/companies/1")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(company)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Company"))
                .andExpect(jsonPath("$.email").value("updated@company.com"))
                .andExpect(jsonPath("$.cnpj").value("11122233000144"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testDeleteCompany() throws Exception {
        // Given
        when(companyRepository.existsById(1)).thenReturn(true);

        // When/Then
        mockMvc.perform(delete("/companies/1")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetCompanyByIdNotFound() throws Exception {
        // Given
        when(companyRepository.findById(999)).thenReturn(Optional.empty());

        // When/Then
        mockMvc.perform(get("/companies/999")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateCompanyWithInvalidEmail() throws Exception {
        // Given
        Company company = new Company();
        company.setName("Invalid Email Company");
        company.setEmail("invalid-email");
        company.setCnpj("12345678000199");

        // When/Then
        mockMvc.perform(post("/companies")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(company)))
                .andExpect(status().isBadRequest());
    }
}
