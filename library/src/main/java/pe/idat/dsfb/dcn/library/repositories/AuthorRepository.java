package pe.idat.dsfb.dcn.library.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pe.idat.dsfb.dcn.library.models.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByName(String name);
    List<Author> findByNameIn(List<String> names); 
    
    @Query("SELECT a FROM Author a WHERE " +
           "a.name LIKE %:name% OR " +
           "a.nationality LIKE %:nationality%")
    Page<Author> findAllWithFilters(@Param("name") String name,
                                    @Param("nationality") String nationality,
                                    Pageable pageable);
}
