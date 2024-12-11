package pe.idat.dsfb.dcn.library.dtos;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.*;

public class BookValidation {
    @NotBlank(message = "Please provide a title.")
    @Size(max = 50, message = "Title should not exceed 50 characters.")
    private String title;

    @NotBlank(message = "Please provide an ISBN.")
    @Size(max = 13, message = "ISBN should not exceed 13 characters.")
    private String ISBN;

    @Min(value = 100, message = "Page count must be at least 100.")
    @Max(value = 1500, message = "Page count cannot exceed 1500.")
    private int pageCount;

    @NotBlank(message = "Please provide a language.")
    @Size(max = 10, message = "Language should not exceed 10 characters.")
    private String language;

    @Min(value = 0, message = "Price cannot be negative.")
    @Max(value = 1000, message = "Price cannot exceed 1000.")
    private double price;

    @NotNull(message = "Publication date cannot be null.")
    @PastOrPresent(message = "Publication date cannot exceed the current date.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate publicationDate;

    @NotBlank(message = "Please provide a format.")
    @Size(max = 20, message = "Format should not exceed 20 characters")
    private String format;

    @NotNull(message = "Author names cannot be null.")
    @Size(min = 1, message = "Author names should not be empty.")
    private List<String> authorNames;

    @NotNull(message = "Category names cannot be null.")
    @Size(min = 1, message = "Category names should not be empty.")
    private List<String> categoryNames;

    @NotNull(message = "Publisher names cannot be null.")
    @Size(min = 1, message = "Publisher names should not be empty.")
    private List<String> publisherNames;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getISBN() {
        return this.ISBN;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getPageCount() {
        return this.pageCount;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return this.price;
    }

    public void setLocalDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public LocalDate getPublicationDate() {
        return this.publicationDate;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getFormat() {
        return this.format;
    }

    public void setAuthorNames(List<String> authorNames) {
        this.authorNames = authorNames;
    }

    public List<String> getAuthorNames() {
        return this.authorNames;
    }

    public void setCategoryNames(List<String> categoryNames) {
        this.categoryNames = categoryNames;
    }

    public List<String> getCategoryNames() {
        return this.categoryNames;
    }

    public void setPublisherNames(List<String> publisherNames) {
        this.publisherNames = publisherNames;
    }

    public List<String> getPublisherNames() {
        return this.publisherNames;
    }
}
