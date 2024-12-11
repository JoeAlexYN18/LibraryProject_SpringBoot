package pe.idat.dsfb.dcn.library.models;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "book")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title", nullable = false, length = 50)
    private String title;
    @Column(name = "ISBN", nullable = false, length = 13)
    private String ISBN;
    @Column(name = "pageCount", nullable = false)
    private int pageCount;
    @Column(name = "language", nullable = false, length = 10)
    private String language;
    @Column(name = "price", nullable = false)
    private double price;
    @Column(name = "publicationDate", nullable = false)
    private LocalDate publicationDate;
    @Column(name = "format", nullable = false, length = 20)
    private String format;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "book_author",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<Author> authors;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "book_publisher",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "publisher_id")
    )
    private List<Publisher> publishers;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "book_category",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;
}
