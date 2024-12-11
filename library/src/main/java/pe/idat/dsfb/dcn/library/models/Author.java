package pe.idat.dsfb.dcn.library.models;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "author")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false, length = 50)
    private String name;
    @Column(name = "nationality", nullable = false, length = 15)
    private String nationality;
    @Column(name = "birthDate", nullable = false)
    private LocalDate birthDate;
    @Column(name = "biography", nullable = false, length = 300)
    private String biography;
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @JsonBackReference
    @ManyToMany(mappedBy = "authors", fetch = FetchType.LAZY)
    private List<Book> books;
}
