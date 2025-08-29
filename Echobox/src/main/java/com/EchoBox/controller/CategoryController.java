package com.EchoBox.controller;

import com.EchoBox.model.Category;
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
@RequestMapping("/category")
@Tag(name = "Category", description = "APIs de gerenciamento de categorias")
public class CategoryController {

    private List<Category> categories = new ArrayList<>();

    @PostMapping
    public ResponseEntity<Category> save( @Valid @RequestBody Category Category) {
        Category.setId(1);
        categories.add(Category);
        return ResponseEntity.status(HttpStatus.CREATED).body(Category);
    }

    @GetMapping
    @Operation(summary = "Obter a lista de categorias", description = "Retorna a lista de categorias")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recuperado com sucesso"),
    })
    public List<Category> findAll() {
        return categories;
    }

    @PutMapping("/{id}")
    public Category update(@PathVariable("id") Integer id, @RequestBody Category Category) {
        Category.setId(id);
        return Category;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
    }

}
