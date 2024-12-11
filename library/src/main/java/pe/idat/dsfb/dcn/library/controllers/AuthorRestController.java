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
import pe.idat.dsfb.dcn.library.dtos.AuthorValidation;
import pe.idat.dsfb.dcn.library.dtos.AuthorInformation;
import pe.idat.dsfb.dcn.library.models.Author;
import pe.idat.dsfb.dcn.library.services.AuthorService;

@RestController
@RequestMapping("/api/authors")
public class AuthorRestController {

    @Autowired
    private AuthorService authorService;

    @PostMapping
    @Operation(
        summary = "Create a new author",
        description = "Creates a new author in the system. The request must include author details, and if successful, returns the created author along with a location header pointing to the created resource."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Author successfully created",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Author.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Validation errors in the provided request data",
            content = @Content(mediaType = "text/plain")
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Conflict: An author with the same name already exists",
            content = @Content(mediaType = "text/plain")
        )
    })
    public ResponseEntity<?> createAuthor(@Valid @RequestBody AuthorValidation authorValidation, BindingResult bindingResult) {
        
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder("Validation errors\n");

            for (ObjectError error: bindingResult.getAllErrors()) {
                errorMessage.append(error.getDefaultMessage()).append("\n");
            }
            
            return new ResponseEntity<>(errorMessage.toString(), HttpStatus.BAD_REQUEST);
        }

        Author existingAuthor = authorService.getByName(authorValidation.getName());
            
        if (existingAuthor != null) {
            return new ResponseEntity<>("An author with this name already exists.", HttpStatus.CONFLICT);
        }

        Author createdAuthor = authorService.create(authorValidation);
        HttpHeaders  headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, "/api/authors/" + createdAuthor.getId());

        return new ResponseEntity<>(createdAuthor, headers, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Update an existing author",
        description = "Updates the details of an existing author based on the provided ID. The request must include updated author details. If the author is found, the updated information is returned, otherwise, a 404 error is returned."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Author successfully updated",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Author.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Validation errors in the provided request data",
            content = @Content(mediaType = "text/plain")
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Author not found with the provided ID",
            content = @Content(mediaType = "text/plain")
        )
    })
    public ResponseEntity<?> updateAuthor(@PathVariable Long id, @Valid @RequestBody AuthorValidation authorValidation, BindingResult bindingResult) {
        
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder("Validation errors:\n");

            for (ObjectError error: bindingResult.getAllErrors()) {
                errorMessage.append(error.getDefaultMessage()).append("\n");
            }

            return new ResponseEntity<>(errorMessage.toString(), HttpStatus.BAD_REQUEST);
        }
        
        Author response = authorService.update(id, authorValidation);

        if (response == null) {
            return new ResponseEntity<>("Author not found with ID: " + id, HttpStatus.NOT_FOUND);
        }
    
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");

        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete an author by ID",
        description = "Deletes an author from the system based on the provided ID. If the author is found, it is removed from the database. If the author is not found, a 404 error is returned."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Author successfully deleted",
            content = @Content(mediaType = "text/plain")
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Author not found with the provided ID",
            content = @Content(mediaType = "text/plain")
        )
    })
    public ResponseEntity<?> deleteAuthor(@PathVariable Long id) {

        Boolean isDeleted = authorService.delete(id);

        if (!isDeleted) {
            return new ResponseEntity<>("Author not found with ID: " + id, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>("Author deleted successfully with ID: " + id, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get author details with books",
        description = "Retrieves detailed information about an author along with the list of books they have authored. Returns a 404 error if the author is not found."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Author details with books successfully retrieved",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthorInformation.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Author not found with the provided ID",
            content = @Content(mediaType = "text/plain")
        )
    })
    public ResponseEntity<?> getAuthorWithBooks(@PathVariable Long id) {
        
        AuthorInformation authorInformation = authorService.getAuthorWithBooks(id);

        if (authorInformation == null) {
            return new ResponseEntity<>("Author not found with ID: " + id, HttpStatus.NOT_FOUND);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");  
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache");

        return new ResponseEntity<>(authorInformation, headers, HttpStatus.OK);
    }

    @GetMapping
    @Operation(
        summary = "Get all authors with their books",
        description = "Retrieves a list of all authors along with the books they have authored. Returns a 404 error if no authors are found."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Authors and their books successfully retrieved",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthorInformation.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "No authors found",
            content = @Content(mediaType = "text/plain")
        )
    })
    public ResponseEntity<?> getAllAuthorsWithBooks() {
        
        List<AuthorInformation> authorsInformation = authorService.getAllAuthorsWithBooks();

        if (authorsInformation.isEmpty()) {
            return new ResponseEntity<>("No authors found", HttpStatus.NOT_FOUND);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");  
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache");

        return new ResponseEntity<>(authorsInformation, headers, HttpStatus.OK);
    }

    @GetMapping("/page")
    @Operation(
        summary = "Get all authors with filters and pagination",
        description = "Retrieves a paginated list of authors filtered by name and nationality. Returns a 404 error if no authors are found."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Authors successfully retrieved with applied filters and pagination",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthorInformation.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "No authors found with the provided filters",
            content = @Content(mediaType = "text/plain")
        )
    })
    public ResponseEntity<?> getAllAuthorsWithFilters(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "") String nationality,
            @RequestParam(defaultValue = "0") int page, 
            @RequestParam(defaultValue = "10") int size) {

        Page<AuthorInformation> authorsInformation = authorService.getAllAuthorsWithFilters(name, nationality, page, size);
        
        if (authorsInformation.isEmpty()) {
            return new ResponseEntity<>("No authors found", HttpStatus.NOT_FOUND);
        }
        
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");  
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache");

        return new ResponseEntity<>(authorsInformation, headers, HttpStatus.OK);      
    }
}
