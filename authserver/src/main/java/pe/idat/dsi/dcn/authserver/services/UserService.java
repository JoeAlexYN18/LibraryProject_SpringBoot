package pe.idat.dsi.dcn.authserver.services;

import java.util.List;

import pe.idat.dsi.dcn.authserver.models.User;

public interface UserService {
    List<User> findAll();
    User save(User user);
    
}
