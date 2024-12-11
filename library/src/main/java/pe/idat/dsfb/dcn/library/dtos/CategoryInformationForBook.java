package pe.idat.dsfb.dcn.library.dtos;

public class CategoryInformationForBook {
    private String name;
    private String description;

    public CategoryInformationForBook(String name, String description) {
        this.name = name;
        this.description = description;
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
}
