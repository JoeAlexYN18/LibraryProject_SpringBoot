package pe.idat.dsfb.dcn.library.models;

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
@Table(name = "publisher")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Publisher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    @Column(name = "contactNumber", nullable = false, length = 15)
    private String contactNumber;
    @Column(name = "email", nullable = false, length = 100)
    private String email;
    @Column(name = "type", nullable = false, length = 20)
    private String type;
    @Column(name = "country", nullable = false, length = 20)
    private String country;
    @Column(name = "website", nullable = false, length = 100)
    private String website;

    @JsonBackReference
    @ManyToMany(mappedBy = "publishers", fetch = FetchType.LAZY)
    private List<Book> books;
}
