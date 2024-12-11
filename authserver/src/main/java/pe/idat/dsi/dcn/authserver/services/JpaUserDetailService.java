package pe.idat.dsi.dcn.authserver.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import pe.idat.dsi.dcn.authserver.models.User;
import pe.idat.dsi.dcn.authserver.repositories.UserRepository;

@Service
public class JpaUserDetailService implements UserDetailsService{

    private UserRepository userRepository;

    public JpaUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var result = this.userRepository.findByUsername(username);
        if(!result.isPresent()){
            throw new UsernameNotFoundException("Usuario no ha sido encontrado en la BD");
        }

        User user = result.orElseThrow();

        List<GrantedAuthority> authorities = user.getRoles()
            .stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toList());
        
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            user.isEnabled(),
            true,
            true,
            true,
         authorities);
    }
    
}
