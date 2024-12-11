package pe.idat.dsfb.dcn.library.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import pe.idat.dsfb.dcn.library.dtos.CategoryInformation;
import pe.idat.dsfb.dcn.library.dtos.CategoryValidation;
import pe.idat.dsfb.dcn.library.models.Category;
import pe.idat.dsfb.dcn.library.services.CategoryService;


@RestController
@RequestMapping("/api/categories")
public class CategoryRestController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    @Operation(
        summary = "Create a new category",
        description = "Creates a new category and returns the created category with a 201 status code. If the category already exists, a 409 conflict status code is returned."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Category created successfully",
            content = @Content(mediaType = "application/json", 
                               schema = @Schema(implementation = Category.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Validation errors in the provided category data",
            content = @Content(mediaType = "text/plain")
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Category with the same name already exists",
            content = @Content(mediaType = "text/plain")
        )
    })
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryValidation categoryValidation, BindingResult bindingResult) {
        
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder("Validation errors:\n");

            for (ObjectError error: bindingResult.getAllErrors()) {
                errorMessage.append(error.getDefaultMessage()).append("\n");
            }
            
            return new ResponseEntity<>(errorMessage.toString(), HttpStatus.BAD_REQUEST);
        }

        Category existingCategory = categoryService.getByName(categoryValidation.getName());
            
        if (existingCategory != null) {
            return new ResponseEntity<>("An category with this name already exists.", HttpStatus.CONFLICT);
        }

        Category createdCategory = categoryService.create(categoryValidation);
        HttpHeaders  headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, "/api/categories/" + createdCategory.getId());

        return new ResponseEntity<>(createdCategory, headers, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Update an existing category",
        description = "Updates an existing category by ID. Returns the updated category or a 404 error if not found."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Category updated successfully",
            content = @Content(mediaType = "application/json", 
                               schema = @Schema(implementation = Category.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Validation errors in the provided category data",
            content = @Content(mediaType = "text/plain")
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Category not found with the provided ID",
            content = @Content(mediaType = "text/plain")
        )
    })
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryValidation categoryValidation, BindingResult bindingResult) {
        
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder("Validation errors:\n");

            for (ObjectError error: bindingResult.getAllErrors()) {
                errorMessage.append(error.getDefaultMessage()).append("\n");
            }

            return new ResponseEntity<>(errorMessage.toString(), HttpStatus.BAD_REQUEST);
        }

        Category response = categoryService.update(id, categoryValidation);

        if (response == null) {
            return new ResponseEntity<>("Category not found with ID: " + id, HttpStatus.NOT_FOUND);
        }
    
        HttpHeaders  headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");

        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete a category",
        description = "Deletes an existing category by ID. Returns a success message or a 404 error if the category is not found."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Category deleted successfully",
            content = @Content(mediaType = "text/plain")
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Category not found with the provided ID",
            content = @Content(mediaType = "text/plain")
        )
    })
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {

        Boolean isDeleted = categoryService.delete(id);

        if (!isDeleted) {
            return new ResponseEntity<>("Category not found with ID: " + id, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>("Category deleted successfully with ID: " + id, HttpStatus.NO_CONTENT);
    }


    @GetMapping("/{id}")
    @Operation(
        summary = "Get category details with associated books",
        description = "Retrieve a category and its associated books using the category's ID. Returns a 404 error if the category is not found."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Category found successfully with its books",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CategoryInformation.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Category not found with the provided ID",
            content = @Content(
                mediaType = "text/plain"
            )
        )
    })
    public ResponseEntity<?> getCategoryWithBooks(@PathVariable Long id) {
        
        CategoryInformation categoryInformation = categoryService.getCategoryWithBooks(id);

        if (categoryInformation == null) {
            return new ResponseEntity<>("Category not found with ID: " + id, HttpStatus.NOT_FOUND);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");  
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache");

        return new ResponseEntity<>(categoryInformation, headers, HttpStatus.OK);
    }


    @GetMapping
    @Operation(
        summary = "Get all categories with their associated books",
        description = "Retrieves a list of all categories, along with the books associated with each category."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved the list of categories with their books",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CategoryInformation.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "No categories found",
            content = @Content(
                mediaType = "text/plain"
            )
        )
    })
    public ResponseEntity<?> getAllCategoriesWithBooks() {
        
        List<CategoryInformation> categoriesInformation = categoryService.getAllCategoriesWithBooks();

        if (categoriesInformation.isEmpty()) {
            return new ResponseEntity<>("No categories found", HttpStatus.NOT_FOUND);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");  
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache");

        return new ResponseEntity<>(categoriesInformation, headers, HttpStatus.OK);
    }

    @GetMapping("/page")
    @Operation(
        summary = "Get all categories with filters and pagination",
        description = "Retrieves a paginated list of categories, optionally filtered by description."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved the list of categories with applied filters",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CategoryInformation.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "No categories found matching the filters",
            content = @Content(
                mediaType = "text/plain"
            )
        )
    })
    public ResponseEntity<?> getAllCategoriesWithFilters(
            @RequestParam(defaultValue = "") String description,
            @RequestParam(defaultValue = "0") int page, 
            @RequestParam(defaultValue = "10") int size) {

        Page<CategoryInformation> categoriesInformation = categoryService.getAllCategoriesWithFilters(description, page, size);
        
        if (categoriesInformation.isEmpty()) {
            return new ResponseEntity<>("No categories found", HttpStatus.NOT_FOUND);
        }
        
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");  
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache");

        return new ResponseEntity<>(categoriesInformation, headers, HttpStatus.OK);    
    }
}
