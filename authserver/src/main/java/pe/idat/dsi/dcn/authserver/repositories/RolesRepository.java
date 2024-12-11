package pe.idat.dsi.dcn.authserver.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.idat.dsi.dcn.authserver.models.Role;

public interface RolesRepository  extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);

}
