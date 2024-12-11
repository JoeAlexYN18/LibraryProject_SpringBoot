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
import pe.idat.dsfb.dcn.library.dtos.PublisherInformation;
import pe.idat.dsfb.dcn.library.dtos.PublisherValidation;
import pe.idat.dsfb.dcn.library.models.Publisher;
import pe.idat.dsfb.dcn.library.services.PublisherService;

@RestController
@RequestMapping("/api/publishers")
public class PublisherRestController {

    @Autowired
    private PublisherService publisherService;

    @PostMapping
    @Operation(
        summary = "Create a new publisher",
        description = "Creates a new publisher in the system, ensuring no publisher with the same name exists."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Publisher successfully created",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Publisher.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Validation errors in the input data",
            content = @Content(
                mediaType = "text/plain"
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = "A publisher with the same name already exists",
            content = @Content(
                mediaType = "text/plain"
            )
        )
    })
    public ResponseEntity<?> createPublisher(@Valid @RequestBody PublisherValidation publisherValidation, BindingResult bindingResult) {
        
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder("Validation errors:\n");

            for (ObjectError error: bindingResult.getAllErrors()) {
                errorMessage.append(error.getDefaultMessage()).append("\n");
            }
            
            return new ResponseEntity<>(errorMessage.toString(), HttpStatus.BAD_REQUEST);
        }

        Publisher existingPublisher = publisherService.getByName(publisherValidation.getName());
            
        if (existingPublisher != null) {
            return new ResponseEntity<>("A publisher with this name already exists.", HttpStatus.CONFLICT);
        }

        Publisher createdPublisher = publisherService.create(publisherValidation);
        HttpHeaders  headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, "/api/publishers/" + createdPublisher.getId());
        
        return new ResponseEntity<>(createdPublisher, headers, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Update a publisher",
        description = "Updates an existing publisher by ID. If the publisher does not exist, a 404 error is returned."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Publisher successfully updated",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Publisher.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Validation errors in the input data",
            content = @Content(
                mediaType = "text/plain"
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Publisher not found with the given ID",
            content = @Content(
                mediaType = "text/plain"
            )
        )
    })
    public ResponseEntity<?> updatePublisher(@PathVariable Long id, @Valid @RequestBody PublisherValidation publisherValidation, BindingResult bindingResult) {
        
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder("Validation errors:\n");

            for (ObjectError error: bindingResult.getAllErrors()) {
                errorMessage.append(error.getDefaultMessage()).append("\n");
            }

            return new ResponseEntity<>(errorMessage.toString(), HttpStatus.BAD_REQUEST);
        }

        Publisher updatedPublisher = publisherService.update(id, publisherValidation);

        if (updatedPublisher == null) {
            return new ResponseEntity<>("Publisher not found with ID: " + id, HttpStatus.NOT_FOUND);
        }
    
        HttpHeaders  headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");

        return new ResponseEntity<>(updatedPublisher, headers, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete a publisher",
        description = "Deletes an existing publisher by ID. Returns a 404 if the publisher is not found."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Publisher successfully deleted",
            content = @Content(
                mediaType = "application/json"
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Publisher not found with the given ID",
            content = @Content(
                mediaType = "application/json"
            )
        )
    })
    public ResponseEntity<?> deletePublisher(@PathVariable Long id) {

        Boolean isDeleted = publisherService.delete(id);

        if (!isDeleted) {
            return new ResponseEntity<>("Publisher not found with ID: " + id, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>("Publisher deleted successfully with ID: " + id, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get publisher details with books",
        description = "Fetches the publisher information along with the list of books associated with them by publisher ID."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Publisher information successfully retrieved",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PublisherInformation.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Publisher not found with the given ID",
            content = @Content(
                mediaType = "application/json"
            )
        )
    })
    public ResponseEntity<?> getPublisherWithBooks(@PathVariable Long id) {
        
        PublisherInformation publisherInformation = publisherService.getPublisherWithBooks(id);

        if (publisherInformation == null) {
            return new ResponseEntity<>("Publisher not found with ID: " + id, HttpStatus.NOT_FOUND);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");  
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache");

        return new ResponseEntity<>(publisherInformation, headers, HttpStatus.OK);
    }

    @GetMapping
    @Operation(
        summary = "Get all publishers with books",
        description = "Fetches all publishers along with the books associated with them."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Publishers successfully retrieved",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PublisherInformation.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "No publishers found",
            content = @Content(
                mediaType = "application/json"
            )
        )
    })
    public ResponseEntity<?> getAllPublishersWithBooks() {
        
        List<PublisherInformation> publishersInformation = publisherService.getAllPublishersWithBooks();

        if (publishersInformation.isEmpty()) {
            return new ResponseEntity<>("No publishers found", HttpStatus.NOT_FOUND);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");  
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache");

        return new ResponseEntity<>(publishersInformation, headers, HttpStatus.OK);
    }

    @GetMapping("/page")
    @Operation(
        summary = "Get publishers with filters",
        description = "Fetches a paginated list of publishers filtered by country and type."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Publishers successfully retrieved",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PublisherInformation.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "No publishers found matching the filters",
            content = @Content(
                mediaType = "application/json"
            )
        )
    })
    public ResponseEntity<?> getAllPublishersWithFilters(
            @RequestParam(defaultValue = "") String country,
            @RequestParam(defaultValue = "") String type,
            @RequestParam(defaultValue = "0") int page, 
            @RequestParam(defaultValue = "10") int size) {

        Page<PublisherInformation> publishersInformation = publisherService.getAllPublishersWithFilters(country, type, page, size);
        
        if (publishersInformation.isEmpty()) {
            return new ResponseEntity<>("No publishers found", HttpStatus.NOT_FOUND);
        }
        
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");  
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache");

        return new ResponseEntity<>(publishersInformation, headers, HttpStatus.OK);     
    }
}
