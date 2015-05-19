package ru.workspace.mbraw.webapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.workspace.mbraw.webapp.pojo.Account;

@Service(value = "userDetailsService")
public class AccountDetailsService implements UserDetailsService {

    @Autowired
    private AccountService accountService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountService.getByUserName(username);

        if (account == null) {
            throw new UsernameNotFoundException("Couldn't find such user");
        } else {
            return new User(account.getUserName(), account.getPassword(),
                    true, true, true, true, AuthorityUtils.NO_AUTHORITIES);
        }
    }
}
