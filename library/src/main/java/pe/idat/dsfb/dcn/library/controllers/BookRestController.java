package pe.idat.dsfb.dcn.library.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.validation.BindingResult;

import jakarta.validation.Valid;

import pe.idat.dsfb.dcn.library.dtos.BookDetails;
import pe.idat.dsfb.dcn.library.dtos.BookValidation;
import pe.idat.dsfb.dcn.library.models.Author;
import pe.idat.dsfb.dcn.library.models.Book;
import pe.idat.dsfb.dcn.library.models.Category;
import pe.idat.dsfb.dcn.library.models.Publisher;
import pe.idat.dsfb.dcn.library.services.AuthorService;
import pe.idat.dsfb.dcn.library.services.BookService;
import pe.idat.dsfb.dcn.library.services.CategoryService;
import pe.idat.dsfb.dcn.library.services.PublisherService;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookRestController {

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private PublisherService publisherService;

    @PostMapping
    @Operation(
        summary = "Create a new book",
        description = "Creates a new book with the provided details, including authors, publishers, and categories. Returns a 400 if validation fails, 404 if authors, categories, or publishers do not exist, and 409 if the book already exists."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Book successfully created",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Validation errors in the provided book data",
            content = @Content(mediaType = "text/plain")
        ),
        @ApiResponse(
            responseCode = "404",
            description = "One or more authors, publishers, or categories not found",
            content = @Content(mediaType = "text/plain")
        ),
        @ApiResponse(
            responseCode = "409",
            description = "A book with the provided ISBN already exists",
            content = @Content(mediaType = "text/plain")
        )
    })
    public ResponseEntity<?> createBook(@Valid @RequestBody BookValidation bookValidation, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder("Validation errors:\n");
            bindingResult.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append("\n"));
            return new ResponseEntity<>(errorMessage.toString(), HttpStatus.BAD_REQUEST);
        }

        Book existingBook = bookService.getByISBN(bookValidation.getISBN());
            
        if (existingBook != null) {
            return new ResponseEntity<>("A book with this ISBN already exists.", HttpStatus.CONFLICT);
        }

        List<Author> authors = authorService.findByNames(bookValidation.getAuthorNames());
        List<Publisher> publishers = publisherService.findByNames(bookValidation.getPublisherNames());
        List<Category> categories = categoryService.findByNames(bookValidation.getCategoryNames());

        if (authors.isEmpty() || categories.isEmpty() || publishers.isEmpty()) {
            return new ResponseEntity<>("One or more of the provided names (author, category or publisher) do not exist.", HttpStatus.NOT_FOUND);
        }

        Book createdBook = bookService.create(bookValidation, authors, publishers, categories);
        HttpHeaders  headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, "/api/books/" + createdBook.getId());

        return new ResponseEntity<>(createdBook, headers, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Update an existing book",
        description = "Updates an existing book with the provided details. Returns 404 if the book is not found, 400 if any validation fails, and 400 if authors, categories, or publishers do not exist."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Book successfully updated",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Validation errors in the provided book data or invalid authors, categories, or publishers",
            content = @Content(mediaType = "text/plain")
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Book not found with the provided ID",
            content = @Content(mediaType = "text/plain")
        )
    })
    public ResponseEntity<?> updateBook(@PathVariable Long id, @Valid @RequestBody BookValidation bookValidation, BindingResult bindingResult) {
        
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder("Validation errors:\n");
            bindingResult.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append("\n"));
            return new ResponseEntity<>(errorMessage.toString(), HttpStatus.BAD_REQUEST);
        }

        Book existingBook = bookService.getById(id);
        if (existingBook == null) {
            return new ResponseEntity<>("Book not found with ID: " + id, HttpStatus.NOT_FOUND);
        }

        List<Author> authors = authorService.findByNames(bookValidation.getAuthorNames());
        List<Category> categories = categoryService.findByNames(bookValidation.getCategoryNames());
        List<Publisher> publishers = publisherService.findByNames(bookValidation.getPublisherNames());

        if (authors.isEmpty() || categories.isEmpty() || publishers.isEmpty()) {
            return new ResponseEntity<>("One or more of the provided names (author, category or publisher) do not exist.", HttpStatus.BAD_REQUEST);
        }

        Book updatedBook = bookService.update(existingBook, bookValidation, authors, publishers, categories);
        HttpHeaders  headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        
        return new ResponseEntity<>(updatedBook, headers, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete a book by its ID",
        description = "Deletes the book associated with the provided ID. Returns 404 if the book is not found, and 204 if the deletion is successful."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Book successfully deleted",
            content = @Content(mediaType = "text/plain")
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Book not found with the provided ID",
            content = @Content(mediaType = "text/plain")
        )
    })
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {

        Boolean isDeleted = bookService.delete(id);

        if (!isDeleted) {
            return new ResponseEntity<>("Book not found with ID: " + id, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>("Book deleted successfully with ID: " + id, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get book details by ID",
        description = "Fetches the details of a book by its unique ID. Returns a 404 status if the book is not found."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully fetched the book details",
            content = @Content(mediaType = "application/json", 
                               schema = @Schema(implementation = BookDetails.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Book not found with the provided ID",
            content = @Content(mediaType = "text/plain")
        )
    })
    public ResponseEntity<?> getBookDetails(@PathVariable Long id) {
        
        BookDetails bookDetails = bookService.getBookDetails(id);

        if (bookDetails == null) {
            return new ResponseEntity<>("Book not found with ID: " + id, HttpStatus.NOT_FOUND);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");  
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache");

        return new ResponseEntity<>(bookDetails, headers, HttpStatus.OK);
    }

    @GetMapping
    @Operation(
        summary = "Get all book details",
        description = "Fetches the details of all available books. Returns a 404 status if no books are found."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully fetched the details of all books",
            content = @Content(mediaType = "application/json", 
                               schema = @Schema(implementation = BookDetails.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "No books found",
            content = @Content(mediaType = "text/plain")
        )
    })
    public ResponseEntity<?> getAllBookDetails() {
        
        List<BookDetails> bookDetails = bookService.getAllBookDetails();

        if (bookDetails.isEmpty()) {
            return new ResponseEntity<>("No books found", HttpStatus.NOT_FOUND);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");  
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache");

        return new ResponseEntity<>(bookDetails, headers, HttpStatus.OK);
    }

    @GetMapping("/page")
    @Operation(
        summary = "Get paginated book details with filters",
        description = "Fetches a paginated list of book details with optional filters for language and format. Returns a 404 status if no books are found."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully fetched paginated book details",
            content = @Content(mediaType = "application/json", 
                               schema = @Schema(implementation = BookDetails.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "No books found matching the provided filters",
            content = @Content(mediaType = "text/plain")
        )
    })
    public ResponseEntity<?> getAllBookDetailsWithFilters(
            @RequestParam(defaultValue = "") String language,
            @RequestParam(defaultValue = "") String format,
            @RequestParam(defaultValue = "0") int page, 
            @RequestParam(defaultValue = "10") int size) {

        Page<BookDetails> bookDetails = bookService.getAllBookDetailsWithFilters(language, format, page, size);
        
        if (bookDetails.isEmpty()) {
            return new ResponseEntity<>("No authors found", HttpStatus.NOT_FOUND);
        }
        
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");  
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache");

        return new ResponseEntity<>(bookDetails, headers, HttpStatus.OK);     
    }
}

