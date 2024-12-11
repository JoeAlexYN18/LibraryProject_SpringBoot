package pe.idat.dsfb.dcn.library.dtos;

import java.time.LocalDate;
import java.util.List;

public class BookDetails {
    private Long id;
    private String title;
    private String ISBN;
    private int pageCount;
    private String language;
    private double price;
    private LocalDate publicationDate;
    private String format;
    List<AuthorInformationForBook> authors;
    List<PublisherInformationForBook> publishers;
    List<CategoryInformationForBook> categories;

    public BookDetails(long id, 
                    String title, 
                    String ISBN, 
                    int pageCount, 
                    String language, 
                    double price, 
                    LocalDate publicationDate,
                    String format,
                    List<AuthorInformationForBook> authors,
                    List<PublisherInformationForBook> publishers,
                    List<CategoryInformationForBook> categories) {

        this.id = id;
        this.title = title;
        this.ISBN = ISBN;
        this.pageCount = pageCount;
        this.language = language;
        this.price = price;
        this.publicationDate = publicationDate;
        this.format = format;
        this.authors = authors;
        this.publishers = publishers;
        this.categories = categories;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return this.id;
    }

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

    public void setPublicationDate(LocalDate publicationDate) {
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

    public void setAuthors(List<AuthorInformationForBook> authors) {
        this.authors = authors;
    }

    public List<AuthorInformationForBook> getAuthors() {
        return this.authors;
    }

    public void setPublishers(List<PublisherInformationForBook> publishers) {
        this.publishers = publishers;
    }

    public List<PublisherInformationForBook> getPublishers() {
        return this.publishers;
    }

    public void setCategories(List<CategoryInformationForBook> categories) {
        this.categories = categories;
    }

    public List<CategoryInformationForBook> getCategories() {
        return this.categories;
    }
}
