package br.com.webstore.service;

import br.com.webstore.Repository.DBUserRepository;
import br.com.webstore.model.DBUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @project: api-webstore
 * @author: Lucas Kanô de Oliveira (lucaskano)
 * @since: 23/04/2020
 */
@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final DBUserRepository dbUserRepository;

    @Autowired
    public CustomUserDetailsService(DBUserRepository dbUserRepository) {
        this.dbUserRepository = dbUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("username CustomUserDetailsService " + username);

        DBUser asda = dbUserRepository.findByUsername(username);

        System.out.println("todos: " + dbUserRepository.findAll());

        DBUser DBUser = Optional.ofNullable(dbUserRepository.findByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<GrantedAuthority> authorityListAdmin = AuthorityUtils.createAuthorityList("ROLE_USER", "ROLE_ADMIN");
        List<GrantedAuthority> authorityListUser = AuthorityUtils.createAuthorityList("ROLE_USER");

        return new org.springframework.security.core.userdetails.User
                (DBUser.getUsername(), DBUser.getPassword(), DBUser.isAdmin() ? authorityListAdmin : authorityListUser);
    }
}
