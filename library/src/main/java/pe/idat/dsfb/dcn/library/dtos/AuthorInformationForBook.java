package pe.idat.dsfb.dcn.library.dtos;

import java.time.LocalDate;

public class AuthorInformationForBook {
    private String name;
    private String nationality;
    private LocalDate birthDate;
    private String biography;
    private String email;

    public AuthorInformationForBook(String name, String nationality, LocalDate birthDate, String biography, String email) {
        this.name = name;
        this.nationality = nationality;
        this.birthDate = birthDate;
        this.biography = biography;
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getNationality() {
        return this.nationality;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getBiography() {
        return this.biography;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }
}
