package pe.idat.dsfb.dcn.library.dtos;

import java.util.List;

public class PublisherInformation {
    private Long id;
    private String name;
    private String contactNumber;
    private String email;
    private String type;
    private String country;
    private String website;
    private List<BookInformation> books;

    public PublisherInformation(long id, 
                                String name,
                                String contactNumber,
                                String email,
                                String type,
                                String country,
                                String website,
                                List<BookInformation> books) {
        
        this.id = id;
        this.name = name;
        this.contactNumber = contactNumber;
        this.email = email;
        this.type = type;
        this.country = country;
        this.website = website;
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

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getConactNumber() {
        return this.contactNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return this.country;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getWebsite() {
        return this.website;
    }

    public void setBooks(List<BookInformation> books) {
        this.books = books;
    }

    public List<BookInformation> getBooks() {
        return this.books;
    }
}
