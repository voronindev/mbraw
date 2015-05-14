package ru.workspace.mbraw.webapp.controllers;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.workspace.mbraw.webapp.dto.AccountDto;

import java.security.Principal;

@RestController
@RequestMapping(value = "/api/accounts",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
public class AccountController {

    @RequestMapping(value = "/me", method = RequestMethod.GET)
    public AccountDto authenticatedUserDetails(Principal principal) {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
        User user = (User) token.getPrincipal();

        return new AccountDto(user.getUsername(), user.getAuthorities());
    }
}
