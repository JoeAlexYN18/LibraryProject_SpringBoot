package pe.idat.dsi.dcn.authserver.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.idat.dsi.dcn.authserver.models.Role;
import pe.idat.dsi.dcn.authserver.models.User;
import pe.idat.dsi.dcn.authserver.repositories.RolesRepository;
import pe.idat.dsi.dcn.authserver.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RolesRepository rolesRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RolesRepository rolesRepository) {
        this.userRepository = userRepository;
        this.rolesRepository = rolesRepository;
    }

    @Override
    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    @Override
    public User save(User user) {
        List<Role> roles = new ArrayList<>();

        user.getRolesName().forEach(roleName -> {
            Optional<Role> role = this.rolesRepository.findByName(roleName);
            role.ifPresent(roles::add);
        });

        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return this.userRepository.saveAndFlush(user);
        
    }
    
}
