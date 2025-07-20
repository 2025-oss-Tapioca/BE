package com.tapioca.BE.config.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;

@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final UUID userId;
    private final String loginId;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    public String getLoginId() {
        return loginId;
    }
    public UUID getUserId() {
        return userId;
    }
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    @Override public String getPassword() { return password; }
    @Override public String getUsername() {  return userId.toString(); }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
