package pe.idat.dsfb.dcn.library.dtos;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.*;

public class AuthorValidation {
    @NotBlank(message = "Please provide a name.")
    @Size(max = 50, message = "Name should not exceed 50 characters.")
    private String name;

    @NotBlank(message = "Please provide a nationality.")
    @Size(max = 15, message = "Nationality should not exceed 30 characters.")
    private String nationality;

    @NotNull(message = "Birth date cannot be null.")
    @Past(message = "Birth date must be in the past")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @NotBlank(message = "Please provide a biography.")
    @Size(max = 300, message = "Biography should not exceed 300 characters.")
    private String biography;

    @NotBlank(message = "Please provide an email.")
    @Email(message = "Email should be valid.")
    @Size(max = 100, message = "Email should not exceed 100 characters.")
    private String email;

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
}
