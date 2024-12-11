package pe.idat.dsfb.dcn.library.dtos;
import java.time.LocalDate;
import java.util.List;

public class AuthorInformation {
    private Long id;
    private String name;
    private String nationality;
    private LocalDate birthDate;
    private String biography;
    private String email;
    private List<BookInformation> books;

    public AuthorInformation(Long id, String name, String nationality, LocalDate birthDate, String biography, String email, List<BookInformation> books) {
        this.id = id;
        this.name = name;
        this.nationality = nationality;
        this.birthDate = birthDate;
        this.biography = biography;
        this.email = email;
        this.books = books;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<BookInformation> getBooks() {
        return books;
    }

    public void setBooks(List<BookInformation> books) {
        this.books = books;
    }
}

