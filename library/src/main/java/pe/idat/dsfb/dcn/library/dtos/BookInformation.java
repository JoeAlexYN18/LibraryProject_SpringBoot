package pe.idat.dsfb.dcn.library.dtos;

import java.time.LocalDate;

public class BookInformation {
    private String title;
    private int pageCount;
    private String language;
    private LocalDate publicationDate;

    public BookInformation(String title, int pageCount, String language, LocalDate publicationDate) {
        this.title = title;
        this.pageCount = pageCount;
        this.language = language;
        this.publicationDate = publicationDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }
}

