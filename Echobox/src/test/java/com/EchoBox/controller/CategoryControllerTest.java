package com.EchoBox.controller;

import com.EchoBox.model.Category;
import com.EchoBox.repository.CategoryRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;


@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryRepository categoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllCategories() throws Exception {
        //Given
        Category category = new Category();
        category.setId(1);
        category.setType("Tecnologia");
        category.setColor("#dedede");

        List<Category> categories = new ArrayList<>();
        categories.add(category);

        when(categoryRepository.findAll()).thenReturn(categories);

        //When/Then
        mockMvc.perform(get("/categories")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].type").value("Tecnologia"))
                .andExpect(jsonPath("$.[0].color").value("#dedede"));
    }

    @Test
    public void testGetCategoryById() throws Exception {
        //Given
        Category category = new Category();
        category.setId(1);
        category.setType("Tecnologia");
        category.setColor("#dedede");

        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));

        //When/Then
        mockMvc.perform(get("/categories/1")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("Tecnologia"))
                .andExpect(jsonPath("$.color").value("#dedede"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testCreateCategory() throws Exception {
        //Given
        Category category = new Category();
        category.setType("Esportes");
        category.setColor("#ff0000");

        Category savedCategory = new Category();
        savedCategory.setId(1);
        savedCategory.setType("Esportes");
        savedCategory.setColor("#ff0000");

        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

        //When/Then
        mockMvc.perform(post("/categories")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.type").value("Esportes"))
                .andExpect(jsonPath("$.color").value("#ff0000"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testUpdateCategory() throws Exception {
        //Given
        Category category = new Category();
        category.setId(1);
        category.setType("Tecnologia Atualizada");
        category.setColor("#00ff00");

        when(categoryRepository.existsById(1)).thenReturn(true);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        //When/Then
        mockMvc.perform(put("/categories/1")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("Tecnologia Atualizada"))
                .andExpect(jsonPath("$.color").value("#00ff00"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testDeleteCategory() throws Exception {
        //Given
        when(categoryRepository.existsById(1)).thenReturn(true);

        //When/Then
        mockMvc.perform(delete("/categories/1")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetCategoryByIdNotFound() throws Exception {
        //Given
        when(categoryRepository.findById(999)).thenReturn(Optional.empty());

        //When/Then
        mockMvc.perform(get("/categories/999")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isNotFound());
    }
}
