package com.mjpascual.gestbanda.config.Security;

import com.mjpascual.gestbanda.entities.Users;
import com.mjpascual.gestbanda.repositories.UsersRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;

@Service
public class UserDetailServiceIMPL implements UserDetailsService {

    private final UsersRepository usersRepository;

    public UserDetailServiceIMPL(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    /**
     * Método encargado de buscar si el email incluido en el token corresponde a algun usuario.
     * @param email Email incluido en el token
     * @return UserDetailsImpl
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = usersRepository.findByEmail(email);
        if(isNull(user)){
            throw new UsernameNotFoundException(email);
        }
        return UserDetailsImpl.builder().user(user).build();
    }
}
