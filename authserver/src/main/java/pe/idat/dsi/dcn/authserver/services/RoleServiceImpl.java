package pe.idat.dsi.dcn.authserver.services;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.idat.dsi.dcn.authserver.models.Role;
import pe.idat.dsi.dcn.authserver.repositories.RolesRepository;

@Service
public class RoleServiceImpl implements RoleService{

    private RolesRepository rolesRepository;

    public RoleServiceImpl(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> findAll() {
        return this.rolesRepository.findAll();
    }


    @Transactional
    @Override
    public Role save(Role role) {
        return this.rolesRepository.saveAndFlush(role);
    }
    
}
