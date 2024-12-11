package pe.idat.dsfb.dcn.library.dtos;

import java.util.List;

public class CategoryInformation {
    private long id;
    private String name;
    private String description;
    private List<BookInformation> books;

    public CategoryInformation(long id, String name, String description, List<BookInformation> books) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.books = books;
    }

    public void setID(long ID) {
        this.id = ID;
    }

    public long getID() {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setBooks(List<BookInformation> books) {
        this.books = books;
    }

    public List<BookInformation> getBooks() {
        return this.books;
    }
}
