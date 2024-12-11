package pe.idat.dsfb.dcn.library.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pe.idat.dsfb.dcn.library.models.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByISBN(String ISBN);

    @Query("SELECT b FROM Book b WHERE " +
           "b.language LIKE %:language% OR " +
           "b.format LIKE %:format%")
    Page<Book> findAllWithFilters(@Param("language") String language,
                                    @Param("format") String format,
                                    Pageable pageable);
}
