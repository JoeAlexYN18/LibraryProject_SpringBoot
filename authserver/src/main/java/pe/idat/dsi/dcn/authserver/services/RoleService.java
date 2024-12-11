package pe.idat.dsi.dcn.authserver.services;

import java.util.List;

import pe.idat.dsi.dcn.authserver.models.Role;

public interface RoleService {
    List<Role> findAll();
    Role save(Role role);
    
}
