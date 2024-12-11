package pe.idat.dsfb.dcn.library.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pe.idat.dsfb.dcn.library.models.Publisher;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    Optional<Publisher> findByName(String name);
    List<Publisher> findByNameIn(List<String> names);

    @Query("SELECT p FROM Publisher p WHERE " +
           "p.country LIKE %:country% OR " +
           "p.type LIKE %:type%")
    Page<Publisher> findAllWithFilters(@Param("country") String country,
                                    @Param("type") String type,
                                    Pageable pageable);
}
