package ru.workspace.mbraw.webapp.services;

import org.springframework.context.annotation.Profile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;

@Service
@Profile("test")
public class AccountDetailsServiceTest implements UserDetailsService {

    private User defaultUser;

    @PostConstruct
    public void defineUser() {
        defaultUser = new User("user", "password",
                true, true, true, true, Collections.singletonList(new SimpleGrantedAuthority("USER")));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("user".equals(username)) {
            return defaultUser;
        }
        return null;
    }
}
