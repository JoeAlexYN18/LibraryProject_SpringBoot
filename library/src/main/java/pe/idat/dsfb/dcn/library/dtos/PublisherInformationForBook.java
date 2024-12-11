package pe.idat.dsfb.dcn.library.dtos;

public class PublisherInformationForBook {
    private String name;
    private String type;
    private String website;

    public PublisherInformationForBook(String name, String type, String website) {
        this.name = name;
        this.type = type;
        this.website = website;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getWebsite() {
        return this.website;
    }
}
