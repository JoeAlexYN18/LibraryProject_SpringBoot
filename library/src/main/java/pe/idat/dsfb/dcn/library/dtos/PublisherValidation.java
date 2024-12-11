package pe.idat.dsfb.dcn.library.dtos;

import jakarta.validation.constraints.*;

public class PublisherValidation {
    @NotBlank(message = "Please provide a name.")
    @Size(max = 100, message = "Name should not exceed 100 characters.")
    private String name;

    @NotBlank(message = "Please provide a contact number.")
    @Size(max = 15, message = "Contact number should not exceed 15 characters.")
    private String contactNumber;

    @NotBlank(message = "Please provide a email.")
    @Email(message = "Email should be valid.")
    @Size(max = 100, message = "Email should not exceed 100 characters.")
    private String email;

    @NotBlank(message = "Please provide a type.")
    @Size(max = 20, message = "Type should not exceed 20 characters.")
    private String type;

    @NotBlank(message = "Please provide a country.")
    @Size(max = 20, message = "Country should not exceed 20 characters.")
    private String country;

    @NotBlank(message = "Please provide a website.")
    @Size(max = 100, message = "Website should not exceed 100 characters.")
    private String website;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getContactNumber() {
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
}
