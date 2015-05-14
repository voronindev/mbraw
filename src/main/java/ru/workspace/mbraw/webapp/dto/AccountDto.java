package ru.workspace.mbraw.webapp.dto;

import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AccountDto {
    private String username;
    private List<GrantedAuthority> authorities;

    public AccountDto(String username, Collection<GrantedAuthority> authorities) {
        this.username = username;
        this.authorities = new ArrayList<GrantedAuthority>(authorities);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }
}
