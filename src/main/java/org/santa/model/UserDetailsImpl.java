package org.santa.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.santa.model.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * Implementation of {@link UserDetails}
 */
public class UserDetailsImpl implements UserDetails {

    private String username;

    private List<GrantedAuthority> authorities;

    public UserDetailsImpl(String username, Role role) {
        this.username = username;

        authorities = asList(new SimpleGrantedAuthority("ROLE_" + role.toString()));
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
