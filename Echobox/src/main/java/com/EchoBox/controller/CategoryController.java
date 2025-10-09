package com.EchoBox.controller;

import com.EchoBox.exception.ErrorCode;
import com.EchoBox.exception.ResourceNotFoundException;
import com.EchoBox.model.Category;
import com.EchoBox.repository.CategoryRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// ############### CRUD OPERATIONS ###############

@RestController
@RequestMapping("/categories")
@Tag(name = "Category", description = "API endpoints for category management")
public class CategoryController {

    // ############### CATEGORY CONSTRUCTOR ###############

    // You can just use @Autowired here, but it's bad practice
    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // ############### POST OPERATION ###############

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Creates a new category", description = "Creates a new category with auto-incremented ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })

    // @RequestBody converts JSON to Java object
    // @Valid triggers the rules set in the model, on its own it does nothing
    // The .save() method takes the json object, creates an insert statement and executes it
    // The pipeline for the save() function is:
    // json object (idless) -> java object (idless) -> into the database (generates id) ->
    // -> (updates the java object with the id) -> java object (w/ id) -> json object (w/ id) -> response

    public ResponseEntity<Category> save(@Valid @RequestBody Category category) {
        Category savedCategory = categoryRepository.save(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
    }

    // ############### GET ALL OPERATION ###############

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Gets a list of categories", description = "Retrieves all categories from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category list retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Failed to retrieve category list")
    })

    // This is just a built-in function that does a "SELECT * FROM category"
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    // ############### DELETE OPERATION ###############

    // This is all built-in functionality, don't worry about it, void is used because the object's body is empty
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deletes a category", description = "Deletes the category with the specified ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException(ErrorCode.CATEGORY_NOT_FOUND);
        }
        categoryRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ############### PUT OPERATION ###############

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Updates a category", description = "Updates the category with the specified ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseEntity<Category> update(@PathVariable("id") Integer id, @RequestBody Category category) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException(ErrorCode.CATEGORY_NOT_FOUND);
        }
        category.setId(id);
        Category updatedCategory = categoryRepository.save(category);
        return ResponseEntity.ok(updatedCategory);
    }

    // ############### GET BY ID OPERATION ###############

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Gets a category by ID", description = "Retrieves the category with the specified ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseEntity<Category> findById(@PathVariable("id") Integer id) {
        return categoryRepository.findById(id) // The findById method functions like an if statement
                .map(ResponseEntity::ok) // This is the method reference for a lambda function
                .orElse(ResponseEntity.notFound().build());
    }

}