package pe.idat.dsfb.dcn.library.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pe.idat.dsfb.dcn.library.models.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
    List<Category> findByNameIn(List<String> names);

    @Query("SELECT c FROM Category c WHERE " +
           "c.description LIKE %:description%")
    Page<Category> findAllWithFilters(@Param("description") String description,
                                    Pageable pageable);
}
