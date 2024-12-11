package pe.idat.dsfb.dcn.library.dtos;

import jakarta.validation.constraints.*;

public class CategoryValidation {
    @NotBlank(message = "Please provide a name.")
    @Size(max = 20, message = "Name should not exceed 20 characters.")
    private String name;
    @NotBlank(message = "Please provide a description.")
    @Size(max = 100, message = "Description should not exceed 100 characters.")
    private String description;

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
}
